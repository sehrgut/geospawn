/**
 * 
 */
package com.alphahelical.bukkit.geospawn;

import java.net.InetAddress;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import com.alphahelical.bukkit.geoip.GeoIp;

/**
 * @author Keith Beckman
 *
 */
public class Util {
	private Util() {}
		
	/**
	 * This cannot be used during PlayerLoginEvent. Use countryCodeOfPlayer(PlayerLoginEvent) instead.
	 * @param p
	 * @return Stored or newly-looked-up country code of player
	 */
	public static String countryCodeOfPlayer(final Player p) {		
		return countryCodeOfPlayer(p, p.getAddress().getAddress());
	}

	/**
	 * Only use during PlayerLoginEvent. At other times, use countryCodeOfPlayer(Player) instead.
	 * @param e PlayerLoginEvent to provide player and address
	 * @return Stored or newly-looked-up country code of player
	 */
	public static String countryCodeOfPlayer(final PlayerLoginEvent e) {
		return countryCodeOfPlayer(e.getPlayer(), e.getAddress());
	}
	
	private static String countryCodeOfPlayer(final Player p, final InetAddress addr) {
		// TODO: Bring back metadata system, because it was cool.
		return GeoIp.countryCodeFromAddress(addr == null ? addr : p.getAddress().getAddress());
	}

	
}
