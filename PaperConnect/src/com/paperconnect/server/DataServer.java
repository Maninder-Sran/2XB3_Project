package com.paperconnect.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.PaperShort;

public class DataServer {
	
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
