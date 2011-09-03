package com.ontometrics.scraper.extraction;

import java.util.List;

public interface FieldExtractor<T extends FieldExtractor<T>> {

	public abstract List<Field> getFields();
	
	public abstract T source(HtmlExtractor htmlExtractor);

}