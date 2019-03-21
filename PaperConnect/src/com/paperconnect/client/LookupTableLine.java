package com.paperconnect.client;

import java.util.ArrayList;

//Similar to Paper.java, but has lists of IDs and titles with a compareTo() method that compares keywords lexicographically
public class LookupTableLine implements Comparable<LookupTableLine> {
	private String keyword;
	private String rightHalf;
	private ArrayList<PaperShort> paperData;

	public LookupTableLine(String keyword) {
		this.keyword = keyword;
		this.rightHalf = "";
	}

	public LookupTableLine(String keyword, String rightHalf) {
		this.keyword = keyword;
		this.rightHalf = rightHalf;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getRightHalf() {
		return this.rightHalf;
	}

	// don't use toString() method. generates strings that are too big for java to
	// handle
	public String toString() {
		return keyword + " = " + rightHalf;
	}

	@Override
	public int compareTo(LookupTableLine j) {
		return (keyword.compareTo(j.keyword));
	}
}
