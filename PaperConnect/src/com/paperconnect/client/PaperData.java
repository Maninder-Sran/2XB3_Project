package com.paperconnect.client;

import com.google.gwt.core.client.JavaScriptObject;

public class PaperData extends JavaScriptObject {
	
	protected PaperData() {}
	
	//JSNI methods to get stock data
	public final native String getId() /*-{return this.id }-*/;
}
