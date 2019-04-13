package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

/**
 * 
 * a Graphing algorithm to go though the nodes of the cited papers and create a
 * graphical representation of its connections with other academic papers
 *
 */
public class BreadthFirstSearch {

	/**
	 * @param graph       Its an object containing the {@link DiGraph} class and its
	 *                    methods so it could be used in this algorithm.
	 * @param startNode   The initial node that we would be providing to the
	 *                    {@link BreadthFirstSearch} for it to go through the nodes
	 *                    and generate the graph.
	 * @param maxDepth    An integer for taking care of the max depth we want the
	 *                    {@link BreadthFirstSearch} to go through into startnode's
	 *                    connections.
	 * @param maxChildren An integral value of the maximum number of child nodes to
	 *                    look at a time.
	 * @return An ArrayList of {@link Paper} as an output to the further algorithm
	 *         so to implement the graph acknowledging the neighboring nodes and the
	 *         child ones.
	 */
	public static ArrayList<Paper> compute(DiGraph graph, Paper startNode, int maxDepth, int maxChildren) {

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
					break;
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

	/**
	 * 
	 * @param graph       It takes in a {@link DiGraph} object for using its defined
	 *                    methods to make them work in this algorithm.
	 * @param startNode   Initial node to start forming the graph for the output.
	 * @param maxDepth    Maximum depth to be achieved while implementing the graph
	 *                    based on the data we would be inputing.
	 * @param maxChildren Maximum number of child nodes the graph would be
	 *                    implementing of a particular parent node.
	 * @return A string of JSON objects containing the parent nodes with their
	 *         children and neighbors.
	 */
	public static String getGraphJSONString(DiGraph graph, Paper startNode, int maxDepth, int maxChildren) {
		int x = 0;
		int y = 0;
		JSONObject obj = new JSONObject();
		JSONObject newObj;
		JSONArray nodes = new JSONArray();
		JSONArray edges = new JSONArray();
		ArrayList<Paper> children;
		ArrayList<Paper> ans = compute(graph, startNode, maxDepth, maxChildren);
		int counter = 0;
		for (Paper p : ans) {
			if (p == null) {
				x = 0;
				y++;
			} else {
				newObj = new JSONObject();
				newObj.put("id", p.getField(PaperFields.ID));
				newObj.put("label", p.getField(PaperFields.TITLE));
				newObj.put("color", "#008cc2");
				newObj.put("x", x);
				newObj.put("y", y);
				newObj.put("size", 3);
				nodes.add(newObj);
				children = graph.getChildren(p.getField(PaperFields.ID));
				if (children != null) {
					for (int i = 0; i < children.size(); i++) {
						if (!children.get(i).getVisited()) {
							continue;
						}
						newObj = new JSONObject();
						newObj.put("id", String.valueOf(counter++));
						newObj.put("source", p.getField(PaperFields.ID));
						newObj.put("color", "#dc143c");
						newObj.put("target", children.get(i).getField(PaperFields.ID));
						edges.add(newObj);
					}
				}
				x++;
				p.setVisited(false);
			}
		}
		obj.put("nodes", nodes);
		obj.put("edges", edges);
		return obj.toJSONString();
	}
}