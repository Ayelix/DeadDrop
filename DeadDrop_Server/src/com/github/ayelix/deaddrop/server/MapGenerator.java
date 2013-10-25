package com.github.ayelix.deaddrop.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.ayelix.deaddrop.Drop;
import com.github.ayelix.deaddrop.Location;

public final class MapGenerator {
	private static Random numberGenerator = new Random();

	private static List<Drop> dropList;

	public static void populate(final DropMap map, final int count) {
		fillList();

		final int listSize = dropList.size();

		for (int i = 0; i < count; i++) {
			final int index = numberGenerator.nextInt(listSize);
			map.put(dropList.get(index));
			System.out.println("Added to map: " + dropList.get(index));
		}
	}

	private static double randomAccuracy() {
		return numberGenerator.nextDouble() * 9.9 + 0.1;
	}

	private static void fillList() {
		if (null == dropList) {
			dropList = new ArrayList<Drop>();

			dropList.add(new Drop("shanghai-tag", "shanghai-data",
					new Location(31.23, 121.47), randomAccuracy(), null));
			dropList.add(new Drop("bombay-tag", "bombay-data", new Location(
					18.96, 72.82), randomAccuracy(), null));
			dropList.add(new Drop("buenosAires-tag", "buenosAires-data",
					new Location(-34.61, -58.37), randomAccuracy(), null));
			dropList.add(new Drop("moscow-tag", "moscow-data", new Location(
					55.75, 37.62), randomAccuracy(), null));
			dropList.add(new Drop("tokyo-tag", "tokyo-data", new Location(
					35.67, 139.77), randomAccuracy(), null));
			dropList.add(new Drop("mexicoCity-tag", "mexicoCity-data",
					new Location(19.43, -99.14), randomAccuracy(), null));
			dropList.add(new Drop("newYork-tag", "newYork-data", new Location(
					40.67, -73.94), randomAccuracy(), null));
			dropList.add(new Drop("london-tag", "london-data", new Location(
					51.52, -0.1), randomAccuracy(), null));
			dropList.add(new Drop("bagdad-tag", "bagdad-data", new Location(
					33.33, 44.44), randomAccuracy(), null));
			dropList.add(new Drop("toronto-tag", "toronto-data", new Location(
					43.65, -79.38), randomAccuracy(), null));
			dropList.add(new Drop("saintPetersburg-tag",
					"saintPetersburg-data", new Location(59.93, 30.32),
					randomAccuracy(), null));
			dropList.add(new Drop("losAngeles-tag", "losAngeles-data",
					new Location(34.11, -118.41), randomAccuracy(), null));
			dropList.add(new Drop("sydney-tag", "sydney-data", new Location(
					-33.87, 151.21), randomAccuracy(), null));
			dropList.add(new Drop("melbourne-tag", "melbourne-data",
					new Location(-37.81, 144.96), randomAccuracy(), null));
			dropList.add(new Drop("capeTown-tag", "capeTown-data",
					new Location(-33.93, 18.46), randomAccuracy(), null));
			dropList.add(new Drop("berlin-tag", "berlin-data", new Location(
					52.52, 13.38), randomAccuracy(), null));
			dropList.add(new Drop("montreal-tag", "montreal-data",
					new Location(45.52, -73.57), randomAccuracy(), null));
			dropList.add(new Drop("nanjing-tag", "nanjing-data", new Location(
					32.05, 118.78), randomAccuracy(), null));
		}
	}
}
