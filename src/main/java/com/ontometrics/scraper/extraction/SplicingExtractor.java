package com.ontometrics.scraper.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class SplicingExtractor extends ExtractionAgent {
	
	private static final Logger log = LoggerFactory.getLogger(SplicingExtractor.class);

	private SpliceOperation operation = SpliceOperation.After;

	private TagOccurrence tagOccurrence;

	public SplicingExtractor(SpliceOperation operation, TagOccurrence tagOccurrence) {
		this.type = OperationType.Manipulator;
		this.operation = operation;
		this.tagOccurrence = tagOccurrence;
	}

	public SpliceOperation getOperation() {
		return operation;
	}

	@Override
	public String performExtraction() {
		return ScraperUtil.extract(getSource().toString(), tagOccurrence.getTag(), tagOccurrence.getOccurrence());
	}
}
