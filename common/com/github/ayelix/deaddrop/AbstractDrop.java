package com.github.ayelix.deaddrop;

/**
 * Abstract class for <i>almost</i> all the data, metadata, and operations
 * related to a drop. The image-related data and operations are missing because
 * they vary between server and client.
 * 
 * @author Alex
 * 
 */
public abstract class AbstractDrop {
	/** The tag identifying the drop */
	private String m_tag;
	/** The dropped data */
	private String m_data;
	/** The drop location */
	private Location m_location;
	/** The required accuracy of a user's location to pick up the drop (in miles). */
	private double m_locationAccuracyMi;

	/** Create a drop with the given data and metadata. */
	public AbstractDrop(final String tag, final String data,
			final Location location, final double locationAccuracyInMiles) {
		m_tag = tag;
		m_data = data;
		m_location = location;
		m_locationAccuracyMi = locationAccuracyInMiles;
	} // End constructor

	public String getTag() {
		return m_tag;
	}

	public void setTag(String tag) {
		this.m_tag = tag;
	}

	public String getData() {
		return m_data;
	}

	public void setData(String data) {
		this.m_data = data;
	}

	public Location getLocation() {
		return m_location;
	}

	public void setLocation(Location location) {
		this.m_location = location;
	}

	/**
	 * Get the required accuracy for this drop in miles.
	 * 
	 * @return Maximum permissible distance in miles.
	 */
	public double getLocationAccuracy() {
		return m_locationAccuracyMi;
	}

	/**
	 * Set the required accuracy for this drop in miles.
	 * 
	 * @param locationAccuracyKm
	 *            Maximum permissible distance in miles.
	 */
	public void setLocationAccuracy(double locationAccuracyMiles) {
		this.m_locationAccuracyMi = locationAccuracyMiles;
	}

} // End class AbstractDrop