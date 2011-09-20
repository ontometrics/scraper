package com.ontometrics.scraper;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Source;

/**
 * @author robwilliams
 * 
 */
public enum HtmlSample {

	/**
	 * This is a listing of Grants where each one appears in a line in the table
	 * with a link to a detail page that could be scraped to get the details.
	 */
	PagedListingTable("/testpages/grants-gov-table.html"),

	/**
	 * Goes with the listing above, shows the details on a given Grant, has a
	 * table of values that can be turned into fields.
	 */
	DetailPage("/testpages/grants-gov-detail-page.html"),

	/**
	 * Complex page with sidebar, lots of id-based components, fields with list
	 * values, etc.
	 */
	ProgramDetailPage("/testpages/cfda-program.html"),

	/**
	 * Complex page with sidebar, lots of id-based components, fields with list
	 * values, etc.
	 */
	ProgramListingPage("/testpages/cfda-program-table.html"),

	/**
	 * This page also has information about the total records in the paging
	 * component.
	 */
	ListingWithNumberedPaging("/testpages/listing-with-numbered-paging.html"),

	/**
	 * Just a table with some html inside with links that have text and hrefs.
	 */
	TableOfLinks("/testpages/table-of-links.html"),

	/**
	 * Sometimes a label with multiple values is done as one or more follow on
	 * rows that have no label.
	 */
	TableWithMultipleValuesOnMultipleRows("/testpages/table-with-multiple-values-on-rows.html"),

	/**
	 * Sometimes a label with multiple values is done as one or more follow on
	 * rows that have no label.
	 */
	TableWithULs("/testpages/table-with-uls.html"),

	/**
	 * It's legal to have alternating TRs with THs on one row and TDs in the
	 * other.
	 */
	TableWithAlternatingRowsOfHeaders("/testpages/table-with-alternating-rows-of-headers.html");

	private String path;

	HtmlSample(String path) {
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
