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
	/** Intent extra for a drop accuracy. */
	public static final String EXTRA_ACCURACY = "com.github.ayelix.deaddrop.androidclient.EXTRA_ACCURACY";
	/** Intent extra for a drop data string. */
	public static final String EXTRA_DATA = "com.github.ayelix.deaddrop.androidclient.EXTRA_DATA";
	/** Intent extra for a drop image string. */
	public static final String EXTRA_IMAGE = "com.github.ayelix.deaddrop.androidclient.EXTRA_IMAGE";

	/** Path for drop requests. */
	public static final String DROP_PATH = "/drop";
	/** Path for pickup requests. */
	public static final String PICKUP_PATH = "/pickup";
}
