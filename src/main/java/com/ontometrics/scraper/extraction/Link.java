package com.ontometrics.scraper.extraction;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Link implements Field, Serializable {

	private String label;

	private String href;
	
	private String name;

    private URL baseUrl;

    private Map<String, Object> params;

    //object which identifies source from which Link has been extracted from
    private Object source;

	public static class Builder {
		private String label;
		private String href;
		private String name;
        private URL baseUrl;
        private Object source;

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

        public Builder source(Object source) {
            this.source = source;
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
        this.source = builder.source;
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

    public Object getSource() {
        return source;
    }

    public URL toUrl() throws MalformedURLException {
        return new URL(getHref());
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

	public URL getBaseUrl() {
		return baseUrl;
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

    public void setHref(String href) {
        this.href = href;
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
