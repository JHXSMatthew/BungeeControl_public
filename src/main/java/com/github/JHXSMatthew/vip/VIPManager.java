package com.github.JHXSMatthew.vip;

import java.sql.SQLException;
import java.util.HashMap;

import com.github.JHXSMatthew.Core;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VIPManager implements Listener {

	private MySQLManager_Money drive = new MySQLManager_Money();
	private HashMap<String,Boolean> cache = new HashMap<String,Boolean>();

	public boolean isVip(String name) {
		if(cache.containsKey(name)){
			return cache.get(name);
		}
		try {
			boolean isVip =  drive.getVipLevel(name) > 0;
			cache.put(name,isVip);
			return isVip;
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	//login check in Listener class
	@EventHandler
	public void onLeaveVIPThread(PlayerDisconnectEvent evt){
		cache.remove(evt.getPlayer().getName());
	}


}
