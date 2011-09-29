package com.ontometrics.scraper.extraction;

public class TextExtractor extends BaseExtractor {

	@Override
	public TextExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	public String getText() {
		return getSource().getTextExtractor().toString();
	}

}
