package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.PagedListingTable;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
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
	private int numberOfLinksInTable3 = 83;
	private int numberOfLinksInTargetTable = 83;
	private int numberOfMatchingLinks = 30;

	@Before
	public void setup() {
		linkExtractor = new LinkExtractor();
	}

	@Test
	public void canExtractLinksFromPage() {
		List<Link> links = linkExtractor.source(html().url(PagedListingTable.getUrl()).table(3)).getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfLinksInTable3));
	}

	@Test
	public void canExtractLinksMatching() {
		List<Link> links = linkExtractor.section(
				html().url(PagedListingTable.getUrl()).table().matching("Opportunity Title")).getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfLinksInTargetTable));

		linkExtractor = new LinkExtractor();
		links = linkExtractor
				.source(html().url(PagedListingTable.getUrl()).table().matching("663-A-08-002"))
				.matching("PAGECHANGE")
				.getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfMatchingLinks));
	}

	@Test
	public void canExtractLinksFromMultipleSections() {
		List<Link> links = linkExtractor
				.section(html().url(PagedListingTable.getUrl()).table().matching("Opportunity Title"))
				.section(html().url(PagedListingTable.getUrl()).table(3))
				.getLinks();
		log.info("found {} links: {}", links.size(), links);
		assertThat(links.size(), is(numberOfLinksInTargetTable + numberOfLinksInTable3));
	}

	@Test
	public void canExtractLinksMatchingAString() {
		String linkPattern = "mode=VIEW";
		List<Link> links = linkExtractor
				.source(html().url(PagedListingTable.getUrl()).table().matching("Opportunity Title"))
				.matching(linkPattern)
				.getLinks();

		log.info("found {} links matching: {}", links.size(), linkPattern);

	}

}
