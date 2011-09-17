package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.List;

import com.ontometrics.scraper.PairedTags;

public interface FieldExtractor<T extends FieldExtractor<T>> {

	public abstract List<Field> getFields();
	
	public abstract T source(HtmlExtractor htmlExtractor);

	public abstract T section(HtmlExtractor htmlExtractor);
	
	public abstract T field(String label, String element);
	
	public abstract T add(PairedTags pairedTags);
	
	public abstract T url(URL url);

}