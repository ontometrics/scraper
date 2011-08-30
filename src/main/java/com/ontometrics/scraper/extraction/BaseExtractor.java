package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import com.ontometrics.scraper.TagOccurrence;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public abstract class BaseExtractor {

	private Source source;

	private URL url;

	private LinkedList<Manipulator> manipulators;
	
	public BaseExtractor url(URL url){
		this.url = url;
		return this;
	}

	public void addManipulator(Manipulator manipulator) {
		if (manipulators == null) {
			manipulators = new LinkedList<Manipulator>();
		} else {
			manipulators.getLast().setSuccessor(manipulator);
		}
		manipulators.add(manipulator);
	}
	
	protected void performManipulations(){
		try {
			source = new Source(url);
			manipulators.getFirst().execute(source);
			source = manipulators.getLast().getSource();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public Source getSource() {
		return source;
	}
	
	public URL getUrl() {
		return url;
	}

	public BaseExtractor table(int occurrence) {
		addManipulator(new ElementManipulator(new TagOccurrence(HTMLElementName.TABLE, occurrence)));
		return this;
	}


}
