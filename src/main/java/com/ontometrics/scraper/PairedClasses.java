package com.ontometrics.scraper;


/**
 * Simple way of telling the extractor how to find paired fields based on the
 * Classes that wrap the label and field.
 * 
 * @author Mina shafik
 * 
 */
public class PairedClasses {

	/**
	 * The Class that is wrapped around each label
	 */
	private String labelClass;

	/**
	 * The Class that is wrapped around each field
	 */
	private String fieldClass;

	public PairedClasses(String h4, String dd) {
		super();
		this.labelClass = h4;
		this.fieldClass = dd;
	}

	public String getLabelClass() {
		return labelClass;
	}

	public String getFieldClass() {
		return fieldClass;
	}

}
