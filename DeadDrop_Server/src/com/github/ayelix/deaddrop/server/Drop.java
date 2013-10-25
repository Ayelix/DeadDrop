package com.github.ayelix.deaddrop.server;

import java.awt.image.BufferedImage;

import com.github.ayelix.deaddrop.AbstractDrop;
import com.github.ayelix.deaddrop.Location;

/**
 * Complete server implementation of a drop, including the associated image.
 * 
 * @author Alex
 *
 */
public final class Drop extends AbstractDrop {
	private BufferedImage m_image;

	public Drop(String tag, String data, Location location,
			double locationAccuracyInMiles) {
		super(tag, data, location, locationAccuracyInMiles);
	}

	public Drop(String tag, String data, Location location,
			double locationAccuracyInMiles, BufferedImage image) {
		super(tag, data, location, locationAccuracyInMiles);
		m_image = image;
	}
	
	public BufferedImage getImage() {
		return m_image;
	}
	
	public void setImage(final BufferedImage image) {
		m_image = image;
	}

}
