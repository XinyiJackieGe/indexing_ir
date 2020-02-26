package com.model;

public class TitleTokenize extends AbstractTokenize {
	public String normalize(String term) {
	    term = term.replaceAll("[0-9-.]", "");
	    return term;
	  }
}
