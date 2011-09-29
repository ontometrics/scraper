package com.ontometrics.scraper;

import java.net.URL;

/**
 * Base class, meant to be extended for specific Builder requirements (e.g.
 * extracting Fields or Records).
 * 
 * @author Rob
 * 
 */
public class Scraper {
	
	public static final String sessionIdKeyword = "$SESSION_ID$";;
	
	/**
	 * Used for relative links. At the moment, this will be just the host name.
	 */
	private URL baseUrl;

	/**
	 * For the scraper to be able to follow links, it's sometimes necessary to
	 * carry forward the session ID (if the site uses URLRewriting).
	 */
	private String sessionIDName;

	public Scraper sessionIDName(String sessionIDName) {
		this.sessionIDName = sessionIDName;
		return this;
	}

	public String getSessionIDName() {
		return sessionIDName;
	}
	
	public URL getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected boolean isRelativeUrl(String url) {
		boolean result = false;
		if (url != null && !url.startsWith("http://")) {
			result = true;
		}
		return result;
	}

	protected String convertToAbsoluteUrl(String link) {
		String absoluteUrlString = (getBaseUrl().toString())+"/"+link;
		return absoluteUrlString;
	}

}
