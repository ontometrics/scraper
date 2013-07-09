package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.CareerBuilderDetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.ArrayList;
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
		List<String> fields = new ArrayList<String>();
		fields.add("job_title");
		fields.add("job_desc");
		fields.add("job_req");
		this.extractor = new SpecifiedFieldExtractor.Builder().identifyFieldsByClass().addFields(fields).build();
	}

	@Test
	public void canBuildWithSpecificField() {

		this.extractor = new SpecifiedFieldExtractor.Builder().identifyFieldsByClass().addField("job_desc").build();
		assertThat(extractor.getFieldsToGet().size(), is(1));

	}
	
	@Test
	public void canBuildUsingListOfFields(){
		List<String> fields = new ArrayList<String>();
		fields.add("job_title");
		fields.add("job_desc");
		fields.add("job_req");
		this.extractor = new SpecifiedFieldExtractor.Builder().identifyFieldsByClass().addFields(fields).build();
		
		assertThat(extractor.getFieldsToGet().size(), is(3));
	}

	@Test
	public void canExtractASpecifiedField() {
		List<Field> fields = extractor.source(html().url(CareerBuilderDetailPage.getUrl())).getFields();
		log.info("fields found: {}", fields);

		Field description = fields.get(0);

		assertThat(description.getValue(), containsString("Front End / HTML Developer"));
	}

}
