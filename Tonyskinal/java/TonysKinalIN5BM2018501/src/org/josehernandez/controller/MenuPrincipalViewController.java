package org.josehernandez.controller;
import org.josehernandez.system.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class MenuPrincipalViewController implements Initializable {
    private MainApp escenarioPrincipal;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    //aqui van los metodos en la cual abriran una ventana desde esta ventana
    public void datosPersonales(){
        escenarioPrincipal.datosPersonales();
    }
    
    public void empresas(){
        escenarioPrincipal.empresas();
    }
    
    public void tiposEmpleados(){
        escenarioPrincipal.tiposEmpleados();
    }
    
    public void tiposPlatos() {
        escenarioPrincipal.tiposPlatos();
    }
    
    public void productos(){
        escenarioPrincipal.productos();
    }
    
    
    
    
    public void servicios_has_platos(){
        escenarioPrincipal.servicios_has_platos();
    }
    
    public void productos_has_platos(){
        escenarioPrincipal.productos_has_platos();
    }
    
    public void servicios_has_empleados(){
        escenarioPrincipal.servicios_has_empleados();
    }
}
