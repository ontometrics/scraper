package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.OutputFormats;
import com.ontometrics.scraper.PairedTags;
import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

/**
 * Does the work of iteratively extracting portions of the page.
 * <p>
 * Note that once the requested elements are specified, this class is held in
 * the scraper and can be successively invoked for jobs that involve iteration
 * (e.g. paging).
 * 
 * @author Rob
 * 
 */
public class Extractor {

	private static final Logger log = LoggerFactory.getLogger(Extractor.class);

	/**
	 * Raw content source
	 */
	private URL url;

	/**
	 * The type of output we should return upon extraction.
	 */
	private OutputFormats outputFormat = OutputFormats.Html;

	/**
	 * Provides means of extracting just an element with a given id.
	 */
	private String idToGet;

	/**
	 * Provides means of extracting element(s) with a given class.
	 */
	private String classToGet;

	/**
	 * Which occurrence of the tag should we extract? (Remember it is 0 indexed
	 * so 1st would be 0.
	 */
	private int occurrence = 0;

	/**
	 * We collect the desired tags, and whether there is some associate
	 * occurrence, e.g. in the dsl we might want to support calls like:
	 * table(3).row(2).cell(3) to get something out of the 3rd table on the
	 * page, its second row and 3rd cell.
	 */
	private List<TagOccurrence> tagsToGet;

	/**
	 * Provides a means of defining paired tags that contain fields.
	 * <p>
	 * For example, if you have a page where the labels are all wrapped with h4
	 * and the fields with dd, you would add a pair like this:
	 * <p>
	 * <code>
	 * scraper.extractor().pair(HTMLElementName.H4HTMLElementName.DD).getFields()
	 * </code>
	 * <p>
	 * Note: The extractor will extract nothing if the pairs are not contiguous
	 * and matching.
	 */
	private List<PairedTags> fieldPairs;

	/**
	 * If there is a parameter to be extracted from a link. Eventually, we might
	 * want to make link extraction its own sub-DSL. If there is a parameter to
	 * be extracted from a link. Eventually, we might want to make link
	 * extraction its own sub-DSL.
	 */
	private String parameter;

	private String matchingPattern;

	private String afterTag;

	private TagOccurrence afterTagOccurrence;

	public Extractor() {
		this.tagsToGet = new ArrayList<TagOccurrence>();
		this.fieldPairs = new ArrayList<PairedTags>();
	}

	public Extractor table(int occurrence) {
		addTagToGet(HTMLElementName.TABLE, occurrence);
		return this;
	}

	/**
	 * Provides means of matching the table based on a string it contains.
	 * 
	 * @param string
	 *            the string to match, case sensitive
	 * @return the whole table tag with all its contents
	 */
	public Extractor table(String match) {
		addTagToGet(HTMLElementName.TABLE, match);
		return this;
	}

	public Extractor url(URL url) {
		this.url = url;
		return this;
	}

	public Extractor id(String id) {
		this.idToGet = id;
		return this;
	}

	public Extractor ofClass(String className) {
		this.classToGet = className;
		return this;
	}

	public Extractor ofClass(String className, int occurrence) {
		this.classToGet = className;
		this.occurrence = occurrence;
		return this;
	}

	public Extractor links() {
		addTagToGet("href");
		return this;
	}

	public Extractor parameter(String parameterName) {
		this.parameter = parameterName;
		return this;
	}

	/**
	 * Call this method to have the scrape performed and the extractions
	 * returned in the desired format {@link #outputFormat}.
	 * 
	 * @return the items from the {@link #url} that were prescribed by the
	 *         various manipulators
	 * @throws IOException
	 */
	public String execute() throws IOException {
		String result = "";
		Source source = new Source(url);
		source.fullSequentialParse();
		log.debug("parsed source: {}", source.toString());

		if (idToGet != null) {
			result = source.getElementById(idToGet).getTextExtractor().toString();
		} else if (classToGet != null) {
			List<Element> classElements = source.getAllElementsByClass(classToGet);
			if (occurrence > 0) {
				result = classElements.get(occurrence).getTextExtractor().toString();
			} else {
				for (Element element : classElements) {
					result += element.getTextExtractor().toString();
				}
			}
		} else {
			if (outputFormat == OutputFormats.Text) {
				result = source.getTextExtractor().toString();
			} else if (outputFormat == OutputFormats.Html) {
				result = source.toString();
			}
			for (TagOccurrence toGet : tagsToGet) {
				if (toGet.getMatching() != null) {
					result = extractTagMatching(source.toString(), toGet);
				} else {
					result = extractTagText(source.toString(), toGet);
				}
			}

		}

		return result;
	}

	/**
	 * Prunes the raw URL content down based on the manipulators that have been
	 * used to specify subelements, then extracts links and potentially
	 * parameters from them.
	 * 
	 * @return a set of extracted elements
	 * @throws IOException
	 */
	public List<String> getResults() throws IOException {
		log.debug("extracting results from url: {}", url);
		List<String> results = new ArrayList<String>();
		Source source = new Source(url);
		source.fullSequentialParse();
		String content = source.toString();
		List<Element> currentElements = null;
		for (TagOccurrence toGet : tagsToGet) {
			log.debug("toGet = {}", toGet);
			if (toGet.getOccurrence() > 0) {
				content = extractTagText(content, toGet);
			} else if (toGet.getMatching() != null) {
				content = extractTagMatching(source.toString(), toGet);
			} else if (toGet.getTag().equals("href")) {
				source = new Source(content);
				currentElements = source.getAllElements(HTMLElementName.A);
				for (Element element : currentElements) {
					String href = element.getAttributeValue("href");
					if (href != null) {
						if (matchingPattern == null || href.contains(matchingPattern)) {
							results.add(href);
						}
					}
				}
			}
		}
		if (this.parameter != null) {
			List<String> foundIds = new ArrayList<String>();
			for (String foundLink : results) {
				String found = ScraperUtil.extractParameter(foundLink, parameter);
				if (found != null) {
					foundIds.add(found);
				}
			}
			results = foundIds;
		}
		return results;
	}

	private Source pruneFrom(Source source, TagOccurrence afterTagOccurrence) {
		String sourceHtml = source.toString();
		String endAfterTag = "</" + afterTagOccurrence.getTag() + ">";
		for (int i = 0; i < afterTagOccurrence.getOccurrence(); i++) {
			sourceHtml = sourceHtml.substring(sourceHtml.indexOf(endAfterTag) + 1);
		}
		log.debug("pruned after count {} of tag {}: {}", new Object[] { afterTagOccurrence.getOccurrence(),
				afterTagOccurrence.getTag(), sourceHtml });
		String afterSource = sourceHtml;
		source = new Source(afterSource);
		source.fullSequentialParse();
		return source;
	}

	public Extractor asText() {
		this.outputFormat = OutputFormats.Text;
		return this;
	}

	public Map<String, String> getFields() throws IOException {
		Map<String, String> extractedFields = new HashMap<String, String>();
		// fill in from the already extracted HTML..
		for (TagOccurrence tagOccurrence : this.tagsToGet) {
			if (!tagOccurrence.getTag().contains(HTMLElementName.TABLE)) {
				throw new IllegalStateException("Only know how to extract fields from tables.");
			} else {
				Source source = new Source(url);
				source.fullSequentialParse();
				String tableText = extractTagText(source.toString(), tagOccurrence);
				source = new Source(tableText);
				source.fullSequentialParse();
				log.debug("about to peel fields from this table: {}", source.toString());

				List<Element> cells = source.getAllElements(HTMLElementName.TD);
				for (int i = 0; i < cells.size(); i++) {
					String label = cells.get(i).getTextExtractor().toString().trim().replaceAll(":$", "");
					String value = cells.get(++i).getTextExtractor().toString().trim();
					extractedFields.put(label, value);
				}
			}
		}
		Source source = new Source(url);
		source.fullSequentialParse();
		if (this.afterTagOccurrence != null) {
			source = pruneFrom(source, afterTagOccurrence);
		}
		for (PairedTags tagPair : this.fieldPairs) {
			List<Element> labels = source.getAllElements(tagPair.getLabelTag());
			List<Element> fields = source.getAllElements(tagPair.getFieldTag());

			removeInvalidFields(fields);

			int fieldCount = Math.min(labels.size(), fields.size());
			for (int i = 0; i < fieldCount; i++) {
				String label = labels.get(i).getTextExtractor().toString().trim();
				String field = fields.get(i).getTextExtractor().toString().trim();
				log.debug("outputting pair: {} : {}", label, field);
				extractedFields.put(label, field);
			}

		}
		return extractedFields;
	}

	private void removeInvalidFields(List<Element> fields) {
		java.util.Iterator<Element> iterator = fields.iterator();
		while (iterator.hasNext()) {
			Element field = iterator.next();
			if (!isAField(field.toString())) {
				log.debug("pruning invalid field: {}", field);
				iterator.remove();
			}
		}
	}

	private boolean isAField(String extract) {
		return !extract.contains(HTMLElementName.TABLE);
	}

	public Extractor matching(String pattern) {
		this.matchingPattern = pattern;
		return this;
	}

	private String extractTagText(String html, TagOccurrence tagOccurrence) {
		String result = null;

		if (StringUtil.isBlank(tagOccurrence.getMatching())) {
			result = ScraperUtil.extract(html, tagOccurrence.getTag(), tagOccurrence.getOccurrence());
		} else {
			result = extractTagMatching(html, tagOccurrence);
		}

		return result;
	}

	private String extractTagMatching(String html, TagOccurrence toGet) {
		log.debug("looking for {} in tags: {}", toGet.getMatching(), toGet.getTag());
		String found = null;
		Source source = new Source(html);
		source.fullSequentialParse();
		List<Element> elements = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elements) {
			String elementText = element.getTextExtractor().toString();
			if (elementText.contains(toGet.getMatching())) {
				found = element.toString();
			}
		}
		return found.toString();
	}

	private void addTagToGet(String tag, int occurrence, String match) {
		tagsToGet.add(new TagOccurrence(tag, occurrence, match));
	}

	private void addTagToGet(String tag) {
		addTagToGet(tag, 0);
	}

	private void addTagToGet(String tag, String match) {
		addTagToGet(tag, 0, match);
	}

	private void addTagToGet(String tag, int occurrence) {
		addTagToGet(tag, occurrence, null);
	}

	public Extractor pair(String labelTag, String fieldTag) {
		this.fieldPairs.add(new PairedTags(labelTag, fieldTag));
		return this;
	}

	public Extractor after(String afterTag) {
		this.afterTag = afterTag;
		return this;
	}

	public Extractor after(String tag, int occurrence) {
		this.afterTagOccurrence = new TagOccurrence(tag, occurrence);
		return this;
	}

}
