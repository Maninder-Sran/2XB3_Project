package com.paperconnect.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaperServiceAsync {

	void retrievePapers(String keyword, AsyncCallback<Paper[]> callback);
}
