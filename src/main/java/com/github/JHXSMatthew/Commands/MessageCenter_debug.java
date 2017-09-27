package com.github.JHXSMatthew.Commands;

import com.github.JHXSMatthew.Core;

import com.github.JHXSMatthew.Listeners.PingListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCenter_debug extends Command{
	
	  public MessageCenter_debug(){
	      super("debug");
	      
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		ProxiedPlayer p = null;
		try {
			p = (ProxiedPlayer) arg0;
		} catch (Exception e) {

		}
		if (arg0.getName().equals("jhxs") || arg0.getName().equals("xiwangniang") || !(arg0 instanceof ProxiedPlayer)) {

			if (!(arg0 instanceof ProxiedPlayer) || !p.getServer().getInfo().getName().contains("auth")) {
				if (arg1.length < 1) {
					sendHelp(arg0);
					return;
				}

				if (arg1[0].equals("show")) {
					Core.getDebug().outPutToPlayer(arg0);

				} else if (arg1[0].equals("clear")) {
					Core.getDebug().clear();
					arg0.sendMessage(new TextComponent("Cleared!"));
				} else if (arg1[0].equals("count")) {
					//arg0.sendMessage(new TextComponent("Count-> " + arg1[1] + " " + Core.getCountSimulator().get(arg1[1]) ));
				} else if (arg1[0].equals("printall")) {
					//Core.getCountSimulator().printAll(arg0,arg1[1]);
				} else if (arg1[0].equals("reping")) {
					//Core.getCountSimulator().rePing(arg1[1]);
					arg0.sendMessage(new TextComponent("repinged!"));
				} else if (arg1[0].equals("motdupdate")) {
					PingListener.get().loadMotd();
				} else {
					sendHelp(arg0);
				}

			}


		}
	}
		//this.core.getLogger().info("É¶´íÁË!");


	private void sendHelp(CommandSender arg0){
		arg0.sendMessage(new TextComponent("===debug=== " +
				"\n -show show debug msg" +
				"\n - clear clear debug msg " +
				"\n - count <ServerName> show inner count on that server " +
				"\n - printall <ServerName> show all inner count to those servers" +
				"\n - reping <serverName> reping all server contain that name" +
				"\n - motdupdate update motds"));
	}
	
	
	
}