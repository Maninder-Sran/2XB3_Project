package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a paper object
 */
public class PaperShort implements Serializable {

	//list of String fields containing information about the paper
	private ArrayList<String> fields;

	/**
	 * {@link PaperShort} constructor
	 */
	public PaperShort() {
		this.fields = new ArrayList<String>();
	}

	/**
	 * {@link PaperShort} constructor
	 * @param fields - ArrayList<String> of fields to initialize this object with
	 */
	public PaperShort(ArrayList<String> fields) {
		this.fields = (ArrayList<String>) fields.clone();
	}

	/**
	 * {@link PaperShort} constructor
	 * @param fields - String array of fields to initialize this object with
	 */
	public PaperShort(String[] fields) {
		this.fields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			addField(fields[i]);
		}
	}

	/**
	 * Adds a field to the list
	 * @param a - String to add to the list of fields
	 */
	public void addField(String a) {
		fields.add(a);
	}

	/**
	 * Returns a specified field
	 * @param field - {@link PaperFields} enumerated type to specify which field
	 * @return the specified field
	 */
	public String getField(PaperFields field) {
		return fields.get(field.ordinal());
	}
}
