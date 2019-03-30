package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

public class BreadthFirstSearch {

	private Paper startNode;
	private DiGraph graph;

	public BreadthFirstSearch(DiGraph graph) {
		startNode  = graph.getRoot();
		this.graph = graph;
	}

	public ArrayList<Paper> compute() {

		ArrayList<Paper> Result = new ArrayList<Paper>();
		Paper current;
		ArrayList<Paper> neighbors;

		Queue<Paper> queue = new LinkedList<>();
		ArrayList<Paper> explored = new ArrayList<>();
		queue.add(startNode);
		queue.add(null);
		Result.add(startNode);

		startNode.setVisited(true);
		int level = 0;

		while (!queue.isEmpty()) {
			current = queue.poll();
			Result.add(current);

			if (current == null) {
				level++;

				queue.add(null);
				if (queue.peek() == null)
					break; // when you have two consecutive null in the queue, this means you are at the
							// end of the queue.
				else
					continue;
			}

			neighbors = graph.getChildren(current.getField(PaperFields.ID));
			for (int i = 0; i < neighbors.size(); i++) {

				if (neighbors.get(i).getVisited() != true) {
					queue.add(neighbors.get(i));
					// Result.add(neighbors.get(i));
					neighbors.get(i).setVisited(true);
				}
			}
		}
		return Result;
	}

}