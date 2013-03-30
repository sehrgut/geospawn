/**
 * 
 */
package com.alphahelical.geospawn;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


import uk.org.whoami.geoip.GeoIPLookup;
import uk.org.whoami.geoip.GeoIPTools;


/**
 * @author Keith Beckman
 *
 */
public class GeoSpawn extends JavaPlugin {
	private GeoSpawnCommandExecutor cmdex;

	private GeoIPLookup m_geoIp = null;
	public GeoIPLookup getGeoIp() {
		if(m_geoIp == null)
			this.m_geoIp = ((GeoIPTools)Bukkit.getServer().getPluginManager().getPlugin("GeoIPTools"))
						.getGeoIPLookup();
		return this.m_geoIp;
	}
	
	private String getMetadataKey() {
		return this.getConfig().get("metadata-key").toString();
	}
	
	private String countryCodeFromAddress(final InetAddress addr) {
		return this.getGeoIp().getCountry(addr).getCode();
	}
	
	
	/**
	 * This cannot be used during PlayerLoginEvent. Use countryCodeOfPlayer(Player, InetAddress) instead.
	 * @param p
	 * @return Stored or newly-looked-up country code of player
	 */
	public String countryCodeOfPlayer(final Player p) {		
		return countryCodeOfPlayer(p, p.getAddress().getAddress());
	}

	/**
	 * Only use during PlayerLoginEvent. At other times, use countryCodeOfPlayer(Player) instead.
	 * @param p
	 * @param addr InetAddress object for player must be supplied if called during PlayerLoginEvent.
	 * @return Stored or newly-looked-up country code of player
	 */
	public String countryCodeOfPlayer(final Player p, final InetAddress addr) {
		String key = this.getMetadataKey();
		if(!p.hasMetadata(key)) {
			String country = this.countryCodeFromAddress(addr == null ? addr : p.getAddress().getAddress());
			p.setMetadata(key,new FixedMetadataValue(this,country));
		}
		// TODO: find first string value, rather than assuming
		return p.getMetadata(key).get(0).asString();
	}
	

	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);

		
		this.cmdex = new GeoSpawnCommandExecutor(this);
		this.getCommand("geospawn").setExecutor(cmdex);
	
		this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
			
	}
	
	@Override
	public void onDisable() {
	}

	protected void storeGeoSpawn(Location loc, String country_code) {
		String path = String.format("spawns.%s", loc.getWorld().getName(), country_code);
		ConfigurationSection spawn = this.getConfig().getConfigurationSection(path);
		spawn.set("world", loc.getWorld());
		spawn.set("coords", loc.toVector());
		spawn.set("yaw", loc.getYaw());
	}

	protected Location retrieveGeoSpawn(String country_code) {
		String path = String.format("spawns.%s",country_code);
		ConfigurationSection spawn = this.getConfig().getConfigurationSection(path);
		World world = this.getServer().getWorld(spawn.getString("world"));
		Vector v = spawn.getVector("coords");
		float yaw = (float)spawn.getDouble("yaw");
		
		Location l = v.toLocation(world, yaw, 0.0f);
		
		return l;
	}
	
	/** Can be used to teleport the player on a slight delay, which gets around a nasty issue that can crash
     * the server if you teleport them during certain events (such as onPlayerJoin).
     * 
     * @param p
     * @param l
     */
	// https://github.com/andune/HomeSpawnPlus/blob/master/src/main/java/org/morganm/homespawnplus/HomeSpawnUtils.java
    public void delayedTeleport(Player p, Location l) {
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new DelayedTeleport(p, l), 2);
    }
    
    private class DelayedTeleport implements Runnable {
    	private Player p;
    	private Location l;
    	
    	public DelayedTeleport(Player p, Location l) {
    		this.p = p;
    		this.l = l;
    	}
    	
    	public void run() {
    		p.teleport(this.l, TeleportCause.PLUGIN);
    	}
    }
}
