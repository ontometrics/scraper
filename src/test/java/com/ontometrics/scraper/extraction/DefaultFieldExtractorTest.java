package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.*;
import com.ontometrics.scraper.util.ScraperUtil;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static com.ontometrics.scraper.HtmlSample.*;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static com.ontometrics.scraper.html.HtmlTable.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DefaultFieldExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(DefaultFieldExtractorTest.class);

	private FieldExtractor<?> fieldExtractor = new DefaultFieldExtractor();

	@Test
	public void extractFieldsAfterTablePairedTags() throws MalformedURLException, IOException {
		List<Field> fields = fieldExtractor.source(html().url(DetailPage.getUrl()).table(3)).getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		log.debug("fields = {}", fields);
	}

	@Test
	public void extractsFieldsFromULs() {

		List<Field> fields = fieldExtractor.source(html().url(HtmlSample.TableWithULs.getUrl())).getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		Record record = new ScrapedRecord(fields);

		Field fieldFromUL = new ScrapedField("Application deadline(s)", "07/13/2010");
		assertThat(record.getFields().contains(fieldFromUL), is(true));

	}

	@Test
	public void extractsFieldFromSpecificTagOccurrence() {

		List<Field> fields = fieldExtractor
				.source(html().url(HtmlSample.TableWithULs.getUrl()))
				.field("source", HTMLElementName.H2)
				.getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		Record record = new ScrapedRecord(fields);

		Field fieldFromUL = new ScrapedField("source", "Autonomous Province of Trento");
		assertThat(record.getFields().contains(fieldFromUL), is(true));

	}

	@Test
	public void extractFieldsFromTableWithHeaders() {
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(ProgramListingPage.getUrl()).tableWithID("lst_indexcfda")).getFields();

		assertThat(fields.size(), greaterThan(0));
		log.info("found fields: {}", fields);
	}

	@Test
	public void extractSelectedFieldsFromTableWithHeaders() {
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(ProgramListingPage.getUrl()).tableWithID("lst_indexcfda")).getFields();

		assertThat(fields.size(), greaterThan(0));
		log.info("found fields: {}", fields);
	}

	@Test
	public void extractSelectedColumnsFromTable() {
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(ProgramListingPage.getUrl()).add(table().withID("lst_indexcfda").columns(2, 3))).getFields();

		assertThat(fields.size(), greaterThan(0));
		log.info("found fields: {}", fields);
	}

	@Test
	public void canSplitLabelAndValueOnClosingTag() {
		List<Field> fields = fieldExtractor.source(html().url(TableWithULs.getUrl())).getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		Record record = new ScrapedRecord(fields);

		Field fieldFromUL = new ScrapedField("Minimum Term", "");
		assertThat(record.getFields().contains(fieldFromUL), is(true));
	}

	@Test
	public void canParseLIWithStrong() {
		String li = "<li><strong> Minimum Term&nbsp;&nbsp;&nbsp;</strong> &nbsp;</li>";

		Source source = new Source(li);
		source.fullSequentialParse();

		String[] parsedOnClosingTag = source.toString().split("</");

		log.info("split on close tag: {} and {}", parsedOnClosingTag[0], parsedOnClosingTag[1]);
		Element liElement = source.getAllElements(HTMLElementName.LI).get(0);
		log.info("li: {}", liElement);
		log.info("li tags: {}", liElement.getAllTags());
		Field field = extractFieldByDetectingTagWrapper(liElement);
		log.info("found field: {}", field);

	}

	@Test
	public void canExtractFieldsFromDivByID() {
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(TableWithULs.getUrl()).divWithID("MainColumn")).getFields();

		assertThat(fields.size(), greaterThan(0));
		log.info("found fields: {}", fields);
	}

	@Test
	public void canExtractFieldsFromTableWithHeadingsOnAlternatingRows() {
		List<Field> fields = new DefaultFieldExtractor()
				.source(html().url(TableWithAlternatingRowsOfHeaders.getUrl()))
				.getFields();
		log.info("found fields: {}", fields);

		assertThat(fields.size(), greaterThan(0));
		assertThat(ScraperUtil.getFieldValue(fields, "REQUEST NO."), is("SPM1C111T5504"));
		assertThat(
				ScraperUtil.getFieldValue(fields, "col0").contains(
						"QUESTIONS REGARDING THE DLA-BSM INTERNET BID BOARD SYSTEM"), is(true));
	}

	@Test
	public void canExtractFieldsFromPairedTags() {
		Field agencyName = new ScrapedField("Agency Name", "Ethiopia USAID-Addis Ababa");
		List<Field> fields = new DefaultFieldExtractor()
				.source(html().url(DetailPage.getUrl()))
				.add(new PairedTags(HTMLElementName.H4, HTMLElementName.DD))
				.getFields();
		log.info("found fields: {}", fields);
		assertThat(fields.contains(agencyName), is(true));
	}

	@Test
	public void canExtractFieldsFromTableAtSpecificOccurrence() {
		List<Field> fields = new DefaultFieldExtractor().source(html().url(DetailPage.getUrl()).table(4)).getFields();
		String cfdaNumbersField = ScraperUtil.getFieldValue(fields, "CFDA Number(s)");
		log.debug("CFDA #s field: {}", cfdaNumbersField);
		assertThat(ScraperUtil.getFieldValue(fields, "Funding Opportunity Number"), is("663-A-08-002"));
		assertThat(
				cfdaNumbersField
						.contains("98.001  --  USAID Foreign Assistance for Programs Overseas;47.049  --  Mathematical"),
				is(true));
	}
	
	@Test
	public void mustTrimTrailingSemiColons(){
		
	}

	@Test
	public void canFindFieldByCSSClass() {
		List<Field> fields = new DefaultFieldExtractor()
				.source(html().url(ListingWithNumberedPaging.getUrl()).ofClass("RecordsDisplay", 0))
				.field("pagingInfo", ElementIdentifierType.cssClass, "RecordsDisplay")
				.getFields();

		Field pagingInfo = new ScrapedField("pagingInfo", "Records 1 thru 75 of 3532 Records.");
		assertThat(fields.contains(pagingInfo), is(true));
		log.info("found fields: {}", fields);

	}

	@Test
	public void canExtractCompoundFieldsIncludingMailto() {
		List<Field> fields = new DefaultFieldExtractor().source(
				html().url(TableWithCompoundContactInfo.getUrl()).ofClass("list", 0)).getFields();

		String firstContact = fields.get(2).getValue();
		log.info("first contact field: {}", firstContact);
		assertThat(firstContact.contains("2103421156"), is(true));

	}

	@Test
	public void willPreserveNewLinesInFields() {
		List<Field> fields = new DefaultFieldExtractor()
				.source(html().url(TableWithAlternatingRowsOfHeaders.getUrl()))
				.getFields();

		String contactInfo = ScraperUtil.getFieldValue(fields, "REQUEST FOR QUOTATION (THIS IS NOT AN ORDER)");
		log.info("contact info: {}", contactInfo);

		assertThat(contactInfo.contains(";"), is(true));

	}

	@Test
	public void willPreserveNewLinesInFieldsWithClean() {
		List<Field> fields = new DefaultFieldExtractor()
				.source(html().url(TableWithAlternatingRowsOfHeaders.getUrl()).clean())
				.getFields();
		
		String contactInfo = ScraperUtil.getFieldValue(fields, "REQUEST FOR QUOTATION (THIS IS NOT AN ORDER)");
		log.info("contact info: {}", contactInfo);

		assertThat(contactInfo.contains(";"), is(true));
		
	}

    @Test
    public void canFindStipulatedFieldsByClass(){
        List<Field> fields = new DefaultFieldExtractor()
                .source(html().url(CareerBuilderDetailPage.getUrl()).clean())
                .field("jobTitle", ElementIdentifierType.cssClass, "job_title")
                .field("jobDescription", ElementIdentifierType.cssClass, "job_desc")
                .field("jobReq", ElementIdentifierType.cssClass, "job_req")
                .getFields();

        log.info("found fields in careerbuilder: {}", fields);

        assertThat(fields.size(), is(529));

    }

    @Test
    public void canPassParsedSourceToExtractor(){
        Source source = html().url(CareerBuilderDetailPage.getUrl()).getSource();

        List<Field> fields = new DefaultFieldExtractor()
                .source(source)
                .field("jobTitle", ElementIdentifierType.cssClass, "job_title")
                .field("jobDescription", ElementIdentifierType.cssClass, "job_desc")
                .field("jobReq", ElementIdentifierType.cssClass, "job_req")
                .getFields();

        log.info("found fields in careerbuilder: {}", fields);

        assertThat(fields.size(), is(526));

    }

	private Field extractFieldByDetectingTagWrapper(Element liElement) {
		Field found = null;
		if (liElement.getAllTags().size() == 4) {
			Tag enclosingTag = liElement.getAllTags().get(1);
			log.info("enclosing tag: {}", enclosingTag);
			log.info("first element of enclosing tag: {}", enclosingTag.getElement().getTextExtractor().toString());
			String tagText = enclosingTag.getElement().getTextExtractor().toString();
			String allText = liElement.getTextExtractor().toString();
			log.info("enclosing tag text starts at: {}", allText.indexOf(tagText));
			log.debug("tagText: {} alltext: {}", tagText, allText);
			if (allText.startsWith(tagText)) {
				found = new ScrapedField(tagText, allText.substring(tagText.length() + 1));
			}
		}

		return found;
	}

    @Test
    public void canGetTagAttributeFields(){
        Source source = html().url(CareerBuilderDetailPage.getUrl()).getSource();

        List<Field> fields = new DefaultFieldExtractor()
                .source(source)
                .add(new DefaultTagAttributeFieldExtractor("meta", "name", "content"))
                .getFields();

        log.info("found fields in careerbuilder: {}", fields);
        Field jobTitle = findFieldByLabel(fields, "job-title");
        assertThat(jobTitle, notNullValue());
        assertThat(jobTitle.getValue(), equalTo("Senior Developer"));

        Field jobContent = findFieldByLabel(fields, "job-content");
        assertThat(jobContent, notNullValue());
        assertThat(jobContent.getValue(), equalTo("Job Content"));
    }

    private Field findFieldByLabel(List<Field> fields, String label) {
        for (Field field : fields) {
            if (label.equals(field.getLabel())) {
                return field;
            }
        }
        return null;
    }

	@Test
	/**
	 * Test that on the page out_of_memory_dice_details.html default field extractor should not fail with OOM error
	 */
	public void canExtractFieldsWOOutOfMemoryError() {
		Source source = html().url(OutOfMemoryDiceDetails.getUrl()).getSource();

		List<Field> fields = new DefaultFieldExtractor()
				.source(html().source(source))
				.add(new DefaultTagAttributeFieldExtractor("meta", "name", "content"))
				.getFields();
		assertThat(fields, not(empty()));
	}

}
