package com.ontometrics.scraper.extraction;

public class DesignatedField {

	private String label;

	private String tagToGetValueFrom;

	public DesignatedField(String label, String tagToGetValueFrom) {
		super();
		this.label = label;
		this.tagToGetValueFrom = tagToGetValueFrom;
	}

	public String getLabel() {
		return label;
	}

	public String getTagToGetValueFrom() {
		return tagToGetValueFrom;
	}

}
