package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Hashtable;

import com.paperconnect.client.Paper;

/**
 * 
 * A Class to define the methods to be used by the graphing algorithm for
 * generating the graph representating the connections between the papers.
 *
 */
public class DiGraph {
	private Hashtable<String, ArrayList<Paper>> citeGraph;
	private Paper root;

	/**
	 * @param root Initializing the fields with the root of the constructor for
	 *             taking the root node in.
	 */
	public DiGraph(Paper root) {
		this.citeGraph = new Hashtable<String, ArrayList<Paper>>();
		this.root = root;
	}

	/**
	 * 
	 * @param vertex A string of vertex defined to keep track of all the nodes been
	 *               looked at to keep their information about their children and
	 *               what nodes is it pointing at.
	 */
	public void addVertex(String vertex) {
		citeGraph.put(vertex, new ArrayList<Paper>());
	}

	/**
	 * 
	 * @param vertex A string of vertex keeping track of all the nodes that had been
	 *               looked at and keeping a record of their other connections.
	 * @param edge   A {@link Paper} node to add create the edge of the graph during
	 *               its construction and when you go through the vertex.
	 */
	public void addCiteEdge(String vertex, Paper edge) {
		citeGraph.get(vertex).add(edge);
	}

	/**
	 * 
	 * @return a method to return the {@link citeGraph}.
	 */
	public Hashtable<String, ArrayList<Paper>> getGraph() {
		return citeGraph;
	}

	/**
	 * 
	 * @param id a String containing the Identification information for which is
	 *           unique for each paper and that's how they are identified.
	 * @return It returns a {@link citeGraph} object with its id being assigned to
	 *         the required node.
	 */
	public ArrayList<Paper> getChildren(String id) {
		return citeGraph.get(id);
	}

	/**
	 * 
	 * @return an accessor method to get the {@link Paper} root of a particular
	 *         academic paper.
	 */
	public Paper getRoot() {
		return root;
	}
}
