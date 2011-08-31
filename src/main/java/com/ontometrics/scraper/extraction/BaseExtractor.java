package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

/**
 * The one thing all Extractors will have in common is the need to marshall some
 * html to perform their specialized extractions on. This class provides a means
 * of getting that source by calling a chain of {@link Manipulator}s that are
 * held in the {@link HtmlExtractor}.
 * <p>
 * The derived classes don't need to know any of the details, they simply string
 * together the manipulators they want on the HtmlExtractor, then ask the base
 * for the source.
 * <p>
 * Note: because we are doing a fluent interface implementation of the Builder
 * Pattern here, we want to allow for syntax like the following:
 * <p>
 * <code>
 * List<Link> links = linkExtractor.source(htmlExtractor.url(PagedListingTable.getUrl()).table(3)).getLinks();
 * </code>
 * <p>
 * In order to do that, subclasses need to override the source method and just
 * call super, but return this (reference to their class, not the base class).
 * 
 * @author Rob
 * 
 */
public abstract class BaseExtractor {

	/**
	 * Does the work of actually honing in on the source we are interested in.
	 */
	private HtmlExtractor htmlExtractor;

	/**
	 * Provides access to the source from the {@link #htmlExtractor} which
	 * derived classes will then perform their extractions on.
	 * 
	 * @param htmlExtractor
	 *            the extractor to handle the appropriation of the html
	 * @return this so chaining can be done, though, subclasses should override
	 *         so they can have one chain of calls. See the note above.
	 */
	public BaseExtractor source(HtmlExtractor htmlExtractor) {
		this.htmlExtractor = htmlExtractor;
		return this;
	}

	/**
	 * Provides a means of kicking off the {@link Manipulation} chain and
	 * harvesting the results.
	 * 
	 * @return the manipulated source gotten from the {@link #htmlExtractor}
	 */
	public Source getSource() {
		return htmlExtractor.getSource();
	}

}
