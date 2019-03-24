package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paperconnect.exception.KeywordException;

@RemoteServiceRelativePath("papers")
public interface PaperService extends RemoteService {

	ArrayList<PaperShort> retrievePapers(String keyword) throws KeywordException;
}
