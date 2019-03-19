package com.paperconnect.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paperconnect.client.Paper;

@RemoteServiceRelativePath("papers")
public interface PaperService extends RemoteService {

	Paper[] retrievePapers(String keyword);
}
