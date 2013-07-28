package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;







import net.htmlparser.jericho.HTMLElementName;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.HtmlSample;

public class MatchedFieldExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(MatchedFieldExtractorTest.class);

	MatchedFieldExtractor extractor;
	@Before
	public void setup() {
		this.extractor = new MatchedFieldExtractor.Builder().identifyFieldsByTag(HTMLElementName.DIV).build();
	}
	@Test
	public void extractAllFieldsWithSameTag() throws MalformedURLException, IOException {
		URL url = HtmlSample.PageWithJobDetails.getUrl();
		List<Field> fields = extractor.source(html().url(url).spanWithID("TrackingJobBody")).getFields();	
		assertThat(fields.size(), is(greaterThan(0)));
		log.debug("fields = {}", fields);
	}
}
