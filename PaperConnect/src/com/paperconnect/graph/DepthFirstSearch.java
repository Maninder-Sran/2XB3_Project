package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Stack;

import com.paperconnect.client.Paper;
import com.paperconnect.client.Paper.Fields;

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
		Paper v;
		ArrayList<Paper> neighbors = new ArrayList<Paper>();
		String results = "[";
		Stack<Paper> S = new Stack<Paper>();
		S.push(startNode);
		startNode.setVisited(true);
		results += "[\n{";
		boolean isDeadEnd = true;
		while(!S.isEmpty()) {
			v = S.pop();
			//System.out.println(v.getField(Fields.ID));
			neighbors = graph.getChildren(v.getField(Fields.ID));
			isDeadEnd = true;
			
			for(int i=0; i<Math.min(maxChildren, neighbors.size()); i++) {
				if (neighbors.get(i).getVisited() != true) {
					isDeadEnd = false;
					S.push(neighbors.get(i));
					neighbors.get(i).setVisited(true);
					//System.out.println(neighbors.get(i).getField(Fields.ID));
				}
			}
			if(isDeadEnd) {
				results += "\n}";
			}
		}
		
		results += "\n}\n]";
		return results;
	}

	public static void main(String[] args) {

		Paper root = new Paper("root", "root", 0);
		Paper p1 = new Paper("p1", "p1", 1);
		Paper p2 = new Paper("p2", "p2", 2);
		Paper p3 = new Paper("p3", "p3", 3);
		Paper p4 = new Paper("p4", "p4", 4);
		Paper p5 = new Paper("p5", "p5", 5);
		Paper p6 = new Paper("p6", "p6", 6);
		DiGraph d = new DiGraph(root);
		d.addVertex("root");
		d.addVertex("p1");
		d.addVertex("p2");
		d.addVertex("p3");
		d.addVertex("p4");
		d.addVertex("p5");
		d.addVertex("p6");
		d.addCiteEdge("root", p1);
		d.addCiteEdge("root", p2);
		d.addCiteEdge("root", p3);
		d.addCiteEdge("root", p4);
		d.addCiteEdge("p2", p5);
		d.addCiteEdge("root", p6);
		init(root, 3, 3);
		String ans = compute(d);
		System.out.println(ans);
	}

}