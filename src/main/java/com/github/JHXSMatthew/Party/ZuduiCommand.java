package com.github.JHXSMatthew.Party;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.github.JHXSMatthew.Core;

public class ZuduiCommand extends Command{
	public ZuduiCommand(){
	      super("zudui");
	  }

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
	
		if(Core.getCdc().isCoolDown(arg0.getName())){
			arg0.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 人家都要被你刷屏啦~等会儿再这样做啦."));
			return;
		}
		
	    ProxiedPlayer p = (ProxiedPlayer)arg0;
	 
		
	    Core.getCdc().addCoolDown(arg0.getName());
	    
	    if( p.getServer().getInfo().getName().contains("auth")){
	    	
	    	return ;
	    }else if(arg1.length < 1){
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "================= 组   队  ================="));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd xin - 创建个人队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd li - 离开当前队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jie - 解散队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jia 玩家名称  - 加入玩家队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd yao 玩家名称  - 邀请玩家加入队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd ti 玩家名称 - 踢出玩家"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd cha - 显示当前队伍信息"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/pmsg - 团队聊天"));
	    
	    }else if(arg1.length ==  1){
	    	if(arg1[0].equalsIgnoreCase("xin")){
	    		String playerName = p.getName();
	    		if(Core.getPc().isInParty(playerName,false) || Core.getPc().isInParty(playerName,true) ){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您在一个队伍里或者是某队伍队长！"));
	    			return ;
	    		}
	    	
	    		Core.getPc().sendPartyCreate(playerName.replace(" ", ""));
	    		p.sendMessage(new TextComponent(ChatColor.AQUA+ "YourCraft >> 队伍创建成功,您可以使用 /zd yao 玩家名称 "+ChatColor.AQUA + "邀请玩家进入队伍"));
	    	
	    		return;
	    	}else if(arg1[0].equalsIgnoreCase("li")){
	    		String playerName = p.getName();
	    		if(!Core.getPc().isInParty(playerName,false)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不在一个队伍里或者是某队伍队长！"));
	    		
	    			return;
	    		}
	    		if(Core.getPc().isInParty(playerName,true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不在一个队伍里或者是某队伍队长！"));
	    		
	    			return;
	    		}
	
	    		Core.getPc().sendPartyLeave(playerName.replace(" ", ""));
	    		p.sendMessage(new TextComponent(ChatColor.AQUA+ "YourCraft >> 离开队伍成功..."));
	    	
	    		return;
	    	}else if(arg1[0].equalsIgnoreCase("jie")){
	    		String playerName = p.getName();
	    		if(!Core.getPc().isInParty(playerName,true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不在一个队伍里或者不是某队伍队长！"));
	    		
	    			return ;
	    		}
	    		Core.getPc().sendPartyDisband(playerName.replace(" ", ""));
	    		p.sendMessage(new TextComponent(ChatColor.AQUA+ "YourCraft >> 队伍解散成功..."));
	    	
	    		return;
	    		
	    	}else if(arg1[0].equalsIgnoreCase("showall")){
	    			Core.getPc().showAll();
	    			return;
	    	}else if (arg1[0].equalsIgnoreCase("cha")){
	    		String playerName = p.getName();
	    		if(!Core.getPc().isInParty(playerName,false)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不在一个队伍里！"));
	    		
	    			return;
	    		}
	    		Core.getPc().sendList(playerName);
	    	
	    		return;
	    	
	    	}else {
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "================= 组   队  ================="));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd xin - 创建个人队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd li - 离开当前队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jie - 解散队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jia 玩家名称  - 加入玩家队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd yao 玩家名称  - 邀请玩家加入队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd ti 玩家名称 - 踢出玩家"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd cha - 显示当前队伍信息"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/pmsg - 团队聊天"));
	    	}
	    	
	    
	    }else if(arg1.length == 2){
	    	if(arg1[0].equalsIgnoreCase("jia")){
	    		String targetName = arg1[1].replace(" ", "");
	    		String playerName = p.getName().replace(" ", "");
	    		if(Core.getPc().isInParty(playerName, false)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您在一个队伍里！"));
	    		
	    			return;
	    		}
	    		if(!Core.getPc().isInParty(targetName, false)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 该玩家不在一个队伍里！"));
	    		
	    			return;
	    		}
	    		Party party = Core.getPc().getParty(targetName, false);
	    		if(party.isFull()){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 队伍已满人..."));
	    		
	    			return;
	    		}
	    		Core.getPc().sendPartyRequest(targetName,playerName);
	    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您加入队伍的请求已经发送！"));
	    	
	    		return;
	    		
	    	}else if(arg1[0].equalsIgnoreCase("yes")){
	    		String capName = p.getName();
	    		if(!Core.getPc().isInParty(capName, true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不是队长..."));
	    			return;
	    		}
	    		Party party = Core.getPc().getParty(capName, true);
	    		if(party.isFull()){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 队伍已满人..."));
	    			return;
	    		}
	    		if(!party.isInRequests(arg1[1])){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 玩家 "+arg1[1] +" 并未向您申请加入队伍，您可以使用 /zd yao "+ arg1[1]+" 邀请该玩家加入队伍"));
	    			return;
	    		}
	    		Core.getPc().sendAccept(capName,arg1[1]);
	    		return;
	    		
	    	}else if(arg1[0].equalsIgnoreCase("yao")){
	    		String capName = p.getName();
	    		if(!Core.getPc().isInParty(capName, true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不是队长..."));
	    			return;
	    		}
	    		Party party = Core.getPc().getParty(capName, true);
	    		if(party.isFull()){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 队伍已满人..."));
	    			return;
	    		}
	    		if(party.isInParty(arg1[1])){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 该玩家已经在您的队伍内了..."));
	    			return;
	    		}
	    		if(Core.getPc().isInParty(arg1[1],false)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 该玩家已经有队伍了..."));
	    			return;
	    		}
	    		
	    		Core.getPc().sendPartyInvite(capName, arg1[1].replace(" ", ""));
	    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 邀请队伍请求发送成功!"));
	    		return;
	    		
	    	}else if(arg1[0].equalsIgnoreCase("ok")){
	    		String invitee = p.getName().replace(" ", "");
	    		if(!Core.getPc().isInParty(arg1[1], true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 该队伍不存在"));
	    			return;
	    		}
	    		Party party = Core.getPc().getParty(arg1[1], true);
	    		
	    		if(party.isFull()){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 队伍已满人..."));
	    			return;
	    		}
	    		if(!party.isInvitee(invitee)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 你没受到邀请..."));
	    			return;
	    		}
	    		Core.getPc().sendPartyInviteOk(arg1[1].replace(" ", ""), invitee);
	    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您已成功加入队伍!"));
	    		return;
	    	}else if(arg1[0].equalsIgnoreCase("ti")){
	    		String capName = p.getName().replace(" ", "");
	    		if(!Core.getPc().isInParty(capName, true)){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 您不是队长..."));
	    			return;
	    		}
	    		Party party = Core.getPc().getParty(capName, true);
	    		if(!party.isInParty(arg1[1])){
	    			p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 这个人并不是您的队员..。"));
	    			return;
	    		}
	    		
	    		Core.getPc().sendPartyKick(capName, arg1[1].replace(" ", ""));
	    		
	    		p.sendMessage(new TextComponent(ChatColor.YELLOW + "YourCraft >> 踢出队员成功..。"));
	    		return;
	    	}else {
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "================= 组   队  ================="));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd xin - 创建个人队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd li - 离开当前队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jie - 解散队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jia 玩家名称  - 加入玩家队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd yao 玩家名称  - 邀请玩家加入队伍"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd ti 玩家名称 - 踢出玩家"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd cha - 显示当前队伍信息"));
		    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/pmsg - 团队聊天"));
	    	}
	    }else{
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "================= 组   队  ================="));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd xin - 创建个人队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd li - 离开当前队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jie - 解散队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd jia 玩家名称  - 加入玩家队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd yao 玩家名称  - 邀请玩家加入队伍"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd ti 玩家名称 - 踢出玩家"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/zd cha - 显示当前队伍信息"));
	    	p.sendMessage(new TextComponent(ChatColor.AQUA + "/pmsg - 团队聊天"));
	    
	    }
		p=null;
	}
	
	
	
	
	
	
  public static boolean isInteger(String input){  
      Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
      return mer.find();  
  }  
}
