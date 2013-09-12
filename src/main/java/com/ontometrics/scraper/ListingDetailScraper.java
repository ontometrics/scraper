package com.ontometrics.scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.extraction.Field;
import com.ontometrics.scraper.extraction.FieldExtractor;
import com.ontometrics.scraper.extraction.Link;
import com.ontometrics.scraper.extraction.LinkExtractor;

public class ListingDetailScraper extends RecordScraper {

	private static final Logger log = LoggerFactory.getLogger(ListingDetailScraper.class);

	private Iterator iterator;
	
	private int pages;

	private List<Link> links = new ArrayList<Link>();

	private boolean convertURLs = true;

	public ListingDetailScraper listing(LinkExtractor linkExtractor) throws MalformedURLException {
		if (iterator != null) {
			int counter = 0;
			while (iterator.hasNext() && counter < pages) {
				log.debug("Inside iterator.hasnext");
				URL nextUrl = iterator.next();
				log.debug("next url = {}", nextUrl);
				if (nextUrl.toString().contains(sessionIdKeyword)) {
					String urlString = nextUrl.toString().replace(sessionIdKeyword, getSessionIDName());
					nextUrl = new URL(urlString);
				}
				linkExtractor.url(nextUrl);
				this.links.addAll(linkExtractor.getLinks());
				++counter;
			}
		} else {
			links = linkExtractor.getLinks();
		}
		return this;
	}

	public ListingDetailScraper iterator(Iterator iterator) {
		this.iterator = iterator;
		return this;
	}

	public ListingDetailScraper details(FieldExtractor<?> detailExtractor) {
		// set base url here if need be
		if (shouldConvertURLs()) {
			setBaseUrl(iterator.getBaseUrl());
		}

		if (links == null || links.size() == 0) {
			throw new IllegalStateException("No links found, can't extract detail pages.");
		}
		log.debug("extracting details from {} links.", links.size());
		for (int i = 0; i < links.size(); i++) {
			log.info("{}) {}", i, links.get(i));
		}

		int counter = 0;
		String builtUrl = null;
		for (Link link : links) {
			log.info("** Current detail link counter = {} of {} total.", counter++, links.size());
			try {
				builtUrl = link.getHref();
				if (shouldConvertURLs() && isRelativeUrl(link.getValue())) {
					builtUrl = convertToAbsoluteUrl(link.getValue());
					log.info("converted built url to absolute, result = {}", builtUrl);
				}
				log.debug("Using link = {}", builtUrl);
				List<Field> fields = new ArrayList<Field>(detailExtractor.url(new URL(builtUrl)).getFields());
				log.debug("returned fields = {}", fields);
				addRecord(new ScrapedRecord(fields));
			} catch (MalformedURLException e) {
				log.info("Bad URL in looping detail page for listing links: {}", e.toString());
			}

		}
		return this;
	}
	
	public ListingDetailScraper pages(int pages) {
		this.pages = pages;
		return this;
	}

	private boolean shouldConvertURLs() {
		return convertURLs;
	}

	public ListingDetailScraper setConvertURLs(boolean convertURLs) {
		this.convertURLs = convertURLs;
		return this;
	}
	
	public List<Link> getLinks() {
		return links;
	}

}

