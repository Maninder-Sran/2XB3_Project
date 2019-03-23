package com.paperconnect.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperService;
import com.paperconnect.client.PaperShort;
import com.paperconnect.exception.KeywordException;

public class PaperServiceImpl extends RemoteServiceServlet implements PaperService {

	@Override
	public ArrayList<PaperShort> retrievePapers(String keyword) throws KeywordException {
		if(!DataServer.LookupTable.isKeywordValid(keyword))
			throw new KeywordException(keyword);
		return DataServer.LookupTable.retrievePapers(keyword);
	}
}
