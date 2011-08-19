package com.ontometrics.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.util.ScraperUtil;

public class Scraper {

	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

	private URL url;

	private String tagToGet;

	private int occurrence = 0;

	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public Scraper tag(String tag) {
		this.tagToGet = tag;
		return this;
	}

	public String execute() throws IOException {
		String htmlContent = extractHTMLContent();
		if (tagToGet != null) {
			htmlContent = extractTagContent(htmlContent);
		}
		return htmlContent;
	}

	private String extractTagContent(String htmlContent) {
		return ScraperUtil.extract(htmlContent, tagToGet, occurrence);
	}

	private String extractHTMLContent() throws IOException {
		InputStream is = (InputStream) url.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String htmlContent = sb.toString();
		return htmlContent;
	}

	public Scraper tag(String tag, int occurrence) {
		this.tagToGet = tag;
		this.occurrence = occurrence;
		return this;
	}

}
