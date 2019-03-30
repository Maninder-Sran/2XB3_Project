package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

public class BreadthFirstSearch {

	static Paper startNode;
	static int maxDepth;
	static int maxChildren;

	public static void init(Paper startNode, int maxDepth, int maxChildren) {
		BreadthFirstSearch.startNode = startNode;
		BreadthFirstSearch.maxDepth = maxDepth;
		BreadthFirstSearch.maxChildren = maxChildren;
	}

	public static ArrayList<Paper> compute(DiGraph graph) {

		ArrayList<Paper> Result = new ArrayList<Paper>();
		Paper current;
		ArrayList<Paper> neighbors;

		Queue<Paper> queue = new LinkedList<>();
		queue.add(startNode);
		queue.add(null);

		startNode.setVisited(true);
		int level = 0;

		while (!queue.isEmpty()) {
			current = queue.poll();
			Result.add(current);

			if (current == null) {
				level++;
				if (level >= maxDepth) {
					break;
				}

				queue.add(null);
				if (queue.peek() == null)
					break; // when you have two consecutive null in the queue, this means you are at the
							// end of the queue.
				else
					continue;
			}

			neighbors = graph.getChildren(current.getField(PaperFields.ID));
			for (int i = 0; i < Math.min(maxChildren, neighbors.size()); i++) {

				if (neighbors != null && neighbors.get(i) != null && neighbors.get(i).getVisited() != true) {
					queue.add(neighbors.get(i));
					// Result.add(neighbors.get(i));
					neighbors.get(i).setVisited(true);
				}
			}
		}
		return Result;
	}

	public static String getGraphJSONString(DiGraph d) {
		int x = 0;
		int y = 0;
		JSONObject obj = new JSONObject();
		JSONObject newObj;
		JSONArray nodes = new JSONArray();
		JSONArray edges = new JSONArray();
		ArrayList<Paper> children;
		ArrayList<Paper> ans = compute(d);
		for (Paper p : ans) {
			if(p == null) {
				x=0;
				y++;
			}
			else {
				newObj = new JSONObject();
				newObj.put("id", p.getField(PaperFields.ID));
				newObj.put("label", p.getField(PaperFields.TITLE));
				newObj.put("x", x);
				newObj.put("y", y);
				newObj.put("size", 3);
				nodes.add(newObj);
				children = d.getChildren(p.getField(PaperFields.ID));
				for(int i = 0; i < children.size(); i++) {
					if(!children.get(i).getVisited()) {
						continue;
					}
					newObj = new JSONObject();
					newObj.put("id", "");
					newObj.put("source", p.getField(PaperFields.ID));
					newObj.put("target", children.get(i).getField(PaperFields.ID));
					edges.add(newObj);
				}
				x++;
			}
		}
		obj.put("nodes", nodes);
		obj.put("edges", edges);
		return obj.toJSONString();
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
		System.out.println(getGraphJSONString(d));
	}

}