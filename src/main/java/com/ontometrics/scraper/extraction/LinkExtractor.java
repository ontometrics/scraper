package com.ontometrics.scraper.extraction;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a simple means of pulling links out of a clump of html.
 * 
 * @author Rob
 */
public class LinkExtractor extends BaseExtractor {

	private static final Logger log = LoggerFactory.getLogger(LinkExtractor.class);

	private String matcher;

	/**
	 * This is the product we are building here.
	 * 
	 * @return list of links found in the source (after manipulation)
	 */
	public List<Link> getLinks() {
		return extractLinks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ontometrics.scraper.extraction.BaseExtractor#source(com.ontometrics .scraper.extraction.HtmlExtractor)
	 * Note: override here so we can do one chain of calls..
	 */
	@Override
	public LinkExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ontometrics.scraper.extraction.BaseExtractor#section(com.ontometrics .scraper.extraction.HtmlExtractor)
	 * Note: override here so we can do one chain of calls..
	 */
	@Override
	public LinkExtractor section(HtmlExtractor htmlExtractor) {
		super.section(htmlExtractor);
		return this;
	}

	public LinkExtractor matching(String matchingExpression) {
		this.matcher = matchingExpression;
		return this;
	}

	private List<Link> extractLinks() {
		List<Link> links = new ArrayList<Link>();
		List<Element> as = getSource().getAllElements(HTMLElementName.A);
		for (Element linkElement : as) {
			String text = linkElement.getTextExtractor().toString();
			String href = linkElement.getAttributeValue("href");
			String name = linkElement.getAttributeValue("name");
			if (href != null || name != null) {
				Link link = new Link.Builder().label(text).href(href).name(name).build();
				log.debug("constructed link: {} from {} must match: {}", new Object[] { link, linkElement, matcher });
				if (matcher == null || (link.getHref() != null && link.getHref().contains(matcher))) {
					links.add(link);
				}
			}
		}
		return links;
	}

}
