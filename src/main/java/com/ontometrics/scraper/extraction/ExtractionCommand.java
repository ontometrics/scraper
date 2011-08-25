package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Source;

public interface ExtractionCommand {

	public void execute(Source source);
	
}
