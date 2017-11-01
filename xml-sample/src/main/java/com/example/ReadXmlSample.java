package com.example;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.XMLEvent;

public class ReadXmlSample {

    public static void main(String[] args) throws Exception {
        final XMLEventReader reader = createReader();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            System.out.printf("%s: %s%n", eventName(event.getEventType()), event);
        }
    }

    static XMLEventReader createReader() throws Exception {
        final XMLInputFactory factory = XMLInputFactory.newFactory();
        final Reader in = new StringReader(
                "<?xml version='1.0'?>"
                        + "<foo>"
                        + "  <bar baz='hello'/>"
                        + "  <qux>world</qux>"
                        + "  <![CDATA[<hoge/>]]>"
                        + "</foo>");
        return factory.createXMLEventReader(in);
    }

    static String eventName(int eventType) {
        switch (eventType) {
        case XMLStreamConstants.START_ELEMENT:
            return "         START_ELEMENT";
        case XMLStreamConstants.END_ELEMENT:
            return "           END_ELEMENT";
        case XMLStreamConstants.CHARACTERS:
            return "            CHARACTERS";
        case XMLStreamConstants.ATTRIBUTE:
            return "             ATTRIBUTE";
        case XMLStreamConstants.NAMESPACE:
            return "             NAMESPACE";
        case XMLStreamConstants.PROCESSING_INSTRUCTION:
            return "PROCESSING_INSTRUCTION";
        case XMLStreamConstants.COMMENT:
            return "               COMMENT";
        case XMLStreamConstants.START_DOCUMENT:
            return "        START_DOCUMENT";
        case XMLStreamConstants.END_DOCUMENT:
            return "          END_DOCUMENT";
        case XMLStreamConstants.DTD:
            return "                   DTD";
        default:
            throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}
