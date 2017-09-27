package com.github.JHXSMatthew.Party;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PartyListener implements Listener {

	
	@EventHandler
	public void onQuitRemoveParty(PlayerDisconnectEvent evt){
		String n = evt.getPlayer().getName();
		if(Core.getPc().isInParty(n, true)){
			Core.getPc().sendPartyDisband(n);
			return;
		}else if(Core.getPc().isInParty(n, false)){
			Core.getPc().sendPartyLeave(n);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onServerConnection(ServerConnectEvent evt){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				String n = evt.getPlayer().getName();
				if (Core.getPc().isInParty(n, true)) {
					Core.getPc().sendAllServer(evt.getTarget().getName(), n);
				}
			}
		});
	}
}
