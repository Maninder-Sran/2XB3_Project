package com.paperconnect.client;

import java.io.Serializable;

/**
 * 
 * Enum class representation of the fields available for a {@code Paper} or the truncated {@code PaperShort} object 
 *
 */
public enum PaperFields implements Serializable {
	ID, TITLE, AUTHOR, PUBLISH_DATE, ABSTRACT, TREE;
}
