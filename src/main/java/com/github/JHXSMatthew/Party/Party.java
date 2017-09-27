package com.github.JHXSMatthew.Party;

import java.util.ArrayList;
import java.util.List;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Party {

	private String cap;
	private List<String> member;
	private List<String> request;
	private List<String> invite;
	
	public Party(String cap){
		member = new ArrayList<String>();
		request = new ArrayList<String>();
		invite = new ArrayList<String>();
		
		this.cap = cap;
	}
	
	
	public boolean isInParty(String p){
		if(cap == null){
			return false;
		}
		if(p.equals(cap)){
			return true;
		}
		
		return member.contains(p);
	}
	
	public boolean isInvitee(String p){
		return invite.contains(p);
	}

	public boolean addInvite(String p){
		if(isInvitee(p)){
			return false;
		}
		invite.add(p);
		return true;
	}
	
	public void printMembers(){
		for(String s : member){
			System.out.print(s);
		}
	}
	
	public boolean addTeam(String p){

		if(member.size() >= PartyController.maxPerParty -1){
			return false;
		}
		try{
			member.add(p);
			member.add(cap);
			for(String pt : member){
				ProxiedPlayer temp = Core.get().getProxy().getPlayer(pt);
				if(temp != null){
					temp.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 玩家 " + ChatColor.AQUA  +p +  ChatColor.YELLOW +" 加入了队伍   " + ChatColor.GREEN + "("+ member.size() + "/" + PartyController.maxPerParty + ")"  ));
				}
			}
			try{
				request.remove(p);
				invite.remove(p);
			}catch(Exception e){
				
			}
			
			member.remove(cap);
			return true;
		}catch(Exception e){
			return false;
		}
		
	
	}
	
	public boolean removeTeam(String p,boolean kicked){
		try{
			member.remove(p);
			member.add(cap);
			for(String pt : member){
				ProxiedPlayer temp = Core.get().getProxy().getPlayer(pt);
				if(temp != null){
					if(kicked){
						temp.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 玩家 " + ChatColor.AQUA  +p +  ChatColor.YELLOW +" 被踢出了了队伍  " + ChatColor.GREEN + "("+ member.size() + "/"+ PartyController.maxPerParty +")"  ));
						continue;
					}
					temp.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 玩家 " + ChatColor.AQUA  +p +  ChatColor.YELLOW +" 离开了队伍  " + ChatColor.GREEN + "("+ member.size() + "/"+ PartyController.maxPerParty + ")"  ));
				}
			}
			member.remove(cap);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	public void showList(){
		member.add(cap);
		for(String pt : member){
			ProxiedPlayer temp = Core.get().getProxy().getPlayer(pt);
			if(temp == null){
				continue;
			}
			
			temp.sendMessage(new TextComponent(ChatColor.YELLOW + "=================== 队伍信息  ==================="));
			temp.sendMessage(new TextComponent(ChatColor.YELLOW + "队长: " + ChatColor.AQUA + cap));
			if(member.size() < 5){
				temp.sendMessage(new TextComponent(ChatColor.YELLOW + " 人数: " + ChatColor.GREEN + (member.size() )));
			}else{
				temp.sendMessage(new TextComponent(ChatColor.YELLOW + " 人数: " + ChatColor.RED + (member.size() )));
			}
			temp.sendMessage(new TextComponent(ChatColor.YELLOW + " 队员 : "));
			for(String s : member){
				if(s.equals(cap)){
					continue;
				}
				temp.sendMessage(new TextComponent(ChatColor.AQUA + "  - " + s));
			}
		
		}
		member.remove(cap);
	}
	
	public boolean isFull(){
		return member.size() >= PartyController.maxPerParty -1;
	}
	
	public boolean teamConnecting(String target){
		boolean returnValue = true;
		for(String temp : member){
			ProxiedPlayer p = Core.get().getProxy().getPlayer(temp);
			if(p != null){
				try{
					p.connect(Core.get().getProxy().getServerInfo(target));
				}catch(Exception e){
					e.printStackTrace();
					returnValue = false;
				}
			}else{
				try{
					Core.get().getProxy().getScheduler().runAsync(Core.get(), new Runnable() {
						@Override
						public void run() {
							Core.getRedis().sendChannelMessage("Team","tp|"+temp+"|"+target);
						}
					});
				}catch(Exception e){
					e.printStackTrace();
					returnValue = false;
				}
			}
		}
		return returnValue;
	}
	
	public boolean isCap(String cap){
		if(this.cap == null){
			return false;
		}
		return cap.equals(this.cap);
	}
	
	public void addRequestList(String name){
		if(request.contains(name)){
			return;
		}
		request.add(name);
	}
	public String getCap(){
		return cap;
	}
	public boolean isInRequests(String name){
		return request.contains(name);
	}
	
	public void clear(){
		member = null;
		request = null;
		invite = null;
		cap = null;
	}
	
	public void sendPartyMessage(String msg){
		TextComponent tx = new TextComponent(msg);
		member.add(cap);
		for(String name : member){
			ProxiedPlayer tempp = Core.get().getProxy().getPlayer(name);
			if(tempp != null){
				tempp.sendMessage(tx);
			}
		}
		member.remove(cap);
	}
	
	public void allSendServer(String serverName){
		ServerInfo f = Core.get().getProxy().getServerInfo(serverName);
		for(String name : member){
			ProxiedPlayer tempp = Core.get().getProxy().getPlayer(name);
			if(tempp != null){
				Core.getCdc().addIgnore(name);
				tempp.connect(f);
				Core.getCdc().removeIgnore(name);
				tempp.sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 您跟随队长一起进入了"+ serverName +"房间.."));
			}
		}
	}
	
}
