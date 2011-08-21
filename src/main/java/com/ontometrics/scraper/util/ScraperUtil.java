package com.ontometrics.scraper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScraperUtil {

	private static final Logger log = LoggerFactory.getLogger(ScraperUtil.class);

	public static String extractParameter(String uri, String parameter) {
		String found = null;
		uri = uri.substring(uri.indexOf("?") + 1);
		String[] parameterSets = uri.split("&");
		log.debug("about to extract parameter: {} from {}", parameter, uri);
		for (String parameterSet : parameterSets) {
			String[] pnv = parameterSet.split("=");
			if (pnv.length == 2) {
				if (pnv[0].equals(parameter)) {
					found = pnv[1];
				}
			}
		}
		return found;
	}

	public static String extract(String source, String tag, int occurrence) {
		log.debug("extracting {} occurrence of tag: {} from source: {}", new Object[] { occurrence, tag, source });
		tag = (tag.endsWith(">")) ? tag.substring(0, tag.length() - 1) : tag;
		String endTag = "</" + tag.substring(1);
		String[] tags = source.split(endTag);

		for (int i = 0; i < tags.length; i++) {
			tags[i] = tags[i] + endTag;
		}
		int begin = tags[occurrence].indexOf(tag);
		int length = tags[occurrence].length();
		log.debug("occurrence {} at {} to {}", new Object[] { occurrence, begin, length });
		return tags[occurrence].substring(begin, length);
	}
}
