package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.PairedTags;
import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

/**
 * This is the simplest form of an extractor: we just tell the scraper how to
 * identify the fields we want. There is no default behavior, no attempt to
 * extract from tables, or paired tags. Each field must be given as a designated
 * field and the scraper will find it in the page and extract its value.
 * 
 * @author Rob
 */
public class SpecifiedFieldExtractor extends BaseExtractor implements FieldExtractor<SpecifiedFieldExtractor> {

	private Logger log = LoggerFactory.getLogger(SpecifiedFieldExtractor.class);

	private Set<DesignatedField> fieldsToGet;

	public static class Builder {

		private ElementIdentifierType defaultElementIdentifier;
		private Set<DesignatedField> fieldsToGet = new HashSet<DesignatedField>();

		public Builder identifyFieldsByClass() {
			this.defaultElementIdentifier = ElementIdentifierType.cssClass;
			return this;
		}

		public Builder addField(String identifier) {
			this.fieldsToGet.add(new DesignatedField(identifier, new TagOccurrence.Builder()
					.elementIdentifierType(defaultElementIdentifier)
						.identifier(identifier)
						.build()));
			return this;
		}

		public Builder addFields(Collection<String> fields) {
			for (String fieldName : fields) {
				addField(fieldName);
			}
			return this;
		}

		public SpecifiedFieldExtractor build() {
			return new SpecifiedFieldExtractor(this);
		}
	}

	@Override
	public SpecifiedFieldExtractor url(URL url) {
		super.url(url);
		return this;
	}

	@Override
	public SpecifiedFieldExtractor section(HtmlExtractor htmlExtractor) {
		super.section(htmlExtractor);
		return this;
	}

	@Override
	public SpecifiedFieldExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	public SpecifiedFieldExtractor(Builder builder) {
		this.fieldsToGet = builder.fieldsToGet;
	}

	public Set<DesignatedField> getFieldsToGet() {
		return fieldsToGet;
	}

	@Override
	public List<Field> getFields() {
		List<Field> extractedFields = new ArrayList<Field>();

		for (DesignatedField designatedField : this.fieldsToGet) {
			Element elementWithValue = ScraperUtil.extract(getSource(), designatedField.getTagToGetValueFrom());
			String value = elementWithValue.getTextExtractor().toString();
			log.debug("looking for field: {}, value: {}", designatedField.getLabel(), value);
			extractedFields.add(new ScrapedField(designatedField.getLabel(), value));
		}

		return extractedFields;
	}

	@Override
	public SpecifiedFieldExtractor field(String label, String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpecifiedFieldExtractor add(PairedTags pairedTags) {
		// TODO Auto-generated method stub
		return null;
	}

}
