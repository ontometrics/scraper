/*package com.ontometrics.scraper;


public class ListingDetailScraperTest {

	private static final Logger log = LoggerFactory.getLogger(ListingDetailScraperTest.class);

	private static final String listingTableKeyword = "Opportunity Title";
	private static final String linkPattern = "mode=VIEW";

	@Test
	public void canExtractLinksFromListingPage() {
		List<Link> foundLinks = new LinkExtractor()
				.source(html().url(PagedListingTable.getUrl()))
				.getLinks();

		log.info("found {} links: {}", foundLinks.size(), foundLinks);
		assertThat(foundLinks.size(), is(greaterThan(0)));
	}

	@Test
	public void canExtractLinksFromMultiplePagesThenFollowToDetailsPage() throws MalformedURLException {
		Iterator pageIterator = new Iterator() {

			List<Link> foundLinks = new LinkExtractor()
                                                .source(html().url(PagedListingTable.getUrl()))
                                                .getLinks();
                        ListIterator foundLinksItertator= foundLinks.listIterator();
                        
			@Override
			public URL next() {
                            Link currentLink =(Link)foundLinksItertator.next();                            
                            log.debug("current iterating page = {}", currentLink.getHref());
                            
                            String CurrentURLStr = PagedListingFolder.getPath() +"/"+currentLink.getHref();
                            URL currentURL = TestUtil.getFileAsURL(CurrentURLStr);
                            
                            return currentURL; 
			}

			@Override 
			public boolean hasNext() {
                            
                            try {
                                Link nextLink =(Link)foundLinks.get(foundLinksItertator.nextIndex()); 
                                log.debug("next page to iterate = {}", nextLink.getHref());
                            } catch (Exception e) {                                
                               log.debug("Page iterator can't peek next link");
                            }
                                      
                            return foundLinksItertator.hasNext();
			}

			@Override
			public URL getBaseUrl() {
				// TODO Auto-generated method stub
				return PagedListingFolder.getUrl();
			}
		};
                HtmlExtractor htmlExtractor = html().url(PagedListingTable.getUrl());
		
                List<Record> records = new ListingDetailScraper()
				.setConvertURLs(true)
				.iterator(pageIterator)
				.listing(new LinkExtractor().source(htmlExtractor))
				.details(new DefaultFieldExtractor().source(htmlExtractor))
				.getRecords();

		assertThat(records.size(), greaterThan(0)); 
											
		log.debug("fields = {}", records);

	}

}
*/