package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.ProgramDetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import net.htmlparser.jericho.Source;
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

    @Test
    public void canGetSourceThenChainWithHtmlExtractor(){
        Source source = html().url(ProgramDetailPage.getUrl()).getSource();

        log.info("source: {}", source.toString());
        assertThat(source, notNullValue());

        String text = new TextExtractor().source(html().source(source).ofClass(eligibilityClassName, 1)).getText();

        log.info("text extracted from prefetched source: {}", text);

        assertThat(text, is("Applicant Eligibilty: 39-Anyone/general public/ 52-Libraries/lnformation/Statistics"));

    }

	@Test
	public void canGetAttributeOfElement() {
		String className = "lst-pp";
		Object attributeName = "onchange";
		String text = new TextExtractor()
				.source(html()
						.url(ProgramDetailPage.getUrl())
						.ofClass(eligibilityClassName, 1)
						.attribute(attributeName))
				.getText();

		log.info("attribute {} from class: {} is: {}", new Object[] { className, attributeName, text });

	}

}
