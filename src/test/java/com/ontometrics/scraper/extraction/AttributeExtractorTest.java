package com.ontometrics.scraper.extraction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.htmlparser.jericho.Source;

import org.junit.Before;
import org.junit.Test;

public class AttributeExtractorTest {

	private MockManipulator mockExtractionAgent;
	private AttributeExtractor attributeExtractor;
	private String targetValue = "testing";

	@Before
	public void setup() {
		mockExtractionAgent = new MockManipulator();
		attributeExtractor = new AttributeExtractor("onchange");
		attributeExtractor.setSuccessor(mockExtractionAgent);

	}

	@Test
	public void canExtractValueOfAnAttribute() {
		attributeExtractor.execute(new Source("<span onchange=\"" + targetValue  + "\">"));
		assertThat(mockExtractionAgent.getResult().toString(), is(targetValue));
	}

}
