package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TagOccurrence;

public class SplicingExtractor extends ExtractionAgent {

	private SpliceOperation operation = SpliceOperation.After;

	public SplicingExtractor(SpliceOperation operation, TagOccurrence tagOccurrence) {
		this.operation = operation;
	}

	public SpliceOperation getOperation() {
		return operation;
	}

	@Override
	public void performExtraction() {

	}

}
