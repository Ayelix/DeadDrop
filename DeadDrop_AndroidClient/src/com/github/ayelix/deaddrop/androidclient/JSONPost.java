package com.github.ayelix.deaddrop.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.util.Log;

public final class JSONPost {
	private final String m_uri;
	private final JSONObject m_json;

	private int m_status = 0;
	private JSONObject m_resultJson = new JSONObject();

	/**
	 * Creates a JSON-formatted HTTP POST to the given URI with the given JSON
	 * data.
	 * 
	 * @param uri
	 *            Destination address
	 * @param json
	 *            JSON object to write to the POST
	 */
	public JSONPost(final String uri, final JSONObject json) {
		m_uri = uri;
		m_json = json;
	}

	/**
	 * Attempts to execute the post and receive the results.
	 * 
	 * @return true on success, false on failure.
	 */
	public boolean execute() {
		// Get the HTTP client and request
		final HttpClient client = new DefaultHttpClient();
		final HttpPost request = new HttpPost(m_uri);

		// Write the JSON string to the request
		request.setHeader("Content-Type", "application/json");
		try {
			request.setEntity(new StringEntity(m_json.toJSONString()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		}

		try {
			// Get the response
			final HttpResponse response = client.execute(request);
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			// Get the status code
			m_status = response.getStatusLine().getStatusCode();

			// Create a JSONObject to parse to results
			m_resultJson = (JSONObject) JSONValue.parse(responseReader);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public int getStatus() {
		return m_status;
	}

	public JSONObject getJSON() {
		return m_resultJson;
	}
}
