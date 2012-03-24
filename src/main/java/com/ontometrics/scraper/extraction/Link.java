package com.ontometrics.scraper.extraction;

public class Link implements Field {

	private String label;

	private String href;
	
	private String name;

	public static class Builder {
		private String label;
		private String href;
		private String name;

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


}
