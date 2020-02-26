package com.model;

import java.util.Map;

public abstract class AbstractTokenize {
	public Boolean isStopWord(String term, Map<String, Integer> stopWords) {
	    if (stopWords.containsKey(term)) {
	      return true;
	    }
	    return false;
	  }
}
