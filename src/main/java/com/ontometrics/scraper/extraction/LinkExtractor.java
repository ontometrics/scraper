package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.List;

import com.ontometrics.scraper.util.ScraperUtil;

public class LinkExtractor extends BaseExtractor implements HtmlExtractor {

	/**
	 * This is the product we are building here.
	 * 
	 * @return list of links found in the source (after manipulation)
	 */
	public List<Link> getLinks() {
		performManipulations();
		return ScraperUtil.extractLinks(getSource().toString());
	}

}
