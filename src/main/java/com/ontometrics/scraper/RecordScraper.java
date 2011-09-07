package com.ontometrics.scraper;

import java.util.List;

public class RecordScraper extends Scraper {
	
	private List<Record> scrapedRecords;
	
	public List<Record> getRecords(){
		return scrapedRecords;
	}

}
