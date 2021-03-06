package com.library.exception;

import javax.ws.rs.core.Response;

/**
 * Custom exception for this suite.
 * 
 * @author mccormam
 */
public class CustomException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Custom Exception with message.
	 * 
	 * @param msg
	 *            - message text
	 */
	public CustomException( String msg )
	{
		super(msg);
	}

	/**
	 * Custom Exception with message and throw cause.
	 * 
	 * @param msg
	 *            - message text
	 * @param cause
	 *            - throw cause.
	 */
	public CustomException( String msg, Throwable cause )
	{
		super(msg, cause);
	}

	public CustomException( String message, Response.Status status )
	{
		super(message + " " + status);
	}

}
