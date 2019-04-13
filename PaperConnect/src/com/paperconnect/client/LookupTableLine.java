package com.paperconnect.client;

import java.util.ArrayList;

/**
 * Represents a line in a lookup table, where the keys are keywords, and the values are {@link PaperShort}'s
 */
public class LookupTableLine implements Comparable<LookupTableLine> {
	
	//Represents the keyword for this line
	private String keyword;
	//Represents everything to the right of '=' on a given line of the input file. Used only when sorting.
	private String rightHalf;
	//Represents all papers related to this keyword
	private ArrayList<PaperShort> paperData;

	/**
	 * Constructor for LookupTableLine
	 * @param keyword - {@link String} representing the keyword for this line
	 */
	public LookupTableLine(String keyword) {
		this.keyword = keyword;
		this.rightHalf = "";
		this.paperData = new ArrayList<PaperShort>();
	}

	/**
	 * Constructor for LookupTableLine
	 * @param keyword - String representing the keyword for this line
	 * @param rightHalf - String representing everything to the right of '=' in the input file
	 */
	public LookupTableLine(String keyword, String rightHalf) {
		this.keyword = keyword;
		this.rightHalf = rightHalf;
	}

	/**
	 * Getter for the keyword field
	 * @return keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * Getter for the rightHalf field
	 * @return rightHalf
	 */
	public String getRightHalf() {
		return this.rightHalf;
	}
	
	/**
	 * Getter for the paperData field
	 * @return paperData
	 */
	public ArrayList<PaperShort> getData(){
		return this.paperData;
	}
	
	/**
	 * Adds a new paper to the list of papers
	 * @param data
	 */
	public void addPaperData(String[] data) {
		paperData.add(new PaperShort(data));
	}

	/**
	 * Returns a string representation of this object
	 * @return String representing this object
	 */
	public String toString() {
		StringBuilder x = new StringBuilder(keyword + " = " + rightHalf);
		for(PaperShort p:paperData) {
			x.append(p.toString());
		}
		return x.toString();
	}

	/**
	 * Compares two LookupTableLine's keywords lexicographically and returns the result
	 * @return int representing the result of the comparison
	 */
	@Override
	public int compareTo(LookupTableLine j) {
		return (keyword.compareTo(j.keyword));
	}
}
