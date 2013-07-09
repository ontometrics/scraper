package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.CareerBuilderDetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecifiedFieldExtractorTest {
	
	private Logger log = LoggerFactory.getLogger(SpecifiedFieldExtractorTest.class);

	private SpecifiedFieldExtractor extractor;

	@Before
	public void setup() {
		this.extractor = new SpecifiedFieldExtractor.Builder().identifyFieldsByClass().addField("job_desc").build();
	}

	@Test
	public void canBuildExtractor() {
		
		assertThat(extractor.getFieldsToGet().size(), is(1));

	}
	
	@Test
	public void canExtractASpecifiedField(){
		List<Field> fields = extractor.source(html().url(CareerBuilderDetailPage.getUrl())).getFields();
		log.info("fields found: {}", fields);
	}

}
