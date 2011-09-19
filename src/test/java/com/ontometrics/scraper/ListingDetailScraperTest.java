package com.ontometrics.scraper;

import static com.ontometrics.scraper.HtmlSample.PagedListingTable;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.extraction.DefaultFieldExtractor;
import com.ontometrics.scraper.extraction.Link;
import com.ontometrics.scraper.extraction.LinkExtractor;

public class ListingDetailScraperTest {

	private static final Logger log = LoggerFactory.getLogger(ListingDetailScraperTest.class);

	private static final String listingTableKeyword = "Opportunity Title";
	private static final String linkPattern = "mode=VIEW";

	@Test
	public void canExtractLinksFromListingPage() {
		List<Link> foundLinks = new LinkExtractor()
				.source(html().url(PagedListingTable.getUrl()).table().matching(listingTableKeyword))
				.matching(linkPattern)
				.getLinks();

		log.info("found {} links: {}", foundLinks.size(), foundLinks);
		assertThat(foundLinks.size(), is(greaterThan(0)));
	}

	@Test
	public void canExtractLinksFromMultiplePagesThenFollowToDetailsPage() throws MalformedURLException {
		Iterator pageIterator = new Iterator() {

			private int startingPage = 1;

			private int currentPage = startingPage;

			private int numberOfPages = 2;

			private URL nextUrl;

			@Override
			public URL next() {
				currentPage++;
				return nextUrl;
			}

			@Override
			public boolean hasNext() {
				String nextPageUrl = MessageFormat.format("/testpages/ids-page-{0}.html", currentPage);
				log.debug("next page to iterate to: {}", nextPageUrl);
				nextUrl = TestUtil.getFileAsURL(nextPageUrl);
				return currentPage < startingPage + numberOfPages;
			}

			@Override
			public URL getBaseUrl() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		List<Record> records = new ListingDetailScraper()
				.setConvertURLs(false)
				.iterator(pageIterator)
				.listing(
						new LinkExtractor().source(
								html().url(PagedListingTable.getUrl()).table().matching(listingTableKeyword)).matching(
								linkPattern))
				.details(new DefaultFieldExtractor())
				.getRecords();

		assertThat(records.size(), is(0)); // this is not going to find any
											// records because the URLs are all
											// invalid
		log.debug("fields = {}", records);

	}

}
