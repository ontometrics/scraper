package com.ontometrics.scraper.grants;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import com.ontometrics.scraper.TestUtil;

public enum GrantHtmlSample {

	/**
	 * This is a listing of Grants where each one appears in a line in the table
	 * with a link to a detail page that could be scraped to get the details.
	 */
	GrantsnetDetailPage("/testpages/grantsnet-detail-page.html");
	
	private String path;

	GrantHtmlSample(String path) {
		this.path = path;
	}

	public URL getUrl() {
		return TestUtil.getFileAsURL(path);
	}

	public Source getSource() {
		Source source = null;
		try {
			source = new Source(getUrl());
			source.fullSequentialParse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}
}
