package Reportes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
//import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import static org.omnifaces.util.Faces.getServletContext;

public class ReportesRegimp {

    public void ReporteVentaPorEmpleado(HttpServletRequest request, HttpServletResponse response) throws SQLException, JRException {
        Connection a;
        a = DriverManager.getConnection("jdbc:mysql://localhost/regimp");
        JasperReport reporte = null;
        reporte = (JasperReport) JRLoader.loadObjectFromFile("jdbc:mysql://localhost/regimp//VentaPorEmpleado.jasper");
        JasperPrint print = JasperFillManager.fillReport(reporte, null, a);
        JasperViewer ver = new JasperViewer(print);
        ver.setTitle("Venta");
        ver.setVisible(true);
    }

    public void ReporteStockProducto() throws SQLException, JRException, IOException {
        FacesContext contex = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) contex.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment;filname=reporte.pdf");
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        contex.responseComplete();
        try {
            Connection a;
            a = DriverManager.getConnection("jdbc:mysql://localhost:3306/regimp", "root", "12345");
            JasperReport reporte = null;
            reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("WEB-INF/StockProducto.jasper"));
            JasperPrint print = JasperFillManager.fillReport(reporte, null, a);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
        } catch (SQLException | JRException e) {
            throw e;
        }

    }
}
