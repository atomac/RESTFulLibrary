package com.library.dao;

import java.util.List;

import com.library.exception.CustomException;
import com.library.model.Library;

/**
 * Interface of Library actions and/or functionality.
 * 
 * @author mccormam
 */
public interface LibraryDAO
{
	/**
	 * Retrieve a list of all the library in the database
	 * 
	 * @return- list of all library books in database.
	 * @throws CustomException
	 */
	List<Library> getAllLibrary() throws CustomException;

	/**
	 * Retrieve the library details by library id.
	 * 
	 * @param libaryId
	 *            - library id.
	 * @return - library details.
	 * @throws CustomException
	 */
	Library getLibraryById( long libraryId ) throws CustomException;

	/**
	 * Create a library record using the library details.
	 * 
	 * @param library
	 *            - library details object.
	 * @return - library id to confirm.
	 * @throws CustomException
	 */
	long createLibrary( Library library ) throws CustomException;

	/**
	 * Delete/Remove library details by library id.
	 * 
	 * @param libraryId
	 *            - library id.
	 * @return
	 * @throws CustomException
	 */
	int deleteLibraryById( long libraryId ) throws CustomException;

	/**
	 * Update given library by library Id.
	 * 
	 * @param libraryId
	 *            - libraryId
	 * @param Library - library record details
	 * @return - number of rows updated
	 */
	int updateLibrary(  long libraryId, Library library )
					throws CustomException;

}
