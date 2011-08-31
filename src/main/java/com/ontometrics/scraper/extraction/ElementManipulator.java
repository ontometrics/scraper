package com.ontometrics.scraper.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class ElementManipulator extends Manipulator {

	private static final Logger log = LoggerFactory.getLogger(ElementManipulator.class);

	private TagOccurrence tagOccurrence;

	public ElementManipulator(TagOccurrence tagOccurrence) {
		this.tagOccurrence = tagOccurrence;
	}

	@Override
	public String performExtraction() {
		return extractTag();
	}

	private String extractTag() {
		String extraction = null;
		if (tagOccurrence.getMatching() != null) {
			extraction = ScraperUtil.extractTagMatching(getSource().toString(), tagOccurrence);
			log.debug("result of matching: {}", extraction);
		} else {
			extraction = ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(),
					tagOccurrence.getOccurrence());
		}
		return extraction;
	}

	@Override
	public void setMatcher(String matcher) {
		this.tagOccurrence.setMatcher(matcher);
	}

}
