package com.ontometrics.scraper.extraction;

import java.util.List;

import com.ontometrics.scraper.util.ScraperUtil;

/**
 * Provides a simple means of pulling links out of a clump of html.
 * 
 * @author Rob
 */
public class LinkExtractor extends BaseExtractor {

	/**
	 * This is the product we are building here.
	 * 
	 * @return list of links found in the source (after manipulation)
	 */
	public List<Link> getLinks() {
		return ScraperUtil.extractLinks(getSource().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ontometrics.scraper.extraction.BaseExtractor#source(com.ontometrics
	 * .scraper.extraction.HtmlExtractor) Note: override here so we can do one
	 * chain of calls..
	 */
	@Override
	public LinkExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ontometrics.scraper.extraction.BaseExtractor#section(com.ontometrics
	 * .scraper.extraction.HtmlExtractor) Note: override here so we can do one
	 * chain of calls..
	 */
	@Override
	public LinkExtractor section(HtmlExtractor htmlExtractor) {
		super.section(htmlExtractor);
		return this;
	}

}
