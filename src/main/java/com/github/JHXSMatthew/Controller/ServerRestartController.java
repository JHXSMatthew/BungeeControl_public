package com.github.JHXSMatthew.Controller;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import sun.util.resources.cldr.aa.CalendarData_aa_DJ;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Matthew on 26/06/2016.
 */
public class ServerRestartController {

    private Timer t = new Timer();
    private static int restartHour = 4;
    private static int restartMin = 30;// must > 10


    public ServerRestartController(){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int currentH = c.get(Calendar.HOUR_OF_DAY);
                int currentMin = c.get(Calendar.MINUTE);
                if(currentH == restartHour - 1 && currentMin == restartMin  && c.get(Calendar.SECOND) < 5){
                    ProxyServer.getInstance().broadcast(new TextComponent(ChatColor.RED + "日常维护 >> 距离服务器日常重启还有1小时!"));
                }else if(currentH == restartHour && restartMin -  currentMin< 10 && c.get(Calendar.SECOND) == 0){
                    if(currentH == restartHour && currentMin == restartMin){
                        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers() ){
                            p.disconnect(new TextComponent("每日 " + restartHour + ":" + restartMin + "日常维护重启!"));
                        }
                        ProxyServer.getInstance().stop("每日 " + restartHour + ":" + restartMin + "日常维护重启!");
                        cancel();
                    }else if(restartMin - currentMin  < 0){
                        return;
                    }
                    ProxyServer.getInstance().broadcast(new TextComponent(ChatColor.RED + "日常维护 >> 距离服务器日常重启还有" + (restartMin - currentMin )+  "分钟!"));
                }
            }
        },1000,1000);
    }
}
