package com.paperconnect.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperService;

public class PaperServiceImpl extends RemoteServiceServlet implements PaperService {

	@Override
	public Paper[] retrievePapers(String keyword) {
		return null;
	}
	
	private boolean isValidKeyword(String keyword) {
		return false;
	}
}
