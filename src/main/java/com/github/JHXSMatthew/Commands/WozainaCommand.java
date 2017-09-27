package com.github.JHXSMatthew.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.github.JHXSMatthew.Core;

public class WozainaCommand extends Command {
	  public WozainaCommand(){
	      super("wozaina");
	   
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {

		if(Core.getCdc().isCoolDown(arg0.getName())){
			arg0.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 人家都要被你刷屏啦~等会儿再这样做啦."));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer)arg0;
		p.sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 您在 编号为 " + ChatColor.GREEN +p.getServer().getInfo().getName() + ChatColor.RED + " 的房间."));
		
		//this.core.getLogger().info("啥错了!");
	}
	
}
