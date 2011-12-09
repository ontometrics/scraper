package com.ontometrics.scraper.extraction;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanManipulator extends Manipulator {
	private static final Logger log = LoggerFactory.getLogger(CleanManipulator.class);

	@Override
	public String performExtraction() {
		// cleaning
		String sourceHtml = getSource().toString();
		sourceHtml = Jsoup.clean(sourceHtml, Whitelist.relaxed());
		return sourceHtml;
	}

	@Override
	public void setMatcher(String matcher) {
		throw new UnsupportedOperationException();
	}

}
