package solo.adilkhanov;

public class Main {
    public static void main(String[] args) {
        String[] columns = {"title", "author", "isbn", "genre"};
        String tableName = "book";
        DatabaseToXML.getXML(tableName, columns);
        ReportGenerator.generateReport(tableName+"_report.jrxml");
    }
}