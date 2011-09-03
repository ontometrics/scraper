package com.ontometrics.scraper.extraction;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFieldExtractor extends BaseExtractor implements FieldExtractor<DefaultFieldExtractor> {

	private static final Logger log = LoggerFactory.getLogger(DefaultFieldExtractor.class);

	@Override
	public List<Field> getFields() {
		List<Field> extractedFields = new ArrayList<Field>();
		List<Element> tables = getSource().getAllElements(HTMLElementName.TABLE);

		for (Element table : tables) {
			extractedFields.addAll(extractFieldsFromTable(table.toString()));
		}

		List<Element> dls = getSource().getAllElements(HTMLElementName.DL);

		for (Element dt : dls) {
			extractedFields.addAll(extractFieldsFromDL(dt.toString()));
		}

		return extractedFields;
	}
	
	public DefaultFieldExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	private List<Field> extractFieldsFromTable(String html) {
		// log.debug("extracting fields from table: {}", html);
		List<Field> extractedFields = new ArrayList<Field>();
		Source source = new Source(html);
		source.fullSequentialParse();
		List<Element> cells = source.getAllElements(HTMLElementName.TD);
		int rows = source.getAllElements(HTMLElementName.TR).size();
		log.debug("found {} cells in {} rows", cells.size(), rows);
		if (cells.size() == (rows * 2)) {
			Field lastField = null;
			log.debug("cells.size: {}", cells.size());
			for (int i = 0; i < cells.size(); i++) {
				Element labelElement = cells.get(i);
				Element valueElement = cells.get(++i);
				String label = labelElement.getTextExtractor().toString().trim().replaceAll(":$", "");
				String value = getValueFieldText(valueElement);
				log.debug("found field: {}={}", label, value);
				if (StringUtils.isEmpty(label) && lastField != null) {
					lastField.addValue(value);
				} else {
					lastField = new ScrapedField(label, value);
					extractedFields.add(lastField);
				}
			}
		}
		return extractedFields;
	}

	private List<Field> extractFieldsFromDL(String html) {
		List<Field> extractedFields = new ArrayList<Field>();
		Source source = new Source(html);
		source.fullSequentialParse();
		List<Element> labels = source.getAllElements(HTMLElementName.DT);
		List<Element> values = source.getAllElements(HTMLElementName.DD);
		int cellCount = Math.min(labels.size(), values.size());
		for (int i = 0; i < cellCount; i++) {
			String label = labels.get(i).getTextExtractor().toString().trim().replaceAll(":$", "");
			Element valueElement = values.get(i);
			log.debug("looking at value element: {}", valueElement);
			String value = getValueFieldText(valueElement);
			extractedFields.add(new ScrapedField(label, value));
		}
		return extractedFields;
	}

	private String getValueFieldText(Element valueElement) {
		String result = "";
		List<Element> subElements = valueElement.getAllElements(HTMLElementName.A);
		if (subElements.size() > 0) {
			log.debug("found a tag inside field!");
			for (Element element : subElements) {
				if (element.getName().equals(HTMLElementName.A) && element.getAttributeValue("href").contains("mailto")) {
					log.debug("found mailto");
					result = element.getAttributeValue("href");
				} else {
					result = element.getTextExtractor().toString();
				}
			}
		} else {
			result = valueElement.getTextExtractor().toString();
		}
		return result;
	}

}
