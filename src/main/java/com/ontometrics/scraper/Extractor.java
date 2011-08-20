package com.ontometrics.scraper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.util.ScraperUtil;

public class Extractor {

	private static final Logger log = LoggerFactory.getLogger(Extractor.class);

	private URL url;

	private int occurrence;

	private List<TagOccurrence> tagsToGet;

	private String parameter;

	public Extractor() {
		this.tagsToGet = new ArrayList<TagOccurrence>();
	}

	public Extractor table(int occurrence) {
		addTagToGet("<table>", occurrence);
		return this;
	}

	private void addTagToGet(String tag) {
		addTagToGet(tag, 0);
	}

	private void addTagToGet(String tag, int occurrence) {
		tagsToGet.add(new TagOccurrence(tag, occurrence));
	}

	public Extractor url(URL url) {
		this.url = url;
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

	public List<String> getResults() throws IOException {
		List<String> results = new ArrayList<String>();
		Source source = new Source(url);
		source.fullSequentialParse();
		String content = source.toString();
		List<Element> currentElements = null;
		for (TagOccurrence toGet : tagsToGet) {
			if (toGet.getOccurrence() > 0) {
				content = extractTagText(content, toGet);
			} else if (toGet.getTag().equals("href")) {
				source = new Source(content);
				currentElements = source.getAllElements(HTMLElementName.A);
			}
		}
		if (this.parameter != null) {
			for (Element element : currentElements) {
				String found = extractParameter(element.toString(), parameter);
				if (found != null) {
					results.add(found);
				}
			}
		}

		return results;
	}

	private String extractParameter(String uri, String parameter) {
		String found = null;
		uri = uri.substring(uri.indexOf("?") + 1);
		String[] parameterSets = uri.split("&");
		log.debug("about to extract parameter: {} from {}", parameter, uri);
		for (String parameterSet : parameterSets) {
			String[] pnv = parameterSet.split("=");
			if (pnv.length == 2) {
				log.debug("parameter: {}={}", pnv[0], pnv[1]);
				if (pnv[0].equals(parameter)) {
					log.debug("found one!: {}", pnv[1]);
					found = pnv[1];
				}
			}
		}
		return found;
	}

	private String extractTagText(String html, TagOccurrence tagOccurrence) {
		return ScraperUtil.extract(html, tagOccurrence.getTag(), tagOccurrence.getOccurrence());
	}

}
