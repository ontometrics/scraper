package com.ontometrics.scraper;


/**
 * Simple way of telling the extractor how to find paired fields based on the
 * tags that wrap the label and field.
 * 
 * @author Rob
 * 
 */
public class PairedTags {

	/**
	 * The tag that is wrapped around each label
	 */
	private String labelTag;

	/**
	 * The tag that is wrapped around each field
	 */
	private String fieldTag;

	public PairedTags(String h4, String dd) {
		super();
		this.labelTag = h4;
		this.fieldTag = dd;
	}

	public String getLabelTag() {
		return labelTag;
	}

	public String getFieldTag() {
		return fieldTag;
	}

}
