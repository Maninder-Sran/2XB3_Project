package com.paperconnect.exception;

import java.io.Serializable;

/**
 * 
 * Class for a serializable exception to handle: exceptions resulting 
 * from id not found.
 *
 */
public class InvalidIdException extends Exception implements Serializable {

	private String id;
	private String message;

	/**
	 * Constructor taking in the invalid id and the error message you want to display
	 * @param id - id that was not found
	 * @param message - error message
	 */
	public InvalidIdException(String id, String message) {
		this.id = id;
		this.message = message;
	}
	
	/**
	 * Getter function for retrieving the invalid id
	 * @return id
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Getter function for retrieving the error message
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}
}
