package com.ontometrics.scraper.extraction;

public class ScrapedField implements Field {

	private static final String delimiter = ";";

	private String label;

	private String value;

	public ScrapedField(String label, String value) {
		super();
		this.label = (label == null) ? null : label.trim();
		this.value = (value == null) ? null : value.trim();
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * Adds a delimiter, then appends the new value to the current value.
	 */
	@Override
	public void addValue(String newValue) {
		this.value += delimiter + newValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ScrapedField other = (ScrapedField) obj;
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ScrapedField [label=" + label + ", value=" + value + "]";
	}

}
