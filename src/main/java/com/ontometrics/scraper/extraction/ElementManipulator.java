package com.ontometrics.scraper.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class ElementManipulator extends Manipulator {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ElementManipulator.class);

	private TagOccurrence tagOccurrence;

	public ElementManipulator(TagOccurrence tagOccurrence) {
		this.tagOccurrence = tagOccurrence;
		this.type = OperationType.Manipulator;
	}

	@Override
	public String performExtraction() {
		return extractTag();
	}

	private String extractTag() {
		String extraction = null;
		if (tagOccurrence.getMatching() != null) {
			extraction = ScraperUtil.extractTagMatching(getSource().toString(), tagOccurrence);
		} else {
			if (tagOccurrence.getElementIdentifierType() != null) {
				extraction = ScraperUtil.extractUsingIdentifier(getSource().toString(), tagOccurrence);
			} else {
				extraction = ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(),
						tagOccurrence.getOccurrence());
			}
		}
		return extraction;
	}

	@Override
	public void setMatcher(String matcher) {
		this.tagOccurrence.setMatcher(matcher);
	}

}
