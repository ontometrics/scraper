package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;

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

	private static final Logger log = LoggerFactory.getLogger(HtmlExtractor.class);

	/**
	 * This holds the source that we are manipulating.
	 */
	private Source source;

	private SourceExtractor sourceExtractor;

	/**
	 * Typical starting point for beginning the process of getting html to
	 * manipulate.
	 */
	private URL url;

	/**
	 * The chain of collaborators who will do the work of transforming the html
	 * source.
	 */
	private Deque<Manipulator> manipulators = new LinkedList<Manipulator>();

	private Map<String, String> httpRequestProperties = new HashMap<String, String>();

	public HtmlExtractor from(SourceExtractor sourceExtractor) {
		this.sourceExtractor = sourceExtractor;
		return this;
	}

	public static HtmlExtractor html() {
		return new HtmlExtractor();
	}

	/**
	 * The idea here is that the various static methods that are used to present
	 * the syntax of the DSL will ultimately enqueue a corresponding command by
	 * calling this method, so for instance, the method:
	 * <p>
	 * <code>
	 * public HtmlExtractor table(int occurrence)
	 * </code>
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
		if (hasManipulators()) {
			manipulators.getLast().setSuccessor(manipulator);
		}
		manipulators.add(manipulator);
	}

	public HtmlExtractor clean() {
		Manipulator cleaner = new CleanManipulator();
		if (hasManipulators()) {
			cleaner.setSuccessor(manipulators.getFirst());
		}
		manipulators.addFirst(cleaner);
		return this;
	}

	/**
	 * Call this when it's time to actually perform the operations on the
	 * source.
	 */
	public void performManipulations() {
		try {
			if (httpRequestProperties.size() > 0) {
				log.debug("Passing a cookie while getting source.");
				HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();

				Set<String> requestPropertiesKeys = httpRequestProperties.keySet();
				for (String key : requestPropertiesKeys) {
					httpUrlConnection.setRequestProperty(key, httpRequestProperties.get(key));
				}
				source = new Source(httpUrlConnection);
			} else {
				log.debug("Getting source normally.");
				source = getSourceExtractor().getSource(this.url);
			}

			if (hasManipulators()) {
				manipulators.getFirst().execute(source);
				source = manipulators.getLast().getSource();
			}
		} catch (IOException e) {
			log.error("IO Error while performing manipulations", e);
		}
	}

	private SourceExtractor getSourceExtractor() {
		if (this.sourceExtractor == null) {
			this.sourceExtractor = new SimpleSourceExtractor();
		}
		return this.sourceExtractor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ontometrics.scraper.extraction.BaseExtractor#getSource()
	 */
	public Source getSource() {
		// if (source == null) {
		performManipulations();
		// }
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
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().tag(HTMLElementName.TABLE)
				.occurrence(occurrence)
				.build()));
		return this;
	}

	/**
	 * Provides means of getting the html after a tag.
	 * 
	 * @param tag
	 *            the element to look for
	 * @param occurrence
	 *            which one
	 * @return all the html after (and including) the element
	 */
	public HtmlExtractor after(String tag, int occurrence) {
		addManipulator(new SplicingExtractor(SpliceOperation.After, new TagOccurrence.Builder().tag(tag)
				.occurrence(occurrence)
				.build()));
		return this;
	}

	/**
	 * Provides a simple means of adding matching to the prior operation. For
	 * example, if you want to find a table that contains a given string, you
	 * would do:
	 * <p>
	 * <code>
	 * table().matching(targetString)
	 * </code>
	 * <p>
	 * How the matching is done is going to be based on the manipulator.
	 * 
	 * @param matcher
	 *            just a simple string to use for matching, or could be a regex
	 *            expression
	 * @return the current HtmlExtractor for call chaining
	 */
	public HtmlExtractor matching(String matcher) {
		manipulators.getLast().setMatcher(matcher);
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

	/**
	 * Provides a means of extracting a table.
	 * 
	 * @return this for method chaining
	 * @see #matching(String)
	 */
	public HtmlExtractor table() {
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().tag(HTMLElementName.TABLE).build()));
		return this;
	}

	private boolean hasManipulators() {
		return this.manipulators.size() > 0;
	}

	public HtmlExtractor tableWithID(String id) {
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().tag(HTMLElementName.TABLE)
				.elementIdentifierType(ElementIdentifierType.ID)
				.identifier(id)
				.build()));
		return this;
	}

	public HtmlExtractor divWithID(String id) {
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().tag(HTMLElementName.DIV)
				.elementIdentifierType(ElementIdentifierType.ID)
				.identifier(id)
				.build()));
		return this;
	}

	public HtmlExtractor spanWithID(String id) {
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().tag(HTMLElementName.SPAN)
				.elementIdentifierType(ElementIdentifierType.ID)
				.identifier(id)
				.build()));
		return this;
	}

	public HtmlExtractor add(Manipulator manipulator) {
		addManipulator(manipulator);
		return this;
	}

	public HtmlExtractor ofClass(String className, int occurrence) {
		addManipulator(new ElementManipulator(new TagOccurrence.Builder().elementIdentifierType(
				ElementIdentifierType.cssClass)
				.identifier(className)
				.ofClass(className)
				.occurrence(occurrence)
				.build()));
		
		return this;
	}

	public HtmlExtractor ofClass(String className) {
		ofClass(className, 0);
		return this;
	}

	public HtmlExtractor addRequestProperty(String key, String value) {
		httpRequestProperties.put(key, value);
		return this;
	}

	public Map<String, String> getHttpRequestProperties() {
		return httpRequestProperties;
	}

	public HtmlExtractor attribute(Object attributeName) {
		return this;
	}
}