package com.github.JHXSMatthew.Party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import com.github.JHXSMatthew.Core;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;

public class PartyController implements Listener {

	private List<Party> pb = null;
	public static int maxPerParty = 3;
	
	
	public PartyController(){
		pb = Collections.synchronizedList(new ArrayList<Party>());
		//Core.getRedis().sendChannelMessage("PartyTrans", "request");
		
	}
	
	public boolean isInParty(String name,boolean cap){
		synchronized (pb) {
			for (Party p : pb) {
				//System.out.print(p.getCap() + " looking ");

				if (cap) {
					if (p.isCap(name)) {
						//	System.out.print(name + " is in party and cap ");
						return true;
					}
				} else {
					if (p.isInParty(name)) {
						//		System.out.print(name + " is in  party ");
						return true;
					}
				}

			}
			//System.out.print(name + " is not in  party  ");
			return false;
		}
	}
	
	private void createParty(String cap){
		synchronized (pb) {
			Party p = new Party(cap);
			pb.add(p);
			//	System.out.print("success " + pb.size());
		}
	//	System.out.print("fail");
	}
	

	public void showAll(){
		synchronized (pb) {
			System.out.print("current Parties " + pb.size());
			for (Party p : pb) {
				System.out.print("Cap is " + p.getCap());
				System.out.print("members ");
				p.printMembers();
			}
		}
	}
	

	
	private void leaveParty(String name){
		synchronized (pb) {
			for (Party p : pb) {
				if (p.isInParty(name)) {
					if (!p.isCap(name)) {
						p.removeTeam(name, false);
					}
				}
			}
		}
		
	}
	private void disbandParty(String name){
		  synchronized (pb){
			  try{
				Iterator<Party> ilt = pb.iterator();
				while(ilt.hasNext()){
					Party party = ilt.next();
					if(party.isCap(name)){
						party.sendPartyMessage(ChatColor.YELLOW + "YourCraft >> 队长 " +ChatColor.AQUA +  party.getCap() + ChatColor.YELLOW + " 解散了队伍...");
						party.clear();
						ilt.remove();
						break;
					}
				}
				}catch(Exception e ){
					e.printStackTrace();
				}
		  }
	
	}
	
	public Party getParty(String name,boolean cap){
		synchronized (pb) {
			for (Party part : pb) {
				if (part.isInParty(name)) {
					if (cap) {
						if (part.isCap(name)) {
							return part;
						}
						return null;
					}
					return part;

				}
			}
			return null;
		}
	}
	private void RequestAnnouncement(String targetName , String name){
		Party pt = getParty(targetName, false);

		if (pt == null) {
			System.out.print("Party Request Error");
			return;
		}
		pt.addRequestList(name);
		ProxiedPlayer p = Core.get().getProxy().getPlayer(pt.getCap());
		if (p == null) {
			return;
		}

		TextComponent tc = new TextComponent("YourCraft >> 您收到了来自 " + name + " 的加入队伍请求.\n 输入/zd yes " + name + "接受该玩家的请求。");
		tc.setColor(ChatColor.YELLOW);
		p.sendMessage(tc);
		tc = new TextComponent(ChatColor.YELLOW + "YourCraft >> 输入/zd yes " + name + ChatColor.YELLOW + "接受该玩家的请求。");
		tc.setColor(ChatColor.YELLOW);
		p.sendMessage(tc);


	}
	private void PartyJoin(String cap, String name){

		Party p = getParty(cap, true);
		try {
			p.addTeam(name);
		} catch (Exception e) {
			System.out.print("Accept Error!");
		}

	}
	
	private void PartyInvite(String cap, String name){

		Party p = getParty(cap, true);
		if (p == null) {
			return;
		}

		if (p.addInvite(name)) {
			ProxiedPlayer pl = Core.get().getProxy().getPlayer(name);
			if (pl == null) {
				return;
			}
			pl.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您收到了来自 " + cap + " 的组队邀请。 输入/zd ok " + cap + ChatColor.YELLOW + " 接受该玩家的邀请。"));
			pl.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >>  输入/zd ok " + cap + ChatColor.YELLOW + " 接受该玩家的邀请。"));
		}

	}
	
	
	
	private void PartyKick(String cap , String name){
		Party p = getParty(cap,true);
		p.removeTeam(name,true);
	}

	public void sendPartyCreate(String cap){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "create|" + cap);
			}
		});
	}
	
	public void sendPartyRequest(String cap,String name){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "request|"+cap+ "|" + name);
			}
		});
	}


	public void sendAccept(String cap,String name){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "accept|" + cap + "|" + name);
			}
		});
	}
	
	
	public void sendPartyLeave(String cap){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "leave|" + cap);
			}
		});
	}
	public void sendPartyDisband(String cap){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "disband|" + cap);
			}
		});
	}
	
	public void sendPartyInvite(String cap, String invitee){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "invite|" + cap + "|" + invitee);
			}
		});
	}
	
	public void sendPartyInviteOk(String cap, String invitee){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "ok|" + cap + "|" + invitee);
			}
		});
	}
	public void sendPartyKick(String cap, String kicked){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "kick|" + cap + "|" + kicked);
			}
		}
		);
	}
	public void sendAllServer(String ServerName , String capName){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "allSend|" + ServerName + "|" + capName);
			}
		}
		);
	}
	public void sendList(String name){
		Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			@Override
			public void run() {
				Core.getRedis().sendChannelMessage("Party", "list|" + name);
			}
		}
		);
	}

	/*
	@EventHandler
	public void onPartyIniMessage(PubSubMessageEvent evt){
		if(!evt.getChannel().equals("PartyTrans")){
			  return;
		  }
		if(evt.getMessage().equals("request")){
			if(this.pb.size() == 0){
				return;
			}else{
				StringBuffer sb = new StringBuffer();

			}
		}else{

		}

	}
	*/
	
	  @EventHandler
	  public void onPartyMessage(PubSubMessageEvent evt){
		  if(!evt.getChannel().equals("Party")){
			  return;
		  }
		  Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
			  @Override
			  public void run() {
					synchronized (pb) {
						StringTokenizer in = new StringTokenizer(evt.getMessage(), "|");
						String action = in.nextToken();

						if (action.equals("create")) {
							String name = in.nextToken();
							createParty(name);
							System.out.print("Party Created " + name);
							return;
						} else if (action.equals("leave")) {
							String name = in.nextToken();
							leaveParty(name);
							System.out.print("Party Leave " + name);
						} else if (action.equals("disband")) {
							String name = in.nextToken();
							disbandParty(name);
							System.out.print("Party Disband " + name);
						} else if (action.equals("request")) {
							String targetName = in.nextToken();
							String name = in.nextToken();
							RequestAnnouncement(targetName, name);
						} else if (action.equals("accept")) {
							String cap = in.nextToken();
							String name = in.nextToken();
							PartyJoin(cap, name);
						} else if (action.equals("invite")) {
							String cap = in.nextToken();
							String name = in.nextToken();
							PartyInvite(cap, name);
						} else if (action.equals("ok")) {
							String cap = in.nextToken();
							String name = in.nextToken();
							PartyJoin(cap, name);
						} else if (action.equals("kick")) {
							String cap = in.nextToken();
							String name = in.nextToken();
							PartyKick(cap, name);
						} else if (action.equals("allSend")) {
							String serverName = in.nextToken();
							String name = in.nextToken();
							Party p = getParty(name, true);
							if (p == null) {
								return;
							}
							// p.sendPartyMessage(serverName);
							p.allSendServer(serverName);

						} else if (action.equals("list")) {
							String name = in.nextToken();
							Party p = getParty(name, false);
							if (p == null) {
								return;
							}
							p.showList();
						}
					}
			  }
		  });
		  
	  }
	
}
