package com.github.JHXSMatthew.Party;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.github.JHXSMatthew.Core;

public class PmsgCommand extends Command{
	public PmsgCommand(){
	      super("pmsg");
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
	
		if(Core.getCdc().isCoolDown(arg0.getName())){
			arg0.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 人家都要被你刷屏啦~等会儿再这样做啦."));
			return;
		}
		
	    ProxiedPlayer p = (ProxiedPlayer)arg0;
	    if( p.getServer().getInfo().getName().contains("auth")){
	    	return ;
	    }
	    String name = p.getName();
	    if(!Core.getPc().isInParty(name, false)){
	    	arg0.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不在一个队伍内."));
	    	return;
	    }
	    StringBuffer msg = new StringBuffer();
	    msg.append(ChatColor.DARK_PURPLE + "[P] " +  ChatColor.DARK_AQUA + name + ChatColor.GOLD + " >> " + ChatColor.GRAY);
	    for(int i = 0 ; i < arg1.length ; i ++){
	    	msg.append( ChatColor.GRAY + arg1[i] + " ");
	    }
	    Core.getPc().getParty(name, false).sendPartyMessage(msg.toString());
	    
		p=null;
	}
	
	
	
	
	
	
  public static boolean isInteger(String input){  
      Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
      return mer.find();  
  }  
}
