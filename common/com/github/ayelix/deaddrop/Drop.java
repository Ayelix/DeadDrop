package com.github.ayelix.deaddrop;

/**
 * Data, metadata, and operations related to a drop. The image is stored as a
 * string so that it is platform-idependent.
 * 
 * @author Alex
 * 
 */
public class Drop {
	/** The tag identifying the drop */
	private String m_tag;
	/** The dropped data */
	private String m_data;
	/** The drop location */
	private Location m_location;
	/** The drop image */
	private String m_image;
	/**
	 * The required accuracy of a user's location to pick up the drop (in
	 * miles).
	 */
	private double m_locationAccuracyMi;

	/** Create a drop with the given data and metadata. */
	public Drop(final String tag, final String data, final Location location,
			final double locationAccuracyInMiles, final String image) {
		m_tag = tag;
		m_data = data;
		m_location = location;
		m_locationAccuracyMi = locationAccuracyInMiles;
		m_image = image;
	} // End constructor

	public String toString() {
		return super.toString() + ", tag=" + getTag() + ", data=" + getData()
				+ ", accuracy=" + getLocationAccuracy();
	}

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

	public String getImage() {
		return m_image;
	}

	public void setImage(final String image) {
		m_image = image;
	}
} // End class AbstractDrop
