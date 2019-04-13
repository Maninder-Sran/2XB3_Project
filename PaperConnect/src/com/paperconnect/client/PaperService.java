package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paperconnect.exception.InvalidIdException;
import com.paperconnect.exception.KeywordException;

/**
 * 
 * RemoteService that can retrieve a list of papers or a singular paper
 *
 */
@RemoteServiceRelativePath("papers")
public interface PaperService extends RemoteService {

	/**
	 * Method for retrieving a ArrayList<{@link PaperShort}> given an input keyword
	 * @param keyword - The keyword input from the user
	 * @return ArrayList<{@link PaperShort}>
	 * @throws KeywordException
	 */
	ArrayList<PaperShort> retrievePaperLs(String keyword) throws KeywordException;
	
	/**
	 * Method for retrieving a {@link Paper} given an id
	 * @param id - The id selected from the table list
	 * @return {@link Paper}
	 * @throws InvalidIdException
	 */
	Paper retrievePaper(String id) throws InvalidIdException;
	
}
