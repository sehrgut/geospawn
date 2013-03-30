/**
 * 
 */
package com.alphahelical.geospawn;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Keith Beckman
 *
 */
public class GeoSpawnCommandExecutor implements CommandExecutor {

	private enum Commands {
		GEOSPAWN;
		
		public static Commands find(String test) {
			for (Commands c : Commands.values())
				if (c.name().equalsIgnoreCase(test))
					return c;
			return null;
		}
		
	}

	GeoSpawn plugin = null;
	
	public GeoSpawnCommandExecutor(GeoSpawn plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Commands cmdname = Commands.find(cmd.getName());

		switch(cmdname) {
			case GEOSPAWN:
				return doGeoSpawn(sender, label, Arrays.copyOfRange(args, 1, args.length));
		}

		return false;
	}

	public boolean doGeoSpawn(CommandSender sender, String label, String[] args) {
		if(args[0].equalsIgnoreCase("set")) {
			return doGeoSpawn_Set(sender, label, Arrays.copyOfRange(args,1,args.length));
		}
		return false;
	}
	
	public boolean doGeoSpawn_Set(CommandSender sender, String label, String[] args) {
		if((args.length == 1)) {
			if (! (sender instanceof Player)) {
				sender.sendMessage("Error: Only players can set GeoSpawns to their current location.");
				return false;
			}
// TODO: check args[0] for valid country code
			Player p = (Player) sender;
			
			this.plugin.storeGeoSpawn(p.getLocation(), args[0]);
		}
		return false;
	}
	

}
