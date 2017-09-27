package com.github.JHXSMatthew;

import java.sql.SQLException;

import com.github.JHXSMatthew.Controller.ServerRestartController;
import net.md_5.bungee.api.plugin.Plugin;

import com.github.JHXSMatthew.Commands.BanCommand;
import com.github.JHXSMatthew.Commands.MaintainCommand;
import com.github.JHXSMatthew.Commands.LOBBY_SpawnCommand;
import com.github.JHXSMatthew.Commands.MessageCenter_debug;
import com.github.JHXSMatthew.Commands.TellCommand;
import com.github.JHXSMatthew.Commands.WozainaCommand;
import com.github.JHXSMatthew.Commands.hubCommand;
import com.github.JHXSMatthew.Commands.LOBBY_likaiCommand;
import com.github.JHXSMatthew.Commands.LOBBY_lobbyCommand;
import com.github.JHXSMatthew.Commands.newYearCommand;
import com.github.JHXSMatthew.Controller.LobbySendBackController;
import com.github.JHXSMatthew.Controller.MessageController;
import com.github.JHXSMatthew.Controller.ServerChangeController;
import com.github.JHXSMatthew.Listeners.AntiCheat;
import com.github.JHXSMatthew.Listeners.Listeners;
import com.github.JHXSMatthew.Listeners.MessageChannel;
import com.github.JHXSMatthew.Listeners.PingListener;
import com.github.JHXSMatthew.MessageCenter.MessageStorer;
import com.github.JHXSMatthew.Party.PartyCommand;
import com.github.JHXSMatthew.Party.PartyController;
import com.github.JHXSMatthew.Party.PartyListener;
import com.github.JHXSMatthew.Party.PmsgCommand;
import com.github.JHXSMatthew.Party.ZuduiCommand;
import com.github.JHXSMatthew.Party.zdCommand;
import com.github.JHXSMatthew.ServerBalancer.BalancerManager;
import com.github.JHXSMatthew.vip.VIPManager;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;

public class Core extends Plugin{
	private static Listeners listener;
	static private Core instance;
	public MySQLManager MySQL;
	static private ServerChangeController cdc;
	static private MessageController msg;
	static private RedisBungeeAPI bc;
	static private PartyController pc;
	static private LobbySendBackController lbc;
	static private VIPManager vip;
	//static private BalancerManager balancer ;
	static private MessageStorer debug;
	ServerRestartController restartController;
	//private static Handler simulator;
	
	@Override
    public void onEnable() {
		instance = this;
	
		msg = new MessageController();
		bc =  RedisBungee.getApi();
		
		bc.registerPubSubChannels("bcBan");
        bc.registerPubSubChannels("lobbyMessage");
        bc.registerPubSubChannels("Party");
        bc.registerPubSubChannels("PartyTrans");
		bc.registerPubSubChannels("PlayerCountTracker");
		bc.registerPubSubChannels("PlayerQuitMsg");
        getProxy().registerChannel("bungeeControl");

		pc = new PartyController();
		lbc = new LobbySendBackController();
		vip = new VIPManager();
		//balancer = new BalancerManager();
		debug = new MessageStorer();
		restartController = new ServerRestartController();
		//simulator = new Handler();
		
		
    	this.MySQL = new MySQLManager();
    	this.MySQL.openConnection();
    	try {
			this.MySQL.CreateTblae();
		} catch (SQLException e) {
			  getLogger().info("COUND NOT CREATE TABLES");
			e.printStackTrace();
		}
    	listener= new Listeners();
    	getProxy().getPluginManager().registerCommand(this, new hubCommand());
    	getProxy().getPluginManager().registerCommand(this, new LOBBY_lobbyCommand());
    	getProxy().getPluginManager().registerCommand(this, new LOBBY_SpawnCommand());
    	getProxy().getPluginManager().registerCommand(this, new LOBBY_likaiCommand());
    	getProxy().getPluginManager().registerCommand(this, new BanCommand());
    //	getProxy().getPluginManager().registerCommand(this, new lbCommand());
    	getProxy().getPluginManager().registerCommand(this, new MaintainCommand());
    	getProxy().getPluginManager().registerCommand(this, new WozainaCommand());
    	getProxy().getPluginManager().registerCommand(this, new TellCommand());
    	//party command
    	getProxy().getPluginManager().registerCommand(this, new PartyCommand());
    	getProxy().getPluginManager().registerCommand(this, new ZuduiCommand());
    	getProxy().getPluginManager().registerCommand(this, new zdCommand());
    	getProxy().getPluginManager().registerCommand(this, new PmsgCommand());
    	getProxy().getPluginManager().registerCommand(this, new newYearCommand());
    	
    	//DebugCommand
    	getProxy().getPluginManager().registerCommand(this, new MessageCenter_debug());
    	
        getProxy().getPluginManager().registerListener(this, listener);
        getProxy().getPluginManager().registerListener(this, new PingListener());
        getProxy().getPluginManager().registerListener(this, new MessageChannel());
        
        //party listeners 
        getProxy().getPluginManager().registerListener(this, pc);
        getProxy().getPluginManager().registerListener(this, new PartyListener());
        
        // anti-cheat listeners
        getProxy().getPluginManager().registerListener(this, new AntiCheat());
        
        //VIP Listener
        getProxy().getPluginManager().registerListener(this, vip);

        //debug Listener
        getProxy().getPluginManager().registerListener(this, debug);
        
        //simulator Listener
        //getProxy().getPluginManager().registerListener(this, simulator);

        
  
        cdc = new ServerChangeController();
    }
	
	
	/*public static Handler getCountSimulator(){
		return simulator;
	}
	*/

	public static LobbySendBackController getLbc(){
		return lbc;
	}
	
	public static PartyController getPc(){
		return pc;
	}
    
	public static ServerChangeController getCdc(){
		return cdc;
	}
	public static Core get(){
		return instance;
	}
	public static MessageController getMsg(){
		return msg;
	}
	public static Listeners getLis(){
		return listener;
	}

	public static RedisBungeeAPI getRedis(){
		return bc;
	}

	
	public static VIPManager getVip(){
		return vip;
	}

	
	/*public static BalancerManager getBalancer(){
		return balancer;
	}
	*/
	
	public static MessageStorer getDebug(){
		return debug;
	}
	
    /*
    
    public boolean isBlocked(ProxiedPlayer p) throws ClassNotFoundException, SQLException{
    	ResultSet r = this.MySQL.getUser(p.getName());
    	boolean returnValue = r.next();
    	r.close();
    	r.getStatement().close();
    	return returnValue;
    }
    */
}
