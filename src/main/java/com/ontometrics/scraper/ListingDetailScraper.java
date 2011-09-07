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

	private LinkExtractor linkExtractor;

	private Iterator iterator;

	private FieldExtractor<?> detailExtractor;

	private List<Link> links;

	private List<Record> extractedRecords = new ArrayList<Record>();

	public Scraper listing(LinkExtractor linkExtractor) throws MalformedURLException {
		this.linkExtractor = linkExtractor;
		links = linkExtractor.getLinks();
		if (iterator != null) {
			int i = 0;
			while (iterator.hasNext()) {
				URL nextUrl = iterator.next();
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

	public Scraper iterator(Iterator iterator) {
		this.iterator = iterator;
		return this;
	}

	public Scraper details(FieldExtractor<?> detailExtractor) {
		this.detailExtractor = detailExtractor;
		String builtUrl = null;
		for (Link link : links) {
			try {
				if (isRelativeUrl(link.getValue())) {
					builtUrl = convertToAbsoluteUrl(link.getValue());
				}
				log.debug("Using link = {}", builtUrl);
				List<Field> fields = new ArrayList<Field>(detailExtractor.url(new URL(builtUrl)).getFields());
				log.debug("returned fields = {}", fields);
				extractedRecords.add(new ScrapedRecord(fields));
			} catch (MalformedURLException e) {
				log.info("Bad URL in looping detail page for listing links: {}", e.toString());
			}

		}
		return this;
	}

}
