package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import com.ontometrics.scraper.TagOccurrence;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

/**
 * Provides a means of collecting {@link Manipulator}s and performing
 * progressive harvesting of html from an original source. This is done through
 * an implementation of the Chain of Responsibility Pattern: the manipulators
 * are held in a LinkedList and when new ones are added, they are bolted on to
 * the end, then, when the source is requested, the first {@link Manipulator} is
 * invoked, setting off the chain of operations. Then the resulting source is
 * extracted.
 * 
 * @author Rob
 * 
 */
public class HtmlExtractor extends BaseExtractor {

	/**
	 * This holds the source that we are manipulating.
	 */
	private Source source;

	/**
	 * Typical starting point for beginning the process of getting html to
	 * manipulate.
	 */
	private URL url;

	/**
	 * The chain of collaborators who will do the work of transforming the html
	 * source.
	 */
	private LinkedList<Manipulator> manipulators;

	/**
	 * The idea here is that the various static methods that are used to present
	 * the syntax of the DSL will ultimately enqueue a corresponding command by
	 * calling this method, so for instance, the method:
	 * <p>
	 * <code>
	 * public HtmlExtractor table(int occurrence)
	 * <code>
	 * <p>
	 * will be turned into a request to take the html that was passed in, parse it,
	 * get the nth occurrence of a table tag, then pass it on to the next Manipulator
	 * in the chain.
	 * 
	 * @param manipulator
	 *            the command to be enqueued at this point in the progressive
	 *            operation of extracting the html source
	 */
	public void addManipulator(Manipulator manipulator) {
		if (manipulators == null) {
			manipulators = new LinkedList<Manipulator>();
		} else {
			manipulators.getLast().setSuccessor(manipulator);
		}
		manipulators.add(manipulator);
	}

	/**
	 * Call this when it's time to actually perform the operations on the
	 * source.
	 */
	public void performManipulations() {
		try {
			source = new Source(url);
			manipulators.getFirst().execute(source);
			source = manipulators.getLast().getSource();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ontometrics.scraper.extraction.BaseExtractor#getSource()
	 */
	public Source getSource() {
		if (source == null) {
			performManipulations();
		}
		return source;
	}

	/**
	 * Provides means of extracting a specific table.
	 * 
	 * @param occurrence
	 *            this would refer to the index in the list of all table tags
	 *            found in the passed html
	 * @return the table tag and all its contents
	 */
	public HtmlExtractor table(int occurrence) {
		addManipulator(new ElementManipulator(new TagOccurrence(HTMLElementName.TABLE, occurrence)));
		return this;
	}

	/**
	 * Usually the starting point: provides the path to a file that would be the
	 * original source that is then progressively transformed by any additional
	 * {@link Manipulator}s.
	 * 
	 * @param url
	 *            valid url point to a page that has html in it
	 * @return this, for chaining
	 */
	public HtmlExtractor url(URL url) {
		this.url = url;
		return this;
	}

}
