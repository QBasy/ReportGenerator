package solo.adilkhanov;

public class Main {
    public static void main(String[] args) {
        String[] columns = {"title", "author", "publication_date", "genre"};
        String tableName = "book";
        DatabaseToXML.getXML(tableName, columns);
        ReportGenerator.generateReport(tableName+"_report.jrxml");
    }
}