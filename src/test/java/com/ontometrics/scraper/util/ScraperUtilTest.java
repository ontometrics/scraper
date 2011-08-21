package com.ontometrics.scraper.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ScraperUtilTest {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ScraperUtilTest.class);

	@Test
	public void extractOccurrenceOfTag() {

		String test = "<table>1</table><table>2</table><table>3</table><table>4</table><table>5</table>";

		String table1 = ScraperUtil.extract(test, "<table>", 0);
		String table3 = ScraperUtil.extract(test, "<table>", 2);

		assertThat(table1, is(equalTo("<table>1</table>")));
		assertThat(table3, is(equalTo("<table>3</table>")));

	}

}
