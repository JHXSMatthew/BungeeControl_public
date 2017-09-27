package com.github.JHXSMatthew;

import net.md_5.bungee.api.ChatColor;

public class BannedInfoContainer {

	public String reason;
	public String date;
	public String tid;
	public int stack;
	

	public String getSendString(){
		StringBuilder build = new StringBuilder();
		try{
			int readId = Integer.parseInt(tid);
			build.append(ChatColor.RED + "您因 " + reason + " 被封禁至 "+ date
					+ ",相关证据举报帖编号为: " + readId
					+ "\n这是您第 " + stack +" 次被封禁.当第 4 次封禁时,您将会被永久停封!"
					+ "\n请您珍惜自己的账号、文明游戏、遵守服务器规定."
					+ "\n我们期待着您认可我们游戏规则的一天,期待着您带着文明回归YourCraft!"
					+ "\n"
					+ "\n"
					+ " 如果您有任何疑问,请登录论坛举报区反馈. "
					+ " www.mcndsj.com");
		}catch(Exception e){
			build.append(ChatColor.RED + "您因 " + reason + " 被封禁至 "+ date
					+ ",封禁您的管理员是: " + tid
					+ "\n"
					+ "这是您第 " + stack +" 次被封禁.当第 4 次封禁时,您将会被永久停封!"
					+ "\n请您珍惜自己的账号、文明游戏、遵守服务器规定."
					+ "\n我们期待着您认可我们游戏规则的一天,期待着您带着文明回归YourCraft!"
					+ "\n"
					+ "\n"
					+ " 如果您有任何疑问,请登录论坛举报区反馈. "
					+ " www.mcndsj.com");
		}
	
		return build.toString();
	}
}
