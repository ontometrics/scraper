package com.ontometrics.scraper.extraction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.HtmlSample;

public class LinkExtractorTest {
	
	private static final Logger log = LoggerFactory.getLogger(LinkExtractorTest.class);

	@Test
	public void canExtractLinksFromPage() {
		
		LinkExtractor linkExtractor = new LinkExtractor();
		List<Link> links = linkExtractor.url(HtmlSample.PagedListingTable.getUrl()).table(3).getLinks();
		
		log.info("found links: {}", links);
		assertThat(links.size(), is(greaterThan(0)));

	}

}
