package com.everyware.xml;

public class XMLToken {
	public final XMLTokenType type;
	public String val;

	public XMLToken(XMLTokenType type) {
		this.type = type;
		this.val = null;
	}

	public XMLToken(XMLTokenType type, String val) {
		this.type = type;
		this.val = val;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("XMLToken[" + type);
		if(type == XMLTokenType.text || type == XMLTokenType.copen
				|| type == XMLTokenType.piopen
				|| type == XMLTokenType.ent) {
			b.append(':').append(val);
		}
		return b.append("]").toString();
	}
}
