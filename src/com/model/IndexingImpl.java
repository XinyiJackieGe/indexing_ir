package com.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.model.Indexing;

/**
 * Implementation of Indexing interface.
 */
public class IndexingImpl implements Indexing {
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> authorInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> titleInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> sourceInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> majorSubjectInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> minorSubjectInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> abstractInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> referenceInvertedIndex;
	private Map<String, TreeMap<Integer, TreeSet<Integer>>> citationInvertedIndex;

	public IndexingImpl() {
		authorInvertedIndex = new HashMap<>();
		titleInvertedIndex = new HashMap<>();
		sourceInvertedIndex = new HashMap<>();
		majorSubjectInvertedIndex = new HashMap<>();
		minorSubjectInvertedIndex = new HashMap<>();
		abstractInvertedIndex = new HashMap<>();
		referenceInvertedIndex = new HashMap<>();
		citationInvertedIndex = new HashMap<>();
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getAuthorInvertedIndex() {
		return authorInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getTitleInvertedIndex() {
		return titleInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getSourceInvertedIndex() {
		return sourceInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getMajorSubInvertedIndex() {
		return majorSubjectInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getMinorSubInvertedIndex() {
		return minorSubjectInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getAbstractInvertedIndex() {
		return abstractInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getReferenceInvertedIndex() {
		return referenceInvertedIndex;
	}

	public Map<String, TreeMap<Integer, TreeSet<Integer>>> getCitationInvertedIndex() {
		return citationInvertedIndex;
	}

	@Override
	public void insertInvertedIndex(String token, int docId, int position,
			Map<String, TreeMap<Integer, TreeSet<Integer>>> invertedIndex) {
		if (!invertedIndex.containsKey(token)) {
			TreeMap<Integer, TreeSet<Integer>> docPostionMap = new TreeMap<Integer, TreeSet<Integer>>();
			docPostionMap.put(docId, new TreeSet<Integer>());
			invertedIndex.put(token, docPostionMap);
		} else {
			if (!invertedIndex.get(token).containsKey(docId)) {
				invertedIndex.get(token).put(docId, new TreeSet<Integer>());
			}
		}
		invertedIndex.get(token).get(docId).add(position);
	}

}
