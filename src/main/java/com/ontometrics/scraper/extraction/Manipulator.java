package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a means of transforming html based on a set of conditions, then it
 * passes on its transformed code to the next manipulator in line (
 * {@link #successor}). This is how the Chain of Responsibility Pattern works.
 * 
 * @author Rob Note: takes its name from the stream operations in C++.
 */
public abstract class Manipulator implements Manipulation {

	private static final Logger log = LoggerFactory.getLogger(Manipulator.class);

	/**
	 * Refers to whether this thing operates on the original source, or
	 * transforms what it was passed.
	 */
	private OperationType type = OperationType.Manipulator;

	/**
	 * The html we are transforming here.
	 */
	private Source source;

	/**
	 * Provides means of letting subclasses decide how to match, so for
	 * instance, a table would look for the text provided here somewhere inside,
	 * and if it found it, it would extract that table element and pass it on.
	 */
	private String matcher;

	/**
	 * The next in line to operate on the text.
	 * 
	 * @see note above about Chain of Responsibility Pattern
	 */
	private Manipulator successor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ontometrics.scraper.extraction.Manipulation#execute(net.htmlparser
	 * .jericho.Source)
	 */
	@Override
	public final void execute(Source source) {
		this.source = source;
		String result = performExtraction();
		if (result == null) {
			throw new IllegalStateException("Manipulator " + this.getClass().getName() + " returned null.");
		}
		if (type == OperationType.Manipulator) {
			log.debug("reassigning source..");
			Source newSource = new Source(result);
			newSource.fullSequentialParse();
			this.source = newSource;
		}
		if (successor != null) {
			successor.execute(this.source);
		}
	}

	/**
	 * This is where the work is done in the subclasses: they can take the
	 * source, do whatever they are going to do to transform it then return it.
	 * All requirements regarding passing it on and reparsing it are handled
	 * transparently here in the base class.
	 * 
	 * @return the resulting html transformed by the specific manipulation
	 */
	public abstract String performExtraction();

	public OperationType getType() {
		return type;
	}

	protected void setType(OperationType type) {
		this.type = type;
	}

	public Source getSource() {
		return source;
	}

	public Manipulator getSuccessor() {
		return successor;
	}

	/**
	 * Have to have a setter as the chain of manipulators is constructed by the
	 * Extractor.
	 * 
	 * @param successor
	 *            next Manipulator in line who will receive our transformed text
	 */
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
