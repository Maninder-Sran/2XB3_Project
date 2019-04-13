package com.paperconnect.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

public class DataCuller {

	/**
	 * This method reads the one of the raw json text file data sets, gets all paper
	 * nodes with atleast one keyword, and writes those nodes in the same format in
	 * another file. It also calls removeNullReferences to remove references (ie
	 * edges) to papers that had no keywords.
	 * 
	 * @param inFileName
	 *            File path of the aminer raw dataset json text file
	 * @param transistionFileName
	 *            File path of transition file that will hold paper nodes without
	 *            keywords
	 * @param outFileName
	 *            File path of final outfile file that will hold paper nodes without
	 *            keywords and with no references to removed nodes.
	 */
	private static void keywordFinder(String inFileName, String transistionFileName, String outFileName) {

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

	/**
	 * This method parses a json text file containing paper nodes and screens out
	 * all paper nodes specified by the input parameter. It puts the remaining paper
	 * nodes into a new json object text file.
	 * 
	 * @param fileName
	 *            File path of the json object text file containing the paper nodes
	 * @param outFileName
	 *            File path for the new outfile json object text file
	 * @param nullReferences
	 *            A hash set containing string ids of all the paper nodes to be
	 *            screened out
	 */
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

	/**
	 * This method takes in json object text file containing paper nodes. From this
	 * data, it creates a set of all unique keywords found in the file. Then, the
	 * method assigns all the paper nodes to all the keyword categories they
	 * contain. In the end, a json object text file is the result, each json object
	 * containing the keyword category and identifying information for all paper
	 * nodes in sorted citation count order (descending) associated with that
	 * keyword.
	 * 
	 * @param inFileName
	 *            File path of json object text file containing paper nodes
	 * @param outFileName
	 *            File path of outfile containing keywords and all its related
	 *            papers
	 */
	// function to make a json lookup table from a json dataset input file
	private static void keywordLookupJSON(String inFileName, String outFileName) {

		// Declaring variables
		JSONObject obj;
		JSONObject temp;
		String id;
		int citations;
		JSONArray keywords, parentPaperKeywords;

		// Data structures for storing loop up table info (This is memory heavy)
		Hashtable<String, ArrayList<Paper>> lookupKey = new Hashtable<String, ArrayList<Paper>>();
		Set<String> keys = new HashSet<String>();
		JSONArray authors;

		String title = "";
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
					if (!keys.contains(thisElement.trim())) {
						// storing keyword in lookup table and hash set keys to keep track of unique
						// keywords
						ArrayList<Paper> paperList = new ArrayList<Paper>();
						keys.add(thisElement.trim());
						lookupKey.put(thisElement.trim(), paperList);
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
				
				if (title == null || title.contains("??"))
					title = (String) obj.get("venue");
				
				id = (String) obj.get("id");
				
				try {
					citations = (int) ((long) obj.get("n_citation"));
				} catch (NullPointerException e) {
					citations = 0;
				}
				
				try {
					authors = (JSONArray) obj.get("authors");
					temp = (JSONObject) authors.get(0);
					author = temp.get("name").toString();
					if (author == "") {
						author = "NA";
					}
				} catch (NullPointerException e) {
					author = "NA";
				}

				try {
					publishDate = String.valueOf((long) obj.get("year"));
				} catch (NullPointerException e) {
					publishDate = "NA";
				}
				Paper paper = new Paper(id, title, author, publishDate, citations);
				// Putting each paper into their respective keyword category
				Iterator<String> iterator = parentPaperKeywords.iterator();
				String thiskeyword = "";
				while (iterator.hasNext()) {
					thiskeyword = iterator.next();
					lookupKey.get(thiskeyword.trim()).add(paper);
				}
			}

			// writing each keyword category along with all its papers (id and title) into
			// output file
			// each line in the output file contains one keyword and all its papers

			lookupKey.forEach((k, v) -> {
				JSONObject keywordLine = new JSONObject();
				JSONArray papers = new JSONArray();
				String keyword = k.trim();

				//sort papers in descending citation count order
				Merge.sortMergeTD(v, v.size());
				
				for (Paper s : v) {
					JSONObject papershort = new JSONObject();
					papershort.put("id", s.getField(PaperFields.ID));
					papershort.put("title", s.getField(PaperFields.TITLE));
					papershort.put("author", s.getField(PaperFields.AUTHOR));
					papershort.put("year", s.getField(PaperFields.PUBLISH_DATE));
					papers.add(papershort);
				}
				keywordLine.put("keyword", keyword);
				keywordLine.put("paper_list", papers);
				try {
					fileWriter.write(keywordLine.toString() + "\n");
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

	/**
	 * A method to check that each raw aminer data set file is sorted
	 * lexicographically with its id key.
	 * 
	 * @param fileName
	 *            File path of raw aminer data set file
	 * @return A boolean value indicating whether file is sorted
	 */
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

	/**
	 * This method takes out all references (ids) in the paper nodes that are not
	 * part of the three file data sets we chose to use. It does so by taking out
	 * all references that are greater than the id of the last paper in the third
	 * aminer data set file, since all ids are sorted in file, and in relation to
	 * each file (aminer_0 has ids are lexicographically less than aminer_1 ids).
	 * Also, shortens the abstract fields of all paper nodes to 500 characters so as
	 * to decrease file size of data set.
	 * 
	 * @param inFileName
	 *            File path of aminer data set input file
	 * @param outFileName
	 *            File path of the outfile containing no references from anywhere
	 *            outside the three data set files being used.
	 */
	private static void removeUnusedReferences(String inFileName, String outFileName) {
		JSONObject obj;
		JSONArray references;

		// Last id of aminer papers_2 as we are only using three aminer data files
		String greatestID = "53e99a0eb7602d9702261faf";
		String line = null;

		try {
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(outFileName);

			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				references = (JSONArray) obj.get("references");

				//shorten abstract early on so the rest of the functions have better performance
				try {
					String abst = (String) obj.get("abstract");
					if (abst.length() > 500)
						obj.put("abstract", abst.substring(0, 500) + "...");
				} catch (NullPointerException e) {

				}

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

	/**
	 * This method merges two json object data set files into one. More
	 * specifically, it appends the contents of one file with the other.
	 * 
	 * @param sourceFileName
	 *            File path of the json object data set being appended to
	 * @param appendFileName
	 *            File path of the json object data set being added
	 */
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

	/**
	 * A parser method to read in the json object look up table generated by
	 * keywordLookupJSON.
	 * 
	 * @param fileName
	 *            File path of lookup table
	 * @return ret An ArrayList of {@link LookupTableLine} objects containing all
	 *         keywords and its related categories
	 */
	private static ArrayList<LookupTableLine> readLookupTableJSON(String fileName) {
		String line = null;
		JSONObject keywordLine;
		ArrayList<LookupTableLine> ret = new ArrayList<LookupTableLine>();
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
					// get json object of stored keyword list in each line of input file
					keywordLine = (JSONObject) new JSONParser().parse(line);

					// place the json object in a new LookupTableLine ADT with the keyword and a
					// string representation of the json object
					paper = new LookupTableLine(keywordLine.get("keyword").toString(), keywordLine.toString());

					// add the initialized LookupTableLine instance to the list of LookupTableLine
					// objects to be returned
					ret.add(paper);

				} catch (Exception f) {
					continue;
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Sort Keyword Lookup table lexicographically based on the keyword string.
	 * 
	 * @param inFileName
	 *            File path of unsorted look up table
	 * @param outFileName
	 *            File path of sorted output look up table
	 */
	private static void sortLookupTableKeywordsJSON(String inFileName, String outFileName) {
		String line = null;
		int count = 0;
		long start = System.nanoTime();

		// Read in all the json lookup table objects
		ArrayList<LookupTableLine> papers = readLookupTableJSON(inFileName);

		// sort them using Merge sort
		Merge.sortMergeTD(papers, papers.size());
		try {
			FileWriter fileWriter = new FileWriter(outFileName);
			for (LookupTableLine p : papers) {
				if (p.getKeyword().length() == 0) {
					continue;
				}
				fileWriter.write(p.getRightHalf());
				fileWriter.write("\n");
				count++;
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Lookup Table Sorted in  " + (System.nanoTime() - start) / 1000000000.0 + " seconds. "
				+ (count) + " Keywords.");
	}

	/**
	 * Final parsing of the final paper nodes list (final formatted dataset).
	 * Parsing all data needed for {@link Paper} objects only.
	 * 
	 * @param inFileName
	 *            File path of final paper node data set
	 * @param outFileName
	 *            File path of final formatted json object paper nodes data set
	 */
	private static void parseRelevantData(String inFileName, String outFileName) {
		// declaring variables
		JSONObject obj, newObj, temp;
		String id, abst, title, author, publishDate;
		long citations;
		JSONArray references, authors;
		Set<String> nullReferences = new HashSet<String>();
		String line = null;
		long start = System.nanoTime();
		int count = 0;

		try {

			// opening files to be be read and written to
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter(outFileName);

			// iterating over each line of file, in other words each paper node
			while ((line = bufferedReader.readLine()) != null) {

				// Getting relevant data from the paper node
				id = null;
				obj = (JSONObject) new JSONParser().parse(line);
				newObj = new JSONObject();

				// Get id
				id = (String) obj.get("id");
				newObj.put("id", id);

				// Get title of paper
				title = obj.get("title").toString();
				if (title == null || title.contains("??")) {
					try {
						title = obj.get("venue").toString();
					} catch (NullPointerException e) {
						title = "Generic paper";
					}
				}
				newObj.put("title", title);

				// Get abstract of paper
				try {
					abst = obj.get("abstract").toString();
				} catch (NullPointerException e) {
					abst = "NA";
				}
				newObj.put("abstract", abst);

				// Get number of citations of paper
				try {
					citations = (int) ((long) obj.get("n_citation"));

				} catch (NullPointerException e) {
					citations = 0;
				}
				newObj.put("n_citation", citations);

				// get references
				references = (JSONArray) obj.get("references");
				if (references == null || references.isEmpty()) {
					references = new JSONArray();
				}
				newObj.put("references", references);

				// Get author of paper
				try {
					authors = (JSONArray) obj.get("authors");
					temp = (JSONObject) authors.get(0);
					author = temp.get("name").toString();
					if (author == "" || author == null) {
						author = "NA";
					}
				} catch (NullPointerException e) {
					author = "NA";
				}
				newObj.put("author", author);

				// get publish date of paper
				try {
					publishDate = obj.get("year").toString();
				} catch (NullPointerException e) {
					publishDate = "NA";
				}
				newObj.put("year", publishDate);

				fileWriter.write(newObj.toJSONString() + "\n");
				count++;
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
	}

	/**
	 * Removes all null references (edges that point to non-existent nodes in the
	 * data set) from the final data set.
	 * 
	 * @param inFileName
	 *            File path of final data set
	 * @param outFileName
	 *            File path of final complete data set
	 */
	private static void removeNullReferencesFromFinal(String inFileName, String outFileName) {
		JSONObject obj;
		JSONArray references;
		String id = "";
		String line = null;
		Set<String> allIDs = new HashSet<String>();

		try {
			
			//open input file
			FileReader fileReader = new FileReader(inFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			//add all paper ids from input into hash set
			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				id = (String) obj.get("id");
				allIDs.add(id);
			}
			bufferedReader.close();

			//open input file again to remove all null references
			bufferedReader = new BufferedReader(new FileReader(inFileName));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFileName));
			line = null;

			while ((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				references = (JSONArray) obj.get("references");

				// write to outfile and continue to next paper node if it contains no references
				if (references == null) {
					bufferedWriter.write(obj.toJSONString() + "\n");
					continue;
				}

				// checking all references of the paper node to find and remove null references
				Iterator<String> iterator = references.iterator();
				String thisElement = "";
				while (iterator.hasNext()) {
					thisElement = iterator.next();
					if (!allIDs.contains(thisElement)) {
						iterator.remove();
					}
				}

				// write to outfile
				bufferedWriter.write(obj.toJSONString() + "\n");
			}
			bufferedWriter.close();
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method to check that the final data set graph is completely free of all null references
	 * (edges to non-existent paper nodes).
	 *  
	 * @param fileName File path of final formatted data set
	 * @return A boolean value indicating that the final network graph has no null edges
	 */
	private static boolean finalNullReferenceCheck(String fileName) {
		JSONObject obj;
		JSONArray references;
		String id = "";
		String line = null;
		int counter = 0;
		Set<String> allIDs = new HashSet<String>();

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				counter++;
				System.out.println(counter);
				obj = (JSONObject) new JSONParser().parse(line);
				id = (String) obj.get("id");
				allIDs.add(id);
			}
			bufferedReader.close();

			bufferedReader = new BufferedReader(new FileReader(fileName));
			line = null;
			counter = 0;

			while ((line = bufferedReader.readLine()) != null) {
				counter++;
				System.out.println(counter);
				obj = (JSONObject) new JSONParser().parse(line);

				references = (JSONArray) obj.get("references");

				// continue to next paper node if it contains no references
				if (references == null) {
					continue;
				}

				// checking all references of the paper node to find and remove null references
				Iterator<String> iterator = references.iterator();
				String thisElement = "";
				while (iterator.hasNext()) {
					thisElement = iterator.next();
					if (!allIDs.contains(thisElement)) {
						bufferedReader.close();
						return false;
					}
				}

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

	/**
	 * Dirver for data culler, the function that makes the final paper list dataset and sorted lookup table
	 * from the three raw aminer data sets (aminer_0, aminer_1, aminer_2)
	 * 
	 */
	public static void main(String[] args) {
		
		//file paths of all input, transistion, and outfiles
		String ap_0 = "data/aminer_papers_0.txt";
		String ap_1 = "data/aminer_papers_1.txt";
		String ap_2 = "data/aminer_papers_2.txt";

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

		String transPaperList = "data/ap_paperlist_trans.txt";
		String FinalPaperList = "data/ap_paperList.txt";

		/*
		 * parse paper nodes with keywords while also removing all references to paper
		 * nodes with no keywords in each file
		 */
		keywordFinder(ap_0, trans0, KwOutFile0);
		keywordFinder(ap_1, trans1, KwOutFile1);
		keywordFinder(ap_2, trans2, KwOutFile2);

		/*
		 * remove all references that point to a paper node outside of the three
		 * selected data set files we are working with. Also limits all abstracts to a
		 * 500 char maximum limit.
		 */
		removeUnusedReferences(KwOutFile0, FormattedOutputFile0);
		removeUnusedReferences(KwOutFile1, FormattedOutputFile1);
		removeUnusedReferences(KwOutFile2, FormattedOutputFile2);

		// combine all data sets into one file
		mergeDataSet(FinalPaperFile, FormattedOutputFile0);
		mergeDataSet(FinalPaperFile, FormattedOutputFile1);
		mergeDataSet(FinalPaperFile, FormattedOutputFile2);

		// create lookup from merged paper list
		keywordLookupJSON(FinalPaperFile, transKeywordFile);
		sortLookupTableKeywordsJSON(transKeywordFile, FinalKeywordFile);

		// parse all relevant fields from paper nodes (ie no need for keyword list
		// anymore because of lookup table)
		parseRelevantData(FinalPaperFile, transPaperList);

		
		// Get rid of all remaining null references, ie a reference that was in a
		// different input file whose paper node did not have a keyword so it was removed
		
		removeNullReferencesFromFinal(transPaperList, FinalPaperList);

		// System.out.println(finalNullReferenceCheck(FinalPaperList));
	}
}
