package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Locale;

public class App {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

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
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }
        });
        Document document = documentBuilder.parse(args[0]);
        NodeList students = document.getElementsByTagName("student");
        System.out.println("Count of student fields is " + students.getLength());
        String studentName;
        for (int i = 0; i < students.getLength(); i++) {
            int subjectCount = 0;
            int mark = 0;
            String averageFromFile = null;
            String averageCount = null;
            Node student = students.item(i);
            studentName = student.getAttributes().getNamedItem("firstname").getTextContent()
                    + " " + student.getAttributes().getNamedItem("lastname").getTextContent();
            NodeList subjects = student.getChildNodes();
            for (int j = 0; j < subjects.getLength(); j++) {

                Node info = subjects.item(j);
                if (info.getNodeType() == Node.ELEMENT_NODE) {
                    if ("subject".equals(info.getNodeName())) {
                        subjectCount++;
                        String temp = info.getAttributes().getNamedItem("mark").getTextContent();
                        try {
                            mark += Integer.parseInt(temp);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if ("average".equals(info.getNodeName())) {
                        averageFromFile = info.getChildNodes().item(0).getTextContent();

                        averageCount = String.format(Locale.ROOT, "%.1f", (double) mark / subjectCount);
                        if (!averageCount.equals(averageFromFile)) {
                            info.getChildNodes().item(0).setTextContent(averageCount);
                        }
                    }
                }
            }

            System.out.println(studentName + " has " + mark + " sum of marks");
            System.out.println("Average from file is " + averageFromFile);
            System.out.println("Count average is " + mark + ":" + subjectCount + " = " + averageCount);
        }
        try (FileOutputStream output =
                     new FileOutputStream(args[1])) {
            writeXml(document, output);
        } catch (TransformerException transformerException) {
            transformerException.printStackTrace();
        }
    }


    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        //new StreamSource(new File(FORMAT_DTD)));

        // pretty print
        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "group.dtd");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}
