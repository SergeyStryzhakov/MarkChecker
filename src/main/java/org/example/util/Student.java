package org.example.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Locale;

public class Student {
    Node node;
    private int subjectsCount;
    private int marks;


    public Student(Node node) {
        this.node = node;
    }

    public void validate() {
        NodeList fields = node.getChildNodes();
        subjectsCount = 0;
        for (int i = 0; i < fields.getLength(); i++) {
            Node field = fields.item(i);
            if (field.getNodeType() == Node.ELEMENT_NODE) {
                if ("subject".equals(field.getNodeName())) {
                    subjectsCount++;
                    marks += Integer.parseInt(field
                            .getAttributes()
                            .getNamedItem("mark")
                            .getTextContent());
                } else if ("average".equals(field.getNodeName())) {
                    String averageXML = field.getChildNodes().item(0).getTextContent();
                    String average = countAverage();
                    if (!average.equals(averageXML)) {
                        field.getChildNodes().item(0).setTextContent(average);
                    }
                }
            }
        }
    }

    private String countAverage() {
        return String.format(Locale.ROOT, "%.1f", (double) marks / subjectsCount);

    }

}

