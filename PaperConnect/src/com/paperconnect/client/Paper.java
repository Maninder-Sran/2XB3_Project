package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

//An ADT used to perform sorting of papers by citation count for the keywordLookup
public class Paper implements Comparable<Paper>, Serializable {
	private int citeNum;
	private ArrayList<String> references;
	private ArrayList<String> fields;
	private ArrayList<Paper> tree;
	private boolean visited = false;
	private String parentID;

	public Paper() {
		fields = new ArrayList<String>();
	}

	// Constructor
	public Paper(String paperID, String paperTitle, String author, String publishDate, int citeNum) {
		this.fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField(author);
		addField(publishDate);
		this.citeNum = citeNum;
		setParentID(null);
	}

	// Constructor
	public Paper(String paperID, String paperTitle, String paperAbstract, ArrayList<String> references,
			String paperAuthor, String publishDate, int citeNum) {
		fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField(paperAuthor);
		addField(publishDate);
		addField(paperAbstract);
		this.references = references;
		this.citeNum = citeNum;
		setParentID(null);
	}

	public Paper(String paperID, String paperTitle, int citeNum) {
		this.fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField("");
		addField("");
		addField("");
		addField("");
		this.citeNum = citeNum;
		setParentID(null);
	}

	public void addField(String a) {
		fields.add(a);
	}

	public String getField(PaperFields field) {
		return fields.get(field.ordinal());
	}

	public ArrayList<String> getReferences() {
		return references;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(boolean A) {
		visited = A;
	}
	
	public String getParentID() {
		return parentID;
	}
	
	public void setParentID(String a) {
		parentID = a;
	}

	public String toString() {
		return getField(PaperFields.ID) + ", " + getField(PaperFields.TITLE) + "\n" + getField(PaperFields.ABSTRACT);
	}
	
	public void setTree(ArrayList<Paper> tree) {
		this.tree = tree;
	}

	@Override
	public int compareTo(Paper j) {
		if (citeNum > j.citeNum)
			return -1;
		else if (citeNum < j.citeNum)
			return 1;
		return 0;
	}
}
