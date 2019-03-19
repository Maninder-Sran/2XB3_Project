package com.paperconnect.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paperconnect.client.Paper;

public interface PaperServiceAsync {

	void retrievePapers(String keyword, AsyncCallback<Paper[]> callback);
}
