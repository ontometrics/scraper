package com.ontometrics.scraper;

import java.util.ArrayList;
import java.util.List;

public class RecordScraper extends Scraper {
	
	private List<Record> scrapedRecords = new ArrayList<Record>();
	
	protected void addRecord(Record newRecord){
		scrapedRecords.add(newRecord);
	}
	
	public List<Record> getRecords(){
		return scrapedRecords;
	}

}
