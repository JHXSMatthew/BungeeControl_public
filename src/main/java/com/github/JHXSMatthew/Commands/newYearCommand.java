package com.github.JHXSMatthew.Commands;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.JHXSMatthew.Core;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class newYearCommand extends Command{
	
	  public newYearCommand(){
	      super("newyear");
	      
	  }
	  
    ScheduledTask task = null;
    ScheduledTask count = null;
    Calendar c =Calendar.getInstance() ;
    int seconds = -1;
    
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		ProxiedPlayer p = null;
		try{
		p = (ProxiedPlayer)arg0;
		}catch(Exception e){
			
		}
		if(arg0.getName().equals("jhxs") || arg0.getName().equals("xiwangniang") || !(arg0 instanceof ProxiedPlayer)){
			//this.core.getLogger().info("名字对!");
			
			if(!(arg0 instanceof ProxiedPlayer) || !p.getServer().getInfo().getName().contains("auth")){
				if(arg1.length < 1 ){
					arg0.sendMessage(new TextComponent (" newyear s 开始计时 \n newyear c 查看当前 \n newyear st 停止计时 \n newyear now 手动开启烟花  \n newyear run 开始10秒倒计时" ));
					return;
				}
				
				if(arg1[0].equals("s")){
					if(task != null){
						arg0.sendMessage(new TextComponent("已经在进行了"));
						return;
					}
				int dayLeft = c.get(Calendar.DAY_OF_YEAR);
				int year = c.get(Calendar.YEAR);
				if(year % 4  == 0){
					dayLeft =  366 - dayLeft;
				}else{
					dayLeft = 365 - dayLeft;
				}
				seconds = dayLeft * 24 * 60 *60;
				seconds += 60*60*24 - (( c.get(Calendar.HOUR_OF_DAY))*60*60  + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND));
				   task = ProxyServer.getInstance().getScheduler().schedule(Core.get(), new Runnable() {
					      public void run() {
					          seconds --;
					          if(seconds < 0){
					        	  task.cancel(); 
					        	  System.out.print("bug");
					        	  return;
					          }
					          if(seconds == 30 || seconds == 60){
					        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "   \n \n \n "));
					        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 距离跨年还剩 " + seconds + " 秒"));
					        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "   \n \n \n "));
					          }
					          if(seconds <= 10 && seconds != 0){
					        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 距离跨年还剩 " + seconds + " 秒"));
					          }
					          if(seconds <= 0){
					        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 新年好!"));
					        	  sendMessage();
					        	  task.cancel();  
					        	  task = null;
					        	  seconds = -1;
					          }
					          
					      }
					    }
					    , 1L, 1L, TimeUnit.SECONDS);
				    
				    
				}else if(arg1[0].equals("c")){
					if(task == null){
						arg0.sendMessage(new TextComponent (" 停止中" ));
						return;

					}else {
						arg0.sendMessage(new TextComponent (" 运行中 \n 当前剩余时间 " + seconds  + "秒 \n 时间制 : "  + seconds / 60/60/24 + " 天 " + seconds /60/60%24 +" 小时 " + seconds /60 %60 + " 分钟 " + seconds%60 + " 秒 "   ));
						
					}
				}else if(arg1[0].equals("st")){
					if(task == null){
						seconds = -1;
						return;
					}
					seconds = -1;
					task.cancel();
					task = null;
				}else if(arg1[0].equals("now")){
					sendMessage();
				}else if(arg1[0].equals("run")){
					seconds = 10;
					if(task == null ){
						 task = ProxyServer.getInstance().getScheduler().schedule(Core.get(), new Runnable() {
						      public void run() {
						          seconds --;
						          
						          if(seconds == 30 || seconds == 60){
						        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "   \n \n \n "));
						        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 距离跨年还剩 " + seconds + " 秒"));
						        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "   \n \n \n "));
						          }
						          if(seconds <= 10 && seconds != 0){
						        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 距离跨年还剩 " + seconds + " 秒"));
						          }
						          if(seconds == 0){
						        	  Core.get().getProxy().broadcast(new TextComponent(ChatColor.RED + "Yourcraft >> 新年好!"));
						        	  sendMessage();
						          }
						      }
						    }
						    , 1L, 1L, TimeUnit.SECONDS);
					}
				}
				
			}
		}
		
	}
	
	
	
	private void sendMessage(){
		 
    	  ByteArrayDataOutput out = ByteStreams.newDataOutput();
    	  out.writeUTF("newyear");
    	  for(int i = 1 ; i < 10 ; i ++){
    		  Core.get().getProxy().getServerInfo("lobby"+ i).sendData("BungeeCord", out.toByteArray());
    	  }
	}
	
	
    public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
        return mer.find();  
    }  
}
