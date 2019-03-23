package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Hashtable;

import com.paperconnect.client.Paper;

/**
 * Our main driver class which instantiates some example nodes and then performs
 * the breadth first search upon these newly created nodes.
 */
public class Driver {

	public static void main(String args[]) {
		DiGraph citeGraph = GraphConstruction.Graph("hello", "hello");
		Hashtable<String, ArrayList<Paper>> graph = citeGraph.getGraph();
		graph.forEach((k, v) -> System.out.println(k + "  " + v));
		BreadthFirstSearch bfs = new BreadthFirstSearch(citeGraph.getRoot());

		BreadthFirstSearch bfs = new BreadthFirstSearch(root);

//		if (bfs.compute())
//			System.out.print("Path Found!");
	}
}