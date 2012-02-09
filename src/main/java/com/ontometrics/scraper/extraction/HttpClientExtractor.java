package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientExtractor implements SourceExtractor {

	private Logger log = LoggerFactory.getLogger(HttpClientExtractor.class);

	private HttpClient client;

	private ResponseHandler<String> responseHandler;

	public HttpClientExtractor() {
		responseHandler = new BasicResponseHandler();
		client = new DefaultHttpClient();

	}

	@Override
	public Source getSource(URL url) {
		String responseBody = null;
		try {
			HttpGet get = new HttpGet(url.toString());
			responseBody = client.execute(get, responseHandler);
		} catch (ClientProtocolException e) {
			log.error("Error getting source in session mode: {}", e);
		} catch (IOException e) {
			log.error("Error getting source in session mode: {}", e);
		}

		Source source = new Source(responseBody);
		return source;
	}
	
	public void close() {
		client.getConnectionManager().shutdown();
	}
}
