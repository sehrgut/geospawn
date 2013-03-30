/**
 * 
 */
package com.alphahelical.geospawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Keith Beckman
 *
 */
public class JoinListener implements Listener {

	private GeoSpawn plugin;
	
	public JoinListener(GeoSpawn plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPlayedBefore()) {
			String country_code = this.plugin.countryCodeOfPlayer(p);
			Location spawn = this.plugin.retrieveGeoSpawn(country_code);
			if (spawn != null) p.teleport(spawn, TeleportCause.PLUGIN);
			
		}
	}
		
}
