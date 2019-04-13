package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Collections;

import com.paperconnect.client.Paper;
import com.paperconnect.server.DataServer;
import com.paperconnect.util.Search;

/**
 * Builds an object that represents the graph
 */
public class GraphConstruction {

	/**
	 * Recursive function to build the graph
	 * @param id - String ID of the root node
	 * @param height - height of the graph (maximum depth)
	 * @param width - width of the graph (maximum number of children a single node can have)
	 * @param citeGraph - {@link DiGraph} object to store the graph into
	 */
	private static void buildGraph(String id, int height, int width,  DiGraph citeGraph) {
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
		citeGraph.addVertex(id);

		for (int i = 0; i < references.size() && counter > 0; i++) {
			source = references.get(i);
			paper = Search.binarySearchID(DataServer.PaperList.papers, source);
			if(paper == null)
				continue;
			citeGraph.addCiteEdge(id, paper);
			buildGraph(source, width, height - 1, citeGraph);
			counter--;
		}

		return;
	}

	/**
	 * Helper function to call the recursive buildGraph function
	 * @param id - String ID of the root node
	 * @param height - height of the graph (maximum depth)
	 * @param width - width of the graph (maximum number of children a single node can have)
	 * @return the constructed {@link DiGraph}
	 */
	public static DiGraph Graph(String id, int height, int width) {
		Paper root = Search.binarySearchID(DataServer.PaperList.papers, id);
		DiGraph citeGraph = new DiGraph(root);
		buildGraph(id, width, height, citeGraph);
		return citeGraph;
	}
}