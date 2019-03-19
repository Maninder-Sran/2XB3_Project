package com.paperconnect.graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import com.google.gwt.core.ext.typeinfo.ParseException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.thirdparty.json.JSONObject;
import com.paperconnect.client.Paper;

public class GraphConstruction {
	
	public static 	Hashtable<String, Paper> intDataSet(String fileName) {
		String  line = null, tempAbstract;
		JSONValue tempTitle;
		JSONValue tempID;
		long numCitations;
		JSONValue value;
		com.google.gwt.json.client.JSONObject obj;
		FileReader fileReader;
		
		Hashtable<String, Paper> paperData = new Hashtable<String, Paper>();
		try {
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				
				value = JSONParser.parse(line);
				obj = value.isObject();
				tempID = obj.get("id");
				tempTitle = obj.get("title");
				if(tempTitle == null || tempTitle.contains("???"))
					tempTitle = (String) obj.get("venue");
				try {
					tempAbstract = (String) obj.get("abstract");
					numCitations = (long) obj.get("n_citation");
				}catch(NullPointerException e) {
					numCitations = 0;
					tempAbstract = null;
				}
				
				Paper paper = new Paper(tempID, tempTitle, tempAbstract,numCitations);
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

	public static void buildGraph(String id, int width, int height, DiGraph citeGraph, Hashtable<String, Paper> paperData) {
		Paper paper = paperData.get(id);
		int counter = width;
//		
//		if(height == 0 || paper.get("references") == null) {
//			citeGraph.addVertex(id);
//			return;
//		}
//		
//		ArrayList<String> references = new ArrayList<String>();
//		references = (ArrayList<String>) paper.get("references"); 
//		Iterator<String> iterator = references.iterator();
//
//		while(counter > 0 && iterator.hasNext()) {
//			paper = paperData.get(iterator.next());
//			citeGraph.addCiteEdge(id, paper);
//			buildGraph((String) paper.get("id"), width, height - 1, citeGraph, paperData);
//			counter--;
//		}
//		
		return;
	}
	
	public static DiGraph Graph(String id, String fileName) {
		Hashtable<String, Paper> paperData = intDataSet(fileName);
		System.out.println("DataSet loaded");
		Paper root = paperData.get(id);
		int width = 2, height = 5;
		DiGraph citeGraph = new DiGraph(root);
		buildGraph(id, width, height, citeGraph, paperData);
		return citeGraph;
	}
	public static JSONObject getPaper(int lineNum) {
		String line = "";
		JSONObject obj = null;
		FileReader fileReader;
		try {
			fileReader = new FileReader("../../../Documents/Software 1/2XB3/final project/data/ap_final.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while (lineNum == 0) {
				line = bufferedReader.readLine();
				lineNum--;
				System.out.println(lineNum);
			}
			obj = (JSONObject) new JSONParser().parse(line);
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
