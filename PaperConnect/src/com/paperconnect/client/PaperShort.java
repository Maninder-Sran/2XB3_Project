package com.paperconnect.client;

import java.util.ArrayList;

public class PaperShort {

	private ArrayList<String> fields;

	public PaperShort() {
		this.fields = new ArrayList<String>();
	}

	public PaperShort(ArrayList<String> fields) {
		this.fields = (ArrayList<String>) fields.clone();
	}

	public PaperShort(String[] fields) {
		this.fields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			this.fields.add(fields[i]);
		}
	}

	public void addField(String a) {
		fields.add(a);
	}

	public String getField(int i) {
		return fields.get(i);
	}

	public void setField(int i, String a) {
		fields.set(i, a);
	}

	public void removeField(int i) {
		fields.remove(i);
	}

	public void removeField(String a) {
		fields.remove(a);
	}
	
	public String toString() {
		StringBuilder x = new StringBuilder();
		for(String s : fields) {
			x.append(" " + s + " ");
		}
		return x.toString();
	}
}
