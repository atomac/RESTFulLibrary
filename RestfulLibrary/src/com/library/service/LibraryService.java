package com.library.service;

import org.apache.log4j.Logger;

import com.library.dao.DAOFactory;
import com.library.exception.CustomException;
import com.library.model.Library;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Library Service
 */
@Path( "/library" )
@Produces( MediaType.APPLICATION_JSON )
public class LibraryService
{
	private static Logger log = Logger.getLogger(LibraryService.class);

	private final DAOFactory daoFactory = DAOFactory
					.getDAOFactory(DAOFactory.DATA);

	/**
	 * Find all library
	 * 
	 * @return - all library details
	 * @throws CustomException
	 */
	@GET
	@Path( "/all" )
	public List<Library> getAllLibrary() throws CustomException
	{
		return daoFactory.getLibraryDAO().getAllLibrary();
	}

	/**
	 * Find by library id
	 * 
	 * @param libraryId
	 *            - library id
	 * @return - library object details.
	 * @throws CustomException
	 */
	@GET
	@Path( "/{libraryId}" )
	public Library getLibary( @PathParam( "libraryId" ) long libraryId )
					throws CustomException
	{
		return daoFactory.getLibraryDAO().getLibraryById(libraryId);
	}

	/**
	 * Create Library
	 * 
	 * @param library
	 *            - library object details.
	 * @return - new library details.
	 * @throws CustomException
	 */
	@PUT
	@Path( "/create" )
	public Library createLibrary( Library library ) throws CustomException
	{
		// Create Library record
		final long libraryId = daoFactory.getLibraryDAO()
						.createLibrary(library);

		// Return library details.
		return daoFactory.getLibraryDAO().getLibraryById(libraryId);
	}

	/**
	 * Delete library by library Id
	 * 
	 * @param libraryId
	 *            - library id
	 * @param library
	 *            - library to delete.
	 * @return - library response
	 * @throws CustomException
	 */
	@DELETE
	@Path( "/{libraryId}" )
	public Response deleteLibrary( @PathParam( "libraryId" ) long libraryId )
					throws CustomException
	{
		// Retrieve library details by library id.
		int deleteCount = daoFactory.getLibraryDAO()
						.deleteLibraryById(libraryId);

		// Compare for valid response for delete
		if (deleteCount == 1)
		{
			return Response.status(Response.Status.OK).build();
		}
		// Otherwise details not found.
		else
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
