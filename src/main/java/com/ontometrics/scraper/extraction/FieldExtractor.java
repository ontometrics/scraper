package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.List;

public interface FieldExtractor<T extends FieldExtractor<T>> {

	public abstract List<Field> getFields();
	
	public abstract T source(HtmlExtractor htmlExtractor);
	
	public abstract T url(URL url);

}