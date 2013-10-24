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
	/** The required accuracy of a user's location to pick up the drop (in km). */
	private double m_locationAccuracyKm;

	/** Create a drop with the given data and metadata. */
	public AbstractDrop(final String tag, final String data, final Location location,
			final double locationAccuracyInKilometers) {
		m_tag = tag;
		m_data = data;
		m_location = location;
		m_locationAccuracyKm = locationAccuracyInKilometers;
	} // End constructor

} // End class AbstractDrop
