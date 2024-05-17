package solo.adilkhanov;

import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    public static void generateReport(String inputFile) {
        try {
            Connection conn = PostgresSQLDB.connect();

            Map<String, Object> parameters = new HashMap<>();

            JasperReport jasperReport = JasperCompileManager.compileReport(PropertiesReader.getProperties("importPath") + inputFile);

            JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport, parameters, conn);

            JasperExportManager.exportReportToPdfFile(jasperPrint1, PropertiesReader.getProperties("exportPath") + "report.pdf");
            System.out.println("Success!!!");
        } catch (SQLException | JRException e) {
            System.out.println(e.getMessage());
        }
    }
}
