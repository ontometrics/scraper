package com.ontometrics.scraper.extraction;

public interface Field {

	public String getLabel();
	
	public String getValue();

	public void addValue(String value);
	
}
