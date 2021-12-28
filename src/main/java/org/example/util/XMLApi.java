package org.example.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XMLApi {
    Document document;
    String file;


    public XMLApi(String file) {
        this.file = file;

    }

    private void readXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());

            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
                System.exit(0);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
                System.exit(0);
            }
        });
       this.document = documentBuilder.parse(this.file);

    }

    private void writeXml(Document doc, OutputStream output)
            throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "group.dtd");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public void save(String file) {
        try (
                FileOutputStream output =
                        new FileOutputStream(file)) {
            writeXml(document, output);

        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void checkData() throws ParserConfigurationException, IOException, SAXException {
        readXML();
        NodeList students = document.getElementsByTagName("student");
        for (int i = 0; i < students.getLength(); i++) {
            Student student = new Student(students.item(i));
            student.validate();

        }
    }
}
