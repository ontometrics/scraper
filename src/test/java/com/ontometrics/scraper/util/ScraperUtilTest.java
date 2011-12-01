package com.ontometrics.scraper.util;

import static com.ontometrics.scraper.HtmlSample.TableOfLinks;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.TestUtil;
import com.ontometrics.scraper.extraction.Link;

public class ScraperUtilTest {

	private static final Logger log = LoggerFactory.getLogger(ScraperUtilTest.class);

	private String testPageSource = "/testpages/ids-page-2.html";

	private URL testPageSourceURL;

	@Before
	public void setup() {
		testPageSourceURL = TestUtil.getFileAsURL(testPageSource);
	}

	@Test
	public void extractLinksWithText() throws IOException {
		Source source = new Source(TableOfLinks.getUrl());
		source.fullSequentialParse();
		List<Link> links = ScraperUtil.extractLinks(source.toString());
		log.info("found following links in table: {}", links);
	}

	@Test
	public void extractOccurrenceOfTag() {

		String test = "<table>1</table><table>2</table><table>3</table><table>4</table><table>5</table>";

		String table1 = ScraperUtil.extract(test, HTMLElementName.TABLE, 0);
		String table3 = ScraperUtil.extract(test, HTMLElementName.TABLE, 2);

		assertThat(table1, is(equalTo("<table>1</table>")));
		assertThat(table3, is(equalTo("<table>3</table>")));

	}

	@Test
	public void extractParameterFromGet() {
		String testUrl = "http://www.grants.gov/search/category.do;jsessionid=yy2pTSxJ2x10GQg11sJhCqBKM6dhnGdry0gXP9YMz73Kqm8nT11x!-25395513";

		String sessionID = ScraperUtil.extractParameter(testUrl, "jsessionid");

		log.info("session ID: {}", sessionID);

		assertThat(sessionID, is("yy2pTSxJ2x10GQg11sJhCqBKM6dhnGdry0gXP9YMz73Kqm8nT11x!-25395513"));
	}

	@Test
	public void extractSessionIDFromLink() throws IOException {

		String sessionID = ScraperUtil.extractSessionId(testPageSourceURL, "jsessionid");
		log.info("sessionID: {}", sessionID);

		assertThat(sessionID, is("np3QTP2BZtspJv5Y38dyMnWZRgC6NV1Zn6lHSKJq1rQQ3tkLTyyZ!336881473"));

	}

	@Test
	public void extractTagMatching() {
		String test = "<table>1</table><table>2</table><table>3</table><table>4</table><table>5</table>";

		String source = ScraperUtil.extractTagMatching(test, new TagOccurrence.Builder()
				.tag(HTMLElementName.TABLE)
				.occurrence(0)
				.matching("3")
				.build());

		log.info("tag matching 3: {}", source);

	}

	@Test
	public void testGetBaseUrl() throws MalformedURLException {
		URL onlyHostName = new URL("http://www.google.com");
		URL anotherUrl = new URL("http://www.google.com/about");
		URL oneDirectoryDeep = new URL("http://www.google.com/about/index.html");

		URL result = null;
		log.info("ohn = {}", onlyHostName.getProtocol());
		result = ScraperUtil.getBaseUrl(onlyHostName);
		assertThat(result.toString(), is("http://www.google.com/"));

		result = ScraperUtil.getBaseUrl(anotherUrl);
		assertThat(result.toString(), is("http://www.google.com/"));

		result = ScraperUtil.getBaseUrl(oneDirectoryDeep);
		assertThat(result.toString(), is("http://www.google.com/about/"));
	}
}
