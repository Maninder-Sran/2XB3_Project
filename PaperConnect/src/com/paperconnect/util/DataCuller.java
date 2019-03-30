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

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperShort;
import com.paperconnect.client.PaperShort.Fields;

public class DataCuller {

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

		// Data structures for storing look up table info (This is memory heavy)
		Hashtable<String, ArrayList<Paper>> lookupKey = new Hashtable<String, ArrayList<Paper>>();
		Set<String> keys = new HashSet<String>();
		JSONArray authors;

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
					if (!keys.contains(thisElement) && !thisElement.equals("=")) {
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
				try {
					authors = (JSONArray) obj.get("authors");
					author = authors.get(0).toString();
				} catch (NullPointerException e) {
					author = "NA";
				}

				try {
					publishDate = String.valueOf((long) obj.get("year"));
				} catch (NullPointerException e) {
					publishDate = "NA";
				}
				// Putting each paper into their respective keyword category
				Iterator<String> iterator = parentPaperKeywords.iterator();
				String thiskeyword = "";
				while (iterator.hasNext()) {
					thiskeyword = iterator.next();
					if (title == null || title.contains("??"))
						title = (String) obj.get("venue");
					Paper paper = new Paper(id, title, author, publishDate, citations);
					if (!thiskeyword.equals("="))
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
					sb.append(s.getField(Paper.Fields.ID) + "::" + s.getField(Paper.Fields.TITLE) + "::"
							+ s.getField(Paper.Fields.AUTHOR) + "::" + s.getField(Paper.Fields.PUBLISH_DATE) + ",");

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

	// function to make a json lookup table from a json dataset input file
	private static void keywordLookupJSON(String inFileName, String outFileName) {

		// Declaring variables
		JSONObject obj;
		JSONObject temp;
		String id;
		long citations;
		JSONArray keywords, parentPaperKeywords;

		// Data structures for storing loop up table info (This is memory heavy)
		Hashtable<String, ArrayList<Paper>> lookupKey = new Hashtable<String, ArrayList<Paper>>();
		Set<String> keys = new HashSet<String>();
		JSONArray authors;

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
					citations = (long) obj.get("n_citation");
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

				Collections.sort(v);
				for (Paper s : v) {
					JSONObject papershort = new JSONObject();
					papershort.put("id", s.getField(Paper.Fields.ID));
					papershort.put("title", s.getField(Paper.Fields.TITLE));
					papershort.put("author", s.getField(Paper.Fields.AUTHOR));
					papershort.put("year", s.getField(Paper.Fields.PUBLISH_DATE));
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
		JSONArray authors;
		String author;
		String publishDate;
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
				try {
					authors = (JSONArray) obj.get("authors");
					author = authors.get(0).toString();
				} catch (NullPointerException e) {
					author = "NA";
				}

				try {
					publishDate = String.valueOf((long) obj.get("year"));
				} catch (NullPointerException e) {
					publishDate = "NA";
				}
				ret.add(new Paper(id, title, author, publishDate, numCitations));
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

				try {
					String abst = (String) obj.get("abstract");
					if (abst.length() > 500)
						obj.put("abstract", abst.substring(0, 500) + "...");
				} catch (NullPointerException e) {

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

	// Function to read in the symbol-based lookup table
	private static ArrayList<LookupTableLine> readLookupTable(String fileName) {
		String line = null;
		JSONObject keywordLine;
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
					lineSplit = new String[] { line.substring(0, line.indexOf("=")),
							line.substring(line.indexOf("=") + 1) };
					lineSplit[0] = lineSplit[0].trim();
					lineSplit[1] = lineSplit[1].trim();
					paper = new LookupTableLine(lineSplit[0], lineSplit[1]);
					ret.add(paper);
					count++;
				} catch (Exception f) {
					continue;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	// Function to read in the JSON keyword lookup table
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
		System.out.println("LookupTable Sorted in  " + (System.nanoTime() - start) / 1000000000.0 + " seconds. "
				+ (count) + "Keywords.");
	}

	// function for sorting the lookup table for the JSON lookup table file
	private static void sortLookupTableKeywordsJSON(String inFileName, String outFileName) {
		String line = null;
		int count = 0;
		long start = System.nanoTime();

		// Read in all the json lookup table objects
		ArrayList<LookupTableLine> papers = readLookupTableJSON(inFileName);

		// sort them using a basic provided collections sort
		Collections.sort(papers);
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

	// Function still not working for symbol-based look up table
	public static void removeSameIdsFromLookup(String inFileName, String outFileName) {
		String line = null, lineSplit[];
		String[] lineSplit2;
		int count = 0;
		long start = System.nanoTime();
		LookupTableLine newKeywordLine;

		ArrayList<LookupTableLine> keywordLookup = readLookupTable(inFileName);

		try {

			FileWriter fileWriter = new FileWriter(outFileName);

			for (LookupTableLine p : keywordLookup) {
				StringBuilder sb = new StringBuilder();
				String paperLine;
				Set<String> ids = new HashSet<String>();
				newKeywordLine = new LookupTableLine(p.getKeyword());
				lineSplit = p.getRightHalf().split(",");
				for (int i = 0; i < lineSplit.length; i++) {
					lineSplit2 = lineSplit[i].split("::");
					System.out.println(lineSplit2.length);
					if (!ids.contains(lineSplit2[0].trim())) {
						newKeywordLine.addPaperData(new String[] { lineSplit2[0].trim(), lineSplit2[1].trim(),
								lineSplit2[2].trim(), lineSplit2[3].trim() });
						ids.add(lineSplit2[0].trim());
					}
				}

				ArrayList<PaperShort> newKeywordPapers = newKeywordLine.getData();
				sb.append(newKeywordLine.getKeyword() + " = [");

				for (int i = 0; i < newKeywordPapers.size(); i++) {
					sb.append(newKeywordPapers.get(i).getField(Fields.ID) + "::"
							+ newKeywordPapers.get(i).getField(Fields.AUTHOR) + "::"
							+ newKeywordPapers.get(i).getField(Fields.TITLE) + "::"
							+ newKeywordPapers.get(i).getField(Fields.PUBLISH_DATE) + ",");
				}
				paperLine = sb.toString().substring(0, sb.length() - 1);
				paperLine = paperLine + "]";

				fileWriter.write(paperLine);
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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
				
				//Get id
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
					citations = (long) obj.get("n_citation");

				} catch (NullPointerException e) {
					citations = 0;
				}
				newObj.put("n_citation", citations);

				// get references
				references = (JSONArray) obj.get("references");
				if(references == null || references.isEmpty()) {
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

	public static void main(String[] args) {
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
		
		String FinalPaperList = "data/ap_paperList.txt";

/*		keywordFinder(ap_0, trans0, KwOutFile0);
		keywordFinder(ap_1, trans1, KwOutFile1);
		keywordFinder(ap_2, trans2, KwOutFile2);

		removeUnusedReferences(KwOutFile0, FormattedOutputFile0);
		removeUnusedReferences(KwOutFile1, FormattedOutputFile1);
		removeUnusedReferences(KwOutFile2, FormattedOutputFile2);

		mergeDataSet(FinalPaperFile, FormattedOutputFile0);
		mergeDataSet(FinalPaperFile, FormattedOutputFile1);
		mergeDataSet(FinalPaperFile, FormattedOutputFile2);

		keywordLookupJSON(FinalPaperFile, transKeywordFile);

		sortLookupTableKeywordsJSON(transKeywordFile, FinalKeywordFile);*/
		
		parseRelevantData(FinalPaperFile, FinalPaperList);
	}
}
