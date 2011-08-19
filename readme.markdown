## scraper ##

Project for scraping content out of pages and/or feeds. 

The big idea here is to use fluent builder to make a simple scraping DSL. For example, the simplest scraping job would look like this:

    String pageContent = new Scraper.Builder().url("http://www.apple.com").execute();

Chaining calls to do things like stipulate whether you want just text, or if you want any transforms performed, so the above could be changed like this to return just the text (for instance for a classification engine):


    String pageContent = new Scraper.Builder().url("http://www.apple.com").asText().execute();

Further manipulators could be used to do things like direct the scraper to certain elements, e.g. suppose we wanted the 3rd table as HTML and nothing more, something like this:


    String pageContent = new Scraper.Builder().url("http://www.apple.com").tag("table", 3).execute();

And so on.
