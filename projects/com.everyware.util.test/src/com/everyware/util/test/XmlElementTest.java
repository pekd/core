package com.everyware.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.everyware.util.xml.XmlElement;

public class XmlElementTest {
	private XmlElement xml;

	@Before
	public void setup() {
		xml = new XmlElement();
	}

	@Test
	public void testEmptyElement() {
		xml.setName("tag");
		assertEquals("<tag/>", xml.toString());
	}

	@Test
	public void testSingleAttribute() {
		xml.setName("tag");
		xml.add("name", "value");
		assertEquals("<tag name=\"value\"/>", xml.toString());
	}

	@Test
	public void testTwoAttributes() {
		xml.setName("tag");
		xml.add("name", "value");
		xml.add("key", "text");
		assertEquals("<tag name=\"value\" key=\"text\"/>",
				xml.toString());
	}

	@Test
	public void testEntities() {
		xml.setName("tag");
		xml.add("command", "x = a < b && b > c;");
		assertEquals("<tag command=\"x = a &lt; b &amp;&amp; b &gt; c;\"/>",
				xml.toString());
	}

	@Test
	public void testNumericEntities() {
		xml.setName("tag");
		xml.add("nl", "\r\n");
		assertEquals("<tag nl=\"&#13;&#10;\"/>", xml.toString());
	}

	@Test
	public void testNoEntities() {
		xml.setName("tag");
		xml.add("chars", "[]_;()$%?!^#+-~*.:'\\{}/");
		assertEquals("<tag chars=\"[]_;()$%?!^#+-~*.:'\\{}/\"/>",
				xml.toString());
	}

	@Test
	public void testQuotes() {
		xml.setName("tag");
		xml.add("quote", "\"text\"");
		assertEquals("<tag quote=\"&quot;text&quot;\"/>",
				xml.toString());
	}

	@Test
	public void testParseTag() {
		xml = XmlElement.parse("<tag/>");
		assertEquals("<tag/>", xml.toString());
	}

	@Test
	public void testParseTagWithWhitespace() {
		xml = XmlElement.parse("   <tag   />  ");
		assertEquals("<tag/>", xml.toString());
	}

	@Test
	public void testParseAttribute() {
		xml = XmlElement.parse("<tag name=\"value\"/>");
		assertEquals("<tag name=\"value\"/>", xml.toString());
	}

	@Test
	public void testParseAttributeWithWhitespace() {
		xml = XmlElement.parse("<tag  name=\"value\" />");
		assertEquals("<tag name=\"value\"/>", xml.toString());
	}

	@Test
	public void testParseAttributes() {
		xml = XmlElement.parse("<tag name=\"value\" key=\"stolen\"/>");
		assertEquals("<tag name=\"value\" key=\"stolen\"/>",
				xml.toString());
	}

	@Test
	public void testParseAttributesWithWhitespace() {
		xml = XmlElement.parse("<tag     name=\"value\"   key=\"stolen\"   />");
		assertEquals("<tag name=\"value\" key=\"stolen\"/>",
				xml.toString());
	}

	@Test
	public void testParseSingleQuotes() {
		xml = XmlElement.parse("<tag name='value' key='stolen'/>");
		assertEquals("<tag name=\"value\" key=\"stolen\"/>",
				xml.toString());
	}

	@Test
	public void testParseSingleQuotesWithWhitespace() {
		xml = XmlElement.parse("<tag     name='value'   key='stolen'   />");
		assertEquals("<tag name=\"value\" key=\"stolen\"/>",
				xml.toString());
	}

	@Test
	public void testParseEntities() {
		xml = XmlElement.parse("<tag name=\"&quot;test &lt; a &amp;&amp; a &gt; b;\"/>");
		assertEquals("<tag name=\"&quot;test &lt; a &amp;&amp; a &gt; b;\"/>",
				xml.toString());
	}

	@Test
	public void testParseNumericEntity() {
		xml = XmlElement.parse("<tag nl='&#13;&#10;'/>");
		assertEquals("<tag nl=\"&#13;&#10;\"/>", xml.toString());
	}

	@Test
	public void testParseHexEntity() {
		xml = XmlElement.parse("<tag nl='&#xd;&#xa;'/>");
		assertEquals("<tag nl=\"&#13;&#10;\"/>", xml.toString());
	}
}
