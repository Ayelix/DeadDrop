package com.github.ayelix.deaddrop;

/**
 * GPS location. Provides method for calculating great-circle distance from
 * another Location (via distanceFrom()). <b>All distances to/from this class
 * must be in miles.</b>
 * 
 * @author Alex
 * 
 */
public final class Location {
	/** Average radius of the Earth in miles, used in distanceFrom(). */
	private static final double EARTH_RADIUS_MI = 3958.761;

	/** Latitude portion */
	private double m_latitude;
	/** Longitude portion */
	private double m_longitude;

	public Location(final double latitude, final double longitude) {
		m_latitude = latitude;
		m_longitude = longitude;
	}

	public double getLatitude() {
		return m_latitude;
	}

	public void setLatitude(double latitude) {
		this.m_latitude = latitude;
	}

	public double getLongitude() {
		return m_longitude;
	}

	public void setLongitude(double longitude) {
		this.m_longitude = longitude;
	}

	/**
	 * Returns great-circle distance from a given Location in miles. The
	 * calculation is performed using the haversine formula.
	 * 
	 * @param target
	 *            Second Location for distance calculation.
	 * @return Great-circle distance in miles.
	 */
	public double distanceFrom(Location target) {
		final double targetLat = target.getLatitude();
		final double targetLong = target.getLongitude();

		// Haversine formula adapted from:
		// http://www.movable-type.co.uk/scripts/latlong.html

		final double latDifferenceRadians = Math.toRadians(targetLat
				- m_latitude);
		final double longDifferenceRadians = Math.toRadians(targetLong
				- m_longitude);
		final double myLatRadians = Math.toRadians(m_latitude);
		final double targetLatRadians = Math.toRadians(targetLat);

		final double a = (Math.sin(latDifferenceRadians / 2) * Math
				.sin(latDifferenceRadians / 2))
				+ (Math.sin(longDifferenceRadians / 2) * Math
						.sin(longDifferenceRadians / 2))
				* Math.cos(myLatRadians) * Math.cos(targetLatRadians);
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (EARTH_RADIUS_MI * c);
	}
}
