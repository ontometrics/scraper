package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.ProgramDetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(TextExtractorTest.class);

	private String eligibilityClassName = "fld_applicant_eligibility";

	@Test
	public void getTextFromElement() {
		String text = new TextExtractor().source(
				html().url(ProgramDetailPage.getUrl()).ofClass(eligibilityClassName, 1)).getText();

		log.info("text from class: {} is: {}", eligibilityClassName, text);

	}

}
