package com.paperconnect.client;

import java.io.Serializable;

//An ADT used to perform sorting of papers by citation count for the keywordLookup function 
public class Paper implements Comparable<Paper>, Serializable {
	private String paperID;
	private String paperTitle;
	private String paperAbstract;
	private long citeNum;
	
	
	public Paper(String paperID, String paperTitle, long citeNum) {
		this.paperID = paperID;
		this.paperTitle = paperTitle;
		this.citeNum = citeNum;
	}
	
	public Paper(String paperID, String paperTitle, String paperAbstract, long citeNum) {
		this.paperID = paperID;
		this.paperTitle = paperTitle;
		this.paperAbstract = paperAbstract;
		this.citeNum = citeNum;
	}
	
	public String getPaperID() {
		return paperID;
	}
	
	public String getPaperTitle() {
		return paperTitle;
	}
	
	
	@Override
	public int compareTo(Paper j) {
		if(citeNum > j.citeNum)
			return -1;
		else if(citeNum < j.citeNum)
			return 1;
		return 0;
	}
}
