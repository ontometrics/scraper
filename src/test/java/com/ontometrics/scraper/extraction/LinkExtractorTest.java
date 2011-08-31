package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.PagedListingTable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(LinkExtractorTest.class);
	private LinkExtractor linkExtractor;
	private HtmlExtractor htmlExtractor;
	private int numberOfLinksInTargetTable = 86;
	

	@Before
	public void setup() {

		htmlExtractor = new HtmlExtractor();
		linkExtractor = new LinkExtractor();

	}

	@Test
	public void canExtractLinksFromPage() {
		List<Link> links = linkExtractor.source(htmlExtractor.url(PagedListingTable.getUrl()).table(3)).getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfLinksInTargetTable));
	}

	@Test
	public void canExtractLinksMatching() {
		List<Link> links = linkExtractor.source(
				htmlExtractor.url(PagedListingTable.getUrl()).table().matching("Opportunity Title")).getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfLinksInTargetTable));
	}

}
