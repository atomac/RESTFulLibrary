package com.library.dao;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import com.library.dao.impl.LibraryDAOImpl;
import com.library.utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Data DAO
 */
public class DataDAOFactory extends DAOFactory
{
	private static Logger log = Logger.getLogger(DataDAOFactory.class);

	// Key-Value from application.properties.
	private static final String DATA_DRIVER = "data_driver";
	private static final String DATA_CONN_URL = "data_connection_url";
	private static final String DATA_USER = "data_user";
	private static final String DATA_PASSWORD = "data_password";

	// Obtain the values from the application.properties file.
	private static final String data_driver = Utils
					.getStringProperty(DATA_DRIVER);
	private static final String data_connection_url = Utils
					.getStringProperty(DATA_CONN_URL);
	private static final String data_user = Utils.getStringProperty(DATA_USER);
	private static final String data_password = Utils
					.getStringProperty(DATA_PASSWORD);
	
	private final LibraryDAOImpl libraryDAO = new LibraryDAOImpl();

	/**
	 * Data DAO constructor for the data driver
	 */
	DataDAOFactory()
	{
		// init: load driver
		DbUtils.loadDriver(data_driver);
	}

	/**
	 * Connect to the data base for library details.
	 */
	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(data_connection_url, data_user,
						data_password);
	}

	/**
	 * Obtain the Library Data Access Object
	 */
	@Override
	public LibraryDAO getLibraryDAO()
	{
		return libraryDAO;
	}

	/**
	 * Execute the process to populate the library test data.
	 */
	@Override
	public void populateTestData()
	{
		log.info("Populating Test Library Table and data ..... ");

		Connection conn = null;

		// Connect to data base and read data source file
		try
		{
			conn = DataDAOFactory.getConnection();
			RunScript.execute(conn,
							new FileReader("src/test/resources/demo.sql"));
		}
		// Catch SQL Exception
		catch (SQLException e)
		{
			log.error("populateTestData(): Error populating library data: ", e);

			throw new RuntimeException(e);
		}
		// Catch File Not Found Exception.
		catch (FileNotFoundException e)
		{
			log.error("populateTestData(): Error finding test script file ", e);

			throw new RuntimeException(e);
		}
		// Close the Database.
		finally
		{
			DbUtils.closeQuietly(conn);
		}
	}


}
