package com.ontometrics.scraper;

public class TagOccurrence {
	
	private String tag;
	
	private int occurrence;
	
	public TagOccurrence(String tag, int occurrence) {
		super();
		this.tag = tag;
		this.occurrence = occurrence;
	}

	public int getOccurrence() {
		return occurrence;
	}
	
	public String getTag() {
		return tag;
	}

}
