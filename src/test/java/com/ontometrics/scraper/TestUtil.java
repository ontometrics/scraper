package com.ontometrics.scraper;

import java.net.URL;

public class TestUtil {

	public static URL getFileAsURL(String path) {
		return TestUtil.class.getResource(path);
	}
}
