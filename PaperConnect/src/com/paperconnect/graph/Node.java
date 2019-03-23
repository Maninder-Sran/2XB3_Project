package com.paperconnect.graph;

import java.util.ArrayList;

public class Node {

	// A Unique Identifier for our node

	// An arraylist containing a list of Nodes that
	// This node is directly connected to - It's child nodes.
	ArrayList<Node> Children;
	private String startingNode;
	/// Node rightChild;

	public Node(String startingNode, ArrayList<Node> Children) {
		this.startingNode = startingNode;
		this.Children = Children;
		// this.rightChild = secondChild;
	}

	public ArrayList<Node> getChildren() {
		ArrayList<Node> childNodes = new ArrayList<>();
		for (int i = 0; i < this.Children.size(); i++) {
			if (this.Children.get(i) != null) {
				childNodes.add(this.Children.get(i));
			}

		}
		return childNodes;
	}

	// An auxiliary function which allows
	// us to remove any child nodes from
	// our list of child nodes.
	public boolean removeChild(Node n) {
		return false;
	}

}