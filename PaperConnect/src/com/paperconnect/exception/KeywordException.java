package com.paperconnect.exception;

import java.io.Serializable;

/**
 * 
 * Class for a serializable exception to handle: exceptions resulting 
 * from keyword not found.
 *
 */
public class KeywordException extends Exception implements Serializable {

	private String keyword;
	private String message;

	/**
	 * Constructor taking in the invalid keyword and the error message you want to display
	 * @param keyword - keyword that was not found
	 * @param message - error message
	 */
	public KeywordException(String keyword, String message) {
		this.keyword = keyword;
		this.message = message;
	}
	
	/**
	 * Getter function for retrieving the keyword associated with this object
	 * @return keyword
	 */
	public String getKeyword() {
		return this.keyword;
	}

	/**
	 * Getter function for retrieving the message associated with this object
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}
}
