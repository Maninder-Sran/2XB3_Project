package com.paperconnect.util;

import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;

public class Search {

	public static Paper binarySearchID(ArrayList<Paper> list, String id) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getId();
			if (id.compareTo(midString) == 0) {
				return list.get(mid);
			} else if (id.compareTo(midString) > 0) {
				min = mid;
			} else {
				max = mid;
			}
			if (max == min && !midString.equals(id)) {
				break;
			}
		}
		return null;
	}

	public static Paper sequentialSearchID(ArrayList<Paper> list, String id) {
		for (Paper p : list) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

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
				min = mid;
			} else {
				max = mid;
			}
			if (max == min && !midString.equals(keyword)) {
				break;
			}
		}
		return null;
	}
}
