package com.alphahelical.bukkit.geospawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * 
 */

/**
 * @author Keith Beckman
 * 
 */
public class DirectTeleportStrategy implements ITeleportStrategy {

	public DirectTeleportStrategy() {

	}

	@Override
	public void teleport(Player p, Location l) {
		p.teleport(l, TeleportCause.PLUGIN);
	}

}
