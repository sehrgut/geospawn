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
public class InterceptTeleportStrategy implements ITeleportStrategy {

	private int timeout;
	private Plugin plugin;
	
	public InterceptTeleportStrategy(Plugin plugin, int timeout) throws NullArgumentException, IllegalArgumentException {
		if(plugin == null) throw new NullArgumentException("plugin");
		if(timeout < 0) throw new IllegalArgumentException("timeout must not be negative");
		
		this.plugin = plugin;
		this.timeout = timeout;
	}
	
	@Override
	public void teleport(Player p, Location l) {
		new TeleportInterceptor(this.plugin, p, l, this.timeout);
	}

}
