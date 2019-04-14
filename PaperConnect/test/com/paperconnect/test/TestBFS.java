package com.paperconnect.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.paperconnect.client.Paper;
import com.paperconnect.graph.BreadthFirstSearch;
import com.paperconnect.graph.DiGraph;

public class TestBFS {

	@Test
	public void testBFS1() {
		Paper root = new Paper("root", "root", 0);
		Paper p1 = new Paper("p1", "p1", 0);
		Paper p2 = new Paper("p2", "p2", 0);
		Paper p3 = new Paper("p3", "p3", 0);
		Paper p4 = new Paper("p4", "p4", 0);
		Paper p5 = new Paper("p5", "p5", 0);
		Paper p6 = new Paper("p6", "p6", 0);
		Paper p7 = new Paper("p7", "p7", 0);
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
		d.addCiteEdge("root", p6);
		d.addCiteEdge("p1", p4);
		d.addCiteEdge("p1", p5);
		d.addCiteEdge("p3", p7);
		ArrayList<Paper> result = BreadthFirstSearch.compute(d, root, 3, 3);
		assert(result.get(0) == root);
		assert(result.get(1) == null);
		assert(result.get(2) == p1);
		assert(result.get(3) == p2);
		assert(result.get(4) == p3);
		assert(result.get(5) == null);
		assert(result.get(6) == p4);
		assert(result.get(7) == p5);
		assert(result.get(8) == p7);
		assert(result.get(9) == null);
	}

	@Test
	public void testBFS2() {
		Paper root = new Paper("root", "root", 0);
		Paper p1 = new Paper("p1", "p1", 0);
		Paper p2 = new Paper("p2", "p2", 0);
		Paper p3 = new Paper("p3", "p3", 0);
		Paper p4 = new Paper("p4", "p4", 0);
		DiGraph d = new DiGraph(root);
		d.addVertex("root");
		d.addVertex("p1");
		d.addVertex("p2");
		d.addVertex("p3");
		d.addVertex("p4");
		d.addCiteEdge("root", p1);
		d.addCiteEdge("root", p2);
		d.addCiteEdge("p1", p3);
		d.addCiteEdge("p1", p4);
		ArrayList<Paper> result = BreadthFirstSearch.compute(d, root, 3, 3);
		assert(result.get(0) == root);
		assert(result.get(1) == null);
		assert(result.get(2) == p1);
		assert(result.get(3) == p2);
		assert(result.get(4) == null);
		assert(result.get(5) == p3);
		assert(result.get(6) == p4);
		assert(result.get(7) == null);
	}
	
}
