package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public class TextExtractor extends BaseExtractor {

	@Override
	public TextExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

    @Override
    public TextExtractor source(Source startingSource) {
        super.source(startingSource);
        return this;
    }

    public String getText() {
		return getSource().getTextExtractor().toString();
	}

}
