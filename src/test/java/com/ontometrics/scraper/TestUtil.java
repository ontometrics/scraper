package com.ontometrics.scraper;

import java.net.URL;

import org.junit.Ignore;

@Ignore
public class TestUtil {

	public static URL getFileAsURL(String path) {
		return TestUtil.class.getResource(path);
	}
}
