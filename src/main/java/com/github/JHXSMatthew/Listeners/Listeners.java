package com.github.JHXSMatthew.Listeners;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.github.JHXSMatthew.Core;
import com.imaginarycode.minecraft.redisbungee.events.PlayerJoinedNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;

import com.mcndsj.BC_RedisConnector.BCRedisConnector;
import gnu.trove.TCollections;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.event.ServerKickEvent.State;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Listeners implements Listener {
	 
	 private final TObjectIntMap<String> addresses = TCollections.synchronizedMap(new TObjectIntHashMap<String>());

	 //private final TObjectIntMap<String> limitMap = TCollections.synchronizedMap(new TObjectIntHashMap<String>());
	 
	 
	 
	 private int limit = 2;
	 private int tenMinlimit = 10;
	 
	 
	// private ArrayList<String> limitSpeed;
	 private boolean maintain =false;

	 
	 
	 
	public Listeners(){

		/*
		
		 ProxyServer.getInstance().getScheduler().schedule(Core.get(), new Runnable() {
		      public void run() {
		    	  limitMap.clear();
		      }
		    }
		    , 10, 10, TimeUnit.MINUTES);

		*/
	
	}
	

	  public void setMaintain(){
		  if(maintain){
			  this.maintain = false;
			  System.out.print("Maintain Mode Disabled");

		  }else{
			  maintain = true;
			  for(ProxiedPlayer p : Core.get().getProxy().getPlayers()) { p.disconnect(new TextComponent("服务器正在维护中,请关注官方公告 www.mcndsj.com"));}
			  System.out.print("Maintain Mode enable");
		  }
		  
	  }
	  
	  public boolean isMaintain(){
		  return maintain;
	  }

	
	  @EventHandler
	  public void onPreLogin(PreLoginEvent evt){
		  if(evt.getConnection().getVersion() < 47){
			  evt.setCancelled(true);
			  evt.setCancelReason(ChatColor.RED + "请您使用 1.8 以上客户端加入游戏 下载地址 www.mcndsj.com");
			  return;
		  }

		  evt.registerIntent(Core.get());
		  ProxyServer.getInstance().getScheduler().runAsync(Core.get(), () -> {
			  //ban
			  try {
				  if (Core.get().MySQL.isBanned(evt.getConnection().getName())) {
					  String s = Core.get().MySQL.getBannedLine(evt.getConnection().getName()).getSendString();
					  evt.setCancelled(true);
					  evt.setCancelReason(s);
				  }
			  }catch(Exception e){
				e.printStackTrace();
			  }
			  //maintain
			  if(!evt.isCancelled()){
				  if(maintain) {
					  if (!evt.getConnection().getName().equalsIgnoreCase("jhxs")) {
						  evt.setCancelled(true);
						  evt.setCancelReason(ChatColor.RED + "服务器正在维护中,请关注官方公告 www.mcndsj.com");
					  }
				  }
			  }
			  //IP Limit
			  if(!evt.isCancelled()) {
				  try {
					  String i = evt.getConnection().getAddress().getAddress().getHostAddress();
					  if (this.addresses.get(i) >= this.limit) {
						  evt.setCancelled(true);
						  evt.setCancelReason(ChatColor.RED + "您已经达到了服务器账号进入上限 \n\n 踢出原因: 移动、长城等特殊宽带用户，因廉价宽带不独立IP导致被拒绝加入。\n我该怎么办: 我们建议您多次重启路由器，或重启网络后再进入游戏。 \n\n www.mcndsj.com");
					  }
				  } catch (Exception e) {

				  }
			  }

			  if(!evt.isCancelled()){
				  Core.getVip().isVip(evt.getConnection().getName());
			  }
			  evt.completeIntent(Core.get());
		  });
	  }

	@EventHandler
	public void onRemoteJoin(PlayerJoinedNetworkEvent evt){
		ProxyServer.getInstance().getScheduler().runAsync(Core.get(), () -> {
			InetAddress i = Core.getRedis().getPlayerIp(evt.getUuid());
			this.addresses.adjustOrPutValue(i.getHostAddress(), 1, 1);
		});
	}
	@EventHandler
	public void onPostLogin(PostLoginEvent evt){
		String ip = evt.getPlayer().getAddress().getAddress().getHostAddress();
		if(this.addresses.containsKey(ip)){
			this.addresses.adjustValue(ip, 1);
		}else{
			this.addresses.put(ip,1);
		}
	}
	  
  /*
	  @EventHandler
	  public void onJoin(LoginEvent evt){


	      if(this.limitMap.get(i) >=  tenMinlimit){
	    	  evt.setCancelled(true);
	    	  evt.setCancelReason(ChatColor.RED + "您触碰了我们的防火墙规则  \n\n 踢出原因: 十分钟内只能加入服务器 "+ tenMinlimit +" 次. \n解决方案: 请等待一段时间后重试,不要一直进服,以免被当成攻击. \n \n www.mcndsj.com");
	    	  return;
	      }

	  }
   */
	  


	  @EventHandler
	  public void onServerKick(ServerKickEvent evt){
		  if(evt.getState() != State.CONNECTED){
			  return;
		  }
		  ProxiedPlayer pp = evt.getPlayer();
		  String currentaPosition = pp.getServer().getInfo().getName();
		  
		
		  String kickMsg = BaseComponent.toLegacyText(evt.getKickReasonComponent());
		  if(kickMsg.contains("starting up") ){
			  evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 该房间正在初始化中,请稍等."));
			  return;
		  }
		  if(kickMsg.contains("满人")){
			  evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 该房间目前人数已满."));
			  return;
		  }
		  
		  if(currentaPosition.contains("auth") || currentaPosition.contains("lobby")){
			  return;
		  }
		  
		  if(!evt.getKickedFrom().getName().contains("auth") && !kickMsg.contains("反作弊")){
			  evt.setCancelled(true);
			  evt.getPlayer().connect(Core.get().getProxy().getServerInfo("auth1"));
			  evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 大厅已满或未知错误，您被传送至登录等待队列."));
			  return;
		  }
		  
		  
		  kickMsg = kickMsg + ChatColor.RED+ " \n\n 如果您有疑问，请截图该画面至论坛反馈问询!  www.mcndsj.com";
		  
		  evt.setKickReasonComponent(TextComponent.fromLegacyText(kickMsg));
	  }


	  //Core.getRedis().sendChannelMessage("PlayerQuitMsg", "ip");


	  @EventHandler
	  public void onRedisMsgReceived(PubSubMessageEvent evt){
		  if(!evt.getChannel().equals("PlayerQuitMsg")){
			  return;
		  }
		  String ip = evt.getMessage();
		  if(this.addresses.containsKey(ip)){
			 this.addresses.adjustValue(ip, -1);
			 if (this.addresses.get(ip) <= 0){
				this.addresses.remove(ip);
		     }
		 }
	  }


	@EventHandler
	public void onQuit(PlayerDisconnectEvent evt){
		 Core.getCdc().removeIgnore(evt.getPlayer().getName());
		 Core.getLbc().remove(evt.getPlayer().getName());
		 InetAddress addr = evt.getPlayer().getAddress().getAddress();
        Core.getRedis().sendChannelMessage("PlayerQuitMsg", addr.getHostAddress());
	}

	
	/*@EventHandler (priority=EventPriority.HIGHEST)
	public void onJoin (PostLoginEvent evt) {
		if(!Core.get().MySQL.isBanned(evt.getPlayer().getName())){
			return;
		}
		String s = Core.get().MySQL.getBannedLine(evt.getPlayer().getName()).getSendString();
		evt.getPlayer().disconnect(new TextComponent(s));
		
		/*
		Core.get().getProxy().getScheduler().schedule(Core.get(), new  Runnable(){

			@Override
			public void run() {
				try {
					;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}, 2, TimeUnit.SECONDS);
		*/
		/*
		ResultSet r = this.core.MySQL.getUser(evt.getPlayer().getName());
		if(r.next()){
			if(r.getDate("Date").after(new Date())){
				evt.getPlayer().disconnect(new TextComponent(ChatColor.RED + "您被封禁至 "+ r.getDate("Date").toString() + " 原因 " + r.getString("Reason") +" 请遵守游戏规则! \n\n 如果您有任何疑问,请登录论坛举报区反馈. \n www.mcndsj.com"));
			}else{
				this.core.MySQL.deletePlayer(evt.getPlayer().getName());
			}
			r.getStatement().close();
			r.close();
		}

		
	}
	*/
	
	


/*
	// auto balancer
	@EventHandler
	public void onBlance(ServerConnectEvent evt){
	
		
		if(Pattern.compile("^lobby?[0-9]+$").matcher(evt.getTarget().getName()).find()){
			if(Core.getCdc().isIgnore(evt.getPlayer().getName())){
				Core.getCdc().removeIgnore(evt.getPlayer().getName());
				if(evt.getPlayer().getServer() != null){
					evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 正在将您从房间编号 " + ChatColor.YELLOW + evt.getPlayer().getServer().getInfo().getName() + ChatColor.RED + " 传送至 " + ChatColor.YELLOW + evt.getTarget().getName() + ChatColor.RED + "!"  ));
				}
				return;
			}
			
			evt.setTarget(core.getProxy().getServerInfo("lobby" + this.currentLobby));
			if(currentLobby >= maxLobby){
				this.currentLobby = 1;
			}else{
				this.currentLobby++;
			}
		}else if(evt.getTarget().getName().contains("auth")){
			evt.setTarget(core.getProxy().getServerInfo("auth" + this.currentAuth));
			if(this.currentAuth >= maxAuth){
				this.currentAuth = 1;
			}else{
				this.currentAuth++;
			}
		}else if(evt.getTarget().getName().contains("swrlobby")){
			evt.setTarget(core.getProxy().getServerInfo("swrlobby" + this.currentSwr));
			if(this.currentSwr >= this.maxswrlobby){
				this.currentSwr = 1;
			}else{
				this.currentSwr++;
			}
		}
		
		if(evt.getPlayer().getServer() != null){
			evt.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "YourCraft >> 正在将您从房间编号 " + ChatColor.YELLOW + evt.getPlayer().getServer().getInfo().getName() + ChatColor.RED + " 传送至 " + ChatColor.YELLOW + evt.getTarget().getName() + ChatColor.RED + "!"  ));
		}

		return;
		
	}
*/
	//chat shared in lobbys
/*

	 @EventHandler
	  public void onChat(ChatEvent evt) {
	    ProxiedPlayer p = (ProxiedPlayer)evt.getSender();
	    if (Pattern.compile("^lobby?[0-9]+$").matcher(p.getServer().getInfo().getName()).find()){
	        if (evt.isCancelled()) {
	            return;
	         }

	        if (evt.getMessage().startsWith("/") ){
	            return;
	        }
	        if(limitSpeed.contains(p.getName())){
	        	return;
	        }
	        String msg = evt.getMessage();
	        int fromInt = Integer.parseInt(p.getServer().getInfo().getName().substring(5,6));
	        for(int i = 1 ; i < 8 ; i++){
	        	if(i!= fromInt){
	        		String targetName = "lobby"+ i;
		        	for(ProxiedPlayer temp : Core.get().getProxy().getServerInfo(targetName).getPlayers()){
		        		temp.sendMessage(new TextComponent(ChatColor.AQUA + "[大厅-"+ Core.getMsg().parseChinese(fromInt) + "] " + ChatColor.DARK_AQUA + p.getName() + ChatColor.GOLD+ " >" + ChatColor.GRAY + msg) );
		        	}
		        	targetName = null;
	        	}
	        }
	        msg = null;
	        limitSpeed.add(p.getName());
	    }
	 }
	 */
}
