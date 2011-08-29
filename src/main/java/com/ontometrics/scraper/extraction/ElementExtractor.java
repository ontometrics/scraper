package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class ElementExtractor extends ExtractionAgent {
	
	private TagOccurrence tagOccurrence;
	
	public ElementExtractor(TagOccurrence tagOccurrence) {
		this.tagOccurrence = tagOccurrence;
	}

	@Override
	public String performExtraction() {
		return ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(), tagOccurrence.getOccurrence());
	}

}
