package com.ontometrics.scraper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScraperTest {

	private static final Logger log = LoggerFactory.getLogger(ScraperTest.class);

	private Scraper scraper;

	private String testTableFileLocation = "/testpages/grants-gov-table.html";

	private String testDetailFileLocation = "/testpages/cfda-program.html";

	private URL testTableHtmlUrl;

	private URL testDetailPageUrl;

	private String eligibilityCodeId = "dnf_class_values_cfda__applicant_eligibility__widget";

	private String eligibilityClassName = "fld_applicant_eligibility";

	@Before
	public void setup() {
		scraper = new Scraper();
		testTableHtmlUrl = TestUtil.getFileAsURL(testTableFileLocation);
		testDetailPageUrl = TestUtil.getFileAsURL(testDetailFileLocation);
	}

	@Test
	public void scrapeUrlReturnsHtml() throws IOException {
		String pageText = new Scraper().url(testTableHtmlUrl).getResult();
		assertThat(pageText.length(), is(greaterThan(0)));
		assertThat(pageText.contains("<html>"), is(true));
		log.debug("pageText = {}", pageText);
	}

	@Test
	public void extractPageText() throws IOException {
		String pageContent = new Scraper().url(testTableHtmlUrl).asText().getResult();
		assertThat(pageContent.contains("<html>"), is(false));
		log.info("Content: {}", pageContent);
	}

	@Test
	public void extractTableFromPage() throws Exception {
		String pageText = scraper.url(testTableHtmlUrl).extract(scraper.extractor().table(3).execute()).getResult();
		log.debug("table extracted: {}", pageText);
		assertThat(pageText.startsWith("<table"), is(true));
		log.info(pageText);

	}

	@Test
	public void extractLinksFromTableOnPage() throws Exception {
		List<String> urls = scraper
				.url(testTableHtmlUrl)
				.extract(scraper.extractor().table(3).links().getResults())
				.getResults();

		assertThat(urls.size(), is(greaterThan(0)));

	}

	@Test
	public void extractContentsOfElementWithId() throws Exception {
		String tagText = scraper
				.url(testDetailPageUrl)
				.extract(scraper.extractor().id(eligibilityCodeId).execute())
				.getResult();
		log.info("tag text: {}", tagText);
		assertThat(tagText.contains("nonprofit institutions of higher education"), is(true));
	}

	@Test
	public void extractContentsByClassAndOccurrence() throws Exception {
		String tagText = scraper
				.url(testDetailPageUrl)
				.extract(scraper.extractor().ofClass(eligibilityClassName, 1).execute())
				.getResult();
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
				.getResults();

		assertThat(ids.size(), is(greaterThan(0)));
		log.info("ids found: {}", ids);
		assertThat(ids, hasItems("40034", "40158", "40095", "40790", "40821", "40849", "40315"));
		assertThat(ids, hasItems("40967", "41255", "41282", "40458", "41599", "41734", "40667", "41771"));
		assertThat(ids, hasItems("41898", "41032", "41896", "42394", "42445"));
	}

	@Test
	public void extractParameterFromLinksInIteratedTables() throws Exception {
		Scraper scraper = new Scraper();
		Iterator pageIterator = new Iterator() {
			@Override
			public URL build(int i) {
				String nextPageUrl = MessageFormat.format("/testpages/ids-page-{0}.html", i + 2);
				log.debug("next page to iterate to: {}", nextPageUrl);
				return TestUtil.getFileAsURL(nextPageUrl);
			}
		};
		List<String> ids = scraper
				.url(testTableHtmlUrl)
				.pages(1)
				.iterator(pageIterator)
				.extract(scraper.extractor().table(3).links().parameter("oppId").getResults())
				.getResults();

		assertThat(ids.size(), is(40));
		log.info("ids {} found: {}", ids.size(), ids);
	}

}
