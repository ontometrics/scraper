package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

public class CookiePassingTest {

	private static final String URL_FOR_SITE_REQUIRING_COOKIE = "https://www.dibbs.bsm.dla.mil/RFQ/RfqRecs.aspx?category=issue&TypeSrch=dt&scope=all&value=9%2F20%2F2011&bPg=1&cPg=1";

	@Test
	public void canPassACookie() throws MalformedURLException {
		URL targetUrl = new URL(URL_FOR_SITE_REQUIRING_COOKIE);
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(targetUrl)
						.addRequestProperty("Cookie", "DIBBSDoDWarning=AGREE")
						.tableWithID("tblSearchResults")).getFields();

		assertThat(fields.size(), greaterThan(0));
	}

}
