package com.paperconnect.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.PaperShort;
import com.paperconnect.util.Search;

public class DataServer {

	public static class LookupTable {
		static ArrayList<LookupTableLine> lookupTable;

		public static void init() {
			readLookupTable("data/ap_0_lookup_sorted.txt");
			/*for (LookupTableLine l : lookupTable) {
				System.out.println(l);
			}*/
		}

		private static void readLookupTable(String fileName) {
			String line = null;
			lookupTable = new ArrayList<LookupTableLine>();
			long start = System.nanoTime();
			int count = 0;
			// used for splitting by "="
			String[] lineSplit = null;
			// used for splitting by ","
			String[] lineSplit2 = null;
			LookupTableLine tableLine = null;
			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				while (true) {
					try {
						line = bufferedReader.readLine();
						if (line == null) {
							break;
						}
						lineSplit = line.split("=");
						lineSplit[0] = lineSplit[0].trim();
						lineSplit[1] = lineSplit[1].trim();
						// set the keyword
						tableLine = new LookupTableLine(lineSplit[0]);
						// remove []
						lineSplit[1] = lineSplit[1].substring(1, lineSplit[1].length() - 1);
						lineSplit = lineSplit[1].split(",");
						for (int i = 0; i < lineSplit.length; i++) {
							lineSplit2 = lineSplit[i].split("::");
							tableLine.addPaperData(new String[] { lineSplit2[0].trim(), lineSplit2[1].trim() });
						}
						lookupTable.add(tableLine);
						count++;
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
		}

		public static boolean isKeywordValid(String keyword) {
			return retrievePapers(keyword) != null;
		}

		public static ArrayList<PaperShort> retrievePapers(String keyword) {
			LookupTableLine result = Search.binarySearchKeyword(lookupTable, keyword);
			if (result == null) {
				return null;
			}
			return result.getData();
		}

	}

	public static void main(String[] args) {
		LookupTable.init();
		// znt2 = 53e997e4b7602d9701fdb48c ZnT2-overexpression represses the cytotoxic
		//étiologie =  53e99803b7602d970201568f  Fibrose médiastinale idiopathique  53e9982cb7602d970204ee8c  Prostaglandines et aspirine 
		//System.out.println(LookupTable.isKeywordValid("sdklfjurhieoghuihorgiurehgwro"));
		//System.out.println(LookupTable.retrievePapers("étiologie"));
	}
}
