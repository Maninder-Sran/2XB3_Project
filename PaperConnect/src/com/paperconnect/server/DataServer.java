package com.paperconnect.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperShort;

public class DataServer {

	static ArrayList<Paper> paperLongs;

	public static void init() {
		readPaperFile("data/ap_final.txt");
	}

	private static void readPaperFile(String fileName) {
		String line = null, id, title, abst, author, publishDate;
		long citations;
		ArrayList<String> references = new ArrayList<String>();

		JSONValue lineValue;
		JSONObject obj;
		JSONValue tempObj;
		JSONArray temp;
		paperLongs = new ArrayList<Paper>();
		Paper paper;
		long start = System.nanoTime();

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				// Get paper node from line in input file
				lineValue = JSONParser.parseStrict(line);
				obj = lineValue.isObject();

				// Get id of paper
				id = obj.get("id").isString().stringValue();

				// Get title of paper
				title = obj.get("title").toString();
				if (title == null || title.contains("??"))
					title = obj.get("venue").toString();

				// Get abstract of paper
				abst = obj.get("abstract").toString();

				// Get number of citations of paper
				try {
					citations = (long) obj.get("n_citation").isNumber().doubleValue();

				} catch (NullPointerException e) {
					citations = 0;
				}

				// get all references of paper
				tempObj = obj.get("references");
				temp = tempObj.isArray();
				for (int i = 0; i < temp.size(); i++) {
					references.add(temp.get(i).isString().stringValue());
				}

				// Get author of paper
				tempObj = obj.get("author");
				temp = tempObj.isArray();
				author = temp.get(0).isString().stringValue();

				// get publish date of paper
				publishDate = obj.get("publishdate").isString().stringValue();

				// Load paper data into PaperADT and store it in list of PaperADTs
				paper = new Paper(id, title, abst, references, author, publishDate, citations);
				paperLongs.add(paper);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static class LookupTable {
		ArrayList<LookupTableLine> lookupTable;

		public void init() {
			lookupTable = readLookupTable("data/ap_0_lookup_sorted.txt");
		}

		public static ArrayList<LookupTableLine> readLookupTable(String fileName) {
			String line = null;
			ArrayList<PaperShort> ret = new ArrayList<PaperShort>();
			long start = System.nanoTime();
			int count = 0;
			// used for splitting by "="
			String[] lineSplit = null;
			LookupTableLine paper = null;
			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				while (true) {
					try {
						line = bufferedReader.readLine();
						if (line == null) {
							break;
						}
						// System.out.println(line);
						lineSplit = line.split("=");
						lineSplit[0] = lineSplit[0].trim();
						lineSplit[1] = lineSplit[1].trim();
						paper = new LookupTableLine(lineSplit[0], lineSplit[1]);
						ret.add(paper);
						count++;
						System.out.println(count);
					} catch (Exception f) {
						continue;
					}
				}
				System.out.println(
						"Done in " + (System.nanoTime() - start) / 1000000000.0 + " seconds. " + (count) + " nodes.");
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}

	}
}
