package com.library;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import javax.ws.rs.core.UriBuilder;

import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.library.dao.DAOFactory;
import com.library.service.LibraryService;
import com.library.service.ServiceExceptionMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.log4j.Logger;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main Class (Starting point)
 */
public class Application
{
	private static Logger log = Logger.getLogger(Application.class);

	public static final URI BASE_URI = getBaseURI();
	public static final int defaultPort = 9998;

	public static ResourceConfig createApp()
	{
		ResourceConfig resourceConfig = new ResourceConfig();
		
		//return new ResourceConfig(). //register(new JettisonFeature())
						//.packages("org.glassfish.jersey.examples.console");
		return resourceConfig;
	}

	private static int getPort( int defaultPort )
	{
		final String port = System
						.getProperty("jersey.config.test.container.port");
		if (null != port)
		{
			try
			{
				return Integer.parseInt(port);
			}
			catch (NumberFormatException e)
			{
				System.out.println(
								"Value of jersey.config.test.container.port property"
												+ " is not a valid positive integer ["
												+ port + "]."
												+ " Reverting to default ["
												+ defaultPort + "].");
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost/resources")
						.port(getPort(defaultPort)).build();
	}

	public static void main( String[] args ) throws Exception
	{
		// Initialize database with demo data
		log.info("Initialize demo .....");

		DAOFactory dataDaoFactory = DAOFactory.getDAOFactory(DAOFactory.DATA);
		dataDaoFactory.populateTestData();

		log.info("Initialisation Complete....");

		// Host service on jetty
		startService();
	}

	/**
	 * Service provisioning.
	 * 
	 * @throws Exception
	 */
	private static void startService() throws Exception
	{
		// Server server = new Server(8080);
		//
		// ServletContextHandler context = new ServletContextHandler(
		// ServletContextHandler.SESSIONS);
		//
		// context.setContextPath("/");
		//
		// server.setHandler(context);
		//
		// ServletHolder servletHolder =
		// context.addServlet(ServletContainer.class, "/*");
		// servletHolder.setInitParameter(
		// "jersey.config.server.provider.classnames",
		// LibraryService.class.getCanonicalName() + "," +
		// ServiceExceptionMapper.class.getCanonicalName() + ","+
		// TransactionService.class.getCanonicalName());
		// /*
		// * Start server. Provide for closure
		// */
		// try
		// {
		// // server. start();
		// server.join();
		// }
		// finally
		// {
		// server.destroy();
		// }

		try
		{
			System.out.println("Simple Console Jersey Example App");

			// final
			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
							///
							//GrizzlyHttpServerFactory
							//.createHttpServer(getBaseURI(), createApp(), false);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					server.stop(0); //  shutdownNow();
				}
			}));
			server.start();

			System.out.println(String.format(
						"Application started.%nTry out %s%nStop the application using CTRL+C",
						BASE_URI + "/form"));

			Thread.currentThread().join();
		}
		catch (IOException | InterruptedException ex)
		{
			log.error(Level.SEVERE, ex);
		}
	}

}
