package com.model;

import java.util.ArrayList;
import java.util.List;

public class AuthorTokenize extends AbstractTokenize {
	public List<String> normalize(String term) {
	    List<String> authorTokens = new ArrayList<String>();
	    String[] authors = term.replaceAll("[.]", " ").toLowerCase().split("\\s+");
	    for (String author : authors) {
	      author = author.replaceAll("[-]", " ");
	      authorTokens.add(author);
	    }
	    
	    return authorTokens;
	  }
}
