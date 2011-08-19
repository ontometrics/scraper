package com.ontometrics.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
public class Scraper {

	@SuppressWarnings("unused")
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

	public Scraper asHtml() {
		this.outputFormat = OutputFormats.Html;
		return this;
	}

	public Scraper asText() {
		this.outputFormat = OutputFormats.Text;
		return this;
	}

	// ---- Builder-style Interface
	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public Scraper tag(String tag) {
		this.tagToGet = tag;
		return this;
	}

	private String extractTagContent(String htmlContent) {
		return ScraperUtil.extract(htmlContent, tagToGet, occurrence);
	}

	public Scraper tag(String tag, int occurrence) {
		this.tagToGet = tag;
		this.occurrence = occurrence;
		return this;
	}
}
