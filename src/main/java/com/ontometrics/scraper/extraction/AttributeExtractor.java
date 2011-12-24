package com.ontometrics.scraper.extraction;

import net.htmlparser.jericho.Element;

public class AttributeExtractor extends Manipulator {

	private String attributeName;

	public AttributeExtractor(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public String performExtraction() {
		String attributeValue = "";
		if (getSource().getAllElements().size() > 0) {
			Element targetElement = getSource().getAllElements().get(0);
			attributeValue = targetElement.getAttributeValue(attributeName);
		}
		return attributeValue;
	}

	@Override
	public void setMatcher(String matcher) {
		// TODO Auto-generated method stub

	}

}
