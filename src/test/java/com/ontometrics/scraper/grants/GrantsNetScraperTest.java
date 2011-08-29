package com.ontometrics.scraper.grants;

import java.net.MalformedURLException;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.Scraper;

public class GrantsNetScraperTest {
	
	private static final Logger log = LoggerFactory.getLogger(GrantsNetScraperTest.class);

	@Test
	public void defaultFieldExtractorWillFindTablesAndDLs() throws MalformedURLException {

		Scraper scraper = new Scraper();
		Map<String, String> fields = scraper.url(GrantHtmlSample.GrantsnetDetailPage.getUrl()).getFields();
		
		log.info("fields: {}", fields);

	}

}
