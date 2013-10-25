package com.github.ayelix.deaddrop.androidclient;

public final class Constants {
	/** Intent action for an attempted pickup. */
	public static final String ACTION_PICKUP = "com.github.ayelix.deaddrop.androidclient.ACTION_PICKUP";
	/** Intent action for a new drop. */
	public static final String ACTION_DROP = "com.github.ayelix.deaddrop.androidclient.ACTION_DROP";

	/** Intent extra for a tag ID string. */
	public static final String EXTRA_ID = "com.github.ayelix.deaddrop.androidclient.EXTRA_ID";
	/** Intent extra for a latitude. */
	public static final String EXTRA_LAT = "com.github.ayelix.deaddrop.androidclient.EXTRA_LAT";
	/** Intent extra for a longitude. */
	public static final String EXTRA_LON = "com.github.ayelix.deaddrop.androidclient.EXTRA_LON";
	
	/** Default server address including port. */
	public static final String DEFAULT_SERVER_ADDR = "http://192.168.1.109:8080";
	/** Path for drop requests. */
	public static final String DROP_PATH = "/drop";
	/** Path for pickup requests. */
	public static final String PICKUP_PATH = "/pickup";
}
