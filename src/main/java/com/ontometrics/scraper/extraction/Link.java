package com.ontometrics.scraper.extraction;

public class Link {

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
	public String toString() {
		return "Link [label=" + label + ", href=" + href + "]";
	}

}
