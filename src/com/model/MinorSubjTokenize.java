package com.model;

import java.util.ArrayList;
import java.util.List;

public class MinorSubjTokenize extends AbstractTokenize {
	public List<String> normalize(String term) {
	    List<String> subjectTokens = new ArrayList<String>();
	    String[] subjects = term.replaceAll("[.]", " ").replaceAll("[,]", "").toLowerCase().split("\\s+");
	    for (String subject : subjects) {
	      String[]subjectArray = subject.replaceAll("[:-]", " ").split("\\s+");
	      for (String subj : subjectArray) {
	        subjectTokens.add(subj);
	      }
	    }
	    return subjectTokens;
	  }
}
