package com.github.JHXSMatthew.Listeners;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.github.JHXSMatthew.Core;

import com.mcndsj.JHXSMatthew.Shared.GameManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {
	private byte any = 0;
	private String theMsg ;
	private boolean pinged = false;

	private TextComponent motd1 = new TextComponent(ChatColor.translateAlternateColorCodes('&',"&e&l● &6&l访问论坛 www.mcndsj.com 获取最新资讯&e&l ●"));
	private TextComponent motd2 = new TextComponent(ChatColor.translateAlternateColorCodes('&',"&e&l● &6&l 网络异常补偿已发放,请登录查收! &e&l ●"));
	private TextComponent motd3 = new TextComponent(ChatColor.translateAlternateColorCodes('&',"&e&l● &6&l  战墙职业 Beta 测试  &e&l ●"));
	private TextComponent motdPrefix =  new TextComponent(ChatColor.translateAlternateColorCodes('&',"                  &a&lYourCraft&7✪&b&l你的世界 \n"));
	private TextComponent maintain = new TextComponent(ChatColor.translateAlternateColorCodes('&', motdPrefix +"\n" + "&e&l            ● &6&l 服务器维护中  &e&l ●"));


	static PingListener instance;
	public PingListener(){
		instance = this;
		theMsg = getVersionNameToShow();
		 ProxyServer.getInstance().getScheduler().schedule(Core.get(), new Runnable() {
		      public void run() {
		        if(pinged){
		        	 theMsg = getVersionNameToShow();
		        }
		      }
		    }
		    , 5000L, 5000L, TimeUnit.MILLISECONDS);

		loadMotd();
		}


	public static PingListener get(){
		return instance;
	}

	public void loadMotd(){

		String temp1 = "ChatColor.translateAlternateColorCodes('&',\"&e&l● &6&l访问论坛 www.mcndsj.com 获取最新资讯&e&l ●\")";
		String temp2 = "&e&l● &6&l 网络异常补偿已发放,请登录查收! &e&l ●";
		String temp3 = "&e&l● &6&l  战墙职业 Beta 测试  &e&l ●";
		String tempPrefix = "                  &a&lYourCraft&7✪&b&l你的世界 \n";

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;;
		int count = 0;
		try {
			connection = GameManager.getInstance().getConnection();
			if(connection == null || connection.isClosed()){
				return;
			}
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `BungeeGlobals` WHERE (`configKey` = \"motd1\" || `configKey` = \"motd2\" || `configKey` = \"motd3\" || `configKey` = \"motdPrefix\");");
			while(resultSet.next()){
				if(resultSet.getString("configKey").equals("motd1")){
					temp1 = resultSet.getString("object");
				}else if(resultSet.getString("configKey").equals("motd2")){
					temp2 = resultSet.getString("object");
				}else if(resultSet.getString("configKey").equals("motd3")) {
					temp3 = resultSet.getString("object");
				}else if(resultSet.getString("configKey").equals("motdPrefix")){
					tempPrefix = resultSet.getString("object");
				}
			}
			motd1 = new TextComponent(tempPrefix + "\n" + centerText(temp1,55));
			motd2 =  new TextComponent(tempPrefix + "\n" + centerText(temp2,55));
			motd3 =  new TextComponent(tempPrefix + "\n" + centerText(temp3,55));
			System.out.println(motd1);
			System.out.println(motd2);
			System.out.println(motd3);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
			if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
			if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
		}
	}

	private String centerText(String text, int lineLength) {
		StringBuilder builder = new StringBuilder(text);
		char space = ' ';
		int distance = (lineLength - text.length()) / 2;
		for (int i = 0; i < distance; ++i) {
			builder.insert(0, space);
			builder.append(space);
		}
		return builder.toString();
	}

	@EventHandler
	public void onPing(ProxyPingEvent evt){
		if(Core.getLis().isMaintain()){
			pinged = true;
			evt.getResponse().setDescriptionComponent(maintain);
			evt.getResponse().getVersion().setName(theMsg);
			evt.getResponse().getVersion().setProtocol(3);
			return;
		}
		pinged = true;
		
		if(any == 0){
			any++;
			evt.getResponse().setDescriptionComponent(motd1);
			evt.getResponse().getVersion().setName(theMsg);
			evt.getResponse().getVersion().setProtocol(3);
		}else if(any == 1){
			any++;
			evt.getResponse().setDescriptionComponent(new TextComponent(motd2));
			evt.getResponse().getVersion().setName(theMsg);
			evt.getResponse().getVersion().setProtocol(3);
		}else{
			any = 0;
			evt.getResponse().setDescriptionComponent(motd3);
			evt.getResponse().getVersion().setName(theMsg);
			evt.getResponse().getVersion().setProtocol(3);
		}
	}
	
	
	public String getVersionNameToShow() {
	//    String toShow = ChatColor.translateAlternateColorCodes('&',"&e&l玩我玩我-->&r");
		//String toShow = ChatColor.translateAlternateColorCodes('&',"&c&l新春☘快乐➤&r");
		String toShow = ChatColor.translateAlternateColorCodes('&',"&e&l玩我玩我-->&r");

	    String sslots = ChatColor.GRAY.toString() + Core.getRedis().getPlayerCount() + "/" + "34657" ;

	    for (int i = 0; i < 73 - 
	      ChatColor.stripColor(sslots).length(); )
	    {
	      toShow = toShow + " ";

	      i++;
	    }

	    toShow = toShow + sslots;
	    return toShow;
	  }
}
