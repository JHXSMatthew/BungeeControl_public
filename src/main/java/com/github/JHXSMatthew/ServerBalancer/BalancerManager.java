package com.github.JHXSMatthew.ServerBalancer;

import com.github.JHXSMatthew.Core;
import com.github.JHXSMatthew.Config.Config;

public class BalancerManager {

	public BalancerManager(){
		register();
	}
	
	
	
	public void register(){
		//Core.get().getProxy().getPluginManager().registerListener(Core.get(), new AbstractBalancer(null, "^auth?[0-9]+$", "auth", 40, 6, true, true));
		//Core.get().getProxy().getPluginManager().registerListener(Core.get(), new AbstractBalancer("auth1", "^lobby?[0-9]+$", "lobby", 60, Config.maxLobby, true, true));
		//Core.get().getProxy().getPluginManager().registerListener(Core.get(), new AbstractBalancer("lobby1", "^swrlobby?[0-9]+$", "swrlobby", 100, 2, true, true));
		//Core.get().getProxy().getPluginManager().registerListener(Core.get(), new AbstractBalancer("lobby1", "^walllobby?[0-9]+$", "walllobby", 60, 5, true, true));
		//Core.get().getProxy().getPluginManager().registerListener(Core.get(), new AbstractBalancer("lobby1", "^uhclobby?[0-9]+$", "uhclobby", 60, 5, true, true));


	}
}
