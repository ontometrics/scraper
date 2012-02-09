package com.ontometrics.scraper;

import java.net.URL;

public interface Iterator {

	public boolean hasNext();

	public URL next();

	public URL getBaseUrl();
}
