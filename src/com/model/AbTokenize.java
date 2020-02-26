package com.model;

public class AbTokenize extends AbstractTokenize{

	public String normalize(String term) {
		term = term.replaceAll("[-. ]", "");
		return term;
	}

}
