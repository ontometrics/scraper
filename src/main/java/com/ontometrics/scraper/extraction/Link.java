package com.ontometrics.scraper.extraction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Link implements Field {

	private String label;

	private String href;
	
	private String name;

    private URL baseUrl;

    private Map<String, Object> params;

	public static class Builder {
		private String label;
		private String href;
		private String name;
        private URL baseUrl;

		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder href(String href) {
			this.href = href;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}

        public Builder baseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

		public Link build() {
			return new Link(this);
		}
	}
	
	public Link(String label, String href) {
		super();
		this.label = label;
		this.href = href;
	}

	public Link(Builder builder) {
		super();
		this.label = builder.label;
		this.href = builder.href;
		this.name = builder.name;
        this.baseUrl = builder.baseUrl;
	}

	public String getLabel() {
		return label;
	}

	public String getHref() {
		return href;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return getHref();
	}

	@Override
	public String toString() {
		return "Link [label=" + label + ", href=" + href + "]";
	}

	@Override
	public void addValue(String value) {
		throw new IllegalStateException("cannot add to the href");
	}

    public String getHrefWithPrependedBaseUrl() throws MalformedURLException {
        if (baseUrl != null && !isValidUrl(href)) {
            return new URL(baseUrl, href).toString();
        }
        return href;
    }

    private boolean isValidUrl(String href) {
        if (href == null) {
            return false;
        }
        try {
            new URL(href);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public Link addParam(String name, Object value) {
        if (this.params == null) {
            params = new HashMap<String, Object>(1);
        }
        params.put(name, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
