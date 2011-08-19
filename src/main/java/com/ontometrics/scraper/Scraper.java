package com.ontometrics.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scraper {
	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

	private URL url;

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

	// ---- Fundamental Stuff
	public Scraper() {
		outputFormat = OutputFormats.Html;
	}

	public URL getUrl() {
		return url;
	}
}
