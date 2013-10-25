package com.github.ayelix.deaddrop.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	}

	/** Simply calls doGet() with the same parameters. */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
}