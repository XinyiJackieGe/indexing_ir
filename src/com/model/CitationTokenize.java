package com.model;

public class CitationTokenize extends AbstractTokenize{
	public String normalize(String term) {
	    term = term.replaceAll("[^a-zA-Z]", "");
	    return term;
	  }
}
