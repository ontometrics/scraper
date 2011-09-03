package com.ontometrics.scraper.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.extraction.Field;
import com.ontometrics.scraper.extraction.Link;

public class ScraperUtil {

	private static final Logger log = LoggerFactory.getLogger(ScraperUtil.class);

	public static String getFieldValue(List<Field> fields, String label) {
		String foundValue = null;
		if (fields != null && fields.size() > 0) {
			for (Field field : fields) {
				if (field.getLabel().equalsIgnoreCase(label)) {
					foundValue = field.getValue();
					break;
				}
			}
		}
		return foundValue;
	}
	
	public static List<Link> extractLinks(String sourceToParse) {
		Source source = new Source(sourceToParse);
		source.fullSequentialParse();
		List<Link> links = new ArrayList<Link>();
		List<Element> as = source.getAllElements(HTMLElementName.A);
		for (Element linkElement : as) {
			links.add(new Link(linkElement.getTextExtractor().toString(), linkElement.getAttributeValue("href")));
		}
		return links;
	}

	public static String extractParameter(String uri, String parameter) {
		String paramDelimiter = (uri.contains("?")) ? "?" : ";";
		String found = null;
		uri = uri.substring(uri.indexOf(paramDelimiter) + 1);
		String[] parameterSets = uri.split("&");
		for (String parameterSet : parameterSets) {
			String[] pnv = parameterSet.split("=");
			if (pnv.length == 2) {
				if (pnv[0].equals(parameter)) {
					found = pnv[1];
				}
			}
		}
		log.debug("Extracted value '{}' for '{}'", found, parameter);
		return found;
	}

	public static String extract(String source, String tag, int occurrence) {
		log.debug("extracting {} occurrence of tag: {}", occurrence, tag);
		tag = tag.startsWith("<") ? tag : "<" + tag;
		tag = (tag.endsWith(">")) ? tag.substring(0, tag.length() - 1) : tag;
		String endTag = "</" + tag.substring(1) + ">";
		log.debug("extracting using tags: {} and {}", tag, endTag);
		String[] tags = source.split(endTag);

		for (int i = 0; i < tags.length; i++) {
			tags[i] = tags[i] + endTag;
		}
		int begin = tags[occurrence].indexOf(tag);
		int length = tags[occurrence].length();
		log.debug("occurrence {} at {} to {}", new Object[] { occurrence, begin, length });
		// log.debug("returning: {}", tags[occurrence].substring(begin,
		// length));
		return tags[occurrence].substring(begin, length);
	}

	public static String extract(String source, TagOccurrence tagOccurrence) {
		log.debug("extracting: {}", tagOccurrence);
		int occurrence = tagOccurrence.getOccurrence();
		String tag = cleanupTag(tagOccurrence.getTag());
		String endTag = "</" + tag.substring(1) + ">";
		String[] tags = source.split(endTag);

		for (int i = 0; i < tags.length; i++) {
			tags[i] = tags[i] + endTag;
		}
		int begin = tags[occurrence].indexOf(tag);
		int length = tags[occurrence].length();
		log.debug("occurrence {} at {} to {}", new Object[] { occurrence, begin, length });
		return tags[occurrence].substring(begin, length);
	}

	public static String extractTagMatching(String html, TagOccurrence toGet) {
		log.debug("looking for {} in tags: {}", toGet.getMatching(), toGet.getTag());
		String found = null;
		Source source = new Source(html);
		source.fullSequentialParse();
		List<Element> elements = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elements) {
			String elementText = element.getTextExtractor().toString();
			if (elementText.contains(toGet.getMatching())) {
				found = element.toString();
				break;
			}
		}
		return found.toString();
	}

	private static String cleanupTag(String tag) {
		tag = tag.startsWith("<") ? tag : "<" + tag;
		tag = (tag.endsWith(">")) ? tag.substring(0, tag.length() - 1) : tag;
		return tag;
	}

	public static String extractSessionId(URL url, String sessionIDName) throws IOException {
		String sessionID = null;
		Source source = new Source(url);
		source.fullSequentialParse();
		List<Element> links = source.getAllElements(HTMLElementName.A);
		for (Element link : links) {
			// log.info("link: {}", link.toString());
			String href = link.getAttributeValue("href");
			if (href != null && href.contains(sessionIDName)) {
				sessionID = extractParameter(href, sessionIDName);
				if (sessionID != null) {
					break;
				}
			}
		}
		return sessionID;
	}

	public static Map<String, String> createFieldMap(List<Field> fields) {
		Map<String, String> fieldMap = new HashMap<String, String>();
		for (Field field : fields) {
			fieldMap.put(field.getLabel(), field.getValue());
		}
		return fieldMap;
	}
}
