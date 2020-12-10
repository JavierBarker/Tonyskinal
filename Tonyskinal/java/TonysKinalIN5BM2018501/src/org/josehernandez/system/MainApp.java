package org.josehernandez.system;

import org.josehernandez.controller.*;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

public class MainApp extends Application {
    private final String PAQUETE_VISTA = "/org/josehernandez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) throws Exception{//se carga la ventana y el metodo menu principal
        //para iniciar el programa con el fxml
       this.escenarioPrincipal = escenarioPrincipal;
       this.escenarioPrincipal.setTitle("Tony's Kinal App");
       escenarioPrincipal.getIcons().add(new Image("org/josehernandez/img/kinallogo.png"));
       //añade el titulo y un icono
       menuPrincipal();//aqui se inicia el metodo que inicia el fxml
       escenarioPrincipal.show();//inicia todo.
    }
    
    public void menuPrincipal(){//manda parametros a la clase cambiarEscena para poder iniciar el fxml
        try{
            MenuPrincipalViewController menuPrincipalView = (MenuPrincipalViewController) cambiarEscena("MenuPrincipalView.fxml",603,406); 
            menuPrincipalView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void datosPersonales(){
        try{
            DatosPropiosViewController misDatos = (DatosPropiosViewController) cambiarEscena("DatosPropiosView.fxml",603,406);
            misDatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void empresas(){
        try{
            EmpresaViewController miEmpresa = (EmpresaViewController) cambiarEscena("EmpresaView.fxml",742,533);
            miEmpresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void presupuestos(){
        try{
            PresupuestoViewController miPresupuesto = (PresupuestoViewController) cambiarEscena("PresupuestoView.fxml",742,533);
            miPresupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void tiposEmpleados(){
        try{
            TipoEmpleadoViewController miTipoEmpleado = (TipoEmpleadoViewController) cambiarEscena("TipoEmpleadoView.fxml",742,443);
            miTipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void empleados(){
        try{
            EmpleadoViewController miEmpleado = (EmpleadoViewController) cambiarEscena("EmpleadoView.fxml",1110,606);
            miEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void servicios(){
        try{
            ServicioViewController miServicio = (ServicioViewController) cambiarEscena("ServicioView.fxml",805,596);
            miServicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void tiposPlatos(){
        try{
            TipoPlatoViewController miTipoPlato = (TipoPlatoViewController) cambiarEscena("TipoPlatoView.fxml",742,443);
            miTipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void productos(){
        try{
            ProductosViewController miProducto = (ProductosViewController) cambiarEscena("ProductosView.fxml",742,443);
            miProducto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void platos(){
        try{
            PlatosViewController miPlato = (PlatosViewController)cambiarEscena("PlatosView.fxml",805,596);
            miPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void servicios_has_platos(){
        try{
            Servicios_Has_PlatosViewController miServicio_Has_Plato = (Servicios_Has_PlatosViewController)cambiarEscena("Servicios_Has_PlatosView.fxml",683,398);
            miServicio_Has_Plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void productos_has_platos(){
        try{
            Productos_Has_PlatosViewController miProducto_Has_Plato = (Productos_Has_PlatosViewController) cambiarEscena("Productos_Has_PlatosView.fxml",683,398);
            miProducto_Has_Plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void servicios_has_empleados(){
        try{
            Servicios_Has_EmpleadosViewController miServicio_Has_Empleado = (Servicios_Has_EmpleadosViewController) cambiarEscena("Servicios_Has_EmpleadosView.fxml",737,596);
            miServicio_Has_Empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{//lee los parametros y carga el fxml
        Initializable resultado = null;// se crea una variable de tipo Initializable que va a tener todo para poder levantar cualquier archivo fxml
        FXMLLoader cargadorFxml = new FXMLLoader();//el FXMLLoader permite cargar los archivos fxml
        InputStream archivo = MainApp.class.getResourceAsStream(PAQUETE_VISTA+fxml);//va a leer la direccion de cualquier archivo
        cargadorFxml.setBuilderFactory(new JavaFXBuilderFactory());//genera un objeto de tipo javaFx
        cargadorFxml.setLocation(MainApp.class.getResource(PAQUETE_VISTA+fxml));//indica en donde estan los archivosque se van a cargar
        escena = new Scene((AnchorPane)cargadorFxml.load(archivo),ancho,alto);//ese AnchorPane es el que se usa en scene builder, si es diferente deberiamos de cambiarlo por el que se esta usando
        //tambien indica que va a cargar en la escena un AnchorPane y en ese AnchorPane va a cargar el fxml
        escenarioPrincipal.setScene(escena);//añade al escenario principal la escena que uno quiere
        escenarioPrincipal.sizeToScene();//ajusta el escenario principal con el tamaño que tiene la escena que se mando
        resultado = (Initializable)cargadorFxml.getController();// el cargadorFxml.getController trae el archivo fxml, pero como es de tipo Fxml lo castea a Initializable para guardarlo en la variable resultado
        return resultado;//manda un resultado que es el archivo fxml
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
