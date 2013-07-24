package com.ontometrics.scraper.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.extraction.ElementIdentifierType;
import com.ontometrics.scraper.extraction.Field;
import com.ontometrics.scraper.extraction.Link;

public class ScraperUtil {

	private static final Logger log = LoggerFactory.getLogger(ScraperUtil.class);

	private ScraperUtil() {
	};

	/**
	 * Get first occurrence of field value
	 * 
	 * @param fields
	 * @param label
	 * @return
	 */
	public static String getFieldValue(List<Field> fields, String label) {
		return getFieldValue(fields, label, 1);
	}

	/**
	 * Occurrence starts at index 1
	 * 
	 * @param fields
	 * @param label
	 * @param occurrence
	 * @return
	 */
	public static String getFieldValue(List<Field> fields, String label, int occurrence) {
		int foundOccurrenceCount = 0;
		String foundValue = null;

		if (fields != null && fields.size() > 0) {
			for (Field field : fields) {
				if (field.getLabel().equalsIgnoreCase(label)) {
					foundOccurrenceCount++;
					if (foundOccurrenceCount == occurrence) {
						foundValue = field.getValue();
						
						// TODO: Need a better method of replacing strange whitespace
						if (foundValue != null && foundValue.contains("Ê")) {
							foundValue = foundValue.replace("Ê", "");
						}
						
						break;
					}
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
		String finalUri = uri.substring(uri.indexOf(paramDelimiter) + 1);
		String found = null;
		String[] parameterSets = finalUri.split("&");
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

	public static String extract(String source, String sourceTag, int occurrence) {
		log.debug("extracting occurrence {} of tag: {} from: {}", new Object[] { occurrence, sourceTag, source });
		String tag = sourceTag.startsWith("<") ? sourceTag : "<" + sourceTag;
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

	public static Element extract(Source source, TagOccurrence tagOccurrence) {
		Element result = null;
		if (tagOccurrence.getElementIdentifierType() == ElementIdentifierType.cssClass) {
			List<Element> elements = source.getAllElementsByClass(tagOccurrence.getIdentifier());
			if(elements != null && !elements.isEmpty())
				result = elements.get(0);
		} else if (tagOccurrence.getElementIdentifierType() == ElementIdentifierType.ID) {
			result = source.getElementById(tagOccurrence.getIdentifier());
		} else {
			List<Element> elements = source.getAllElements(tagOccurrence.getTag());
			if(elements.size() > tagOccurrence.getOccurrence())
				result = elements.get(tagOccurrence.getOccurrence());
		}
		return result;
	}

	public static String extractUsingIdentifier(String html, TagOccurrence tagOccurrence) {
		String result = null;
		Source source = new Source(html);
		source.fullSequentialParse();
		if (tagOccurrence.getElementIdentifierType() == ElementIdentifierType.ID) {
			log.debug("extracting tag by id: {}", tagOccurrence.getIdentifier());
			Element idElement = source.getElementById(tagOccurrence.getIdentifier());
			if (idElement != null) {
				result = idElement.toString();
			} else {
				result = "";
			}
		} else if (tagOccurrence.getElementIdentifierType() == ElementIdentifierType.cssClass) {
			log.debug("extracting: {}", tagOccurrence);
			List<Element> elements = source.getAllElementsByClass(tagOccurrence.getIdentifier());
			if(elements.size() > tagOccurrence.getOccurrence())
				result = elements.get(tagOccurrence.getOccurrence()).toString();
		}
		log.debug("identifier: {}/{} result: {}",
				new Object[] { tagOccurrence.getIdentifier(), tagOccurrence.getElementIdentifierType(), result });
		return result;
	}

	public static String extractTagMatching(String html, TagOccurrence toGet) {
		log.debug("looking for {} in tags: {}", toGet.getMatching(), toGet.getTag());
		String found = null;
		Source source = new Source(html);
		source.fullSequentialParse();
		log.debug("source = {}", source);
		List<Element> elements = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elements) {
			log.debug("this element = {}", element);
			String elementText = element.getTextExtractor().toString();
			if (elementText.contains(toGet.getMatching())) {
				found = element.toString();
				log.debug("found element text containing matching text, found = {}", found);
				break;
			}
		}
		log.debug("found = {}", found);
		return found;
	}

	private static String cleanupTag(String sourceTag) {
		String tag = sourceTag.startsWith("<") ? sourceTag : "<".concat(sourceTag);
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

	public static URL getBaseUrl(URL nextUrl) {
		String baseUrlString = nextUrl.getProtocol() + "://" + nextUrl.getHost();

		String path = nextUrl.getPath();
		if (!StringUtils.isEmpty(path)) {
			int indexOfLastSlash = path.lastIndexOf('/');
			if (indexOfLastSlash != -1) {
				baseUrlString += path.substring(0, indexOfLastSlash);
			}
		}
		if (!baseUrlString.endsWith("/")) {
			baseUrlString += "/";
		}

		URL result = null;
		try {
			result = new URL(baseUrlString);
		} catch (MalformedURLException e) {
			log.error("malformed base url", e);
		}
		return result;
	}

	public static String safeReplaceSemicolonsWithNewLinesWithTrim(String text) {
		if (text == null) {
			return null;
		}

		String[] splitString = text.split(";");

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < splitString.length; i++) {
			stringBuilder.append(splitString[i].trim());
			if (i != splitString.length - 1) {
				stringBuilder.append(System.getProperty("line.separator"));
			}
		}

		return stringBuilder.toString();
	}

	public static boolean isAbsoluteURLString(String urlString) {
		String findResult = getRegexFirstGroupCaseInsensitive("^http[a-z]*:", urlString);
		if (findResult != null) {
			return true;
		}
		return false;
	}

	public static String getBaseUrlString(URL url) {
		String result = MessageFormat.format("{0}://{1}", url.getProtocol(), url.getHost());
		return result;
	}

	public static String getRegexFirstGroupCaseInsensitive(String regex, String string) {
		String result = null;

		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		if (m.find()) {
			result = m.group(0);
		}

		return result;
	}
}