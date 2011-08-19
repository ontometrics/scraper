package com.ontometrics.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.util.ScraperUtil;

public class Scraper {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

	private URL url;

	private String tagToGet;

	private int occurrence = 0;

	private OutputFormats outputFormat;

	public String execute() throws IOException {
		String result = "";
		Source source = new Source(url);
		source.fullSequentialParse();

		if (outputFormat == OutputFormats.Text) {
			result = source.getTextExtractor().toString();
		} else if (outputFormat == OutputFormats.Html) {
			result = source.toString();
		}
		
		if (tagToGet != null){
			result = extractTagContent(result);
		}
		return result;
	}

	public Scraper asHtml() {
		this.outputFormat = OutputFormats.Html;
		return this;
	}

	public Scraper asText() {
		this.outputFormat = OutputFormats.Text;
		return this;
	}
	
	// ---- Builder-style Interface
	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public Scraper tag(String tag) {
		this.tagToGet = tag;
		return this;
	}

	private String extractTagContent(String htmlContent) {
		return ScraperUtil.extract(htmlContent, tagToGet, occurrence);
	}

	public Scraper() {
		outputFormat = OutputFormats.Html;
	}

	public Scraper tag(String tag, int occurrence) {
		this.tagToGet = tag;
		this.occurrence = occurrence;
		return this;
	}
}
