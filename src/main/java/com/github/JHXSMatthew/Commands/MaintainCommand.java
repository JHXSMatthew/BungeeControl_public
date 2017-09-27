package com.github.JHXSMatthew.Commands;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MaintainCommand extends Command{
	public MaintainCommand(){
	      super("maintain");
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!(arg0 instanceof ProxiedPlayer) || arg0.hasPermission("mymaintain.perm")){
			
			Core.getLis().setMaintain(); 
	}
	
	
	}
	
	
	

}
