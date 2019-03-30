package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		Paper v;
		ArrayList<Paper> neighbors = new ArrayList<Paper>();
		HashMap<String, JSONObject> objects = new HashMap<String, JSONObject>();
		Stack<Paper> S = new Stack<Paper>();
		S.push(startNode);
		startNode.setVisited(true);
		while(!S.isEmpty()) {
			v = S.pop();
			obj = new JSONObject();
			neighbors = graph.getChildren(v.getField(Fields.ID));
			children = new JSONArray();
			for(int i=0; i<Math.min(maxChildren, neighbors.size()); i++) {
				if (neighbors.get(i).getVisited() != true) {
					S.push(neighbors.get(i));
					neighbors.get(i).setVisited(true);
				}
			}
		}
		return arr.toJSONString();
	}

	public static void main(String[] args) {

		Paper root = new Paper("root", "root", 0);
		Paper p1 = new Paper("p1", "p1", 1);
		Paper p2 = new Paper("p2", "p2", 2);
		Paper p3 = new Paper("p3", "p3", 3);
		Paper p4 = new Paper("p4", "p4", 4);
		Paper p5 = new Paper("p5", "p5", 5);
		Paper p6 = new Paper("p6", "p6", 6);
		Paper p7 = new Paper("p7", "p7", 7);
		DiGraph d = new DiGraph(root);
		d.addVertex("root");
		d.addVertex("p1");
		d.addVertex("p2");
		d.addVertex("p3");
		d.addVertex("p4");
		d.addVertex("p5");
		d.addVertex("p6");
		d.addVertex("p7");
		d.addCiteEdge("root", p1);
		d.addCiteEdge("root", p2);
		d.addCiteEdge("root", p3);
		d.addCiteEdge("root", p4);
		d.addCiteEdge("p2", p5);
		d.addCiteEdge("p3", p6);
		d.addCiteEdge("p6", p7);
		init(root, 3, 3);
		//System.out.println(p5.getParentID());
		String ans = compute(d);
		System.out.println(ans);
	}

}