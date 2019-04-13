package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.paperconnect.exception.InvalidIdException;
import com.paperconnect.exception.KeywordException;

/**
 * Entry point class for the application
 */
public class PaperConnect implements EntryPoint {
	
	//List to store the papers currently in the table
	private ArrayList<PaperShort> papers = new ArrayList<PaperShort>();
	
	//Asynchronous PaperService object to make calls to the server 
	private PaperServiceAsync paperSvc = GWT.create(PaperService.class);
	private Label errorMsgLabel = new Label();

	/**
	 * Method for initializing and populating the HTML page
	 */
	public void onModuleLoad() {

		//Set the container used for displaying the sigma.js graph invisible
		RootPanel.get("graphContainer").setVisible(false);

		// Associate the errorMessageLabel with the HTML host page
		RootPanel.get("errorMessage").add(errorMsgLabel);

		//Initialize the JSNI method for the searchBar
		searchListener(this);
		
		//Initialize the JSNI method for the tableListener
		tableListener(this);
	}

	/**
	 * Method for adding the list of papers queried to the list of papers for the table 
	 * @param paperLs
	 */
	private void addPapers(ArrayList<PaperShort> paperLs) {
		papers.clear();
		papers = paperLs;
	}

	/**
	 * Method for retrieving a {@link Paper} from the server
	 * @param title - the title of the {@link PaperShort} selected
	 */
	private void retrievePaper(String title) {
		String id = "";
		//Look through the list of papers in the table
		for(PaperShort paper : papers) {
			//Check if this is the paper that was selected
			if(paper.getField(PaperFields.TITLE).equals(title)) {
				//Retrieve the id for this paper
				id = paper.getField(PaperFields.ID);
				System.out.println(id+" "+paper.getField(PaperFields.TITLE));
				break;
			}
		}
		
		// Initialize the service proxy
		if (paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}

		// Set up the callback object.
		AsyncCallback<Paper> callback = new AsyncCallback<Paper>() {
			public void onFailure(Throwable caught) {
				if(caught instanceof InvalidIdException) {
					//Set the errorMessage label text and make it visible
					errorMsgLabel.setText(((InvalidIdException) caught).getMessage());
					errorMsgLabel.setVisible(true);
				}
			}

			@Override
			public void onSuccess(Paper result) {
				//Initialize the HTML table for displaying the paper
				initPaperTable("paperTable", true);
				
				JSONObject obj = new JSONObject();
				
				//Initialize the JSON object with the fields that will be displayed
				obj.put("title", new JSONString(result.getField(PaperFields.TITLE)));
				obj.put("author", new JSONString(result.getField(PaperFields.AUTHOR)));
				obj.put("publishDate", new JSONString(result.getField(PaperFields.PUBLISH_DATE)));
				obj.put("abstract", new JSONString(result.getField(PaperFields.ABSTRACT)));
				
				//Add the paper to the HTML table
				addPaperInTable(obj, "paperTable", true);
				
				//Parse the JSONString for the graph into JSONObject that can be used by sigma.js
				JSONValue val = JSONParser.parseStrict(result.getField(PaperFields.TREE));
				JSONObject graphObj = val.isObject();
				
				//Set the container used for displaying the sigma.js graph visible
				RootPanel.get("graphContainer").setVisible(true);
				
				//Display the graph in the graphContainer using sigma.js
				displayGraph(graphObj, "graphContainer");
				
				//Set the errorMessage label to invisible
				errorMsgLabel.setVisible(false);
			}
		};

		// Make the call to retrieve the paper
		paperSvc.retrievePaper(id, callback);
	}

	/**
	 * Method for retrieving a list of {@link PaperShort} related to the inputed keyword
	 * @param keyword - the keyword inputed in the search bar
	 */
	private void retrievePaperLs(String keyword) {
		// Initialize the service proxy
		if (paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}

		// Set up the callback object.
		AsyncCallback<ArrayList<PaperShort>> callback = new AsyncCallback<ArrayList<PaperShort>>() {
			public void onFailure(Throwable caught) {
				if(caught instanceof KeywordException) {
					//Set the errorMessage label text and make it visible
					errorMsgLabel.setText(((KeywordException) caught).getMessage());
					errorMsgLabel.setVisible(true);
				}
			}

			public void onSuccess(ArrayList<PaperShort> result) {
				
				//Set the container used for displaying the sigma.js graph invisible
				RootPanel.get("graphContainer").setVisible(false);
				
				//Initialize the HTML table for displaying the list of papers
				initPaperTable("paperTable", false);
				
				//Add the papers from the query to the list of papers that will be in the table
				addPapers(result);
				
				//Add every paper in from the query into the table
				for (PaperShort paper : result) {
					JSONObject obj = new JSONObject();
					
					//Initialize the JSON object with the fields that will be displayed
					obj.put("title", new JSONString(paper.getField(PaperFields.TITLE)));
					obj.put("author", new JSONString(paper.getField(PaperFields.AUTHOR)));
					obj.put("publishDate", new JSONString(paper.getField(PaperFields.PUBLISH_DATE)));
					
					//Add the paper to the HTML table
					addPaperInTable(obj, "paperTable", false);
				}
				//Set click handlers for the rows of the HTML table
				addClickHandlersToTable("paperTable");
				
				//Set the errorMessage label to invisible
				errorMsgLabel.setVisible(false);
			}
		};

		// Make the call to retrieve the list of papers
		paperSvc.retrievePaperLs(keyword, callback);
	}

	/**
	 * JSNI method for calling the JavaScript method to initialize the HTML table, for either single or
	 * multiple paper configurations
	 * @param elementId - The id of the element in which to generate the table
	 * @param singlePaper - Whether the table configuration should for a single paper or multiple
	 */
	public native void initPaperTable(String elementId, boolean singlePaper) /*-{
		$wnd.initPaperTable(elementId, singlePaper);
	}-*/;

	/**
	 * JSNI method for calling the JavaScript method to draw the graph using sigma.js
	 * @param obj - JSONObject containing all the relevant data (nodes and edges) for the graph
	 * @param elementId - The id of the element in which to draw the graph
	 */
	public native void displayGraph(JSONObject obj, String elementId) /*-{
		$wnd.drawGraph(obj, elementId);
	}-*/;

	/**
	 * JSNI method for calling the JavaScript method to add a paper to the table
	 * @param obj - JSONObject containing the all the relevant data for the paper
	 * @param elementId - The id of the element in which the table exists
	 * @param singlePaper - Which configuration of the table is it adding the paper to 
	 */
	public native void addPaperInTable(JSONObject obj, String elementId, boolean singlePaper) /*-{
		$wnd.addPaperToTable(obj, elementId, singlePaper);
	}-*/;

	/**
	 * JSNI method for calling the JavaScript method to add clickHandlers for all the rows of the table
	 */
	public native void addClickHandlersToTable(String elementId) /*-{
		$wnd.addRowHandlers();
	}-*/;

	/**
	 * JSNI method calling the Java method retrievePaperLs from the client side in the event of a search bar query 
	 * @param pc - instance of this class
	 */
	public native void searchListener(PaperConnect pc) /*-{
		$wnd.onSearchEnter = function(keyword) {
			pc.@com.paperconnect.client.PaperConnect::retrievePaperLs(Ljava/lang/String;)(keyword);
		};
	}-*/;

	/**
	 * JSNI method calling the Java method retrievePaper from the client side in the event of a paper being selected
	 * @param pc - instance of this class
	 */
	public native void tableListener(PaperConnect pc) /*-{
		$wnd.onTableClick = function(title) {
			pc.@com.paperconnect.client.PaperConnect::retrievePaper(Ljava/lang/String;)(title);
		};
	}-*/;
}
