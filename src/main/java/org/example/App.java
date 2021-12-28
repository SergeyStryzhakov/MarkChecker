package org.example;

import org.example.util.XMLApi;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {


        XMLApi file = new XMLApi(args[0]);
        file.checkData();
        file.save(args[1]);

    }


}
