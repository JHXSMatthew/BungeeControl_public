package com.github.JHXSMatthew.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.JHXSMatthew.Core;
import com.github.JHXSMatthew.Config.Config;

import com.mcndsj.BC_RedisConnector.BCRedisConnector;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class hubCommand extends Command{
	public hubCommand(){
	      super("hub");
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
	
		if(Core.getCdc().isCoolDown(arg0.getName())){
			arg0.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 人家都要被你刷屏啦~等会儿再这样做啦."));
			return;
		}
		
	    ProxiedPlayer p = (ProxiedPlayer)arg0;
	    if( p.getServer().getInfo().getName().contains("auth")){
	    	p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 请您耐心等待."));
	    	return ;
	    }
	    
	    if(!Core.getLbc().Canteleport(p)){
	    	p.sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 您将被传送至大厅，请您在5秒内再次输入 /hub 确认该操作。"));
	    	return ;
	    }
	    
		if(arg1.length < 1){
			if(p.getServer().getInfo().getName().contains("lobby")){
				p.connect(Core.get().getProxy().getServerInfo("lobby1"));
			}else {
				p.connect(Core.get().getProxy().getServerInfo(BCRedisConnector.getInstance().getAPI().getLastLobby(p.getName()) + "1"));
			}
			Core.getCdc().addCoolDown(arg0.getName());
		 }else{
		    	if(!isInteger(arg1[0])){
		    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 请您输入一个正确的大厅编号,比如/lobby 2."));
			return;
		}
			int num = Integer.parseInt(arg1[0]);
			if(num > Config.maxLobby){
	    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 目前lobby只有1-"+Config.maxLobby+"哦"));
	    		return;
	    	}
			Core.getCdc().addIgnore(p.getName());
			p.connect(Core.get().getProxy().getServerInfo("lobby"+num));
			Core.getCdc().addCoolDown(arg0.getName());
			Core.getLbc().remove(p.getName());
		}
	    
	    
	    
		p=null;
	}
	
	
	
	
	
	
    public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
        return mer.find();  
    }  
}
