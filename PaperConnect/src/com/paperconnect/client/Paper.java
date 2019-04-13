package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a {@link Paper} object, which models an academic paper
 */
public class Paper implements Comparable<Paper>, Serializable {
	
	//Number of times this academic paper has been cited
	private int citeNum;
	//List of paper IDs of the papers this paper references
	private ArrayList<String> references;
	//List of information about the paper
	private ArrayList<String> fields;
	//Boolean value used by the Breadth-First Search to determine which nodes have already been visited
	private boolean visited = false;

	/**
	 * {@link Paper} constructor
	 */
	public Paper() {
		fields = new ArrayList<String>();
	}

	/**
	 * {@link Paper} constructor
	 * @param paperID - ID of the paper
	 * @param paperTitle - title of the paper
	 * @param author - author of the paper
	 * @param publishDate - date the paper was published
	 * @param citeNum - number of times this paper has been cited
	 */
	public Paper(String paperID, String paperTitle, String author, String publishDate, int citeNum) {
		this.fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField(author);
		addField(publishDate);
		addField("");
		addField("");
		this.citeNum = citeNum;
	}

	/**
	 * {@link Paper} constructor
	 * @param paperID - ID of the paper
	 * @param paperTitle - title of the paper
	 * @param paperAbstract - this paper's abstract section
	 * @param references - list of papers this paper cites
	 * @param author - author of the paper
	 * @param publishDate - date the paper was published
	 * @param citeNum - number of times this paper has been cited
	 */
	public Paper(String paperID, String paperTitle, String paperAbstract, ArrayList<String> references,
			String paperAuthor, String publishDate, int citeNum) {
		fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField(paperAuthor);
		addField(publishDate);
		addField(paperAbstract);
		addField("");
		this.references = references;
		this.citeNum = citeNum;
	}

	/**
	 * {@link Paper} constructor
	 * @param paperID - ID of the paper
	 * @param paperTitle - title of the paper
	 * @param citeNum - number of times this paper has been cited
	 */
	public Paper(String paperID, String paperTitle, int citeNum) {
		this.fields = new ArrayList<String>();
		addField(paperID);
		addField(paperTitle);
		addField("");
		addField("");
		addField("");
		addField("");
		this.citeNum = citeNum;
	}

	/**
	 * Adds a String to the list of fields
	 * @param a - String to be added to the list of fields
	 */
	public void addField(String a) {
		fields.add(a);
	}

	/**
	 * Getter for any field in the list of fields
	 * @param field - {@link PaperFields} enumerated type to specify a field
	 * @return the specified field for this object
	 */
	public String getField(PaperFields field) {
		return fields.get(field.ordinal());
	}
	
	/**
	 * Setter for any field in the list of fields
	 * @param field - {@link PaperFields} enumerated type to specify a field
	 * @param data - new value to set this field to
	 */
	public void setField(PaperFields field, String data) {
		fields.set(field.ordinal(), data);
	}

	/**
	 * Getter for references
	 * @return references
	 */
	public ArrayList<String> getReferences() {
		return references;
	}

	/**
	 * Getter for visited
	 * @return visited
	 */
	public boolean getVisited() {
		return visited;
	}

	/**
	 * Setter for visited
	 * @param A new value to set visited to
	 */
	public void setVisited(boolean A) {
		visited = A;
	}

	/**
	 * Returns a String representation of this Paper object
	 * @return String representing this object
	 */
	public String toString() {
		return getField(PaperFields.ID) + ", " + getField(PaperFields.TITLE) + "\n" + getField(PaperFields.ABSTRACT);
	}

	/**
	 * Compares two Papers by citation number and returns the result
	 * @return int -1 if greater, 1 if less than, 0 if equal
	 */
	@Override
	public int compareTo(Paper j) {
		if (citeNum > j.citeNum)
			return -1;
		else if (citeNum < j.citeNum)
			return 1;
		return 0;
	}
}
