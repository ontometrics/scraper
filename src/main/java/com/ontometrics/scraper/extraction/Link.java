package com.ontometrics.scraper.extraction;

public class Link implements Field {

	private String label;

	private String href;

	public Link(String label, String href) {
		super();
		this.label = label;
		this.href = href;
	}

	public String getLabel() {
		return label;
	}

	public String getHref() {
		return href;
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
