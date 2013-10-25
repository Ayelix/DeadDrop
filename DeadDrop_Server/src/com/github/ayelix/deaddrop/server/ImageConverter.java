package com.github.ayelix.deaddrop.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public final class ImageConverter {
	/**
	 * Converts a Base64 encoded string to a BufferedImage.
	 * 
	 * @param imageStr
	 *            String to decode.
	 * @return BufferedImage containing result or null if an error occurred.
	 */
	public static BufferedImage stringToImage(final String imageStr) {
		BufferedImage image = null;

		// Decode the Base64 string
		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageStr);
		//
		ByteArrayInputStream stream = new ByteArrayInputStream(decodedBytes);
		try {
			image = ImageIO.read(stream);
			stream.close();
		} catch (IOException e) {
			// Eat the exception and just return null
			e.printStackTrace();
		}

		return image;
	}

	/**
	 * Converts a BufferedImage to a Base64 encoded string.
	 * 
	 * @param image
	 *            Image to encode.
	 * @return String containing result or null if an error occurred.
	 */
	public static String imageToString(final BufferedImage image) {
		String imageStr = null;

		// Write the image to a byte stream
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", stream);
			stream.flush();
			// Create a string from the byte stream's contents
			imageStr = DatatypeConverter
					.printBase64Binary(stream.toByteArray());
			stream.close();
		} catch (IOException e) {
			// Eat the exception and just return null
			e.printStackTrace();
		}

		return imageStr;
	}
}
