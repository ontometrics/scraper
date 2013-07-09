package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.CareerBuilderDetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecifiedFieldExtractorTest {

	private Logger log = LoggerFactory.getLogger(SpecifiedFieldExtractorTest.class);

	private SpecifiedFieldExtractor extractor;

	private Map<String, Field> fieldsFound = new HashMap<String, Field>();

	@Before
	public void setup() {
		this.extractor = new SpecifiedFieldExtractor.Builder()
				.identifyFieldsByClass()
					.addFields(Arrays.asList("job_title", "job_desc", "job_req"))
					.build();
	}

	@Test
	public void canBuildWithSpecificField() {

		this.extractor = new SpecifiedFieldExtractor.Builder().identifyFieldsByClass().addField("job_desc").build();
		assertThat(extractor.getFieldsToGet().size(), is(1));

	}

	@Test
	public void canBuildUsingListOfFields() {

		assertThat(extractor.getFieldsToGet().size(), is(3));
	}

	@Test
	public void canExtractASpecifiedField() {
		List<Field> fields = extractor.source(html().url(CareerBuilderDetailPage.getUrl())).getFields();
		log.info("fields found: {}", fields);

		extractFoundFields(fields);

		Field title = fieldsFound.get("job_title");
		Field description = fieldsFound.get("job_desc");

		assertThat(title.getValue(), is("Front End / HTML Developer"));
		assertThat(description.getValue(), containsString("Front End / HTML Developer"));
	}

	private void extractFoundFields(List<Field> fields) {
		for (Field field : fields) {
			fieldsFound.put(field.getLabel(), field);
		}
	}

}
