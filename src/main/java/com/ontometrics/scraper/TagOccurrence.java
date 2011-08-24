package com.ontometrics.scraper;

public class TagOccurrence {

	private String tag;

	private int occurrence;

	private String matching;

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

	@Override
	public String toString() {
		return "TagOccurrence [tag=" + tag + ", occurrence=" + occurrence + ", matching=" + matching + "]";
	}
}
