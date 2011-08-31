package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Manipulator implements Manipulation {
	
	private static final Logger log = LoggerFactory.getLogger(Manipulator.class);

	protected OperationType type = OperationType.Manipulator;

	private Source source;

	private String matcher;

	private Manipulator successor;

	@Override
	public final void execute(Source source) {
		this.source = source;
		String result = performExtraction();
		log.debug("result of extraction: {}", result);
		if (type == OperationType.Manipulator) {
			log.debug("reassigning source..");
			source = new Source(result);
			source.fullSequentialParse();
			this.source = source;
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

	public String getMatcher() {
		return matcher;
	}

	public boolean hasMatcher() {
		return this.matcher != null;
	}

	public abstract void setMatcher(String matcher);
}
