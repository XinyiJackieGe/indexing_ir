package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceTokenize extends AbstractTokenize {
	public List<String> normalize(String term) {
	    List<String> sourceTokens = new ArrayList<String>();
	    List<String> words = Arrays.asList(term.replaceAll("[ ]", ";").replaceAll("[.]", " ").toLowerCase().split("\\s+")).subList(0, 2);
	    for (String word : words) {
	      String[]sourceArray = word.replaceAll("[-;]", " ").replaceAll("[\\[\\]]", "").trim().split("\\s+");
	      for (String source : sourceArray) {
	        sourceTokens.add(source);
	      }   
	    }
	    return sourceTokens;
	  }
}
