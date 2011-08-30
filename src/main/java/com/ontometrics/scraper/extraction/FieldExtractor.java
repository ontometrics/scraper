package com.ontometrics.scraper.extraction;

import java.util.Map;

public interface FieldExtractor {

	public abstract Map<String, String> getFields();

}