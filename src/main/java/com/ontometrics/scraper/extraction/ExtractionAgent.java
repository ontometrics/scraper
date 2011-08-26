package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public abstract class ExtractionAgent implements ExtractionCommand {
	
	protected OperationType type = OperationType.Unbound;

	private Source source;

	private ExtractionAgent successor;

	@Override
	public final void execute(Source source) {
		this.source = source;
		String result = performExtraction();
		if (type==OperationType.Manipulator){
			source = new Source(result);
			source.fullSequentialParse();
		}
		if (successor != null) {
			successor.execute(source);
		}
	}

	public abstract String performExtraction();
	
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
