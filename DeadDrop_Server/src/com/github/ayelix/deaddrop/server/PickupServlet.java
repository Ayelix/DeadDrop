package com.github.ayelix.deaddrop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.ayelix.deaddrop.Drop;
import com.github.ayelix.deaddrop.JSONParser;
import com.github.ayelix.deaddrop.Location;

/**
 * Handles pickups (retrieved via /pickup).
 * 
 * @author Alex
 */
public final class PickupServlet extends HttpServlet {
	/**
	 * Checks incoming request type, parses provided JSON object, and returns
	 * the drop if the user is close enough (and the requested drop exists).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// Prepare to respond with JSON
		resp.setContentType("application/json");
		final PrintWriter writer = resp.getWriter();
		final BufferedReader reader = req.getReader();
		final JSONObject respObj = new JSONObject();

		// Assume error unless all the request checks pass
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		// Get the request content type
		final String contentType = req.getContentType();

		// Make sure there is a content type
		if (null != contentType) {
			// Make sure the content type is correct
			if (contentType.equals("application/json")) {
				// Parse the incoming JSON text
				final JSONObject reqObj = (JSONObject) JSONValue.parse(reader);
				final Drop parsedDrop = JSONParser.parseDrop(reqObj, true);

				// Check if a drop exists with the parsed tag
				final Drop drop = DropMap.getInstance()
						.get(parsedDrop.getTag());
				if (null != drop) {
					try {
						// Check the user's location against the required
						// accuracy
						// for the drop
						final Location userLocation = parsedDrop.getLocation();
						final Location dropLocation = drop.getLocation();
						final Double distance = userLocation
								.distanceFrom(dropLocation);
						if (distance <= drop.getLocationAccuracy()) {
							// Populate the JSON response with the relevant drop
							// info
							respObj.put("data", drop.getData());
							respObj.put("image", drop.getImage());
							respObj.put("distance", distance);

							// Mark the response as OK
							resp.setStatus(HttpServletResponse.SC_OK);
							respObj.put("status", "OK");

						} else {
							respObj.put("status",
									"Drop not valid in current location.");
						}

					} catch (NullPointerException npe) {
						// This means something was null in the location
						// parameters
						respObj.put("status",
								"One or more parameters missing from request.");
					}

				} else {
					respObj.put("status", "Drop not found for given tag.");
				}

			} else {
				respObj.put("status", "Invalid content type");
			}

		} else {
			respObj.put("status", "Missing content type");
		}

		// Write the JSON response object
		writer.write(respObj.toJSONString());
	}

	/** Simply calls doGet() with the same parameters. */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
}
