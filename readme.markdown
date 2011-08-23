## scraper ##

Project for scraping content out of pages and/or feeds. 

The big idea here is to use fluent builder to make a simple scraping DSL. For example, the simplest scraping job would look like this:

    String pageContent = new Scraper.Builder().url("http://www.apple.com").getResult();

Chaining calls to do things like stipulate whether you want just text, or if you want any transforms performed, so the above could be changed like this to return just the text (for instance for a classification engine):


    String pageContent = new Scraper.Builder().url("http://www.apple.com").asText().getResult();

Further manipulators could be used to do things like direct the scraper to certain elements, e.g. suppose we wanted the 3rd table as HTML and nothing more, something like this:

	List<String> urls = scraper
		.url(testTableHtmlUrl)
		.extract(scraper.extractor().table(3).links().getResults())
		.getResults();

Here is a more advanced case. We want to get the 3rd table, extract the links from it, then get the value of the parameter oppId from each link:

	List<String> ids = scraper
		.url(testTableHtmlUrl)
		.extract(scraper.extractor().table(3).links().parameter("oppId").getResults())
		.getResults();

Note that the keys are collected in the getResults() method in the extractor, but another getResults() call is needed in the scraper because we might have to iterate, in which case each page would have an extraction and the results would be collected.


### Iteration ###

In a lot of cases, you want to scrape something from a page, but the same form is repeated on multiple pages. To support that, we have the notion of an iterator. The scraper will call the iterator each time it's ready for a new page. All the iterator has to do is construct the URL for the next page. Like this:

		Scraper scraper = new Scraper();
		Iterator pageIterator = new Iterator() {
			@Override
			public URL build(int i) {
				String nextPageUrl = MessageFormat.format("/testpages/ids-page-{0}.html", i + 2);
				log.debug("next page to iterate to: {}", nextPageUrl);
				return TestUtil.getFileAsURL(nextPageUrl);
			}
		};
		List<String> ids = scraper
				.url(testTableHtmlUrl)
				.pages(1)
				.iterator(pageIterator)
				.extract(scraper.extractor().table(3).links().parameter("oppId").getResults())
				.getResults();

		assertThat(ids.size(), is(86));

Notice that we are constraining the iteration with the pages method. We probably want to support an open-ended iteration where the scraper will keep trying to get more pages until it gets a 404 and then it will exit. This is necessary because we may not know how many pages there are and pages may be added at some point. (Implementing this is not very difficult: inside the scraper, it sets up the extractor, gets the results, then checks if there is an iterator and if there is, it calls it in a loop, collecting all the results.)


## Listing and Detail: Following Links to a Detail Page ##

Another common scenario is that you have a set of links that you have to follow to a detail page where the actual content is that you want to scrape. That's what this syntax is meant to support. Here is an example:

	@Test
	public void useIteratedListingAndDetailInterface() throws IOException {
		Scraper scraper = new Scraper();
		Iterator pageIterator = new Iterator() {
			@Override
			public URL build(int i) {
				String nextPageUrl = MessageFormat.format("/testpages/ids-page-{0}.html", i + 2);
				log.debug("next page to iterate to: {}", nextPageUrl);
				return TestUtil.getFileAsURL(nextPageUrl);
			}
		};
		Scraper detailScraper = new Scraper();
		List<Map<String, String>> records = scraper
				.url(testTableHtmlUrl)
				.pages(3)
				.iterator(pageIterator)
				.listing(scraper.extractor().table(3).links().getResults())
				.detail(detailScraper)
				.getRecords();

		assertThat(records.size(), is(greaterThan(0)));
		log.debug("fields = {}", records);

	}

Notice that we have to have a separate scraper for extracting the details.
