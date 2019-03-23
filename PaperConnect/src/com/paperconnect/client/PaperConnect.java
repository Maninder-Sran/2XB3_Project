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
import com.paperconnect.exception.KeywordException;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PaperConnect implements EntryPoint {

	private VerticalPanel          mainPanel       = new VerticalPanel();
	private FlexTable              papersFlexTable = new FlexTable();
	private HorizontalPanel        addPanel        = new HorizontalPanel();
	private TextBox                keywordTextBox  = new TextBox();
	private Button                 searchButton    = new Button("Search");
	private ArrayList<Paper>       papers          = new ArrayList<Paper>();
	private PaperServiceAsync      paperSvc        = GWT.create(PaperService.class);
	private Label                  errorMsgLabel   = new Label();

	public void onModuleLoad() {
		
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
				Paper paperSelected = papers.get(row+1);
			}
		});
	}
	
	private void addPapers(Paper[] paperLs) {
		for(int i = 0; i < paperLs.length; i++) {
			//TODO Error checking for correctness of ids
			
			
			if(papers.contains(paperLs[i]))
				return;
			
			//Add the paper to the table
			int row = papersFlexTable.getRowCount();
			papers.add(paperLs[i]);
			papersFlexTable.setText(row, 0, paperLs[i].getTitle());
			papersFlexTable.setText(row, 1, paperLs[i].getAuthor());
			papersFlexTable.setText(row, 2, paperLs[i].getPublishDate());
		}
	}
	
	private void retrievePapers(String keyword) {
		//Initialize the service proxy
		if(paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}
		
		//Set up the callback object.
		AsyncCallback<Paper[]> callback = new AsyncCallback<Paper[]>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Keyword:"+((KeywordException)caught).getKeyword()+"not valid");
				errorMsgLabel.setVisible(true);
			}
			public void onSuccess(Paper[] result) {
				addPapers(result);
				updateTable(result);
			}
		};
		
		//Make the call to the paper service
		paperSvc.retrievePapers(keyword, callback);
	}
	
	private void updateTable(Paper[] papers) {
		for(int i = 0; i < papers.length; i++) {
			updateTable(papers[i]);
		}
		
		//Clear any errors
		errorMsgLabel.setVisible(false);
	}
	
	private void updateTable(Paper paper) {
		int row = papersFlexTable.getRowCount();
		papers.add(paper);
		papersFlexTable.setText(row, 0, paper.getTitle());
		papersFlexTable.setText(row, 1, paper.getId());
		papersFlexTable.setText(row, 2, paper.getPublishDate());
	}
}
