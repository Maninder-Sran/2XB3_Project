package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.paperconnect.exception.KeywordException;
import com.paperconnect.server.DataServer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PaperConnect implements EntryPoint {

	private VerticalPanel          mainPanel       = new VerticalPanel();
	private FlexTable              papersFlexTable = new FlexTable();
	private HorizontalPanel        addPanel        = new HorizontalPanel();
	private TextBox                keywordTextBox  = new TextBox();
	private Button                 searchButton    = new Button("Search");
	private ArrayList<PaperShort>  papers          = new ArrayList<PaperShort>();
	private PaperServiceAsync      paperSvc        = GWT.create(PaperService.class);
	private Label                  errorMsgLabel   = new Label();

	public void onModuleLoad() {
		
		DataServer.init();
		
		//Create table for the list of papers found
		papersFlexTable.setText(0, 0, "Title");
		papersFlexTable.setText(0, 1, "Author");
		papersFlexTable.setText(0, 2, "Date Published");
		
		//Add styles to elements in the papers list table
		papersFlexTable.setCellPadding(6);
		papersFlexTable.getRowFormatter().addStyleName(0, "paperListHeader");
		papersFlexTable.addStyleName("paperList");
		
		//Assemble Keyword Search Panel
		addPanel.add(keywordTextBox);
		addPanel.add(searchButton);
		addPanel.addStyleName("addPanel");
		
		//Assemble Main panel
		mainPanel.add(addPanel);
		mainPanel.add(errorMsgLabel);
		mainPanel.add(papersFlexTable);
		
		//Associate the Main panel with the HTML host page
		RootPanel.get("paperList").add(mainPanel);
		
		//Move Cursor focus to the input box
		keywordTextBox.setFocus(true);
		
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				retrievePapers(keywordTextBox.getText());
			}
		});
		
		//Listen for keyboard events in the input box
		keywordTextBox.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					retrievePapers(keywordTextBox.getText());
				}
				
			}
		});	
		
		//Listen for mouse click on a row in the flex table
		papersFlexTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cell cell = papersFlexTable.getCellForEvent(event);
				int row = cell.getRowIndex();
				PaperShort paperSelected = papers.get(row+1);
			}
		});
	}
	
	private void retrievePapers(String keyword) {
		//Initialize the service proxy
		if(paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}
		
		//Set up the callback object.
		AsyncCallback<ArrayList<PaperShort>> callback = new AsyncCallback<ArrayList<PaperShort>>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Keyword:"+((KeywordException)caught).getKeyword()+"not valid");
				errorMsgLabel.setVisible(true);
			}
			public void onSuccess(ArrayList<PaperShort> result) {
				addPapers(result);
				updateTable(result);
			}
		};
		
		//Make the call to the paper service
		paperSvc.retrievePapers(keyword, callback);
	}

	private void addPapers(ArrayList<PaperShort> paperLs) {
		for(int i = 0; i < paperLs.size(); i++) {
			//TODO Error checking for correctness of ids
			
			if(papers.contains(paperLs.get(i)))
				return;
			
			//Add the paper to the table
			int row = papersFlexTable.getRowCount();
			papers.add(paperLs.get(i));
			papersFlexTable.setText(row, 0, paperLs.get(i).getField(0));
			//papersFlexTable.setText(row, 1, paperLs.get(i).getAuthor());
			//papersFlexTable.setText(row, 2, paperLs.get(i).getPublishDate());
		}
	}
	
	private void updateTable(ArrayList<PaperShort> papers) {
		for(int i = 0; i < papers.size(); i++) {
			updateTable(papers.get(i));
		}
		
		//Clear any errors
		errorMsgLabel.setVisible(false);
	}
	
	private void updateTable(PaperShort paper) {
		int row = papersFlexTable.getRowCount();
		papers.add(paper);
		papersFlexTable.setText(row, 0, paper.getField(0));
		//papersFlexTable.setText(row, 1, paper.getId());
		//papersFlexTable.setText(row, 2, paper.getPublishDate());
	}
}
