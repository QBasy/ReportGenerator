package solo.adilkhanov;

import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    public void generateReport(String username) {
        try {
            Connection conn = PostgresSQLDB.connect();

            Map<String, Object> parameters = new HashMap<>();

            JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/s.adilkhanov/IdeaProjects/JasperReports/src/main/java/solo/SA/user__report.jrxml");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport, parameters, conn);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/Users/s.adilkhanov/IdeaProjects/JasperReports/src/main/java/solo/SA/reports/report.pdf");

            JasperExportManager.exportReportToPdfFile(jasperPrint1, "C:/Users/s.adilkhanov/IdeaProjects/JasperReports/src/main/java/solo/SA/reports/report2.pdf");
        } catch (SQLException | JRException e) {
            System.out.println(e.getMessage());
        }
    }
}
