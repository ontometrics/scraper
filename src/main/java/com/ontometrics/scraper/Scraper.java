package com.ontometrics.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.util.ScraperUtil;

/**
 * Provides a mechanism for extracting items from pages or feeds.
 * <p>
 * Uses a fluent builder pattern in a fashion that does border on being a DSL.
 * The idea is that a {@link #url} is provided, then through a set of
 * manipulator methods, the operations to be performed are framed. All scrapings
 * require a url call and then the execute thread at the end to perform the
 * scraping.
 * <p>
 * Internally, the manipulators are triggered by basic conditions right now. We
 * will need a more sophisticated architecture as more operations pile up
 * (perhaps something like a Chain of Responsibility Pattern).
 * 
 * @author Rob
 * 
 */
/**
 * @author robwilliams
 * 
 */
public class Scraper {

	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

	/**
	 * Remote resource we will be extracting content from.
	 */
	private URL url;

	/**
	 * Sometimes we want to extract just part of the feed or page.
	 */
	private String tagToGet;

	/**
	 * Which occurrence of the tag should we extract? (Remember it is 0 indexed
	 * so 1st would be 0.
	 */
	private int occurrence = 0;

	/**
	 * The type of output we should return upon extraction.
	 */
	private OutputFormats outputFormat;

	public Scraper() {
		outputFormat = OutputFormats.Html;
	}

	/**
	 * Call this method to have the scrape performed and the extractions
	 * returned in the desired format {@link #outputFormat}.
	 * 
	 * @return the items from the {@link #url} that were prescribed by the
	 *         various manipulators
	 * @throws IOException
	 */
	public String execute() throws IOException {
		String result = "";
		Source source = new Source(url);
		source.fullSequentialParse();

		if (outputFormat == OutputFormats.Text) {
			result = source.getTextExtractor().toString();
		} else if (outputFormat == OutputFormats.Html) {
			result = source.toString();
		}

		if (tagToGet != null) {
			result = extractTagContent(result);
		}

		return result;
	}

	/**
	 * Provides a means of just extracting links.
	 * 
	 * @return a list of the links that were valid {@link URL}s.
	 * @throws IOException
	 */
	public List<URL> getLinks() throws IOException {
		List<URL> links = new ArrayList<URL>();
		Source source = new Source(url);
		source.fullSequentialParse();
		List<Element> linkElements = source.getAllElements(HTMLElementName.A);
		for (Element linkElement : linkElements) {
			String href = linkElement.getAttributeValue("href");
			if (href == null)
				continue;
			// A element can contain other tags so need to extract the text from
			// it:
			String label = linkElement.getContent().getTextExtractor().toString();
			log.debug(label + " <" + href + '>');
			URL currentUrl;
			try {
				currentUrl = new URL(href);
				links.add(currentUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return links;
	}

	/**
	 * This is the default: scrape a page and get the html from the {@link #url}
	 * .
	 */
	public Scraper asHtml() {
		this.outputFormat = OutputFormats.Html;
		return this;
	}

	/**
	 * Provides means of asking that just the text of the page be extracted.
	 */
	public Scraper asText() {
		this.outputFormat = OutputFormats.Text;
		return this;
	}

	// ---- Builder-style Interface
	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	/**
	 * Sometimes, just a certain tag is desired, for instance, a table.
	 * 
	 * @param tag
	 *            the tag you want extracted
	 * @see #occurrence
	 */
	public Scraper tag(String tag) {
		this.tagToGet = tag;
		return this;
	}

	private String extractTagContent(String htmlContent) {
		return ScraperUtil.extract(htmlContent, tagToGet, occurrence);
	}

	/**
	 * Provides a means of asking for the nth occurrence of a tag, so if the
	 * desired content is located in the 3rd table on the page, you could do
	 * tag("
	 * <table>
	 * ", 3).
	 * 
	 * @param tag
	 *            the target tag to look for
	 * @param occurrence
	 *            the 0-based number of its occurrence in the page/feed.
	 */
	public Scraper tag(String tag, int occurrence) {
		this.tagToGet = tag;
		this.occurrence = occurrence;
		return this;
	}
}
