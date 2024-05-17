package solo.adilkhanov;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatabaseToXML {

    public static void main(String[] args) {
        String tableName = "user_";

        try (Connection connection = PostgresSQLDB.connect()) {
            Map<String, String> columns = getTableStructure(connection, tableName);

            generateXML(columns, tableName);

            System.out.println("File saved!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getTableStructure(Connection connection, String tableName) throws SQLException {
        Map<String, String> columns = new HashMap<>();
        String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "'";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                columns.put(columnName, mapDataType(dataType));
            }
        }

        return columns;
    }

    private static String mapDataType(String dataType) {
        switch (dataType) {
            case "character varying":
            case "varchar":
            case "text":
                return "java.lang.String";
            case "integer":
                return "java.lang.Integer";
            case "boolean":
                return "java.lang.Boolean";
            // Add more type mappings as needed
            default:
                return "java.lang.Object";
        }
    }

    private static void generateXML(Map<String, String> columns, String tableName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("jasperReport");
            doc.appendChild(rootElement);

            rootElement.setAttribute("xmlns", "http://jasperreports.sourceforge.net/jasperreports");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:schemaLocation", "http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd");
            rootElement.setAttribute("name", tableName + "_report");
            rootElement.setAttribute("language", "groovy");

            String[] pageAttributes = {"pageWidth", "pageHeight", "columnWidth", "leftMargin", "rightMargin", "topMargin", "bottomMargin", "uuid"};
            String[] pageValues = {"595", "842", "555", "20", "20", "20", "20", java.util.UUID.randomUUID().toString()};

            Map<String, String> attributes = new HashMap<>();
            for (int i = 0; i < pageAttributes.length; i++) {
                attributes.put(pageAttributes[i], pageValues[i]);
            }

            for (String key : attributes.keySet()) {
                rootElement.setAttribute(key, attributes.get(key));
            }

            Element queryString = doc.createElement("queryString");
            int n = new Scanner(System.in).nextInt();
            String columnsNames = "";
            for (int i = 0; i < n; i++) {
                columnsNames += new Scanner(System.in).nextLine();
                if (i != n - 1) {
                    columnsNames += ",";
                }
                columnsNames += " ";
            }
            queryString.appendChild(doc.createCDATASection("SELECT " + columnsNames + "FROM " + tableName));
            rootElement.appendChild(queryString);

            for (Map.Entry<String, String> entry : columns.entrySet()) {
                Element field = doc.createElement("field");
                field.setAttribute("name", entry.getKey());
                field.setAttribute("class", entry.getValue());
                rootElement.appendChild(field);
            }

            Element columnHeader = doc.createElement("columnHeader");
            Element band = doc.createElement("band");
            band.setAttribute("height", "30");

            int x = 0;
            for (String columnName : columns.keySet()) {
                Element staticText = doc.createElement("staticText");
                Element reportElement = doc.createElement("reportElement");
                reportElement.setAttribute("x", String.valueOf(x));
                reportElement.setAttribute("y", "0");
                reportElement.setAttribute("width", "100");
                reportElement.setAttribute("height", "30");
                staticText.appendChild(reportElement);

                Element text = doc.createElement("text");
                text.appendChild(doc.createCDATASection(columnName));
                staticText.appendChild(text);

                band.appendChild(staticText);
                x += 100;
            }
            columnHeader.appendChild(band);
            rootElement.appendChild(columnHeader);

            Element detail = doc.createElement("detail");
            band = doc.createElement("band");
            band.setAttribute("height", "30");

            x = 0;
            for (String columnName : columns.keySet()) {
                Element textField = doc.createElement("textField");
                Element reportElement = doc.createElement("reportElement");
                reportElement.setAttribute("x", String.valueOf(x));
                reportElement.setAttribute("y", "0");
                reportElement.setAttribute("width", "100");
                reportElement.setAttribute("height", "30");
                textField.appendChild(reportElement);

                Element textFieldExpression = doc.createElement("textFieldExpression");
                textFieldExpression.appendChild(doc.createCDATASection("$F{" + columnName + "}"));
                textField.appendChild(textFieldExpression);

                band.appendChild(textField);
                x += 100;
            }
            detail.appendChild(band);
            rootElement.appendChild(detail);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:/Users/s.adilkhanov/IdeaProjects/ReportGenerator/src/main/java/solo/adilkhanov/" + tableName + "_report.jrxml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
