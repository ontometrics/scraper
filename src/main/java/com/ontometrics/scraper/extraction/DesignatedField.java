package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TagOccurrence;

/**
 * Provides a means of stipulating fields we expect to find by assigning a label
 * then finding the corresponding element.
 * 
 * @author Rob
 */
public class DesignatedField {

	private String label;

	private TagOccurrence tagToGetValueFrom;

	public DesignatedField(String label, TagOccurrence tagToGetValueFrom) {
		super();
		this.label = label;
		this.tagToGetValueFrom = tagToGetValueFrom;
	}

	public String getLabel() {
		return label;
	}

	public TagOccurrence getTagToGetValueFrom() {
		return tagToGetValueFrom;
	}

}
