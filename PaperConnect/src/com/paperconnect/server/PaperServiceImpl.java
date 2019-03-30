package com.paperconnect.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paperconnect.client.PaperService;
import com.paperconnect.client.PaperShort;
import com.paperconnect.exception.KeywordException;

public class PaperServiceImpl extends RemoteServiceServlet implements PaperService {

	@Override
	public ArrayList<PaperShort> retrievePapers(String keyword) throws KeywordException {
		ArrayList<PaperShort> result = DataServer.LookupTable.retrievePapers(keyword);
		if(result == null)
			throw new KeywordException(keyword);
		
		return result;
	}
}
