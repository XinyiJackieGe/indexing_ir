package com.model;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Interface of indexing.
 */
public interface Indexing {
	/**
	 * Insert term, doc id and position in the document into inverted index.
	 * @param token term
	 * @param docId term in
	 * @param position term's position in the document
	 * @param invertedIndex to add values in
	 */
	public void insertInvertedIndex(String token, int docId, int position,
			Map<String, TreeMap<Integer, TreeSet<Integer>>> invertedIndex);
}
