package com.everyware.xml.dom;

import java.io.IOException;
import java.io.InputStream;

import com.everyware.xml.XMLParser;

public class XMLReader {
	public static Element read(InputStream in) throws IOException {
		Collector collector = new Collector();
		XMLParser parser = new XMLParser(collector);
		byte[] buf = new byte[256];
		int n;
		parser.start();
		while((n = in.read(buf)) != -1) {
			parser.process(buf, 0, n);
		}
		parser.end();
		return collector.getRoot();
	}
}
