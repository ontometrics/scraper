package com.ontometrics.scraper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScraperTest {
	
	private static final Logger log = LoggerFactory.getLogger(ScraperTest.class);

	private String grantsTableUrl = "http://www.grants.gov/search/search.do?mode=CATSEARCH&fundActivity=*";

	@Test
	public void scrapeUrlReturnsText() throws IOException {
		String pageText = new Scraper().url(grantsTableUrl).execute();
		assertThat(pageText.length(), is(greaterThan(0)));
		log.info(pageText);
		
	}
}
