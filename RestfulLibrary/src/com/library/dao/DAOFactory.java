package com.library.dao;

/**
 * Data Access object factory.
 * 
 * @author mccormam
 */
public abstract class DAOFactory
{
	// DATA enumeration.
	public static final int DATA = 1;

	// Retrieve Library Data Access Objects.
	public abstract LibraryDAO getLibraryDAO();

	// Populate the data for testing.
	public abstract void populateTestData();

	// Establish the Data Access Object factory
	public static DAOFactory getDAOFactory( int factoryCode )
	{
		// Check on the factory code enumeration, for source of data.
		switch (factoryCode)
		{
			case DATA:
			{
				return new DataDAOFactory();
			}
			default:
			{
				// by default using Data in memory database
				return new DataDAOFactory();
			}
		}
	}
}
