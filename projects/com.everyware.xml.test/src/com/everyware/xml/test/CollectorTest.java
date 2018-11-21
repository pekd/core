package com.everyware.xml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.everyware.xml.XMLParser;
import com.everyware.xml.dom.Attribute;
import com.everyware.xml.dom.Collector;
import com.everyware.xml.dom.Element;
import com.everyware.xml.dom.Text;

public class CollectorTest {
	private XMLParser parser;
	private Collector collector;

	@Before
	public void setup() {
		parser = new XMLParser();
		collector = new Collector();
		parser.setContentHandler(collector);
	}

	private Element run(String xml) {
		parser.start();
		parser.process(xml.getBytes());
		parser.end();
		return collector.getRoot();
	}

	@Test
	public void test1() {
		Element root = run("<?xml version='1.0'?><root/>");
		assertEquals("root", root.name);
		assertEquals(0, root.getAttributes().length);
		assertEquals(0, root.getChildren().length);
	}

	@Test
	public void test2() {
		Element root = run("<?xml version='1.0'?><root><node/></root>");
		assertEquals("root", root.name);
		assertEquals(0, root.getAttributes().length);
		Element[] children = root.getChildren();
		assertEquals(1, children.length);
		assertEquals("node", children[0].name);
		assertEquals(0, children[0].getAttributes().length);
		assertEquals(0, children[0].getChildren().length);
	}

	@Test
	public void test3() {
		Element root = run("<?xml version='1.0'?><root attr='val'><node/></root>");
		assertEquals("root", root.name);
		Attribute[] attributes = root.getAttributes();
		assertEquals(1, attributes.length);
		assertEquals("attr", attributes[0].name);
		assertEquals("val", attributes[0].value);
		Element[] children = root.getChildren();
		assertEquals(1, children.length);
		assertEquals("node", children[0].name);
		assertEquals(0, children[0].getAttributes().length);
		assertEquals(0, children[0].getChildren().length);
	}

	@Test
	public void test4() {
		Element root = run("<?xml version='1.0'?><root><node attr='val'/></root>");
		assertEquals("root", root.name);
		assertEquals(0, root.getAttributes().length);
		Element[] children = root.getChildren();
		assertEquals(1, children.length);
		assertEquals("node", children[0].name);
		Attribute[] attributes = children[0].getAttributes();
		assertEquals(1, attributes.length);
		assertEquals("attr", attributes[0].name);
		assertEquals("val", attributes[0].value);
		assertEquals(0, children[0].getChildren().length);
	}

	@Test
	public void text1() {
		Element root = run("<?xml version='1.0'?><root>text</root>");
		assertEquals("root", root.name);
		assertEquals(0, root.getAttributes().length);
		assertEquals(0, root.getChildren().length);
		assertEquals("text", root.value);
	}

	@Test
	public void text2() {
		Element root = run("<?xml version='1.0'?><root>text with <i>elements</i> somewhere</root>");
		assertEquals("root", root.name);
		assertEquals(null, root.value);
		Element[] children = root.getChildren();
		assertEquals(0, root.getAttributes().length);
		assertEquals(3, children.length);
		assertTrue(children[0] instanceof Text);
		assertFalse(children[1] instanceof Text);
		assertTrue(children[2] instanceof Text);
		assertEquals("text with ", children[0].value);
		assertEquals("i", children[1].name);
		assertEquals("elements", children[1].value);
		assertEquals(" somewhere", children[2].value);
	}
}
