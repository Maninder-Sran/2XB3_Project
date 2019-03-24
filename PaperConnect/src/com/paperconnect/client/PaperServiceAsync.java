package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaperServiceAsync {

	void retrievePapers(String keyword, AsyncCallback<ArrayList<PaperShort>> callback);
}
