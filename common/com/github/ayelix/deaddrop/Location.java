package com.github.ayelix.deaddrop;

/**
 * GPS location. Provides method for calculating great-circle distance from
 * another Location (via distanceFrom()). <b>All distances to/from this class
 * must be in kilometers.</b>
 * 
 * @author Alex
 * 
 */
public class Location {
	/** Average radius of the Earth in km, used in distanceFrom(). */
	private static final int EARTH_RADIUS_KM = 6371;

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
	 * Returns great-circle distance from a given Location in kilometers. The
	 * calculation is performed using the haversine formula.
	 * 
	 * @param target
	 *            Second Location for distance calculation.
	 * @return Great-circle distance in kilometers.
	 */
	public double distanceFrom(Location target) {
		throw new UnsupportedOperationException(
				"Location.distanceFrom() is not yet implemented.");
	}
}
