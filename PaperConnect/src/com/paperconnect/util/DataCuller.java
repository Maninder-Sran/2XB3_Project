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

import com.google.gwt.dev.json.JsonObject;
import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.Paper.Fields;

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

	private static void removeNullReferences(String fileName, String outFileName, Set<String> nullReferences) {

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

	private static void keywordLookup(String inFileName, String outFileName) {

		// Declaring variables
		JSONObject obj;
		String id;
		long citations;
		JSONArray keywords, parentPaperKeywords;

		// Data structures for storing loop up table info (This is memory heavy)
		Hashtable<String, ArrayList<Paper>> lookupKey = new Hashtable<String, ArrayList<Paper>>();
		Set<String> keys = new HashSet<String>();

		String title = "";
		String abst = "";
		String author = "";
		String publishDate = "";

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
				author = (String) obj.get("authors");
				publishDate = (String) obj.get("year");
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
					sb.append(s.getField(Fields.ID) + "::" + s.getField(Fields.TITLE) + "::" + s.getField(Fields.AUTHOR) + "::" + s.getField(Fields.PUBLISH_DATE) + ",");

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

	private static boolean isSorted(String fileName) {
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

	private static ArrayList<Paper> readPaperList(String fileName) {
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
				title = (String) obj.get("title");
				if (title == null || title.contains("???"))
					title = (String) obj.get("venue");
				try {
					numCitations = (long) obj.get("n_citation");
				} catch (NullPointerException e) {
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

	private static void removeUnusedReferences(String inFileName, String outFileName) {
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
				
				String abst = (String) obj.get("abstract");
				obj.put("abstract", abst.subSequence(0, 499) + "...");

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

	private static void mergeDataSet(String sourceFileName, String appendFileName) {
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

	private static ArrayList<LookupTableLine> readLookupTable(String fileName) {
		String line = null;
		ArrayList<LookupTableLine> ret = new ArrayList<LookupTableLine>();
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

	private static void sortLookupTableKeywords(String inFileName, String outFileName) {
		String line = null;
		int count = 0;
		long start = System.nanoTime();
		ArrayList<LookupTableLine> papers = readLookupTable(inFileName);
		Collections.sort(papers);
		try {
			FileWriter fileWriter = new FileWriter(outFileName);
			for (LookupTableLine p : papers) {
				if (p.getKeyword().length() == 0) {
					continue;
				}
				fileWriter.write(p.getKeyword());
				fileWriter.write(" = ");
				fileWriter.write(p.getRightHalf());
				fileWriter.write("\n");
				count++;

				// System.out.println(count);
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String  ap_0 = "data/aminer_papers_0.txt";
		String  ap_1 = "data/aminer_papers_0.txt";
		String  ap_2 = "data/aminer_papers_0.txt";
		
		String trans0 = "data/ap_kw_0.txt";
		String trans1 = "data/ap_kw_1.txt";
		String trans2 = "data/ap_kw_2.txt";
		
		String KwOutFile0 = "data/ap_nr_0.txt";
		String KwOutFile1 = "data/ap_nr_1.txt";
		String KwOutFile2 = "data/ap_nr_2.txt";
		
		String FormattedOutputFile0 = "data/ap_0_final.txt";
		String FormattedOutputFile1 = "data/ap_1_final.txt";
		String FormattedOutputFile2 = "data/ap_2_final.txt";
		
		String FinalPaperFile = "data/ap_final.txt";
		String transKeywordFile = "data/ap_translu.txt";
		String FinalKeywordFile = "data/ap_lookup.txt";
		
		keywordFinder(ap_0, trans0, KwOutFile0);
		keywordFinder(ap_1, trans1, KwOutFile1);
		keywordFinder(ap_2, trans2, KwOutFile2);
		
		removeUnusedReferences(KwOutFile0, FormattedOutputFile0);
		removeUnusedReferences(KwOutFile1, FormattedOutputFile1);
		removeUnusedReferences(KwOutFile2, FormattedOutputFile2);
		
		mergeDataSet(FinalPaperFile, FormattedOutputFile0);
		mergeDataSet(FinalPaperFile, FormattedOutputFile1);
		mergeDataSet(FinalPaperFile, FormattedOutputFile2);
		
		keywordLookup(FinalPaperFile, transKeywordFile);
		sortLookupTableKeywords(transKeywordFile, FinalKeywordFile);
	}
}
