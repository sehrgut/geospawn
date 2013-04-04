package com.alphahelical.bukkit.geospawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ITeleportStrategy {
	public void teleport(Player p, Location l);
}
