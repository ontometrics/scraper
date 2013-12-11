package com.ontometrics.scraper;

import com.ontometrics.scraper.extraction.Field;
import net.htmlparser.jericho.Element;

/**
 * Extractor of the {@link Field} from the document tag
 *
 * TagAttributeFieldExtractor.java
 * Created on 12/11/2013 by Nikolay Chorniy
 */
public interface TagAttributeFieldExtractor {
    /**
     * Identifies element (tag name) to find for in the document
     * @return tag name
     */
    String getTagName();

    /**
     *
     * @param element element (tag)
     * @return Field extracted from the element or null if field information is not available
     */
    Field extractField(Element element);
}
