package solo.adilkhanov;

public class Main {
    public static void main(String[] args) {
        String[] columns = {"username", "active", "first_name"};
        DatabaseToXML.getXML("user_", columns);
        ReportGenerator.generateReport("user__report.jrxml");
    }
}