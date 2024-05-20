package solo.adilkhanov;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    public static void generateReportToPDF(String inputFile) {
        try (Connection conn = PostgresSQLDB.connect()) {
            String fontName = "Times New Roman";
            boolean isBold = true;
            String fontSize = "14";
            JasperPrint jasperPrint = compileAndFillReport(inputFile, fontName, fontSize, isBold, conn);

            String outputPath = PropertiesReader.getProperties("exportPath") + inputFile + "report.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            System.out.println("PDF Report generated successfully!");
        } catch (SQLException | JRException e) {
            System.out.println("Error generating PDF report: " + e.getMessage());
        }
    }

    private static String compileReport(String inputFile) throws JRException {
        String destination = PropertiesReader.getProperties("importPath") + inputFile;
        String compiledReportPath = destination.replace(".jrxml", ".jasper");
        JasperCompileManager.compileReportToFile(destination, compiledReportPath);

        return compiledReportPath;
    }

    public static void generateReportToHTML(String inputFile) {
        try {
            String destination = PropertiesReader.getProperties("importPath") + inputFile;

            JasperExportManager.exportReportToHtmlFile(destination, PropertiesReader.getProperties("exportPath") + inputFile + "report.xml");
            System.out.println("Success!!!");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateReportToXML(String inputFile) {
        try {
            String destination = PropertiesReader.getProperties("importPath") + inputFile;

            JasperExportManager.exportReportToXmlFile(destination, PropertiesReader.getProperties("exportPath") + inputFile + "report.xml", false);
            System.out.println("Success!!!");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    private static JasperPrint compileAndFillReport(String inputFile, String fontName, String fontSize, boolean isBold, Connection conn) throws JRException {
        String compiledReportPath = compileReport(inputFile);
        Map<String, Object> parameters = new HashMap<>();

        if (fontName != null && fontSize != null) {
            parameters.put("fontName", fontName);
            parameters.put("fontSize", fontSize);
            parameters.put("isBold", isBold);
        }

        return JasperFillManager.fillReport(compiledReportPath, parameters, conn);
    }
}
