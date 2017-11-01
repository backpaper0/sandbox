package com.example;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;

public class ReadWriteXmlSample {

    public static void main(String[] args) throws Exception {

        final XMLOutputFactory factory = XMLOutputFactory.newFactory();

        final Writer out = new StringWriter();
        final XMLEventWriter writer = factory.createXMLEventWriter(out);

        writer.add(ReadXmlSample.createReader());

        writer.flush();
        writer.close();

        System.out.println(out);
    }
}
