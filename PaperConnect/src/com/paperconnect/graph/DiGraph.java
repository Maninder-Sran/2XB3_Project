package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Hashtable;

import com.paperconnect.client.Paper;

public class DiGraph {
	private Hashtable<String, ArrayList<Paper>> citeGraph;
	private ArrayList<String> vertices;
	private Paper root;

	public DiGraph(Paper root) {
		this.citeGraph = new Hashtable<String, ArrayList<Paper>>();
		this.root = root; 
		this.vertices = new ArrayList<String>();
	}
	
	public void addVertex(String vertex) {
		citeGraph.put(vertex, new ArrayList<Paper>());
		vertices.add(vertex);
	}
	
	public void addCiteEdge(String vertex, Paper edge) {
		citeGraph.get(vertex).add(edge);
	}
	
	public Hashtable<String, ArrayList<Paper>> getGraph() {
		return citeGraph;
	}
	
	public ArrayList<Paper> getChildren(String id){
		return citeGraph.get(id);
	}
	
	public boolean checkVertices(String vertex) {
		return citeGraph.containsKey(vertex);
	}
	public Paper getRoot() {
		return root;
	}
}
