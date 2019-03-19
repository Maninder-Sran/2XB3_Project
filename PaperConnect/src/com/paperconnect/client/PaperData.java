package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;

public class PaperData extends JavaScriptObject {
	
	protected PaperData() {}
	
	//JSNI methods to get stock data
	public final native String getId() /*-{return this.id }-*/;
	
	public final native String getTitle() /*-{return this.title }-*/;
	
	public final native ArrayList<String> getreferences() /*-{return this.references }-*/;
	
	public final native long getCitationCount() /*-{return this.n_citation }-*/;
	
	public final native Paper getAbstract() /*-{return this.Abstract }-*/;
	
}
