package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class SplicingExtractor extends Manipulator {

	private static final Logger log = LoggerFactory.getLogger(SplicingExtractor.class);

	private SpliceOperation operation = SpliceOperation.After;

	private TagOccurrence tagOccurrence;

	public SplicingExtractor(SpliceOperation operation, TagOccurrence tagOccurrence) {
		setType(OperationType.Manipulator);
		this.operation = operation;
		this.tagOccurrence = tagOccurrence;
	}

	public SpliceOperation getOperation() {
		return operation;
	}

	@Override
	public String performExtraction() {
		String extractedSource = "";
		if (tagOccurrence.getIdentifier() != null) {
			log.debug("about to splice: {}", tagOccurrence);
			Element element = ScraperUtil.extract(getSource(), tagOccurrence);
			if (element != null) {
				extractedSource = element.toString();
			}
			log.debug("spliced out: {}", extractedSource);
		} else {
			extractedSource = ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(),
					tagOccurrence.getOccurrence());
		}
		return extractedSource;
	}

	@Override
	public void setMatcher(String matcher) {
		this.tagOccurrence.setMatcher(matcher);
	}

}
