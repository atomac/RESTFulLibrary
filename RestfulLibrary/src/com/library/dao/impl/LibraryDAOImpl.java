package com.library.dao.impl;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.library.dao.LibraryDAO;
import com.library.dao.DataDAOFactory;
import com.library.exception.CustomException;
import com.library.model.Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LibraryDAOImpl implements LibraryDAO
{
	private static Logger log = Logger.getLogger(LibraryDAOImpl.class);

	// SQL Statements
	private final static String SQL_GET_LIBRARY_BY_ID = "SELECT * FROM Library WHERE LibraryId = ? ";
	private final static String SQL_LOCK_LIBRARY_BY_ID = "SELECT * FROM Library WHERE LibraryId = ? FOR UPDATE";
	private final static String SQL_CREATE_LIBRARY = "INSERT INTO Library (author, title) VALUES (?, ?)";
	private final static String SQL_UPDATE_LIBRARY = "UPDATE Library SET author = ?, title = ? WHERE LibraryId = ? ";
	private final static String SQL_GET_ALL_LIBRARY = "SELECT * FROM Library";
	private final static String SQL_DELETE_LIBRARY_BY_ID = "DELETE FROM Library WHERE LibraryId = ?";

	/**
	 * Retrieve all the library books from the LIBRARY data table.
	 */
	@Override
	public List<Library> getAllLibrary() throws CustomException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Library> allLibrary = new ArrayList<Library>();

		// Connect to database and execute the query.
		try
		{
			conn = DataDAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ALL_LIBRARY);
			rs = stmt.executeQuery();

			// Process the retrieved data.
			while (rs.next())
			{
				Library library = new Library(rs.getLong("LibraryId"),
								rs.getString("author"),
								rs.getString("title"));

				// Output debug if enabled.
				if (log.isDebugEnabled())
				{
					log.debug("getAllLibrary(): Get Books " + library);
				}

				// Add library details to library list
				allLibrary.add(library);
			}

			return allLibrary;
		}
		// Catch SQL Exception
		catch (SQLException e)
		{
			throw new CustomException(
							"getLibraryById(): Error reading library data", e);
		}
		// Finally close the database.
		finally
		{
			DbUtils.closeQuietly(conn, stmt, rs);
		}
	}

	/**
	 * Get library by id
	 */
	public Library getLibraryById( long libraryId ) throws CustomException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Library library = null;

		// Connect to database and execute the sql statement.
		try
		{
			conn = DataDAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_LIBRARY_BY_ID);
			stmt.setLong(1, libraryId);
			rs = stmt.executeQuery();

			// Process the retrieved data.
			if (rs.next())
			{
				library = new Library(rs.getLong("LibraryId"),
								rs.getString("author"),
								rs.getString("title"));

				// Output message is debug is enabled
				if (log.isDebugEnabled())
				{
					log.debug("Retrieve Library By Id: " + library);
				}
			}

			return library;
		}
		// Catch SQL Exception
		catch (SQLException e)
		{
			throw new CustomException(
							"getLibraryById(): Error reading library data", e);
		}
		// Finally close the database.
		finally
		{
			DbUtils.closeQuietly(conn, stmt, rs);
		}

	}

	/**
	 * Create library
	 */
	@Override
	public long createLibrary( Library library ) throws CustomException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;

		// Connect to database. Set the values for the library creation.
		// Execute the sql statement.
		try
		{
			conn = DataDAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_CREATE_LIBRARY);
			stmt.setString(1, library.getAuthor());
			stmt.setString(3, library.getTitle());

			int affectedRows = stmt.executeUpdate();

			// Check the number of rows created.
			if (affectedRows == 0)
			{
				log.error("createLibrary(): Creating library failed, no rows affected.");
				throw new CustomException("Library Cannot be created");
			}

			generatedKeys = stmt.getGeneratedKeys();

			// Check key created.
			if (generatedKeys.next())
			{
				return generatedKeys.getLong(1);
			}
			// Otherwise, library creation failed.
			else
			{
				log.error("Creating library failed, no ID obtained.");
				throw new CustomException("Library Cannot be created");
			}
		}
		// Catch SQL Exception
		catch (SQLException e)
		{
			log.error("Error Inserting Library  " + library);
			throw new CustomException(
							"createLibrary(): Error creating library "
											+ library, e);
		}
		// Finally, close database.
		finally
		{
			DbUtils.closeQuietly(conn, stmt, generatedKeys);
		}
	}

	/**
	 * Delete library by id
	 */
	@Override
	public int deleteLibraryById( long libraryId ) throws CustomException
	{
		Connection conn = null;
		PreparedStatement stmt = null;

		try
		{
			conn = DataDAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_DELETE_LIBRARY_BY_ID);
			stmt.setLong(1, libraryId);
			return stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new CustomException(
							"deleteLibraryById(): Error deleting library Id "
											+ libraryId, e);
		}
		finally
		{
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(stmt);
		}
	}


	@Override
	public int updateLibrary( long libraryId, Library library )
					throws CustomException
	{
		Connection conn = null;
		PreparedStatement lockStmt = null;
		PreparedStatement updateStmt = null;
		Library targetLibrary = null;
		ResultSet rs = null;
		
		int updateCount = -1;

		try
		{
			conn = DataDAOFactory.getConnection();
			conn.setAutoCommit(false);

			// lock library for writing:
			lockStmt = conn.prepareStatement(SQL_LOCK_LIBRARY_BY_ID);
			lockStmt.setLong(1, libraryId);
			rs = lockStmt.executeQuery();

			if (rs.next())
			{
				targetLibrary = new Library(rs.getLong("LibraryId"),
								rs.getString("author"),
								rs.getString("title"));
				
				if (log.isDebugEnabled())
				{
					log.debug("updateLibrary from Library: "
									+ targetLibrary);
				}
			}

			if (targetLibrary == null)
			{
				throw new CustomException(
								"updateLibrary(): fail to lock library : "
												+ libraryId);
			}

			updateStmt = conn.prepareStatement(SQL_UPDATE_LIBRARY);
			updateStmt.setString(1, library.getAuthor());
			updateStmt.setString(2, library.getTitle());
			updateCount = updateStmt.executeUpdate();

			conn.commit();

			if (log.isDebugEnabled())
			{
				log.debug("New Balance after Update: " + targetLibrary);
			}

			return updateCount;
		}
		catch (SQLException se)
		{
			// Rollback transaction if exception occurs
			log.error("updateLibrary(): Library Transaction Failed, rollback initiated for: "
							+ libraryId, se);
			try
			{
				if (conn != null)
				{
					conn.rollback();
				}
			}
			catch (SQLException re)
			{
				throw new CustomException("Fail to rollback transaction", re);

			}
		}
		finally
		{
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(lockStmt);
			DbUtils.closeQuietly(updateStmt);
		}
		
		return updateCount;
	}


}
