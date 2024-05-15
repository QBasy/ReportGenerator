package org.example;

public class Main {
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();
        String sqlQuery = "SELECT username, password, email FROM user_";
        String templatePath = "template.jrxml";
        String outputPath = "output.pdf";
        try {
            generator.generateReport(sqlQuery, templatePath, outputPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Hello world!");
    }
}