package com.github.JHXSMatthew.Listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.JHXSMatthew.Core;
import com.github.JHXSMatthew.Config.Config;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageChannel implements Listener {

	  @EventHandler
	  public void onPluginMessage(PluginMessageEvent ev)
	  {
	    if (!ev.getTag().equals("bungeeControl")) {
	      return;
	    }

	    ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
	    DataInputStream in = new DataInputStream(stream);
	    try {
	    	whatToDo(in.readUTF());
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		  try {
			  in.close();
			  stream.close();
		  }catch(IOException e){

		  }
	  }
	  
	  public int getNumbers(String content) {  
	       Pattern pattern = Pattern.compile("\\d+");  
	       Matcher matcher = pattern.matcher(content);  
	       while (matcher.find()) {  
	          return Integer.parseInt(matcher.group(0));  
	       }  
	       return -1;  
	   }  
	  
	  
	  
	  @EventHandler
	  public void onRedisMsgReceived(PubSubMessageEvent evt){
		  if(!evt.getChannel().equals("lobbyMessage")){
			  return;
		  }
		  for(int i = 1 ; i < Config.maxLobby + 1 ; i++){
      		String targetName = "lobby"+ i;
	        for(ProxiedPlayer temp : Core.get().getProxy().getServerInfo(targetName).getPlayers()){
	        		temp.sendMessage(new TextComponent(evt.getMessage()));        		
	        }
	        	targetName = null;
		  }
		  
		  
	  }
	  
	  @EventHandler
	  public void onBanMessageReceived(PubSubMessageEvent evt){
		 
		  if(!evt.getChannel().equals("bcBan")){
	
			  return;
		  }
		  
		  StringTokenizer in = new StringTokenizer(evt.getMessage(),"|");
		  String name = in.nextToken();
		  ProxiedPlayer p = Core.get().getProxy().getPlayer(name);
		  if(p != null){
			  String msg = in.nextToken();
			  p.disconnect(new TextComponent(msg));
		  }
		  in = null;
		  name = null;

	  }
	  
	  public void whatToDo(String buffer){
		  final StringTokenizer in = new StringTokenizer(buffer,"|");
		  
		  String section = in.nextToken();
		  
		  final String name = in.nextToken();
		
		  if(section.equals("lobby")){
			  ProxiedPlayer p = Core.get().getProxy().getPlayer(name);
			  if(!p.getServer().getInfo().getName().contains("lobby")){
				  p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您必须在大厅服务器才可以这样做."));
				  return;
			  }
			  
			 
			  String to = in.nextToken();
			  int num = 0;
			  ServerInfo target =Core.get().getProxy().getServerInfo(to);
			  if(!target.getName().contains("lobby")){
				  return;
			  }
			  
			  try{
				  num= Integer.parseInt( target.getName().substring(5,6));
			  }catch(NumberFormatException  e){
				  e.printStackTrace();
			  }
			  
			  p.sendMessage(new TextComponent(ChatColor.AQUA + "YourCraft >> 正在将您传送至 大厅" + Core.getMsg().parseChinese(num) + " ,请稍后....."));
			  
			  Core.getCdc().addIgnore(p.getName());
			  p.connect(target);
			  Core.getCdc().addCoolDown(p.getName());
			  
			  to = null;
			  target = null;
			  p = null;
		  }else if (section.equals("chat")){
			  

			  /*
			  String serverName = Core.get().getProxy().getPlayer(name).getServer().getInfo().getName();
			  int num = getNumbers(serverName);
			  
			  for(int i = 1 ; i < 8 ; i++){
	        		String targetName = "lobby"+ i;
		        	for(ProxiedPlayer temp : Core.get().getProxy().getServerInfo(targetName).getPlayers()){
		        		temp.sendMessage(new TextComponent(ChatColor.AQUA + "[大厅-"+ Core.getMsg().parseChinese(num) + "] " +  msg) );        		
		        	}
		        	targetName = null;
			  }
			  */
			  Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
				  @Override
				  public void run() {
					  String msg = in.nextToken();
					  int num = getNumbers(Core.getRedis().getServerFor(Core.getRedis().getUuidFromName(name)).getName());
					  Core.getRedis().sendChannelMessage("lobbyMessage",ChatColor.AQUA + "[大厅-"+ Core.getMsg().parseChinese(num) + "] " +  msg);
				  }
			  });
		  }
		  

	  }



}
