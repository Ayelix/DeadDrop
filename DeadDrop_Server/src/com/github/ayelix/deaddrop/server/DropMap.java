package com.github.ayelix.deaddrop.server;

import java.util.HashMap;

/**
 * Singleton for a map containing drops.
 * 
 * @author Alex
 * 
 */
public class DropMap extends HashMap<String, Drop> {
	/** Map instance */
	private static DropMap s_dropMap;

	/** Prevent instantiation. */
	protected DropMap() {
	}

	/**
	 * Returns the map instance. If the map has not yet been created, it will be
	 * created.
	 * 
	 * @return Map instance.
	 */
	public static DropMap getInstance() {
		// If the map hasn't been created, do so
		if (null == s_dropMap) {
			s_dropMap = new DropMap();
			System.out.println("DropMap instance created.");
		}
		// Return the map instance
		return s_dropMap;
	}

	/**
	 * Shortcut method to add a Drop using its ID as the key.
	 * 
	 * @param drop
	 *            Drop to add.
	 */
	public void put(final Drop drop) {
		put(drop.getTag(), drop);
	}
}
