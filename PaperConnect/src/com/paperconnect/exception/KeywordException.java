package com.paperconnect.exception;

import java.io.Serializable;

public class KeywordException extends Exception implements Serializable {
	
	private String keyword;
	
	public KeywordException() {}
	
	public KeywordException(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKeyword() {
		return this.keyword;
	}
}
