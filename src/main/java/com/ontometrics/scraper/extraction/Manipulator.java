package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public abstract class Manipulator implements Manipulation {
	
	protected OperationType type = OperationType.Unbound;

	private Source source;

	private Manipulator successor;

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

	public Manipulator getSuccessor() {
		return successor;
	}

	public void setSuccessor(Manipulator successor) {
		this.successor = successor;
	}

}
