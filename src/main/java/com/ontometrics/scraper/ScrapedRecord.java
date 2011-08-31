package com.ontometrics.scraper;

import java.util.List;

import com.ontometrics.scraper.extraction.Field;
import com.ontometrics.scraper.util.ScraperUtil;

public class ScrapedRecord implements Record {

	private List<Field> fields;

	public ScrapedRecord(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public List<Field> getFields() {
		return this.fields;
	}

	@Override
	public String getFieldValue(String titleFieldKey) {
		return ScraperUtil.getFieldValue(fields, titleFieldKey);
	}

	@Override
	public String toString() {
		return "ScrapedRecord [fields=" + fields + "]";
	}
}
