package com.ontometrics.scraper.extraction;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class CleanManipulator extends Manipulator {

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
