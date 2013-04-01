/**
 * 
 */
package com.alphahelical.geospawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


import uk.org.whoami.geoip.GeoIPLookup;
import uk.org.whoami.geoip.GeoIPTools;


/**
 * @author Keith Beckman
 *
 */
public class GeoSpawn extends JavaPlugin {
	
	public TeleportModes getTeleportMode() {
		String mode = this.getConfig().getString("teleport-mode");
		return Util.findInEnum(TeleportModes.class, mode, TeleportModes.DELAY);
	}
	
	public int getInterceptTimeout() {
		return this.getConfig().getInt("intercept-timeout");
	}
	
	public int getTeleportDelay() {
		return this.getConfig().getInt("teleport-delay");
	}
	
	private GeoIPLookup m_geoIp = null;
	protected GeoIPLookup getGeoIp() {
		if(m_geoIp == null)
			this.m_geoIp = ((GeoIPTools)Bukkit.getServer().getPluginManager().getPlugin("GeoIPTools"))
						.getGeoIPLookup();		
		return this.m_geoIp;
	}
	
	private ITeleportStrategy tp = null;
	protected ITeleportStrategy getTeleportStrategy() {
		if (tp == null) {
			switch (this.getTeleportMode()) {
				case DELAY:
					tp = new DelayedTeleportStrategy(this, this.getTeleportDelay());
				case INTERCEPT:
					tp = new InterceptTeleportStrategy(this, this.getInterceptTimeout());
				case DIRECT:
					tp = new DirectTeleportStrategy();
			}
		}
		return tp;
	}
	

	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		this.getCommand("geospawn").setExecutor(new GeoSpawnCommandExecutor(this));
		
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
			
	}
	
	@Override
	public void onDisable() {
		this.saveConfig(); 
	}

	public void deleteGeoSpawn(String country_code) {
		String path = String.format("spawns.%s", country_code.toUpperCase());
		this.getConfig().set(path, null);
		this.saveConfig();
	}
	
	public void storeGeoSpawn(Location l, String country_code) {
		String path = String.format("spawns.%s", country_code.toUpperCase());
		ConfigurationSection spawn = this.getConfig().getConfigurationSection(path);
		
		if (spawn == null)
			spawn = this.getConfig().createSection(path);
		
		l.setX((double)Math.round(l.getX()));
		l.setY((double)Math.round(l.getY()));
		l.setZ((double)Math.round(l.getZ()));
		
		//TODO: Minecraft deals with positive values produced, but really should be in range [-180,180] for cleanliness
		float yaw = Math.round(l.getYaw() / 90) * 90.0f;
		
		l.setYaw(yaw);
		

		spawn.set("world", l.getWorld().getName());
		spawn.set("coords", l.toVector());
		spawn.set("yaw", l.getYaw());
		this.saveConfig();
	}

	public Location retrieveGeoSpawn(String country_code) {
		String path = String.format("spawns.%s",country_code.toUpperCase());
		ConfigurationSection spawn = this.getConfig().getConfigurationSection(path);

		Location l = null;
		
		if(spawn != null) {
			World world = this.getServer().getWorld(spawn.getString("world"));
			Vector v = spawn.getVector("coords");
			float yaw = (float)spawn.getDouble("yaw");
			
			l = v.toLocation(world, yaw, 0.0f);
		}
		
		return l;
	}

	public void sendPlayerToGeoSpawn(Player p, String country_code) {
		Location spawn = this.retrieveGeoSpawn(country_code);
		if (spawn != null) {
			// TODO: SpawnStrategy to pick world spawn if undefined, also to spawn all players
			this.getTeleportStrategy().teleport(p, spawn);
		}
	}

}
