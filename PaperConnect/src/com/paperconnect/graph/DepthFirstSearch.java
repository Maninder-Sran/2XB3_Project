package com.paperconnect.graph;

import com.paperconnect.client.Paper;

public class DepthFirstSearch {

	static Paper startNode;
	static int maxDepth;
	static int maxChildren;

	public static void init(Paper startNode, int maxDepth, int maxChildren) {
		DepthFirstSearch.startNode = startNode;
		DepthFirstSearch.maxDepth = maxDepth;
		DepthFirstSearch.maxChildren = maxChildren;
	}

	public static String compute(DiGraph graph) {
		return null;
	}

	public static void main(String[] args) {
		Paper root = new Paper("root", "root", 0);
		Paper p1 = new Paper("p1", "p1", 1);
		Paper p2 = new Paper("p2", "p2", 2);
		Paper p3 = new Paper("p3", "p3", 3);
		Paper p4 = new Paper("p4", "p4", 4);
		Paper p5 = new Paper("p5", "p5", 5);
		DiGraph d = new DiGraph(root);
		d.addVertex("root");
		d.addVertex("p1");
		d.addVertex("p2");
		d.addVertex("p3");
		d.addVertex("p4");
		d.addVertex("p5");
		d.addCiteEdge("root", p1);
		d.addCiteEdge("root", p2);
		d.addCiteEdge("root", p3);
		d.addCiteEdge("root", p4);
		d.addCiteEdge("p2", p5);
		init(root, 3, 3);
		String ans = compute(d);
		System.out.println(ans);
	}

}