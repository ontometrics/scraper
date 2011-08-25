package com.ontometrics.scraper.extraction;

public class FieldToGet {
	private String fieldname;

	private String tag;

	public FieldToGet(String name, String tag) {
		this.fieldname = name;
		this.tag = tag;
	}

	public String getFieldname() {
		return fieldname;
	}

	public String getTag() {
		return tag;
	}

}
