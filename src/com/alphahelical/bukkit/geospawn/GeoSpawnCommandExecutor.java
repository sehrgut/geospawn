/**
 * 
 */
package com.alphahelical.bukkit.geospawn;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alphahelical.bukkit.teleport.DirectTeleportStrategy;
import com.alphahelical.bukkit.teleport.ITeleportStrategy;
import com.alphahelical.collections.ArrayUtil;
import com.alphahelical.util.EnumUtil;

/**
 * @author Keith Beckman
 *
 */
public class GeoSpawnCommandExecutor implements CommandExecutor {

	private enum Commands {
		GEOSPAWN,
		UNKNOWN;		
	}

	private ITeleportStrategy tp = null;
	private ITeleportStrategy getTeleportStrategy() {
		if (tp == null)
			tp = new DirectTeleportStrategy();
		return tp;
	}
	
	private GeoSpawn plugin = null;
	
	public GeoSpawnCommandExecutor(GeoSpawn plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Commands cmdname = EnumUtil.find(Commands.class, cmd.getName(), Commands.UNKNOWN);

		switch(cmdname) {
			case GEOSPAWN:
				return doGeoSpawn(sender, label, args);
			case UNKNOWN:
				sender.sendMessage("Don't know how it happened, but I seem to be registered for commands I don't know.");
				return false;
		}

		return false;
	}

	public boolean doGeoSpawn(CommandSender sender, String label, String[] args) {

		if(args.length == 0) {
			sender.sendMessage("Error: No subcommand specified. Use set, get, tp, or delete.");
			return false;
		}
		
		String subcmd = args[0];
		String[] subargs = ArrayUtil.shiftOff(args);
		
		
		
		if(args.length > 0 && subcmd.equalsIgnoreCase("set")) {
			return doGeoSpawn_Set(sender, label, subargs);
		} else if(args.length > 0 && subcmd.equalsIgnoreCase("get")) {
			return doGeoSpawn_Get(sender, label, subargs);
		} else if(args.length > 0 && subcmd.equalsIgnoreCase("tp")) {
			return doGeoSpawn_Tp(sender, label, subargs);
		} else if(args.length > 0 && subcmd.equalsIgnoreCase("delete")) {
			return doGeoSpawn_Delete(sender, label, subargs);
		}
		return false;
	}
	
	public boolean doGeoSpawn_Delete(CommandSender sender, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("Error: Please provide a country code. That's the whole point.");
			return false;
		}

		if(args.length > 1) {
			sender.sendMessage("Error: Too many arguments.");
			return false;
		}
		
		if(args[0].length() != 2) {
			sender.sendMessage("Error: 2-character ISO country codes must be used.");
			return false;
		}			

		String country_code = args[0].toUpperCase();
		this.plugin.deleteGeoSpawn(country_code);
		sender.sendMessage(String.format("Deleted GeoSpawn for %s.", country_code));
		
		return true;
	}
	
	public boolean doGeoSpawn_Tp(CommandSender sender, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("Error: Please provide a country code. That's the whole point.");
			return false;
		}

		if(args.length > 1) {
			sender.sendMessage("Error: Too many arguments.");
			return false;
		}
		
		if(args[0].length() != 2) {
			sender.sendMessage("Error: 2-character ISO country codes must be used.");
			return false;
		}			

		if(!(sender instanceof Player)) {
			sender.sendMessage("Error: Cannot teleport a console.");
			return false;
		}
		
		String country_code = args[0].toUpperCase();
		Location l = this.plugin.retrieveGeoSpawn(country_code);
		if(l == null) {
			sender.sendMessage(String.format("No GeoSpawn set for %s.", country_code));
		} else {
			Player p = (Player)sender;
			this.getTeleportStrategy().teleport(p, l);
		}
		
		return true;
	}
	
	public boolean doGeoSpawn_Get(CommandSender sender, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("Error: Please provide a country code. That's the whole point.");
			return false;
		}

		if(args.length > 1) {
			sender.sendMessage("Error: Too many arguments.");
			return false;
		}
		
		if(args[0].length() != 2) {
			sender.sendMessage("Error: 2-character ISO country codes must be used.");
			return false;
		}			
	
		String country_code = args[0].toUpperCase();
		Location l = this.plugin.retrieveGeoSpawn(country_code);
		if(l == null) {
			sender.sendMessage(String.format("No GeoSpawn set for %s", country_code));
		} else {
			String msg = String.format("GeoSpawn for %s is %s:%f,%f,%f:%f.",
					country_code, l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw());
			sender.sendMessage(msg);
		}
		return true;
	}

	public boolean doGeoSpawn_Set(CommandSender sender, String label, String[] args) {
			if (! (sender instanceof Player)) {
				sender.sendMessage("Error: Only players can set GeoSpawns to their current location.");
				return false;
			}
			
			if(args.length == 0) {
				sender.sendMessage("Error: Please provide a country code. That's the whole point.");
				return false;
			}
			
			if(args.length > 1) {
				sender.sendMessage("Error: Too many arguments.");
				return false;
			}
			
			if(args[0].length() != 2) {
				sender.sendMessage("Error: 2-character ISO country codes must be used.");
				return false;
			}			
			
			Player p = (Player) sender;
			Location l = p.getLocation();
			String country_code = args[0].toUpperCase();
			
			this.plugin.storeGeoSpawn(l, country_code);
			
			Location l2 = this.plugin.retrieveGeoSpawn(country_code);
			if(l2 == null) {
				sender.sendMessage("Error: could not set GeoSpawn because of Reasons.");
			} else {
				String msg = String.format("Set GeoSpawn for %s to %s:%f,%f,%f:%f.",
						country_code, l2.getWorld().getName(), l2.getX(), l2.getY(), l2.getZ(), l2.getYaw());
				sender.sendMessage(msg);
				//TODO: test l2 == l1 for world and int x y z yaw
			}
		return true;
	}
	

}
