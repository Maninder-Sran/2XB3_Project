package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

//An ADT used to perform sorting of papers by citation count for the keywordLookup
public class Paper implements Comparable<Paper>, Serializable{
	private String id;
	private String title;
	private String abst;
	private long citeNum;
	private ArrayList<String> references;
	private String author;
	private String publishDate;
	
	public Paper() {}
	
	//Constructor
	public Paper(String paperID, String paperTitle, long citeNum) {
		this.id = paperID;
		this.title = paperTitle;
		this.citeNum = citeNum;
	}
	
	//Constructor
	public Paper(String paperID, String paperTitle, String paperAbstract, ArrayList<String> references, String paperAuthor, String publishDate,long citeNum) {
		this.id = paperID;
		this.title = paperTitle;
		this.abst = paperAbstract;
		this.citeNum = citeNum;
		this.references = references;
		this.author = paperAuthor;
		this.publishDate = publishDate;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAbstract() {
		return abst;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public ArrayList<String> getReferences(){
		return references;
	}
	
	public String toString() {
		return id + ", " + title + "\n" + abst;
	}
	
	public String getAuthor() {
		return author;
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
