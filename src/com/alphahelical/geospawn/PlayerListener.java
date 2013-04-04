/**
 * 
 */
package com.alphahelical.geospawn;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * @author Keith Beckman
 *
 */
public class PlayerListener implements Listener {

	private GeoSpawn plugin;
	
	public PlayerListener(GeoSpawn plugin) {
		if(plugin == null) throw new NullArgumentException("plugin");
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPlayedBefore()) {
			String country_code = Util.countryCodeOfPlayer(p);
			this.plugin.sendPlayerToGeoSpawn(p, country_code);
		}		
	}
}

