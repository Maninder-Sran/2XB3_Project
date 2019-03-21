package com.paperconnect.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.paperconnect.client.Paper;

public class DataCuller {

	public DataCuller() {

	}

	public static void keywordFinder(String inFileName, String transistionFileName, String outFileName) {

		// declaring variables
		JSONObject obj;
		String id;
		JSONArray keywords;
		Set<String> nullReferences = new HashSet<String>();
		String line = null;
		long start = System.nanoTime();
		int count = 0;

		try {

			// opening files to be be read and written to
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(transistionFileName);

			// iterating over each line of file, in other words each paper node
			while ((line = bufferedReader.readLine()) != null) {

				// Getting relevant data from the paper node
				id = null;
				obj = (JSONObject) new JSONParser().parse(line);
				id = (String) obj.get("id");
				if (id == null) {
					continue;
				}
				keywords = (JSONArray) obj.get("keywords");
				// skipping over all paper nodes with no keywords, while also adding them
				// to a nullReferences list so they can be removed later on from the dataset
				if (keywords == null) {
					nullReferences.add(id);
					continue;
				}

				// writing all paper nodes with keywords into a different file
				Iterator<String> iterator = keywords.iterator();
				// if there is at least one keyword
				if (iterator.hasNext()) {
					fileWriter.write(obj.toJSONString() + "\n");
					count++;
				}
			}

			// printing out run time of function and number of nodes with keywords in file
			System.out.println("Done in " + (System.nanoTime() - start) / 1000000000.0 + " seconds. " + (count)
					+ " nodes with keywords.");
			bufferedReader.close();
			fileWriter.flush();
			fileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// removing all null referernces, ie the paper node ids cited in other papers
		// that had no keywords
		removeNullReferences(transistionFileName, outFileName, nullReferences);
	}

	private static void removeNullReferences(String fileName, String outFileName,Set<String> nullReferences) {

		// Declaring Variables
		JSONObject obj;
		JSONArray references;

		String line = null;
		long start = System.nanoTime();
		int count = 0;

		try {

			// Opening files for reading and another one for writing
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(outFileName);

			// Iterating over each line of the read file, each line contains a paper node
			while ((line = bufferedReader.readLine()) != null) {

				// Getting relevant data from each paper node
				obj = (JSONObject) new JSONParser().parse(line);
				references = (JSONArray) obj.get("references");

				// continue to next paper node if it contains no references
				if (references == null) {
					fileWriter.write(obj.toJSONString() + "\n");
					continue;
				}

				// checking all references of the paper node to find and remove null references
				Iterator<String> iterator = references.iterator();
				String thisElement = "";
				while (iterator.hasNext()) {
					thisElement = iterator.next();
					if (nullReferences.contains(thisElement)) {
						iterator.remove();
					}
				}
				
				// add edited paper node to new file
				fileWriter.write(obj.toJSONString() + "\n");
			}
			System.out.println("Set made in " + (System.nanoTime() - start) / 1000000000.0 + " seconds. " + (count)
					+ " nodes with keywords.");
			bufferedReader.close();
			fileWriter.flush();
			fileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void keywordLookup(String inFileName, String outFileName) {

		// Declaring variables
		JSONObject obj;
		String id;
		long citations;
		JSONArray keywords, parentPaperKeywords;

		// Data structures for storing loop up table info (This is memory heavy)
		Hashtable<String, ArrayList<Paper>> lookupKey = new Hashtable<String, ArrayList<Paper>>();
		Set<String> keys = new HashSet<String>();

		String title = "";

		String line = null, thisElement;
		long start = System.nanoTime();
		int count = 0;

		try {
			// Opening input file for reading
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// iterating through each input file line/paper node
			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				keywords = (JSONArray) obj.get("keywords");

				// Skipping over paper node if it contains no keywords
				if (keywords == null) {
					continue;
				}

				// Initializing hash table for all unique keywords serving as keys and empty
				// arraylist<string>
				// being values to store paper info later on
				Iterator<String> iterator = keywords.iterator();
				while (iterator.hasNext()) {

					// keyword being added if its unique
					thisElement = iterator.next();
					if (!keys.contains(thisElement)) {
						// storing keyword in lookup table and hash set keys to keep track of unique
						// keywords
						ArrayList<Paper> paperList = new ArrayList<Paper>();
						keys.add(thisElement);
						lookupKey.put(thisElement, paperList);
						count++;
					}
				}
			}
			bufferedReader.close();

			// open input and output files again
			FileWriter fileWriter = new FileWriter(outFileName);
			fileReader = new FileReader(inFileName);
			bufferedReader = new BufferedReader(fileReader);

			// Iterating through each line/paper node in input file
			while ((line = bufferedReader.readLine()) != null) {

				// parse relevant data
				obj = (JSONObject) new JSONParser().parse(line);
				parentPaperKeywords = (JSONArray) obj.get("keywords");
				title = (String) obj.get("title");
				id = (String) obj.get("id");
				try {
					citations = (long) obj.get("n_citation");
				} catch (NullPointerException e) {
					citations = 0;
				}

				// Putting each paper into their respective keyword category
				Iterator<String> iterator = parentPaperKeywords.iterator();
				String thiskeyword = "";
				while (iterator.hasNext()) {
					thiskeyword = iterator.next();
					if (title == null || title.contains("??"))
						title = (String) obj.get("venue");
					Paper paper = new Paper(id, title, citations);
					lookupKey.get(thiskeyword).add(paper);
				}
			}

			// writing each keyword category along with all its papers (id and title) into
			// output file
			// each line in the output file contains one keyword and all its papers
			lookupKey.forEach((k, v) -> {
				StringBuilder sb = new StringBuilder();
				sb.append(k + " = " + "[");

				Collections.sort(v);
				for (Paper s : v)
					sb.append(s.getId() + "::"+ s.getTitle() + ",");

				try {
					fileWriter.write(sb.toString().substring(0, sb.length() - 1) + "]\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			// close all file readers and writers
			fileWriter.flush();
			fileWriter.close();
			bufferedReader.close();

			// print out run time of function and number of keywords
			System.out.println("Keyword look up table made in " + (System.nanoTime() - start) / 1000000000.0
					+ " seconds. " + (count) + " keywords.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static boolean isSorted(String fileName) {
		JSONObject obj, obj2;
		String id, id2;
		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			line = bufferedReader.readLine();
			obj = (JSONObject) new JSONParser().parse(line);
			id = (String) obj.get("id");

			while ((line = bufferedReader.readLine()) != null) {
				obj2 = (JSONObject) new JSONParser().parse(line);
				id2 = (String) obj2.get("id");
				if (id.compareTo(id2) > 0) {
					return false;
				}
				id = id2;
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;

	}

	public static ArrayList<Paper> readPaperList(String fileName) {
		JSONObject obj;
		String id, title;
		long numCitations = 0;
		String line = null;
		ArrayList<Paper> ret = new ArrayList<Paper>();
		long start = System.nanoTime();
		int count = 0;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				id = (String) obj.get("id");
				title  = (String) obj.get("title");
				if(title == null || title.contains("???"))
					title = (String) obj.get("venue");
				try {
					numCitations = (long) obj.get("n_citation");
				}catch(NullPointerException e) {
					numCitations = 0;
				}
				ret.add(new Paper(id, title, numCitations));
				count++;
			}
			System.out.println(
					"Done in " + (System.nanoTime() - start) / 1000000000.0 + " seconds. " + (count) + " nodes.");
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public static void removeUnusedReferences(String inFileName, String outFileName) {
		JSONObject obj;
		JSONArray references;
		String greatestID = "53e99a0eb7602d9702261faf";
		String line = null;
		
		try {
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(outFileName);
			
			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				references = (JSONArray) obj.get("references");
				
				// continue to next paper node if it contains no references
				if (references == null) {
					fileWriter.write(obj.toJSONString() + "\n");
					continue;
				}

				// checking all references of the paper node to find and remove null references
				Iterator<String> iterator = references.iterator();
				String thisElement = "";
				while (iterator.hasNext()) {
					thisElement = iterator.next();
					if (thisElement.compareTo(greatestID) > 0) {
						iterator.remove();
					}
				}
				
				fileWriter.write(obj.toJSONString() + "\n");
			}
			bufferedReader.close();
			fileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	public static void mergeDataSet(String sourceFileName, String appendFileName) {
		JSONObject obj;
		String line = null;
		
		try {
			FileReader fileReader = new FileReader(appendFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(sourceFileName, true);
			
			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				fileWriter.write(obj.toJSONString() + "\n");
			}
			bufferedReader.close();
			fileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		// System.out.println(DataCuller.isSorted());\
		// DataCuller.removeNullReferences();
		// DataCuller.references();
		// DataCuller.keywordFinder();
//		DataCuller.keywordLookup("data/aminer_papers_0_nr_test.json",
//		 "data/ap_0_lookup.txt");
//		Integer[] test = { 1, 2, 3, 4, 4, 5, 6, 7 };
//		Quick.sortBasicQuick(test);
//		for (int i : test)
//			System.out.print(i + " ");
		// DataCuller.keywordFinder("data/aminer_papers_0.txt",
		// "data/aminer_papers_0_kw.json");
		String infile = "../../../Downloads/Compressed/aminer_papers_2.txt";
		String transistion = "../../../Documents/Software 1/2XB3/final project/data/ap_2/ap_kw_2.txt";
		String outfile = "../../../Documents/Software 1/2XB3/final project/data/ap_2/ap_nr_2.txt";
		String outfile2 = "../../../Documents/Software 1/2XB3/final project/data/ap_2/ap_final_2.txt";
		String sourceFile = "../../../Documents/Software 1/2XB3/final project/data/ap_final.txt";
		mergeDataSet(sourceFile, outfile2);
//		keywordFinder(infile, transistion, outfile);
//		removeUnusedReferences(outfile, outfile2);
		System.out.println("\nDone");
//		ArrayList<Paper> list = DataCuller.readPaperList("data/aminer_papers_0_nr.json");
//		long start = System.nanoTime();
//		System.out.println(Search.binarySearchID(list, "53e997d1b7602d9701fc90ff"));
//		System.out.println("Binary search done in " + (System.nanoTime()-start)/1000000000.0 + " seconds.");
//		start = System.nanoTime();
//		System.out.println(Search.sequentialSearchID(list, "53e997d1b7602d9701fc90ff"));
//		System.out.println("Sequential search done in " + (System.nanoTime()-start)/1000000000.0 + " seconds.");
	}
}
