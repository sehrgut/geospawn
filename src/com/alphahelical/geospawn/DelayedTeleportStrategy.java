/**
 * 
 */
package com.alphahelical.geospawn;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Keith Beckman
 *
 */
public class DelayedTeleportStrategy implements ITeleportStrategy {
	private int delay;
	private Plugin plugin;
	
	public DelayedTeleportStrategy(Plugin plugin, int delay) throws NullArgumentException, IllegalArgumentException {
		if(plugin == null) throw new NullArgumentException("plugin");
		if(delay < 0) throw new IllegalArgumentException("delay must not be negative");
		
		this.plugin = plugin;
		this.delay = delay;
	}
	
	public void teleport(Player p, Location l) {
		new DelayedTeleport(this.plugin, p, l, this.delay);
	}
}

