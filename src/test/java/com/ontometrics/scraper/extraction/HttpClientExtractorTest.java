package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientExtractorTest {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(HttpClientExtractorTest.class);

	private static final String FirstUrlString = "https://www.fbo.gov/index?s=opportunity&mode=form&id=4b04739c57d224a3e6dcea5c0ede9413&tab=ivl&tabmode=list";
	private static final String secondUrlString = "https://www.fbo.gov/index?_so_list_aat9a15dfc9a75119393533747dabcfae37=1000";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void clientExtractorShouldHoldCookieStateBetweenInvocations() throws MalformedURLException {
		HttpClientExtractor sourceExtractor = new HttpClientExtractor();
		URL firstUrl = new URL(FirstUrlString);
		URL secondUrl = new URL(secondUrlString);

		List<Field> fields = null;
		fields = new DefaultFieldExtractor().source(html().from(sourceExtractor).url(firstUrl)).getFields();
		fields = new DefaultFieldExtractor().source(html().from(sourceExtractor).url(secondUrl)).getFields();
		sourceExtractor.close();

		boolean foundLastVendor = false;
		for (Field field : fields) {
			if (field.getValue().equals("walker")) {
				foundLastVendor = true;
				break;
			}
		}
		assertThat(foundLastVendor, is(true)); // this last vendor will only show up if cookie state works properly due
												// to the paging mechanism
	}
}
