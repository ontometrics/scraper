package com.ontometrics.scraper.html;

import java.util.List;

import com.ontometrics.scraper.extraction.Manipulator;

public class HtmlTable extends Manipulator {

	private String id;

	private List<Integer> columnsToGet;

	private String className;

	private String containsText;

	public static HtmlTable table() {
		return new HtmlTable();
	}

	public HtmlTable withID(String id) {
		this.id = id;
		return this;
	}

	public HtmlTable columns(int... columnNumbers) {
		for (int columnNumber : columnNumbers) {
			this.columnsToGet.add(columnNumber);
		}
		return this;
	}

	public HtmlTable ofClass(String className) {
		this.className = className;
		return this;
	}

	public HtmlTable containing(String text) {
		this.containsText = text;
		return this;
	}

	@Override
	public String performExtraction() {
		String result = null;
		if (id!=null){
			result = getSource().getElementById(id).toString();
		}
		return result;
	}

	@Override
	public void setMatcher(String matcher) {
		
	}

}
