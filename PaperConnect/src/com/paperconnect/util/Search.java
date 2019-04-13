package com.paperconnect.util;

import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

/**
 * Allows for data to be searched through
 */
public class Search {

	/**
	 * Searches through a list of papers for an ID using binary search
	 * @param list ArrayList<Paper> representing the list of papers to be searched through
	 * @param id String representing the paper ID to look for
	 * @return Paper object representing the Paper within list with a matching ID, or null if not found
	 */
	public static Paper binarySearchID(ArrayList<Paper> list, String id) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getField(PaperFields.ID);
			if (id.compareTo(midString) == 0) {
				return list.get(mid);
			} else if (id.compareTo(midString) > 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return null;
	}

	/**
	 * Searches through a list of papers for an ID using sequential search
	 * @param list ArrayList<Paper> representing the list of papers to be searched through
	 * @param id String representing the paper ID to look for
	 * @return Paper object representing the Paper within list with a matching ID, or null if not found
	 */
	public static Paper sequentialSearchID(ArrayList<Paper> list, String id) {
		for (Paper p : list) {
			if (p.getField(PaperFields.ID).equals(id)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Searches through a lookup table for a keyword using binary search
	 * @param list ArrayList<LookupTableLine> representing the lookup table to be searched through
	 * @param keyword String representing the key
	 * @return LookupTableLine object representing the corresponding value to the keyword, or null if not found
	 */
	public static LookupTableLine binarySearchKeyword(ArrayList<LookupTableLine> list, String keyword) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getKeyword();
			if (keyword.compareTo(midString) == 0) {
				return list.get(mid);
			} else if (keyword.compareTo(midString) > 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return null;
	}
}
