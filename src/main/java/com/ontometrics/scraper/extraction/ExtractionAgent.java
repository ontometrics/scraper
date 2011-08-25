package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public abstract class ExtractionAgent implements ExtractionCommand {

	private Source source;

	private ExtractionAgent successor;

	@Override
	public final void execute(Source source) {
		this.source = source;
		performExtraction();
		if (successor != null) {
			successor.execute(source);
		}
	}

	public abstract void performExtraction();
	
	public Source getSource() {
		return source;
	}

	public ExtractionAgent getSuccessor() {
		return successor;
	}

	public void setSuccessor(ExtractionAgent successor) {
		this.successor = successor;
	}

}
