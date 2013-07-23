package com.ontometrics.scraper;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class, meant to be extended for specific Builder requirements (e.g.
 * extracting Fields or Records).
 * 
 * @author Rob
 * 
 */
public class Scraper {

	public static final String sessionIdKeyword = "$SESSION_ID$";
	
	private static final Logger log = LoggerFactory.getLogger(Scraper.class);

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
		URL baseUrl = getBaseUrl();
		StringBuffer absoluteUrlStringBuffer = new StringBuffer(baseUrl.getHost());
		if(baseUrl.getPort() > 0) {
			absoluteUrlStringBuffer.append(':').append(getBaseUrl().getPort());
		}
		if(!link.startsWith("/") && baseUrl.getPath() != null && !baseUrl.getPath().isEmpty()) {
			absoluteUrlStringBuffer.append(baseUrl.getPath());
		}
		absoluteUrlStringBuffer.append('/').append(link);
		String absoluteUrlString = baseUrl.getProtocol() + "://" + 
				absoluteUrlStringBuffer.toString().replaceAll("(/){2,}+", "/");
		log.debug("absolute url: {}", absoluteUrlString);
		return absoluteUrlString;
	}

}

