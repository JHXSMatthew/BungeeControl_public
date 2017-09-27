/*
package com.github.JHXSMatthew.ServerBalancer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import com.github.JHXSMatthew.Core;

public class AbstractBalancer implements Listener{
	//lobby rex "^lobby?[0-9]+$"
	private String backupServer;
	private String nameRex;
	private int maxPlayer ;
	boolean willForceKick = false;
	
	private List<String> nameList = Collections.synchronizedList(new ArrayList<String>());
	private ConcurrentHashMap<String,Boolean> isOnlineMap = new ConcurrentHashMap<String,Boolean> ();
	
	public AbstractBalancer(String backUpServer, String nameRex,String nameBase
			,int maxPerServer ,int ServerCount 
			,boolean useCount, boolean willForceKick ){
		this.nameRex = nameRex;
		this.backupServer = backUpServer;
		this.maxPlayer = maxPerServer;
		this.willForceKick = willForceKick;
	
		if(useCount){
			for(int i = 0; i < ServerCount ; i ++){
				nameList.add(nameBase+ ( i + 1) );
				isOnlineMap.put(nameBase+ ( i + 1), true);
			}
		}else{
			
		}
		
		
		for(final String s : nameList){
			Core.get().getProxy().getServerInfo(s).ping(new Callback<ServerPing>(){
				public void done(ServerPing arg0, Throwable arg1) {
					if(arg1 != null){
						isOnlineMap.put(s, false);
						Core.getDebug().store("OfflineServer Notification " , "Name: " + s , "OfflineTime: " + Calendar.getInstance().getTime().toString());
					}else{
						isOnlineMap.put(s, true);
						maxPlayer = arg0.getPlayers().getMax();
					}
					
				}
				
			});
		}
		
		Core.get().getProxy().getScheduler().schedule(Core.get(), new Runnable(){
			public void run() {
				for(final String s : nameList){
					Core.get().getProxy().getServerInfo(s).ping(new Callback<ServerPing>(){
						public void done(ServerPing arg0, Throwable arg1) {
							if(arg1 != null){
								isOnlineMap.put(s, false);
								Core.getDebug().store("OfflineServer Notification " , "Name: " + s , "OfflineTime: " + Calendar.getInstance().getTime().toString());
							}else{
								isOnlineMap.put(s, true);
							}
						}
					});
				}
				
			}
			
		},60,60,TimeUnit.SECONDS);
	}
	
	private boolean isServerOnline(String name){
		if(!isOnlineMap.containsKey(name)) return false;
		return isOnlineMap.get(name);
	}
	
	private ServerInfo findAvaliableServer(){
		ServerInfo returnValue = null;
		int preOnline = -1;
		for(String s : nameList){
			if(!isServerOnline(s)) {
				continue;
			}
			int onlineNow = Core.getCountSimulator().get(s);
			if(onlineNow < maxPlayer){
				if(onlineNow < (maxPlayer - maxPlayer/4)){
					return Core.get().getProxy().getServerInfo(s);
				}else{
					if(returnValue != null){
						if(onlineNow <= preOnline ){
							returnValue = Core.get().getProxy().getServerInfo(s);
							preOnline = onlineNow;
						}
					}else{
						returnValue = Core.get().getProxy().getServerInfo(s);
						preOnline = onlineNow;
					}
				}
			}
		}
		return returnValue;
	}
	
	
	
	private void sendServerFull(final ProxiedPlayer p,String name){
		p.sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 您正在尝试连接的房间" + name + "已满,购买会员可以体验更加舒畅的绿色通道!"));
		if(willForceKick){
			if(Core.getLis().isMaintain()){
				return;
			}
			if(backupServer == null || backupServer == ""){
				p.disconnect(new TextComponent("您目前尝试加入的协调服务器队列人数已满,购买会员可以体验更加舒畅的绿色通道!"));
			}else if(p.getServer() != null && p.getServer().getInfo().getName().startsWith(backupServer.substring(0, backupServer.length() - 2 ))){
				return;
			}else{
				ServerInfo info = null;
				try{
					info = Core.get().getProxy().getServerInfo(backupServer);
				}catch(Exception e){
					
				}
				if(info == null){
					p.disconnect(new TextComponent("您所尝试加入的服务器已满"));
					return;
				}
				p.connect(info);
			}
		}
	}
	
	@EventHandler
	public void onBalance(ServerConnectEvent evt){
		if(Pattern.compile(nameRex).matcher(evt.getTarget().getName()).find()){
			//if(Pattern.compile(fromRex).matcher(evt.getPlayer().getServer().getInfo().getName()).find()){
			if(Core.getCdc().isIgnore(evt.getPlayer().getName())){
				if(evt.getTarget().getPlayers().size() > maxPlayer 
						&&!Core.getVip().isVip(evt.getPlayer().getName())) {
					evt.setCancelled(true);
					sendServerFull(evt.getPlayer(), evt.getTarget().getName());
					return;
				}
			}else{
				ServerInfo target = null;
				if(Core.getVip().isVip(evt.getPlayer().getName())){
					Random r = new Random();
					target = Core.get().getProxy().getServerInfo(nameList.get(r.nextInt(nameList.size())));
					if(!isServerOnline(target.getName())){
						target = findAvaliableServer();
					}
				}else{
					target = findAvaliableServer();
				}
				 
				if(target == null){
					if(!Core.getVip().isVip(evt.getPlayer().getName())){
						evt.setCancelled(true);
						sendServerFull(evt.getPlayer(),evt.getTarget().getName());
						return;
					}
					return;
				}else{
					if(evt.getPlayer().getServer() != null){
						if(evt.getPlayer().getServer() == target){
							evt.setCancelled(true);
							return;
						}
					}
					evt.setTarget(target);
				}
			}
			//}else{
				
			//}
			if(evt.getPlayer().getServer() != null){
				evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 正在将您从房间编号 " + ChatColor.YELLOW + evt.getPlayer().getServer().getInfo().getName() + ChatColor.RED + " 传送至 " + ChatColor.YELLOW + evt.getTarget().getName() + ChatColor.RED + "!"  ));
			}
		}
		
		/*
		if(Pattern.compile(nameRex).matcher(evt.getTarget().getName()).find()){
			if(Core.getCdc().isIgnore(evt.getPlayer().getName())){
				Core.getCdc().removeIgnore(evt.getPlayer().getName());
				return;
			}
			if(findAvaliableServer() == null){
				evt.setCancelled(true);
			
				evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 您尝试加入的房间全部满员,请稍后再试."));
				return;
			}
			evt.setTarget(core.getProxy().getServerInfo("lobby" + this.currentLobby));
			if(currentLobby >= maxLobby){
				this.currentLobby = 1;
			}else{
				this.currentLobby++;
			}
		}else if(evt.getTarget().getName().contains("auth")){
			evt.setTarget(core.getProxy().getServerInfo("auth" + this.currentAuth));
			if(this.currentAuth >= maxAuth){
				this.currentAuth = 1;
			}else{
				this.currentAuth++;
			}
		}else if(evt.getTarget().getName().contains("swrlobby")){
			evt.setTarget(core.getProxy().getServerInfo("swrlobby" + this.currentSwr));
			if(this.currentSwr >= this.maxswrlobby){
				this.currentSwr = 1;
			}else{
				this.currentSwr++;
			}
		}
		
		if(evt.getPlayer().getServer() != null){
			evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 正在将您从房间编号 " + ChatColor.YELLOW + evt.getPlayer().getServer().getInfo().getName() + ChatColor.RED + " 传送至 " + ChatColor.YELLOW + evt.getTarget().getName() + ChatColor.RED + "!"  ));
		}
		return;
		//////hey there is a commen!!!* or
	}
}

			*/