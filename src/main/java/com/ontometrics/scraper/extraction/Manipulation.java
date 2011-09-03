package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

/**
 * Manipulators are added to a queue, thus we need an implementation of the
 * Command Pattern. This one is with a twist: the work does not happen in
 * execute because we want the base class to handle the Chain of Responsibility
 * requirements, so it invokes an abstract method then processes the results.
 * 
 * @see Manipulator
 * @author Rob
 */
public interface Manipulation {

	/**
	 * The source that is passed to each Manipulator in the chain represents the
	 * state of the source as of the most recent transformation.
	 * 
	 * @param source
	 *            the html the implementer is going to perform its manipulations
	 *            on
	 */
	public void execute(Source source);

}
