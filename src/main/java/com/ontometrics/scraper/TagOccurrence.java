package com.ontometrics.scraper;

import com.ontometrics.scraper.extraction.ElementIdentifierType;

public class TagOccurrence {

	private String tag;
	
	private ElementIdentifierType elementIdentifierType;
	
	private String identifier;

	private int occurrence;

	private String matching;
	
	public TagOccurrence(String element, ElementIdentifierType identifierType, String identifier){
		this.elementIdentifierType = identifierType;
		this.identifier = identifier;
		this.tag = element;
	}

	public TagOccurrence(String tag, int occurrence) {
		super();
		this.tag = tag;
		this.occurrence = occurrence;
	}

	public TagOccurrence(String tag, int occurrence, String matching) {
		super();
		this.tag = tag;
		this.occurrence = occurrence;
		this.matching = matching;
	}

	public int getOccurrence() {
		return occurrence;
	}

	public String getTag() {
		return tag;
	}

	public String getMatching() {
		return matching;
	}
	
	public ElementIdentifierType getElementIdentifierType() {
		return elementIdentifierType;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setMatcher(String matcher) {
		this.matching = matcher;
	}

	@Override
	public String toString() {
		return "TagOccurrence [tag=" + tag + ", occurrence=" + occurrence + ", matching=" + matching + "]";
	}

}
