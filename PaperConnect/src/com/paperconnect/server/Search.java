package com.paperconnect.server;

import java.util.ArrayList;

import com.paperconnect.client.Paper;

public class Search {

	public static Paper binarySearchID(ArrayList<Paper> list, String id) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getPaperID();
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
		for(Paper p: list) {
			if(p.getPaperID().equals(id)) {
				return p;
			}
		}
		return null;
	}
	
}
