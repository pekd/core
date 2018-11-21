package com.everyware.xml.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Element extends Node {
	public final String uri;
	public final String name;
	public final String qName;
	public String value;

	private Set<Attribute> attributes;
	private List<Element> children;

	public Element(String localName) {
		this("", localName, localName);
	}

	public Element(String localName, String value) {
		this("", localName, localName, value);
	}

	public Element(String uri, String localName, String qName) {
		this(uri, localName, qName, null);
	}

	public Element(String uri, String localName, String qName, String value) {
		super(TAG);
		this.uri = uri;
		this.name = localName;
		this.qName = qName;
		this.value = value;
		attributes = new HashSet<>();
		children = new ArrayList<>();
	}

	public void addAttribute(Attribute attr) {
		attributes.add(attr);
	}

	public void addAttributes(Collection<Attribute> attrs) {
		attributes.addAll(attrs);
	}

	public Attribute[] getAttributes() {
		return attributes.toArray(new Attribute[attributes.size()]);
	}

	public void addChild(Element elem) {
		children.add(elem);
	}

	public Element[] getChildren() {
		return children.toArray(new Element[children.size()]);
	}

	public Element getFirstChild() {
		if(children.size() == 0) {
			return null;
		} else {
			return children.get(0);
		}
	}

	public String getNodeValue() {
		return value;
	}

	public void addAttribute(String localName, String val) {
		addAttribute(new Attribute(localName, val));
	}

	public String getAttribute(String localName) {
		for(Attribute a : attributes) {
			if(a.name.equals(localName)) {
				return a.value;
			}
		}
		return null;
	}

	public Element[] getElementsByTagName(String localName) {
		return children.stream().filter((x) -> x.name.equals(localName)).toArray(Element[]::new);
	}

	protected void compress() {
		if((children.size() == 1) && (children.get(0) instanceof Text)) {
			value = children.get(0).value;
			children.clear();
		}
	}

	private static String rep(String s, int n) {
		StringBuilder buf = new StringBuilder(n * s.length());
		for(int i = 0; i < n; i++) {
			buf.append(s);
		}
		return buf.toString();
	}

	protected String serialize(int level) {
		String indent = rep("\t", level);
		StringBuffer buf = new StringBuffer(indent).append("<").append(name);
		if(!attributes.isEmpty()) {
			buf.append(" ").append(attributes.stream().sorted((x, y) -> x.name.compareTo(y.name))
					.map(Object::toString).collect(Collectors.joining(" ")));
		}
		if(!children.isEmpty()) {
			buf.append(">\n");
			for(Element e : children) {
				buf.append(e.serialize(level + 1));
			}
		}
		if(value != null) {
			buf.append(">").append(escape(value)).append("</").append(name).append(">");
		} else if(children.isEmpty()) {
			buf.append("/>");
		} else {
			buf.append(indent).append("</").append(name).append(">");
		}
		return buf.append("\n").toString();
	}

	@Override
	public String toString() {
		compress();
		return "<?xml version=\"1.0\" charset=\"utf-8\"?>\n" + serialize(0);
	}
}
