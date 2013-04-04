/**
 * 
 */
package com.alphahelical.bukkit.geospawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Keith Beckman
 *
 */
public class TeleportInterceptor implements Listener, Runnable {
	private Player p = null;
	private Location l = null;
	
	public TeleportInterceptor(Plugin plugin, Player p, Location l, long cancelAfterTicks) throws IllegalArgumentException {
		if (p == null) throw new IllegalArgumentException("Player must not be null.");
		if (l == null) throw new IllegalArgumentException("Location must not be null.");
		
		this.p = p;
		this.l = l;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, cancelAfterTicks);
	}
	
	public void run() {
		this.cancel();
	}
		
	public void cancel() {
		PlayerTeleportEvent.getHandlerList().unregister(this);		
	}
	
	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if(e.getPlayer().equals(this.p)) {
			e.setTo(l);
			this.cancel();
		}
	}
	
}