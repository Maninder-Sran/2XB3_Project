package com.paperconnect.graph;

public class GraphPaperNode implements Comparable<GraphPaperNode> {
	private String paperID;
	private long citeNum;
	
	public GraphPaperNode(String paperID, long citeNum) {
		this.paperID = paperID;
		this.citeNum = citeNum;
	}
	
	public String getPaperID() {
		return paperID;
	}
	
	public String toString() {
		return (paperID + ", " + citeNum);
	}
	
	@Override
	public int compareTo(GraphPaperNode j) {
		if(citeNum > j.citeNum)
			return -1;
		else if(citeNum < j.citeNum)
			return 1;
		return 0;
	}
}
