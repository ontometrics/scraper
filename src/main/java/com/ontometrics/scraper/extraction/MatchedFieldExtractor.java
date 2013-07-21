package com.ontometrics.scraper.extraction;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.PairedTags;
import com.ontometrics.scraper.util.ScraperUtil;

/**
 * This is a form of extractor that gets fields having tag name in common. 
 * and it will be extended to get fields having same class Name... 
 * 
 * @author Mina Shafik
 * 
 */
public class MatchedFieldExtractor extends BaseExtractor implements FieldExtractor<MatchedFieldExtractor>{
	
	private Logger log = LoggerFactory.getLogger(MatchedFieldExtractor.class);

	private Set<DesignatedField> fieldsToGet;
	
	private String defaultTagIdentifier;
	
	public static class Builder {

		private Set<DesignatedField> fieldsToGet = new HashSet<DesignatedField>();
		private String defaultTagIdentifier;

		public Builder identifyFieldsByTag(String tag) {
			this.defaultTagIdentifier = tag;
			return this;
		}


		public MatchedFieldExtractor build() {
			return new MatchedFieldExtractor(this);
		}
	}
	
	@Override
	public MatchedFieldExtractor url(URL url) {
		super.url(url);
		return this;
	}
	
	@Override
	public MatchedFieldExtractor section(HtmlExtractor htmlExtractor) {
		super.section(htmlExtractor);
		return this;
	}
	
	@Override
	public MatchedFieldExtractor source(HtmlExtractor htmlExtractor) {
		super.source(htmlExtractor);
		return this;
	}

	public MatchedFieldExtractor(Builder builder) {
		this.fieldsToGet = builder.fieldsToGet;
		this.defaultTagIdentifier= builder.defaultTagIdentifier;
	}

	public Set<DesignatedField> getFieldsToGet() {
		return fieldsToGet;
	}

	@Override
	public List<Field> getFields() {
		List<Field> extractedFields = new ArrayList<Field>();
		List<Element> elementWithValue = ScraperUtil.extractAllTagMatching(getSource().toString(), defaultTagIdentifier);
		int counter =0;
		for (Element element : elementWithValue) {
			String value = element.getTextExtractor().toString();
			extractedFields.add(new ScrapedField(counter+"", value));
			log.debug("looking for field: {}, value: {}", counter,value);
			counter++;
		}

		return extractedFields;
	}

	@Override
	public MatchedFieldExtractor field(String label, String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatchedFieldExtractor add(PairedTags pairedTags) {
		// TODO Auto-generated method stub
		return null;
	}

}
