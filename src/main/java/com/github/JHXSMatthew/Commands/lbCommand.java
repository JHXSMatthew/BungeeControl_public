package com.github.JHXSMatthew.Commands;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class lbCommand extends Command{
	public lbCommand(){
	      super("qf");
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		String length = " ->";
		if(arg1.length > 0){
			for(String i : arg1){
				length = length + i;
			}
			for(ProxiedPlayer p : Core.get().getProxy().getPlayers()){
				p.sendMessage(new TextComponent(ChatColor.GOLD + "【全服】 " + ChatColor.RESET + arg0.getName() + length));
				
			}
		}else{
			arg0.sendMessage(new TextComponent(ChatColor.AQUA + "YourCraft >> 请输入/qf 信息 全服发言."));
		}
	}
	
}
