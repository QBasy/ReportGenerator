package solo.adilkhanov;

import java.io.File;
import java.sql.Connection;
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
    public static void getXML(String tableName, String[] columnsName) {
        try (Connection connection = PostgresSQLDB.connect()) {
            Map<String, String> columns = getTableStructure(connection, tableName, columnsName);

            generateXML(columns, tableName, columnsName);

            System.out.println("File saved!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getTableStructure(Connection connection, String tableName, String[] columnsName) throws SQLException {
        Map<String, String> columns = new HashMap<>();

        String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        try (Statement stmt = connection.createStatement(); ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String dataType = resultSet.getString("data_type");
                if (columnsName == null || columnsName.length == 0 || containsIgnoreCase(columnsName, columnName)) {
                    columns.put(columnName, mapDataType(dataType));
                }
            }
        }

        return columns;
    }

    private static boolean containsIgnoreCase(String[] array, String str) {
        for (String s : array) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private static String mapDataType(String dataType) {
        return switch (dataType) {
            case "character varying", "varchar", "text" -> "java.lang.String";
            case "integer" -> "java.lang.Integer";
            case "boolean" -> "java.lang.Boolean";
            default -> "java.lang.Object";
        };
    }

    private static void generateXML(Map<String, String> columns, String tableName, String[] columnsName) {
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
            int n = columnsName.length;
            String columnsNames = "";
            for (int i = 0; i < n; i++) {
                columnsNames += columnsName[i];
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

            Element title = doc.createElement("title");
            Element titleBand = doc.createElement("band");
            titleBand.setAttribute("height", "60");
            titleBand.setAttribute("splitType", "Stretch");

            Element staticTextTitle = doc.createElement("staticText");
            Element reportElementTitle = doc.createElement("reportElement");
            reportElementTitle.setAttribute("x", "140");
            reportElementTitle.setAttribute("y", "0");
            reportElementTitle.setAttribute("width", "290");
            reportElementTitle.setAttribute("height", "20");
            reportElementTitle.setAttribute("uuid", "9fd52a6a-9375-4541-b567-4d27b121d863");
            staticTextTitle.appendChild(reportElementTitle);

            Element textElementTitle = doc.createElement("textElement");
            Element fontTitle = doc.createElement("font");
            fontTitle.setAttribute("fontName", "Times New Roman");
            fontTitle.setAttribute("size", "14");
            fontTitle.setAttribute("isBold", "true");
            textElementTitle.setAttribute("textAlignment", "Center");
            textElementTitle.appendChild(fontTitle);

            Element textTitle = doc.createElement("text");
            textTitle.appendChild(doc.createCDATASection(tableName));
            staticTextTitle.appendChild(textElementTitle);
            staticTextTitle.appendChild(textTitle);
            titleBand.appendChild(staticTextTitle);
            title.appendChild(titleBand);
            rootElement.appendChild(title);

            new Scanner(System.in).nextLine();
            Element columnHeader = doc.createElement("columnHeader");
            Element band = doc.createElement("band");
            band.setAttribute("height", "30");

            int x = 90;
            for (String columnName : columns.keySet()) {
                Element staticText = doc.createElement("staticText");
                Element reportElement = doc.createElement("reportElement");
                reportElement.setAttribute("x", String.valueOf(x));
                reportElement.setAttribute("y", "0");
                reportElement.setAttribute("width", "100");
                reportElement.setAttribute("height", "30");
                staticText.appendChild(reportElement);

                Element box = doc.createElement("box");
                box.setAttribute("topPadding", "0");
                box.setAttribute("leftPadding", "0");
                box.setAttribute("bottomPadding", "0");
                box.setAttribute("rightPadding", "0");

                Element topPen = doc.createElement("topPen");
                topPen.setAttribute("lineWidth", "1.0");

                Element leftPen = doc.createElement("leftPen");
                leftPen.setAttribute("lineWidth", "1.0");

                Element bottomPen  = doc.createElement("bottomPen");
                bottomPen.setAttribute("lineWidth", "1.0");

                Element rightPen = doc.createElement("rightPen");
                rightPen.setAttribute("lineWidth", "1.0");

                box.appendChild(topPen);
                box.appendChild(leftPen);
                box.appendChild(bottomPen);
                box.appendChild(rightPen);

                staticText.appendChild(box);

                Element textElement = doc.createElement("textElement");
                textElement.setAttribute("textAlignment", "Center");

                Element font = doc.createElement("font");
                font.setAttribute("fontName", "Times New Roman");
                font.setAttribute("size", "10");
                font.setAttribute("isBold", "true");

                Element paragraph = doc.createElement("paragraph");
                paragraph.setAttribute("lineSpacing", "Single");

                textElement.appendChild(font);
                textElement.appendChild(paragraph);

                staticText.appendChild(textElement);

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

            x = 90;
            for (String columnName : columns.keySet()) {
                Element textField = doc.createElement("textField");
                Element reportElement = doc.createElement("reportElement");
                reportElement.setAttribute("x", String.valueOf(x));
                reportElement.setAttribute("y", "0");
                reportElement.setAttribute("width", "100");
                reportElement.setAttribute("height", "30");
                textField.appendChild(reportElement);

                Element box = doc.createElement("box");
                box.setAttribute("topPadding", "0");
                box.setAttribute("leftPadding", "0");
                box.setAttribute("bottomPadding", "0");
                box.setAttribute("rightPadding", "0");

                Element topPen = doc.createElement("topPen");
                topPen.setAttribute("lineWidth", "1.0");

                Element leftPen = doc.createElement("leftPen");
                leftPen.setAttribute("lineWidth", "1.0");

                Element bottomPen = doc.createElement("bottomPen");
                bottomPen.setAttribute("lineWidth", "1.0");

                Element rightPen = doc.createElement("rightPen");
                rightPen.setAttribute("lineWidth", "1.0");

                box.appendChild(topPen);
                box.appendChild(leftPen);
                box.appendChild(bottomPen);
                box.appendChild(rightPen);

                textField.appendChild(box);

                Element textElement = doc.createElement("textElement");
                textElement.setAttribute("textAlignment", "Center");

                Element font = doc.createElement("font");
                font.setAttribute("fontName", "Times New Roman");
                font.setAttribute("size", "10");
                font.setAttribute("isBold", "true");

                Element paragraph = doc.createElement("paragraph");
                paragraph.setAttribute("lineSpacing", "Single");

                textElement.appendChild(font);
                textElement.appendChild(paragraph);

                textField.appendChild(textElement);

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
            StreamResult result = new StreamResult(new File( PropertiesReader.getProperties("importPath") + tableName + "_report.jrxml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
