package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public abstract class ExtractionAgent implements ExtractionCommand {

	private ExtractionAgent successor;

	@Override
	public abstract void execute(Source source);

	public ExtractionAgent getSuccessor() {
		return successor;
	}

}
