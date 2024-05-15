package org.example;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    private Connection getConnection() throws SQLException {
        return PostgresSQLDB.connect();
    }

    private ResultSet executeQuery(String query) throws Exception {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    private JasperDesign loadTemplate(String templatePath) throws Exception {
        return JRXmlLoader.load(templatePath);
    }

    private JasperReport compileReport(JasperDesign design) throws Exception {
        return JasperCompileManager.compileReport(design);
    }

    private JasperPrint fillReport(JasperReport report, ResultSet resultSet) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        return JasperFillManager.fillReport(report, parameters, new JRResultSetDataSource(resultSet));
    }

    private byte[] exportReportToXmlStream(JasperPrint filledReport) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToXmlStream(filledReport, outputStream);
        return outputStream.toByteArray();
    }

    public void generateReport(String sqlQuery, String templatePath, String outputFile) {
        try {
            ResultSet resultSet = executeQuery(sqlQuery);
            JasperDesign design = loadTemplate(templatePath);
            JasperReport report = compileReport(design);
            System.out.println(2);
            JasperPrint filledReport = fillReport(report, resultSet);

            System.out.println(3);
            exportReportToPdfFile(filledReport, outputFile);
            System.out.println("Report generated successfully: " + outputFile);
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private void exportReportToPdfFile(JasperPrint filledReport, String outputFile) throws Exception {
        JasperExportManager.exportReportToPdfFile(filledReport, outputFile);
    }
}
