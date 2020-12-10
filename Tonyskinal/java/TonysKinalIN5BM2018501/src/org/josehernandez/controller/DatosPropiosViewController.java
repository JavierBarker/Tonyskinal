package org.josehernandez.controller;
import java.awt.event.ActionEvent;
import javafx.scene.control.Label;
import org.josehernandez.system.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;


public class DatosPropiosViewController implements Initializable{
    private MainApp escenarioPrincipal;
    // elementos del archivo fxml
    /*Prefijos para los objetos
    
    Text Field = txt
    Button = btn 
    Table View = tbl
    Column = col
    Label = lbl
    
    */
    @FXML
    private ImageView imgRegresar;
    @FXML//ayuda a idendificar que es un elemento del archivo FXML
    private Label lblEtiqueta;//indicamos que elemento del archivo FXML es, pero el nombre se define en Scene Builder
    
    public void regresar(){
        imgRegresar.setStyle("-fx-opacity: 0.60;");
    }
    public void botonesNormales(){
        imgRegresar.setStyle("-fx-opacity: 1;");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //todo lo que se programe en este método, se ejecutara al iniciar la vista
    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    /*public void mostrar(){//en este metodo, muestra que la etiqueta cambie el texto del label
        lblEtiqueta.setText("Hola");
    }*/
   
    //aqui van los metodos en la cual abriran una ventana desde esta ventana
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
