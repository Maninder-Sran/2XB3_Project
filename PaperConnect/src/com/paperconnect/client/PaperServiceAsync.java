package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * Asynchronous class definition for PaperService
 *
 */
public interface PaperServiceAsync {

	/**
	 * Method for retrieving a ArrayList<{@link PaperShort}> given an input keyword
	 * @param keyword - the keyword input from the user
	 * @param callback - AsyncCallback<{@link PaperShort}> object to handle the waiting for and processing of response from the server
	 */
	void retrievePaperLs(String keyword, AsyncCallback<ArrayList<PaperShort>> callback);
	
	/**
	 * Method for retrieving a {@link Paper} given an id
	 * @param id - The id selected from the table list
	 * @param callback - AsyncCallback<{@link Paper}> object to handle the waiting for and processing of response from the server
	 */
	void retrievePaper(String id, AsyncCallback<Paper> callback);
}