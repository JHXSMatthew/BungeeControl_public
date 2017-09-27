package com.github.JHXSMatthew.Controller;

import java.util.HashMap;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LobbySendBackController {
	private HashMap<String,Long> commandDelay;
	
	public LobbySendBackController(){
		commandDelay = new HashMap<String,Long>();
	}
	
	
	public boolean Canteleport(ProxiedPlayer p){
		if(p.getServer().getInfo().getName().contains("lobby")){
			return true;
		}
		long currentTime = System.currentTimeMillis();
		Object value = commandDelay.get(p.getName());
		
		if(value == null){
			commandDelay.put(p.getName(), currentTime);
			return false;
		}
		
		if((Long)value + 5000 < currentTime){
			commandDelay.put(p.getName(), currentTime);
			return false;
		}
		return true;
	}
	
	public void remove(String name){
		commandDelay.remove(name);
	}
	
	
}
