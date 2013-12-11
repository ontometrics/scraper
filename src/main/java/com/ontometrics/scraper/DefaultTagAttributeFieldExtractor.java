package com.ontometrics.scraper;

import com.ontometrics.scraper.extraction.Field;
import com.ontometrics.scraper.extraction.ScrapedField;
import net.htmlparser.jericho.Element;
import org.apache.commons.lang.StringUtils;

/**
 * Extractor of {@link Field} from the tag
 *
 * DefaultTagAttributeFieldExtractor.java
 * Created on 12/11/2013 by Nikolay Chorniy
 */
public class DefaultTagAttributeFieldExtractor implements TagAttributeFieldExtractor {
    //tag name to process
    private String tagName;

    //field label attribute
    private String labelAttribute;

    //field value attribute
    private String valueAttribute;

    public DefaultTagAttributeFieldExtractor(String tagName, String labelAttribute, String valueAttribute) {
        this.tagName = tagName;
        this.labelAttribute = labelAttribute;
        this.valueAttribute = valueAttribute;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public Field extractField(Element element) {
        String label = element.getAttributeValue(labelAttribute);
        String value = element.getAttributeValue(valueAttribute);
        if (StringUtils.isEmpty(label) || StringUtils.isEmpty(value)) {
            return null;
        }
        return new ScrapedField(label, value);
    }
}
