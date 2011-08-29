package com.ontometrics.scraper;

import java.util.List;

import com.ontometrics.scraper.extraction.Field;

public interface Record {

	public List<Field> getFields();

	public String getFieldValue(String titleFieldKey);
	
}
