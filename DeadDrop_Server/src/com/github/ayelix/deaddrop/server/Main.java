package com.github.ayelix.deaddrop.server;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public final class Main extends HttpServlet {
	/** Command-line argument passed to generate drops at startup. */
	private static final String ARGUMENT_GENERATE = "-g";

	/**
	 * Starts the server and optionally generates some sample drops (since the
	 * drops are not currently persistent when the server is stopped).
	 * 
	 * @param args
	 *            Command-line arguments. Pass the argument "-g" to enable drop
	 *            generation.
	 */
	public static void main(String[] args) {
		// Check for command-line arguments
		if (1 == args.length) {
			if (args[0].equals(ARGUMENT_GENERATE)) {
				generateMap();
			}
		} else if (args.length > 1) {
			System.err
					.println("Too many arguments, this server accepts one optional argument "
							+ ARGUMENT_GENERATE
							+ " to generate sample drops at startup.");
			System.exit(1);
		}
		
		// Start the server
		Server server = new Server(8080);
		WebAppContext context = new WebAppContext();
		context.setWar("war");
		context.setContextPath("/");
		server.setHandler(context);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.err.println("Could not start server:");
			e.printStackTrace();
		}
	}

	/** Add some sample drops to the DropMap. */
	public static void generateMap() {
		DropMap map = DropMap.getInstance();

		MapGenerator.populate(map, 3);

		System.out.println("Drops generated.");
	}
}
