package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public class MockExtractionAgent extends ExtractionAgent {

	public Source getResult() {
		return getSource();
	}

	@Override
	public void performExtraction() {
	}

}
