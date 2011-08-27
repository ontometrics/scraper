package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import com.ontometrics.scraper.TagOccurrence;
import com.ontometrics.scraper.util.ScraperUtil;

public class LinkExtractor implements HtmlExtractor {
	
	private Source source;
	
	private ExtractionAgent extractionAgent;

	private URL url;

	public LinkExtractor url(URL url){
		this.url = url;
		return this;
	}	
	
	public List<Link> getLinks(){
		try {
			source = new Source(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		source.fullSequentialParse();
		extractionAgent.execute(source);
		return ScraperUtil.extractLinks(source.toString());
	}
	
	public LinkExtractor table(int occurrence){
		extractionAgent = new ElementExtractor(new TagOccurrence(HTMLElementName.TABLE, occurrence));
		return this;
	}

}
