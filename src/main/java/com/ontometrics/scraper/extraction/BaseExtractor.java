package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.LinkedList;

import net.htmlparser.jericho.Source;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * The one thing all Extractors will have in common is the need to marshall some html to perform their specialized
 * extractions on. This class provides a means of getting that source by calling a chain of {@link Manipulator}s that
 * are held in the {@link HtmlExtractor}.
 * <p>
 * The derived classes don't need to know any of the details, they simply string together the manipulators they want on
 * the HtmlExtractor, then ask the base for the source.
 * <p>
 * Note: because we are doing a fluent interface implementation of the Builder Pattern here, we want to allow for syntax
 * like the following:
 * <p>
 * <code>
 * List<Link> links = linkExtractor.source(htmlExtractor.url(PagedListingTable.getUrl()).table(3)).getLinks();
 * </code>
 * <p>
 * In order to do that, subclasses need to override the source method and just call super, but return this (reference to
 * their class, not the base class).
 * 
 * @author Rob
 * 
 */
public abstract class BaseExtractor {

	/**
	 * Does the work of actually honing in on the source we are interested in.
	 */
	private LinkedList<HtmlExtractor> htmlExtractors = new LinkedList<HtmlExtractor>();

	/**
	 * Provides access to the source from the {@link #htmlExtractor} which derived classes will then perform their
	 * extractions on.
	 * 
	 * @param htmlExtractor
	 *            the extractor to handle the appropriation of the html
	 * @return this so chaining can be done, though, subclasses should override so they can have one chain of calls. See
	 *         the note above.
	 */
	public BaseExtractor source(HtmlExtractor htmlExtractor) {
		this.htmlExtractors.add(htmlExtractor);
		return this;
	}

	/**
	 * Provided as a convenience method for use in cases where might want to extract things from multiple sections of
	 * the page.
	 * 
	 * @param htmlExtractor
	 *            the chain of {@link Manipulator}s that will be used to get interested html element
	 * @return this for chaining, note: overload this in subclasses so calls can be chained.
	 */
	public BaseExtractor section(HtmlExtractor htmlExtractor) {
		source(htmlExtractor);
		return this;
	}

	public BaseExtractor url(URL url) {
		for (HtmlExtractor extractor : htmlExtractors) {
			extractor.url(url);
		}
		return this;
	}

	protected HtmlExtractor getCurrentHtmlExtractor() {
		return this.htmlExtractors.getLast();
	}

	/**
	 * Provides a means of kicking off the {@link Manipulation} chain and harvesting the results.
	 * 
	 * @return the manipulated source gotten from the {@link #htmlExtractor}
	 */
	public Source getSource() {
		StringBuffer accumulatedSource = new StringBuffer();
		for (HtmlExtractor extractor : htmlExtractors) {
			accumulatedSource.append(extractor.getSource().toString());
		}
		Source combinedSource = new Source(accumulatedSource);
		combinedSource.fullSequentialParse();
		return combinedSource;
	}

}
