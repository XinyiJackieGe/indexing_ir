package com.model;

public class ReferenceTokenize extends AbstractTokenize {
	public String normalize(String term) {
	    term = term.replaceAll("[^a-zA-Z]", "");
	    return term;
	  }
}
