package solo.adilkhanov;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXMLFile {

    public static void main(String[] args) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("jasperReport");
            doc.appendChild(rootElement);

            rootElement.setAttribute("xmlns","http://jasperreports.sourceforge.net/jasperreports");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:schemaLocation", "http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd");
            rootElement.setAttribute("name", "user_report");
            rootElement.setAttribute("language", "groovy");

            String[] page = {"pageWidth", "pageHeight", "columnWidth", "leftMargin", "rightMargin", "topMargin", "bottomMargin", "uuid"};
            String[] values = {"595", "842", "555", "20", "20", "20", "20", "0bf6c571-f46c-42f2-949f-68e3c37a1b93"};

            Map<String, String> attributes = new HashMap<>();
            for (int i = 0; i < page.length; i++) {
                attributes.put(page[i], values[i]);
            }

            for (String key : attributes.keySet()) {
                rootElement.setAttribute(key, attributes.get(key));
            }

            Element queryString = doc.createElement("queryString");
            queryString.appendChild(doc.createCDATASection("SELECT username, password, email FROM user_"));
            rootElement.appendChild(queryString);

            Element field = doc.createElement("field");
            Map<String, String> attributesField = new HashMap<>();
            attributesField.put("name", "username");
            attributesField.put("class", "java.lang.String");

            for (String key : attributesField.keySet()) {
                field.setAttribute(key, attributesField.get(key));
            }

            rootElement.appendChild(field);

            Element columnHeader = doc.createElement("columnHeader");

            Element band = doc.createElement("band");
            band.setAttribute("height", "30");

            Element staticText = doc.createElement("staticText");

            Element reportElement = doc.createElement("reportElement");
            reportElement.setAttribute("x", "0");
            reportElement.setAttribute("y", "0");
            reportElement.setAttribute("width", "100");
            reportElement.setAttribute("height", "30");
            reportElement.setAttribute("uuid", "7c2b053d-84f9-4763-a4a7-08d6ff7f1eeb");

            Element text = doc.createElement("text");
            text.appendChild(doc.createCDATASection("Username"));

            staticText.appendChild(reportElement);
            staticText.appendChild(text);

            band.appendChild(staticText);
            columnHeader.appendChild(band);
            rootElement.appendChild(columnHeader);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:/Users/s.adilkhanov/IdeaProjects/ReportGenerator/src/main/java/solo/adilkhanov/file.jrxml"));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
