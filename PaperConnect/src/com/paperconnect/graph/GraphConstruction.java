package com.paperconnect.graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.gwt.core.ext.typeinfo.ParseException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.thirdparty.json.JSONObject;
import com.paperconnect.client.Paper;

public class GraphConstruction {

	public static Hashtable<String, Paper> intDataSet(String fileName) {
		String line = null, tempID, tempAbstract, tempTitle;
		long numCitations;
		JSONObject obj;
		FileReader fileReader;

		Hashtable<String, Paper> paperData = new Hashtable<String, Paper>();
		try {
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) JSONParser.parse(line);
				tempID = (String) obj.get("id");
				tempTitle = (String) obj.get("title");
				if (tempTitle == null || tempTitle.contains("???"))
					tempTitle = (String) obj.get("venue");
				try {
					tempAbstract = (String) obj.get("abstract");
					tempAbstract = tempAbstract.substring(0, 499);
					numCitations = (long) obj.get("n_citation");
				} catch (NullPointerException e) {
					numCitations = 0;
					tempAbstract = null;
				}
				Paper paper = new Paper(tempID, tempTitle, tempAbstract, numCitations);
				paperData.put(tempID, paper);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paperData;
	}

	public static void buildGraph(String id, int width, int height, DiGraph citeGraph,
			Hashtable<String, Paper> paperData) {
		Paper paper = paperData.get(id);
		int counter = width;
		String source = null;

		if (height == 0 || paper.getReferences() == null) {
			citeGraph.addVertex(id);
			return;
		}

		ArrayList<String> references = paper.getReferences();
		Collections.sort(references);
		Iterator<String> iterator = references.iterator();
		citeGraph.addVertex(id);

		while (counter > 0 && iterator.hasNext()) {
			source = iterator.next();
			paper = paperData.get(source);
			citeGraph.addCiteEdge(id, paper);
			buildGraph(source, width, height - 1, citeGraph, paperData);
			counter--;
		}

		return;
	}

	public static DiGraph Graph(String id, String fileName) {
//		Hashtable<String, Paper> paperData = intDataSet(fileName);
//		System.out.println("DataSet loaded");
//		Paper root = paperData.get(id);
		Paper root = new Paper("hello", "test", "this is a test", new ArrayList<String>(Arrays.asList("xyz", "abc")),
				12);
		Hashtable<String, Paper> paperData = new Hashtable<String, Paper>();
		paperData.put("hello", root);
		paperData.put("xyz",
				new Paper("xyz", "test2", "this is a test2", new ArrayList<String>(Arrays.asList("okay")), 12));
		paperData.put("abc", new Paper("abc", "test3", "this is a test3", new ArrayList<String>(Arrays.asList()), 13));
		paperData.put("okay",
				new Paper("okay", "test4", "this is a test4", new ArrayList<String>(Arrays.asList()), 12));

		int width = 2, height = 5;
		DiGraph citeGraph = new DiGraph(root);
		buildGraph(id, width, height, citeGraph, paperData);
		return citeGraph;
	}

	public static void main(String[] args) {
		DiGraph citeGraph = Graph("hello", "hello");
		Hashtable<String, ArrayList<Paper>> graph = citeGraph.getGraph();
		graph.forEach((k, v) -> System.out.println(k + "  " + v));
//		citeGraph = Graph("53e99838b7602d970205e7e4", "../../../Documents/Software 1/2XB3/final project/data/ap_final.txt");
	}}}catch(

	IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(
	ParseException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return obj;
	}

	public static void main(String[] args) {
		System.out.println(getPaper(1));
//		DiGraph citeGraph;
//		Hashtable<String, Paper> paperData = intDataSet(fileName);
//		citeGraph = Graph("53e99838b7602d970205e7e4", "../../../Documents/Software 1/2XB3/final project/data/ap_final.txt");
	}
}
