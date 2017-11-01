package com.example;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;

public class WriteXmlSample {

    public static void main(String[] args) throws Exception {

        final XMLOutputFactory factory = XMLOutputFactory.newFactory();

        final Writer out = new StringWriter();
        final XMLEventWriter writer = factory.createXMLEventWriter(out);

        final XMLEventFactory factory2 = XMLEventFactory.newFactory();
        writer.add(factory2.createStartDocument());
        writer.add(factory2.createStartElement("", null, "foo"));
        writer.add(factory2.createStartElement("", null, "bar"));
        writer.add(factory2.createAttribute("baz", "hello"));
        writer.add(factory2.createEndElement("", null, "bar"));
        writer.add(factory2.createStartElement("", null, "qux"));
        writer.add(factory2.createCharacters("world"));
        writer.add(factory2.createEndElement("", null, "qux"));
        writer.add(factory2.createCData("<hoge/>"));
        writer.add(factory2.createEndElement("", null, "foo"));
        writer.add(factory2.createEndDocument());
        writer.flush();
        writer.close();

        System.out.println(out);
    }
}
