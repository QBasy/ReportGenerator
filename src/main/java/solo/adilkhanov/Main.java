package solo.adilkhanov;

public class Main {
    public static void main(String[] args) {
        String[] columns = {"username", "first_name", "active", "email"};
        String tableName = "user_";
        DatabaseToXML.getXML(tableName, columns);
        ReportGenerator.generateReport(tableName+"_report.jrxml");
    }
}