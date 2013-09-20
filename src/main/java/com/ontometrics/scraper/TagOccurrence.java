package com.ontometrics.scraper;

import com.ontometrics.scraper.extraction.ElementIdentifierType;

public class TagOccurrence {

	private String tag;

	private ElementIdentifierType elementIdentifierType;

	private String identifier;

	private int occurrence;

	private String matching;

	private String ofClass;

	public static class Builder {

		private String tag;
		private int occurrence;
		private String matching;
		private String ofClass;
		private ElementIdentifierType elementIdentifierType;
		private String identifier;

		public Builder elementIdentifierType(ElementIdentifierType elementIdentifierType) {
			this.elementIdentifierType = elementIdentifierType;
			return this;
		}

		public Builder tag(String tag) {
			this.tag = tag;
			return this;
		}

		public Builder occurrence(int occurrence) {
			this.occurrence = occurrence;
			return this;
		}

		public Builder matching(String matching) {
			this.matching = matching;
			return this;
		}

		public Builder ofClass(String ofClass) {
			this.ofClass = ofClass;
			return this;
		}

		public Builder identifier(String id) {
			this.identifier = id;
			return this;
		}

		public TagOccurrence build() {
			return new TagOccurrence(this);
		}

	}

	public TagOccurrence(Builder builder) {
		this.ofClass = builder.ofClass;
		this.tag = builder.tag;
		this.matching = builder.matching;
		this.occurrence = builder.occurrence;
		this.elementIdentifierType = builder.elementIdentifierType;
		this.identifier = builder.identifier;
	}

	public int getOccurrence() {
		return occurrence;
	}

	public String getTag() {
		return tag;
	}

	public String getMatching() {
		return matching;
	}

	public String getOfClass() {
		return ofClass;
	}

	public ElementIdentifierType getElementIdentifierType() {
		return elementIdentifierType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setMatcher(String matcher) {
		this.matching = matcher;
	}

    @Override
    public String toString() {
        return "TagOccurrence{" +
                "tag='" + tag + '\'' +
                ", elementIdentifierType=" + elementIdentifierType +
                ", identifier='" + identifier + '\'' +
                ", occurrence=" + occurrence +
                ", matching='" + matching + '\'' +
                ", ofClass='" + ofClass + '\'' +
                '}';
    }
}
