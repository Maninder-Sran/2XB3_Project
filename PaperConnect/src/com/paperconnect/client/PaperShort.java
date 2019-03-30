package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

public class PaperShort implements Serializable {

	private ArrayList<String> fields;

	public enum Fields {
		ID, TITLE, AUTHOR, PUBLISH_DATE;
	}

	public PaperShort() {
		this.fields = new ArrayList<String>();
	}

	public PaperShort(ArrayList<String> fields) {
		this.fields = (ArrayList<String>) fields.clone();
	}

	public PaperShort(String[] fields) {
		this.fields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			addField(fields[i]);
		}
	}

	public void addField(String a) {
		fields.add(a);
	}

	public String getField(Fields field) {
		return fields.get(field.ordinal());
	}

	public String toString() {
		StringBuilder x = new StringBuilder();
		for (String s : fields) {
			x.append(" " + s + " ");
		}
		return x.toString();
	}
}
