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
	 * Method prototype for retrieving a list of papers given an input keyword
	 * @param keyword
	 * @return ArrayList<PaperShort>
	 * @throws KeywordException
	 */
	ArrayList<PaperShort> retrievePaperLs(String keyword) throws KeywordException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws InvalidIdException
	 */
	Paper retrievePaper(String id) throws InvalidIdException;
	
}
