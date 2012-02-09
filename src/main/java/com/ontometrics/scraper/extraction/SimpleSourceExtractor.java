package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSourceExtractor implements SourceExtractor {

	private Logger log = LoggerFactory.getLogger(SimpleSourceExtractor.class);

	@Override
	public Source getSource(URL url) {
		Source source = null;
		try {
			source = new Source(url);
		} catch (IOException e) {
			log.error("Error extracting source", e);
		}
		return source;
	}

}
