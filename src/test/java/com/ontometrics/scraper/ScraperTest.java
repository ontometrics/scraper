package com.ontometrics.scraper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScraperTest {

	private static final Logger log = LoggerFactory.getLogger(ScraperTest.class);

	private String testTableFileLocation = "/testpages/grants-gov-table.html";

	private String testDetailFileLocation = "/testpages/cfda-program.html";

	private URL testTableHtmlUrl;

	private URL testDetailPageUrl;

	private String eligibilityCodeId = "dnf_class_values_cfda__applicant_eligibility__widget";

	private String eligibilityClassName = "fld_applicant_eligibility";

	@Before
	public void setup() {
		testTableHtmlUrl = TestUtil.getFileAsURL(testTableFileLocation);
		testDetailPageUrl = TestUtil.getFileAsURL(testDetailFileLocation);
	}

	@Test
	public void scrapeUrlReturnsHtml() throws IOException {
		String pageText = new Scraper().url(testTableHtmlUrl).execute();
		assertThat(pageText.length(), is(greaterThan(0)));
		assertThat(pageText.contains("<html>"), is(true));
	}

	@Test
	public void extractPageText() throws IOException {
		String pageContent = new Scraper().url(testTableHtmlUrl).asText().execute();
		assertThat(pageContent.contains("<html>"), is(false));
		log.info("Content: {}", pageContent);
	}

	@Test
	public void extractTableFromPage() throws Exception {
		String pageText = new Scraper().url(testTableHtmlUrl).tag("<table>", 3).execute();
		log.debug("table extracted: {}", pageText);
		assertThat(pageText.startsWith("<table"), is(true));
		log.info(pageText);

	}

	@Test
	public void extractLinksFromTableOnPage() throws Exception {
		List<URL> urls = new Scraper().url(testTableHtmlUrl).tag("<table>", 3).getLinks();

		assertThat(urls.size(), is(greaterThan(0)));

	}

	@Test
	public void extractContentsOfElementWithId() throws Exception {
		String tagText = new Scraper().url(testDetailPageUrl).id(eligibilityCodeId).execute();
		log.info("tag text: {}", tagText);
		assertThat(tagText.contains("nonprofit institutions of higher education"), is(true));
	}

	@Test
	public void extractContentsByClassAndOccurrence() throws Exception {
		String tagText = new Scraper().url(testDetailPageUrl).ofClass(eligibilityClassName, 1).execute();
		log.info("tag text: {}", tagText);
		assertThat(tagText.contains("39"), is(true));
		assertThat(tagText.contains("52"), is(true));
	}

	@Test
	public void extractParameterFromLinksInTable() throws Exception {
		Scraper scraper = new Scraper();
		List<String> ids = scraper
				.url(testTableHtmlUrl)
				.extract(scraper.extractor().table(3).links().parameter("oppId").getResults())
				.iterator("/search?mode=VIEW&pagenum=#")
				.getResults();
		
		assertThat(ids.size(), is(greaterThan(0)));
		log.info("ids found: {}", ids);

	}

}
