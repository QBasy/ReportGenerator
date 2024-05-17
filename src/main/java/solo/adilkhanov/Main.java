package solo.adilkhanov;

public class Main {
    public static void main(String[] args) {
        System.out.println("App Born");

        ReportGenerator generator = new ReportGenerator();
        String sqlQuery = "SELECT username, password, email FROM user_";
        String templatePath = "C:/Users/s.adilkhanov/IdeaProjects/ReportGenerator/src/main/java/solo/adilkhanov/file.jrxml";
        String outputPath = "C:/Users/s.adilkhanov/IdeaProjects/ReportGenerator/src/main/java/solo/adilkhanov/output.pdf";
        try {
            generator.generateReport(sqlQuery, templatePath, outputPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("App Dead");
    }
}