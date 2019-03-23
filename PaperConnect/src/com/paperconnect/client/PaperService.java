package com.paperconnect.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paperconnect.exception.KeywordException;

@RemoteServiceRelativePath("papers")
public interface PaperService extends RemoteService {

	Paper[] retrievePapers(String keyword) throws KeywordException;
}
