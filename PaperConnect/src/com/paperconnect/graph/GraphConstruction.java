package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import com.paperconnect.client.Paper;
import com.paperconnect.server.DataServer;
import com.paperconnect.util.Search;

public class GraphConstruction {

	public static void buildGraph(String id, int width, int height, DiGraph citeGraph) {
		Paper paper = Search.binarySearchID(DataServer.PaperList.papers, id);
		int counter = width;
		String source = null;

		if (height == 0 || paper == null) {
			return;
		}

		if(paper.getReferences().size() == 0) {
			citeGraph.addVertex(id);
			return;
		}
		
		ArrayList<String> references = paper.getReferences();
		Collections.sort(references);
		Iterator<String> iterator = references.iterator();
		citeGraph.addVertex(id);

		while (counter > 0 && iterator.hasNext()) {
			source = iterator.next();
			paper = Search.binarySearchID(DataServer.PaperList.papers, source);
			citeGraph.addCiteEdge(id, paper);
			buildGraph(source, width, height - 1, citeGraph);
			counter--;
		}

		return;
	}

	public static DiGraph Graph(String id, int width, int height) {
		Paper root = Search.binarySearchID(DataServer.PaperList.papers, id);
		DiGraph citeGraph = new DiGraph(root);
		buildGraph(id, width, height, citeGraph);
		return citeGraph;
	}

	public static void main(String[] args) {
		DiGraph citeGraph = Graph("hello", 2, 5);
		Hashtable<String, ArrayList<Paper>> graph = citeGraph.getGraph();
		graph.forEach((k, v) -> System.out.println(k + "  " + v));
		// citeGraph = Graph("53e99838b7602d970205e7e4", "../../../Documents/Software
		// 1/2XB3/final project/data/ap_final.txt");
	}
}