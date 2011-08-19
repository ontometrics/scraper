package com.ontometrics.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Scraper {

	private URL url;

	public Scraper url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public String execute() throws IOException {
		InputStream is = (InputStream) url.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String htmlContent = sb.toString();
		return htmlContent;
	}

	public URL getUrl() {
		return url;
	}

}
