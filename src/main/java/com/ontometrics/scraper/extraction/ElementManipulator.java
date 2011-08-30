package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class ElementManipulator extends Manipulator {
	
	private TagOccurrence tagOccurrence;
	
	public ElementManipulator(TagOccurrence tagOccurrence) {
		this.tagOccurrence = tagOccurrence;
	}

	@Override
	public String performExtraction() {
		return ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(), tagOccurrence.getOccurrence());
	}

}
