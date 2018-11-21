package com.everyware.xml.dom;

import java.util.Objects;

public class Attribute extends Node {
	public final String name;
	public final String uri;
	public final String qName;
	public final String value;

	public Attribute(String localName, String value) {
		this("", localName, localName, value);
	}

	public Attribute(String uri, String localName, String qName, String value) {
		super(ATTRIBUTE);
		this.uri = uri;
		this.name = localName;
		this.qName = qName;
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, uri, qName, value);
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(!(o instanceof Attribute)) {
			return false;
		}
		Attribute a = (Attribute) o;
		return name.equals(a.name) && uri.equals(a.uri) && qName.equals(a.qName) && value.equals(a.value);
	}

	@Override
	public String toString() {
		return name + "=\"" + Node.escape(value) + "\"";
	}
}
