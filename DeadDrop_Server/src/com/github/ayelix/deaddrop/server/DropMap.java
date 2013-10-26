package com.github.ayelix.deaddrop.server;

import java.util.HashMap;

import com.github.ayelix.deaddrop.Drop;

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

	/**
	 * Shortcut method to check if a Drop exists in the map (i.e. one with the
	 * given drop's ID).
	 * 
	 * @param drop
	 *            Drop to check for
	 * @return true if the map contains a Drop with the same ID as the given
	 *         Drop. Otherwise, false.
	 */
	public boolean contains(final Drop drop) {
		return containsKey(drop.getTag());
	}
}
