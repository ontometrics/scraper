package com.ontometrics.scraper.extraction;

import java.net.URL;

import net.htmlparser.jericho.Source;

public interface SourceExtractor {
	
	Source getSource(URL url);

}
