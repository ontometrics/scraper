package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.DetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFieldExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(DefaultFieldExtractorTest.class);

	private FieldExtractor<?> fieldExtractor = new DefaultFieldExtractor();

	@Test
	public void extractFieldsAfterTablePairedTags() throws MalformedURLException, IOException {
		List<Field> fields = fieldExtractor.source(html().url(DetailPage.getUrl())).getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		log.debug("fields = {}", fields);

	}

}
