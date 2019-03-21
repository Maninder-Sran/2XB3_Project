package com.paperconnect.client;

import java.util.ArrayList;

//Similar to Paper.java, but has lists of IDs and titles with a compareTo() method that compares keywords lexicographically
public class LookupTablePaper implements Comparable<LookupTablePaper> {
	private String keyword;
	private ArrayList<String> IDs;
	private ArrayList<String> titles;

	public LookupTablePaper(String keyword) {
		this.keyword = keyword;
		this.IDs = new ArrayList<String>();
		this.titles = new ArrayList<String>();
	}

	public LookupTablePaper(String keyword, ArrayList<String> IDs, ArrayList<String> titles) {
		this.keyword = keyword;
		this.IDs = IDs;
		this.titles = titles;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getID(int a) {
		return this.IDs.get(a);
	}

	public String getTitle(int a) {
		return this.titles.get(a);
	}

	public void addID(String a) {
		this.IDs.add(a);
	}

	public void addTitle(String a) {
		this.titles.add(a);
	}

	public int getNumIDs() {
		return this.IDs.size();
	}

	// don't use toString() method. generates strings that are too big for java to
	// handle
	public String toString() {
		String ret = keyword + " = [";
		for (int i = 0; i < IDs.size(); i++) {
			ret += getID(i) + "::" + getTitle(i);
			if (i != IDs.size() - 1) {
				ret += ",";
			}
		}
		ret += "]";
		return ret;
	}

	@Override
	public int compareTo(LookupTablePaper j) {
		return (keyword.compareTo(j.keyword));
	}
}
