package com.everyware.util.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElement {
	private String name;
	private List<String> order;
	private Map<String, String> values;
	public static Map<String, String> ENTITIES;

	static {
		ENTITIES = new HashMap<>();
		ENTITIES.put("amp", "&");
		ENTITIES.put("lt", "<");
		ENTITIES.put("gt", ">");
		ENTITIES.put("quot", "\"");
		ENTITIES.put("apos", "'");
	}

	public XmlElement() {
		order = new ArrayList<>();
		values = new HashMap<>();
	}

	public XmlElement(String name) {
		this();
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void update(String attribName, String value) {
		synchronized(values) {
			values.put(attribName, value);
		}
	}

	public void add(String attribName, String value) {
		synchronized(values) {
			order.remove(attribName);
			values.put(attribName, value);
			order.add(attribName);
		}
	}

	public void add(String attribName, int value) {
		add(attribName, Integer.toString(value));
	}

	public void add(String attribName, long value) {
		add(attribName, Long.toString(value));
	}

	public void add(String attribName, boolean value) {
		add(attribName, Boolean.toString(value));
	}

	public String get(String attribName) {
		return values.get(attribName);
	}

	public void remove(String attribName) {
		order.remove(attribName);
		values.remove(attribName);
	}

	private static String escape(String s) {
		StringBuilder b = new StringBuilder();
		for(char c : s.toCharArray()) {
			switch(c) {
			case '&':
				b.append("&amp;");
				break;
			case '<':
				b.append("&lt;");
				break;
			case '>':
				b.append("&gt;");
				break;
			case '"':
				b.append("&quot;");
				break;
			default:
				if(c < 32 || c > 126)
					b.append(String.format("&#%d;", (int) c));
				else
					b.append(c);
			}
		}
		return b.toString();
	}

	private static String escapeName(String s) {
		StringBuilder b = new StringBuilder(s.length());
		for(char c : s.toCharArray()) {
			if(c == '_' || c == '-') {
				b.append(c);
			} else if(c >= '0' && c <= '9') {
				b.append(c);
			} else if(c < 'A' || c > 'z' || (c > 'Z' && c < 'a')) {
				b.append('-');
			} else {
				b.append(c);
			}
		}
		return b.toString();
	}

	private static String decode(String entity) {
		if(entity.charAt(0) == '#') {
			if(entity.charAt(1) == 'x') {
				return Character.toString((char) Integer
						.parseInt(entity.substring(2),
								16));
			} else {
				return Character.toString((char) Integer
						.parseInt(entity.substring(1)));
			}
		}
		return ENTITIES.get(entity);
	}

	public static XmlElement parse(String s) {
		StringReader in = new StringReader(s);
		int ch;
		int state = 0;
		StringBuilder buf = new StringBuilder();
		String name = null;
		XmlElement element = new XmlElement();
		try {
			while((ch = in.read()) != -1) {
				char c = (char) ch;
				switch(state) {
				case 0: // looking for "<"
					if(c == '<') {
						state = 1;
					} else if(!Character.isWhitespace(c)) {
						throw new IllegalArgumentException(
								"invalid xml");
					}
					break;
				case 1: // tag name
					if(Character.isWhitespace(c)) {
						element.setName(buf.toString());
						buf = new StringBuilder();
						state = 2;
					} else if(ch == '/') {
						element.setName(buf.toString());
						buf = new StringBuilder();
						state = 3;
					} else {
						buf.append(c);
					}
					break;
				case 2: // whitespace between attributes
					if(c == '/') {
						state = 3;
					} else if(!Character.isWhitespace(c)) {
						buf.append(c);
						state = 5;
					}
					break;
				case 3: // '>'
					if(c != '>') {
						throw new IllegalArgumentException(
								"invalid xml");
					}
					state = 4;
					break;
				case 4: // end of tag
					if(!Character.isWhitespace(c)) {
						throw new IllegalArgumentException(
								"invalid xml");
					}
					break;
				case 5: // attribute name
					if(Character.isWhitespace(c)) {
						throw new IllegalArgumentException(
								"invalid xml");
					}
					if(c == '=') {
						name = buf.toString();
						buf = new StringBuilder();
						state = 6;
					} else {
						buf.append(c);
					}
					break;
				case 6:
					if(c == '"') {
						state = 7;
					} else if(c == '\'') {
						state = 8;
					} else {
						throw new IllegalArgumentException(
								"invalid xml");
					}
					break;
				case 7:
					if(c == '"') {
						state = 2;
						element.add(name,
								buf.toString());
						buf = new StringBuilder();
					} else if(c == '&') {
						StringBuilder b = new StringBuilder();
						while(true) {
							ch = in.read();
							if(ch == -1 ||
									ch == ';') {
								break;
							}
							b.append((char) ch);
						}
						String entity = decode(b
								.toString());
						if(entity == null) {
							throw new IllegalArgumentException(
									"invalid xml");
						}
						buf.append(entity);
					} else {
						buf.append(c);
					}
					break;
				case 8:
					if(c == '\'') {
						state = 2;
						element.add(name,
								buf.toString());
						buf = new StringBuilder();
					} else if(c == '&') {
						StringBuilder b = new StringBuilder();
						while(true) {
							ch = in.read();
							if(ch == -1 ||
									ch == ';') {
								break;
							}
							b.append((char) ch);
						}
						String entity = decode(b
								.toString());
						if(entity == null) {
							throw new IllegalArgumentException(
									"invalid xml");
						}
						buf.append(entity);
					} else {
						buf.append(c);
					}
					break;
				}
			}
		} catch(IOException e) {
			// should never happen
			e.printStackTrace();
		}
		if(state != 4) {
			throw new IllegalArgumentException("invalid xml");
		}
		in.close();
		return element;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("<").append(escapeName(name));
		synchronized(values) {
			for(String entry : order) {
				b.append(" ")
						.append(escapeName(entry))
						.append("=\"")
						.append(escape(values
								.get(entry)))
						.append("\"");
			}
		}
		return b.append("/>").toString();
	}
}
