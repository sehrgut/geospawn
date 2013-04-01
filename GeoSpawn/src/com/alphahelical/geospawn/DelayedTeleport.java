package com.alphahelical.geospawn;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

/** 
 * Can be used to teleport the player on a slight delay, which gets around a nasty issue that can crash
 * the server if you teleport them during certain events (such as onPlayerJoin).
 * @author Keith Beckman
 * @url https://github.com/andune/HomeSpawnPlus/blob/master/src/main/java/org/morganm/homespawnplus/HomeSpawnUtils.java
 *
 */

public class DelayedTeleport implements Runnable {
	Player p = null;
	Location l = null;
	
	public DelayedTeleport(Plugin plugin, Player p, Location l, long ticks) throws IllegalArgumentException {
		if (p == null) throw new IllegalArgumentException("Player must not be null.");
		if (l == null) throw new IllegalArgumentException("Location must not be null.");

		this.p = p;
		this.l = l;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, ticks);
	}
	
	public void run() {
		Logger.getLogger("DelayedTeleport").info(this.l.toVector().toString());
		this.p.teleport(this.l, TeleportCause.PLUGIN);
	}
}
