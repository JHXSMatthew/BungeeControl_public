package com.github.JHXSMatthew.Commands;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.JHXSMatthew.Core;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command{
	
	  public BanCommand(){
	      super("ban");
	      
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		ProxiedPlayer p = null;
		try{
			p = (ProxiedPlayer)arg0;
		}catch(Exception e){
			
		}
		
		if(arg0.getName().equals("jhxs") || arg0.getName().equals("xiwangniang") || !(arg0 instanceof ProxiedPlayer)){
			
			if(!(arg0 instanceof ProxiedPlayer) || !p.getServer().getInfo().getName().contains("auth")){
				if(arg1.length < 2){
					arg0.sendMessage(new TextComponent("==================="
							+ "\n"
							+ " 使用/ban 名字 理由 帖子id(或留空) 来封禁玩家"
							+ "\n"
							+ "理由可快捷填写数字"
							+ "\n"
							+ " 1 谩骂 "
							+ "\n"
							+ " 2 干扰他人游戏"
							+ "\n"
							+ " 3 不文明游戏"
							+ "\n"
							+ " 4 宣传"
							+ "\n"
							+ " 5 使用第三方软件"
							+ "\n"
							+ " 6 违反法律法规"
							+ "\n"
							+ " 7 智商不足"
							+ "\n"
							+ " ==================="
							));
				}else {
					String banned = arg1[0];
					String reason = getReason(arg1[1]);
					String tid = null;
				
					
					if(arg1.length == 2){
						tid = arg0.getName();
					}else{
						tid = arg1[2];
					}
					int stack = -1;
					try {
						 stack = Core.get().MySQL.getStack(banned);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					//increase stack
					stack ++;
					
					ProxiedPlayer banner = Core.get().getProxy().getPlayer(banned);
					if(banner != null){
						banner.disconnect(new TextComponent(getSendString(reason,tid,stack)));
					
					}else{
						Core.getRedis().sendChannelMessage("bcBan", arg1[0] + "|" + getSendString(reason,tid,stack));
					}
					
					/*
					Core.get().getProxy().broadcast(new TextComponent( ChatColor.RED + "YourCraft封禁 >> 用户 "
						   	+ChatColor.YELLOW+ banned+ ChatColor.RED +  " 因 "
							+ ChatColor.YELLOW + reason +ChatColor.RED + " 被封禁账号 " + ChatColor.YELLOW + getDayToBan(stack)+ " 天 " + ChatColor.RED +"!" ));
					*/
					arg0.sendMessage(new TextComponent( ChatColor.RED + "管理>> 用户 "
						   	+ChatColor.YELLOW+ banned+ ChatColor.RED +  " 因 "
							+ ChatColor.YELLOW + reason +ChatColor.RED + " 被封禁账号 " + ChatColor.YELLOW + getDayToBan(stack)+ " 天 " + ChatColor.RED +"!" ));
					
					boolean isNew = false;
					if(stack == 1) isNew = true;

					Core.get().MySQL.updateBannedPlayer(banned, reason, getDayToBan(stack), isNew, tid, stack);


					
				}
				
			}
		}
		//this.core.getLogger().info("啥错了!");
	}
	
	
	
	private String getSendString(String reason ,String tid, int stack){
		StringBuilder build = new StringBuilder();
		try{
			int readId = Integer.parseInt(tid);
			build.append(ChatColor.RED + "您因 " + reason + " 被封禁 "+ getDayToBan(stack) +" 天"
					+ ",相关证据举报帖编号为: " + readId
					+ "\n这是您第 " + stack +" 次被封禁.当第 4 次封禁时,您将会被永久停封!"
					+ "\n请您珍惜自己的账号、文明游戏、遵守服务器规定."
					+ "\n我们期待着您认可我们游戏规则的一天,期待着您带着文明回归YourCraft!"
					+ "\n"
					+ "\n"
					+ " 如果您有任何疑问,请登录论坛举报区反馈. "
					+ "\n"
					+ " www.mcndsj.com");
		}catch(Exception e){
			build.append(ChatColor.RED + "您因 " + reason + " 被封禁 "+ getDayToBan(stack) +" 天"
					+ ",封禁您的管理员是: " + tid
					+ "\n"
					+ "这是您第 " + stack +" 次被封禁.当第 4 次封禁时,您将会被永久停封!"
					+ "\n请您珍惜自己的账号、文明游戏、遵守服务器规定."
					+ "\n我们期待着您认可我们游戏规则的一天,期待着您带着文明回归YourCraft!"
					+ "\n"
					+ "\n"
					+ " 如果您有任何疑问,请登录论坛举报区反馈. "
					+ "\n"
					+ " www.mcndsj.com");
		}
	
		return build.toString();
	}
	
	

	
	private String getReason(String s){
		try{
			switch(Integer.parseInt(s)){
				case 1 : return "谩骂";
				case 2 : return "干扰他人游戏";
				case 3 : return "不文明游戏";
				case 4 : return "宣传";
				case 5 : return "使用第三方软件";
				case 6 : return "违反法律法规";
				case 7 : return "智商不足";
			}
			return s;
		}catch(Exception e){
			return s;
		}
	}
	
	public static int getDayToBan(int input){
		switch(input){
			case 1: return 3;
			case 2: return 7;
			case 3: return 30;
			case 4: return 36500;
		}
		return 36500;
	}
	
    public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
        return mer.find();  
    }  
}
