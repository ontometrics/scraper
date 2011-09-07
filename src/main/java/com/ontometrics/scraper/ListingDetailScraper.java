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

	private List<Link> links;

	private boolean convertURLs = true;

	public ListingDetailScraper listing(LinkExtractor linkExtractor) throws MalformedURLException {
		links = linkExtractor.getLinks();
		if (iterator != null) {
			while (iterator.hasNext()) {
				URL nextUrl = iterator.next();
				log.debug("nexturl: {}", nextUrl);
				if (nextUrl.toString().contains(sessionIdKeyword)) {
					String urlString = nextUrl.toString().replace(sessionIdKeyword, getSessionIDName());
					nextUrl = new URL(urlString);
				}
				log.debug("next url = {}", nextUrl);
				linkExtractor.url(nextUrl);
				this.links.addAll(linkExtractor.getLinks());
			}
		}
		return this;
	}

	public ListingDetailScraper iterator(Iterator iterator) {
		this.iterator = iterator;
		return this;
	}

	public ListingDetailScraper details(FieldExtractor<?> detailExtractor) {
		log.debug("extracting details from {} found links", links.size());
		String builtUrl = null;
		for (Link link : links) {
			try {
				builtUrl = link.getHref();
				if (shouldConvertURLs() && isRelativeUrl(link.getValue())) {
					builtUrl = convertToAbsoluteUrl(link.getValue());
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

	private boolean shouldConvertURLs() {
		return convertURLs;
	}

	public ListingDetailScraper setConvertURLs(boolean convertURLs) {
		this.convertURLs = convertURLs;
		return this;
	}

}
