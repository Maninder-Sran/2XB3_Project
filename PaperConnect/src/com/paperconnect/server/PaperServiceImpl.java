package com.paperconnect.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperService;
import com.paperconnect.exception.KeywordException;

public class PaperServiceImpl extends RemoteServiceServlet implements PaperService {

	@Override
	public Paper[] retrievePapers(String keyword) throws KeywordException {
		throw new KeywordException(keyword);
	}
}
