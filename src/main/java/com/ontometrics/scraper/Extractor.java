package com.ontometrics.scraper;

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
	 * If there is a parameter to be extracted from a link. Eventually, we might
	 * want to make link extraction its own sub-DSL.
	 */
	private String parameter;

	private String matchingPattern;

	public Extractor() {
		this.tagsToGet = new ArrayList<TagOccurrence>();
	}

	public Extractor table(int occurrence) {
		addTagToGet("<table>", occurrence);
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
		addTagToGet("<table>", match);
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
				if (toGet.getOccurrence() > 0) {
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
						if (matchingPattern==null || href.contains(matchingPattern)) {
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

	public Extractor asText() {
		this.outputFormat = OutputFormats.Text;
		return this;
	}

	public Map<String, String> getFields() throws IOException {
		Map<String, String> extractedFields = new HashMap<String, String>();
		// fill in from the already extracted HTML..
		for (TagOccurrence tagOccurrence : this.tagsToGet) {
			if (!tagOccurrence.getTag().contains("table")) {
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
		return extractedFields;
	}

	public Extractor matching(String pattern) {
		this.matchingPattern = pattern;
		return this;
	}

	private String extractTagText(String html, TagOccurrence tagOccurrence) {
		return ScraperUtil.extract(html, tagOccurrence.getTag(), tagOccurrence.getOccurrence());
	}

	private String extractTagMatching(String html, TagOccurrence toGet) {
		log.debug("looking for {} in tags: {}", toGet.getMatching(), toGet.getTag());
		String found = null;
		Source source = new Source(html);
		source.fullSequentialParse();
		List<Element> elements = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elements){
			String elementText = element.getTextExtractor().toString();
			log.debug("element of type {}: {}", element.toString(), elementText);
			if (elementText.contains(toGet.getMatching())){
				log.debug("found our matching tag!");
				found = element.toString();
				break;
			}
		}
		log.debug("returning matching tag: {}", found);
		return found.toString();
	}

	private void addTagToGet(String tag, int i, String match) {
		tagsToGet.add(new TagOccurrence(tag, occurrence, match));
	}

	private void addTagToGet(String tag) {
		addTagToGet(tag, 0);
	}
	
	private void addTagToGet(String tag, String match){
		addTagToGet(tag, 0, match);
	}

	private void addTagToGet(String tag, int occurrence) {
		addTagToGet(tag, occurrence, null);
	}


}
