package solo.adilkhanov;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        String[] columns = {"title", "author", "isbn", "genre"};
        String tableName = "book";
        DatabaseToXML.getXML(tableName, columns);
        ReportGenerator.generateReportToPDF(tableName+"_report.jrxml");

        String[] columns2 = {"username", "first_name", "email", "active", "last_name", "time_zone_id"};
        String tableName2 = "user_";
        String query = "SELECT " + String.join(", ", columns2) + " FROM " + tableName2 + " WHERE email IS NOT NULL;";
        System.out.println(query);
        DatabaseToXML.getXML(tableName2, columns2, query);
        */

        List<String> columns = new ArrayList<>();
        columns.add(0, "id");
        columns.add(1, "name");
        columns.add(2,"date_contract");
        columns.add(3,"name_kz");
        columns.add(4,"date_start");
        columns.add(5,"date_end");
        String tableName = "vvtapi_dic_contracts";
        String[] columnsNames = columns.toArray(new String[columns.size()]);

        String query = "SELECT " + String.join(", ", columnsNames) + " FROM " + tableName + " WHERE name IS NOT NULL GROUP BY id ORDER BY id ASC;";
        System.out.println(query);
        System.out.println(columnsNames.length);
        DatabaseToXML.getXML(tableName, columnsNames, query);

        String inputFile = tableName+"_report.jrxml";

        ReportGenerator.generateReportToPDF(inputFile);
        //ReportGenerator.generateReportToHTML(inputFile);
        //ReportGenerator.generateReportToXML(inputFile);
    }
}
