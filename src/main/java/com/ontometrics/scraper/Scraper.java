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
 */
public class Scraper {

	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

	/**
	 * Remote resource we will be extracting content from.
	 */
	private URL url;

	/**
	 * Does the work of actually extracting the desired content. If there is an
	 * {@link #iterator}, the scraper iterates through the {@link #url}s and
	 * collects the results.
	 */
	private Extractor extractor;

	/**
	 * Usually Builders have a single product. We support two kinds of products:
	 * a single string that represents a scrape and a set of extracted elements.
	 */
	private List<String> results;

	/**
	 * Scraper will call this to get the URL of the next page.
	 */
	private Iterator iterator;

	private int pages = 0;

	public Scraper() {
		this.extractor = new Extractor();
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
	 * Provides means of asking that just the text of the page be extracted.
	 */
	public Scraper asText() {
		extractor.asText();
		return this;
	}

	// ---- Builder-style Interface
	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public Scraper url(URL url) {
		this.url = url;
		extractor.url(url);
		return this;
	}

	public Extractor extractor() {
		return this.extractor;
	}

	public Scraper extract(List<String> results) throws IOException {
		log.debug("inside extract, iterator is: " + iterator);
		this.results = results;
		if (iterator != null) {
			for (int i = 0; i < pages; i++) {
				extractor.url(iterator.build(i));
				results.addAll(extractor.getResults());
			}
		}
		return this;
	}

	public Scraper iterator(Iterator iterator) {
		log.debug("setting iterator: {}", iterator);
		this.iterator = iterator;
		return this;
	}
	
	public String getResult() throws IOException{
		return extractor.execute();
	}

	public List<String> getResults() {
		return this.results;
	}

	public Scraper pages(int i) {
		this.pages = i;
		return this;
	}

	public Scraper extract(String extraction) {
		return this;
	}

}
