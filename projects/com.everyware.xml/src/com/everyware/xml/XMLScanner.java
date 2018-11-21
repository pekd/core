package com.everyware.xml;

import java.util.Deque;
import java.util.LinkedList;

public class XMLScanner {
	private State state;
	private StringBuffer buf;
	private Deque<XMLToken> tokens;

	private static enum State {
		INIT, LT, QM, LTEXCL, LTEXCLDASH, LTEXCLSQ, LTEXCLSQC, LTEXCLSQCD, LTEXCLSQCDA, LTEXCLSQCDAT, LTEXCLSQCDATA, COMMENT, DASH, DASHDASH, SLASH, SQ, SQSQ, ENT, TEXT, CDATA;
	}

	public XMLScanner() {
		state = State.INIT;
		buf = new StringBuffer();
		tokens = new LinkedList<>();
	}

	public void process(byte[] data, int off, int len) {
		String s = new String(data, off, len);
		for(char c : s.toCharArray()) {
			scan(c);
		}
	}

	public void process(byte[] data) {
		String s = new String(data);
		for(char c : s.toCharArray()) {
			scan(c);
		}
	}

	public boolean available() {
		return !tokens.isEmpty();
	}

	public XMLToken scan() {
		if(tokens.isEmpty()) {
			return null;
		}
		return tokens.removeFirst();
	}

	private void push(XMLToken t) {
		tokens.add(t);
	}

	private void scan(char c) {
		switch(state) {
		case INIT:
			if(c != '<') {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<";
				push(t);
			} else {
				state = State.LT;
			}
			break;
		case LT:
			if(c == '?') {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.piopen));
			} else if(c == '!') {
				state = State.LTEXCL;
			} else if(c == '/') {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.tsopen));
			} else {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.topen));
				buf.append(c);
			}
			break;
		case LTEXCL:
			if(c == '-') {
				state = State.LTEXCLDASH;
			} else if(c == '[') {
				state = State.LTEXCLSQ;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<!" + c;
				push(t);
			}
			break;
		case LTEXCLSQ:
			if(c == 'C') {
				state = State.LTEXCLSQC;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![" + c;
				push(t);
			}
			break;
		case LTEXCLSQC:
			if(c == 'D') {
				state = State.LTEXCLSQCD;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![C" + c;
				push(t);
			}
			break;
		case LTEXCLSQCD:
			if(c == 'A') {
				state = State.LTEXCLSQCDA;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![CD" + c;
				push(t);
			}
			break;
		case LTEXCLSQCDA:
			if(c == 'T') {
				state = State.LTEXCLSQCDAT;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![CDA" + c;
				push(t);
			}
			break;
		case LTEXCLSQCDAT:
			if(c == 'A') {
				state = State.LTEXCLSQCDATA;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![CDAT" + c;
				push(t);
			}
			break;
		case LTEXCLSQCDATA:
			if(c == '[') {
				state = State.CDATA;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<![CDATA" + c;
				push(t);
			}
			break;
		case LTEXCLDASH:
			if(c == '-') {
				state = State.COMMENT;
			} else {
				XMLToken t = new XMLToken(XMLTokenType.error);
				t.val = "<!-" + c;
				push(t);
			}
			break;
		case COMMENT:
			if(c == '-') {
				state = State.DASH;
			}
			break;
		case DASH:
			if(c == '-') {
				state = State.DASHDASH;
			} else {
				state = State.DASH;
			}
			break;
		case DASHDASH:
			if(c == '>') {
				state = State.TEXT;
			} else if(c != '-') {
				state = State.COMMENT;
			}
			break;
		case QM:
			if(c == '>') {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.piclose));
			} else if(c == '?') {
				buf.append('?');
			} else {
				state = State.TEXT;
				buf.append('?').append(c);
			}
			break;
		case SLASH:
			if(c == '>') {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.tcloses));
			} else if(c == '/') {
				buf.append(c);
			} else if(c == '<') {
				buf.append('/');
				state = State.LT;
			} else {
				state = State.TEXT;
				buf.append('/');
				buf.append(c);
			}
			break;
		case TEXT:
			switch(c) {
			case '<':
				state = State.LT;
				break;
			case '?':
				state = State.QM;
				break;
			case '&':
				state = State.ENT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
			case '\t':
			case '\r':
			case '\n':
			case ' ':
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.space, Character.toString(c)));
				break;
			case '"':
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.quote));
				break;
			case '\'':
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.squote));
				break;
			case '=':
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.equal));
				break;
			case '/':
				state = State.SLASH;
				break;
			case '>':
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
				push(new XMLToken(XMLTokenType.tclose));
				break;
			default:
				buf.append(c);
			}
			break;
		case CDATA:
			if(c == ']') {
				state = State.SQ;
			} else {
				state = State.CDATA;
				buf.append(c);
			}
			break;
		case SQ:
			if(c == ']') {
				state = State.SQSQ;
			} else {
				state = State.CDATA;
				buf.append(']');
				buf.append(c);
			}
			break;
		case SQSQ:
			if(c == '>') {
				state = State.TEXT;
				if(buf.length() > 0) {
					XMLToken t = new XMLToken(XMLTokenType.text);
					t.val = buf.toString();
					buf = new StringBuffer();
					push(t);
				}
			} else if(c == ']') {
				buf.append(']');
			} else {
				buf.append("]]");
				buf.append(c);
			}
			break;
		case ENT:
			if(c == ';') {
				state = State.TEXT;
				XMLToken t = new XMLToken(XMLTokenType.ent);
				t.val = buf.toString();
				buf = new StringBuffer();
				push(t);
			} else {
				buf.append(c);
			}
			break;
		}
	}
}
