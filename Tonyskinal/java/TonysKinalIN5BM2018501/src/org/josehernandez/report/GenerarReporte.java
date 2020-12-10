package org.josehernandez.report;
import net.sf.jasperreports.engine.util.JRLoader;//busca el archivo
import net.sf.jasperreports.engine.JasperReport;//obtiene el archivo seleccionado
import net.sf.jasperreports.engine.JasperPrint;//hace que podamos ver el archivo impreso
import net.sf.jasperreports.engine.JasperPrintManager;//es el manejador del JasperPrint
import net.sf.jasperreports.engine.JasperFillManager;//es para saber cual quiero imprimir 
import net.sf.jasperreports.view.JasperViewer;// hace que podamos ver todo
import java.util.Map;//interfaz a utilizar
import java.io.InputStream;//levanta los archivos
import org.josehernandez.bd.Conexion;
public class GenerarReporte {
    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
