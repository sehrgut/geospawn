/**
 * 
 */
package com.alphahelical.geospawn;

import java.net.InetAddress;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author Keith Beckman
 *
 */
public class Util {
	private Util() {}
		
	public static <T extends Enum<T>> T findInEnum(Class<T> e, String val, T default_value) {
		for (T c : e.getEnumConstants())
			if (c.name().equalsIgnoreCase(val)) return c;
		return default_value;
	}
	
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
		return countryCodeFromAddress(addr == null ? addr : p.getAddress().getAddress());
	}
	
	public static String countryCodeFromAddress(final InetAddress addr) {
		String country_code = Config.getPlugin().getGeoIp().getCountry(addr).getCode();
		country_code = (country_code.equalsIgnoreCase("--") ? null : country_code);
		return country_code;
	}

	public static <T> T[] shiftOff(T[] a) {
		return (a.length == 0 ? a : Arrays.copyOfRange(a, 1, a.length));
	}

	
}
