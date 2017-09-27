package com.github.JHXSMatthew.MessageCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageStorer implements Listener {
	private HashMap<Integer,List<String>> messageStorer  = new HashMap<Integer,List<String>>();
	
	
	@EventHandler
	public void onServerConnection(final ServerConnectEvent evt){
		if(evt.getPlayer().getName().equals("jhxs") ||evt.getPlayer().getName().equals("momo") ){
			if(evt.getTarget().getName().contains("lobby")){
				Core.get().getProxy().getScheduler().schedule(Core.get(), new Runnable(){
					public void run() {
						outPutToPlayer(evt.getPlayer());
					}
					
				}, 5, TimeUnit.SECONDS);
				
			}
		}
	}
	
	public void outPutToPlayer(CommandSender p){
		for(Entry<Integer,List<String>> e : messageStorer.entrySet()){
		
			if(e.getValue() == null || e.getValue().isEmpty()){
				continue;
			}
			p.sendMessage(new TextComponent( "Title: " +e.getValue().get(0)));
			
			StringBuilder sb = new StringBuilder();
			List<String> list = new ArrayList<String>();
			list.addAll(e.getValue());
			list.remove(0);
			for(String s : list){
				sb.append("|_");
				sb.append(s);
				sb.append("\n");
			
			}
			sb.append("\n");
			p.sendMessage(new TextComponent( sb.toString()));
			
		}
	}
		
	public void clear(){
		messageStorer.clear();
	}
	
	/**
	 * @param s
	 * protocol: title + value
	 */
	public void store(String... s){
		if(messageStorer.size() > 15){
			messageStorer.remove(messageStorer.size()-1);
			
		}
		
		if(s.length < 1){
			return;
		}
		
	
		List<String> list = new ArrayList<String>();
		for(String str : s){
			list.add(str);
		}
		
		messageStorer.put(messageStorer.size() ,list );
	}

}
