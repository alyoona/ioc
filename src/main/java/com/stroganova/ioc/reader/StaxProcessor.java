package com.stroganova.ioc.reader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

public class StaxProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    StaxProcessor(InputStream is) {
        try {
            reader = FACTORY.createXMLStreamReader(is);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    boolean startElement(String element, String parent) {
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                if (parent != null && event == XMLEvent.END_ELEMENT && parent.equals(reader.getLocalName())) {
                    return false;
                }
                if (event == XMLEvent.START_ELEMENT && element.equals(reader.getLocalName())) {
                    return true;
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    String getBeanId() {
        return getAttributeValue("id");
    }

    String getBeanClass() {
        return getAttributeValue("class");
    }

    String getDependencyName() {
        return getAttributeValue("name");
    }

    String getDependencyValue() {
        return getAttributeValue("value");
    }

    String getDependencyRefValue() {
        return getAttributeValue("ref");
    }

    private String getAttributeValue(String name) {
        return reader.getAttributeValue(null, name);
    }

    boolean isDependency() {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if ("value".equals(reader.getAttributeName(i).toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
