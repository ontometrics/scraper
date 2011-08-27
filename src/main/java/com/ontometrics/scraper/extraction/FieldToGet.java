package com.ontometrics.scraper.extraction;

public class FieldToGet {

	private FieldSearchType searchType = FieldSearchType.Tag;

	private String fieldname;

	private String label;

	public FieldToGet(String name, String label) {
		this.fieldname = name;
		this.label = label;
	}
	
	public FieldSearchType getSearchType() {
		return searchType;
	}

	public String getFieldname() {
		return fieldname;
	}

	public String getLabel() {
		return label;
	}

}
