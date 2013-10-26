package com.github.ayelix.deaddrop;

import org.json.simple.JSONObject;

public final class JSONParser {
	/**
	 * Parses the provided JSON object and creates a Drop with the parsed
	 * values. Values which could not be parsed will be zero or null.
	 * 
	 * @param obj
	 *            JSON object to parse.
	 * @return Drop constructed from parsed values.
	 */
	public static Drop parseDrop(final JSONObject obj) {
		final String tag = parseString(obj, "tag");
		final String data = parseString(obj, "data");
		final Double lat = parseDouble(obj, "lat");
		final Double lon = parseDouble(obj, "long");
		final Double accuracy = parseDouble(obj, "accuracy");
		final String image = parseString(obj, "image");

		// Create a Drop with the parsed values
		return new Drop(tag, data, new Location(lat, lon), accuracy, image);
	}

	/**
	 * Parses a Double from the provided JSON object with the given key. Returns
	 * null if parsing fails.
	 * 
	 * @param obj
	 *            JSON object to parse.
	 * @param key
	 *            JSON key for the desired value.
	 * @return Parsed value or null if parsing fails.
	 */
	public static Double parseDouble(final JSONObject obj, final String key) {
		Double retVal = null;
		try {
			retVal = ((Number) obj.get(key)).doubleValue();
		} catch (Exception e) {
			// No action needed, just return null
			System.out
					.println("JSONParser.parseDouble(): No value parsed for key \""
							+ key + "\"");
		}
		return retVal;
	}

	/**
	 * Parses a String from the provided JSON object with the given key. Returns
	 * null if parsing fails.
	 * 
	 * @param obj
	 *            JSON object to parse.
	 * @param key
	 *            JSON key for the desired value.
	 * @return Parsed value or null if parsing fails.
	 */
	public static String parseString(final JSONObject obj, final String key) {
		String retVal = null;
		try {
			retVal = (String) obj.get(key);
		} catch (Exception e) {
			// No action needed, just return null
			System.out
					.println("JSONParser.parseString(): No value parsed for key \""
							+ key + "\"");
		}
		return retVal;
	}

	/**
	 * Writes a Drop to the provided JSON object.
	 * 
	 * @param drop
	 *            Drop to write
	 * @param obj
	 *            JSONObject to which Drop will be written
	 */
	public static void writeDrop(final Drop drop, final JSONObject obj) {
		obj.put("tag", drop.getTag());
		obj.put("data", drop.getData());
		obj.put("lat", drop.getLocation().getLatitude());
		obj.put("long", drop.getLocation().getLongitude());
		obj.put("accuracy", drop.getLocationAccuracy());
		obj.put("image", drop.getImage());
	}
}
