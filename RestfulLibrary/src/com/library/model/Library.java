package com.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * 
 * Data objects and database columns for the library object.
 * 
 * @author mccormam
 */
public class Library
{

	// Library Id.
	@JsonIgnore
	private long libraryId;

	// Name of Author
	@JsonProperty( required = true )
	private String author;

	// Title of Book.
	@JsonProperty( required = true )
	private String title;

	/**
	 * Library constructor.
	 */
	public Library()
	{
	}

	/**
	 * Library object constructor for details.
	 * 
	 * @param author - author.
	 * @param title - title.
	 */
	public Library( String author, String title )
	{
		this.author = author;
		this.title = title;
	}

	/**
	 * Library object constructor for details.
	 * 
	 * @param libraryId - library id
	 * @param author - author.
	 * @param title - title.
	 */
	public Library( long libraryId, String author, String title )
	{
		this.libraryId = libraryId;
		this.author = author;
		this.title = title;
	}

	/**
	 * Retrieve library id
	 * 
	 * @return - library id.
	 */
	public long getLibraryId()
	{
		return libraryId;
	}

	/**
	 * Retrieve author.
	 * 
	 * @return - author.
	 */
	public String getAuthor()
	{
		return author;
	}
	
	/**
	 * Retrieve the book title.
	 * 
	 * @return - book title.
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Override the meaning of equals in comparing library details.
	 */
	@Override
	public boolean equals( Object o )
	{
		// If there is an equivalence of this static object, objects are equal.
		if (this == o)
		{
			return true;
		}
		
		// If object is null or not of the same class, return false.
		if ((o == null) || (getClass() != o.getClass()))
		{
			return false;
		}

		// Instantiate the library object.
		Library library = (Library) o;

		// If the library ids do not match, return false.
		if (libraryId != library.libraryId)
		{
			return false;
		}
		
		// If the author names are not equal, return false.
		if (!author.equals(library.author))
		{
			return false;
		}
		
		// If the titles are not equal, return false.
		if (!title.equals(library.title))
		{
			return false;
		}
		
		// Library details are equal, return true.
		return true;
	}

	/**
	 * Override the hash for the Library object details.
	 */
	@Override
	public int hashCode()
	{
		int result = (int) (libraryId ^ (libraryId >>> 32));
		
		result = ((31*result) + author.hashCode());
		result = ((31*result) + title.hashCode());
		
		return result;
	}

	/**
	 * Print library details.
	 */
	@Override
	public String toString()
	{
		return "Library{" + "library=" + libraryId + ", author='"
						+ author + '\'' + ", title='" + title + '\'' + '}';
	}
	
}
