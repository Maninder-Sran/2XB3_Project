package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PaperConnect implements EntryPoint {

	private VerticalPanel    mainPanel       = new VerticalPanel();
	private FlexTable        papersFlexTable = new FlexTable();
	private HorizontalPanel  addPanel        = new HorizontalPanel();
	private TextBox          keywordTextBox  = new TextBox();
	private Button           searchButton    = new Button("Search");
	private ArrayList<Paper> papers          = new ArrayList<Paper>();
	private Label            errorMsgLabel   = new Label();

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
				searchKeyword();
			}
		});
		
		//Listen for keyboard events in the input box
		keywordTextBox.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchKeyword();
				}
				
			}
		});	
	}
	
	private void searchKeyword() {
		
	}
}
