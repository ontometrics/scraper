package com.ontometrics.scraper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScraperTest {

	private static final Logger log = LoggerFactory.getLogger(ScraperTest.class);

	private String grantsTableUrl = "http://www.grants.gov/search/search.do?mode=CATSEARCH&fundActivity=*";

	@Test
	public void scrapeUrlReturnsHtml() throws IOException {
		String pageText = new Scraper().url(grantsTableUrl).execute();
		assertThat(pageText.length(), is(greaterThan(0)));
		assertThat(pageText.contains("<html>"), is(true));
	}

	@Test
	public void testAsText() throws IOException {
		String pageContent = new Scraper().url("http://www.apple.com").asText().execute();
		assertThat(pageContent.contains("<html>"), is(false));
		log.info("Content: {}", pageContent);
	}
	
	@Test
	public void extractTableFromPage() throws Exception {
		String pageText = new Scraper().url(grantsTableUrl).tag("<table>", 3).execute();
		log.debug("table extracted: {}", pageText);
		assertThat(pageText.startsWith("<table"), is(true));
		log.info(pageText);
		
	}
	
	@Test
	public void extractLinksFromTableOnPage() throws Exception {
		List<URL> urls = new Scraper().url(grantsTableUrl).tag("<table>", 3).getLinks();
		
		assertThat(urls.size(), is(greaterThan(0)));
		
	}
	
}
