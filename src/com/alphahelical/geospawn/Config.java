/**
 * 
 */
package com.alphahelical.geospawn;

import org.bukkit.Bukkit;

/**
 * @author Keith Beckman
 *
 */
public class Config {
	private Config() {}
	
	private static GeoSpawn plugin;
	protected static GeoSpawn getPlugin() {
		if (plugin == null)
			plugin = (GeoSpawn) Bukkit.getServer().getPluginManager().getPlugin("GeoSpawn");
		return plugin;
	}

	public static TeleportModes getTeleportMode() {
		String mode = getPlugin().getConfig().getString("teleport-mode");
		return Util.findInEnum(TeleportModes.class, mode, TeleportModes.DELAY);
	}
	
	public static int getInterceptTimeout() {
		return getPlugin().getConfig().getInt("intercept-timeout");
	}
	
	public static int getTeleportDelay() {
		return getPlugin().getConfig().getInt("teleport-delay");
	}

	public static String getDefaultCountry() {
		return getPlugin().getConfig().getString("default-country");
	}

}
