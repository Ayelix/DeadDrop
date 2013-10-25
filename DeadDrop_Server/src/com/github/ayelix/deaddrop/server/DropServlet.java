package com.github.ayelix.deaddrop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.ayelix.deaddrop.Location;

/**
 * Handles drops (uploaded via /drop).
 * 
 * @author Alex
 */
public class DropServlet extends HttpServlet {
	static int tag = 0;
	static int data = 100;

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
				final String tag = (String) reqObj.get("tag");
				final String data = (String) reqObj.get("data");
				final double lat = (double) reqObj.get("lat");
				final double lon = (double) reqObj.get("long");
				final double accuracy = (double) reqObj.get("accuracy");
				final String imageStr = (String) reqObj.get("image");
				
				// Create a Drop with the parsed values
				final Drop drop = new Drop(tag, data, new Location(lat, lon),
						accuracy, image);

				resp.setStatus(HttpServletResponse.SC_OK);
				respObj.put("status", "OK");
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