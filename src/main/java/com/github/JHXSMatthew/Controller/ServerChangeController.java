package com.github.JHXSMatthew.Controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.ProxyServer;

public class ServerChangeController {

	private ArrayList<String> coolDown;
	private ArrayList<String> ignore;
	
	public ServerChangeController(){
		coolDown = new ArrayList<String>();
		ignore = new ArrayList<String>();
	    ProxyServer.getInstance().getScheduler().schedule(Core.get(), new Runnable() {
	      public void run() {
	        Core.getCdc().Clear();
	      }
	    }
	    , 5000L, 5000L, TimeUnit.MILLISECONDS);
	}
	
	
	public void addIgnore(String s){
		this.ignore.add(s);
	}
	public void removeIgnore(String s){
		this.ignore.remove(s);
	}
	public boolean isIgnore(String s){
		return this.ignore.contains(s);
	}
	
	public void Clear(){
		coolDown.clear();
	}
	public boolean isCoolDown(String s){
		return coolDown.contains(s);
	}
	public void addCoolDown(String s){
		coolDown.add(s);
	}
	
}
