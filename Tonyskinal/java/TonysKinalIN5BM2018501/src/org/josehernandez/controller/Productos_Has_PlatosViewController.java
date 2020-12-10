package org.josehernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.josehernandez.bd.Conexion;
import org.josehernandez.bean.Platos;
import org.josehernandez.bean.Productos;
import org.josehernandez.bean.Productos_Has_Platos;
import org.josehernandez.system.MainApp;


public class Productos_Has_PlatosViewController implements Initializable {
    private ObservableList <Platos> listaPlato;
    private ObservableList <Productos> listaProducto;
    private ObservableList <Productos_Has_Platos> listaProductoHasPlato;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private ComboBox cmbCodigoProducto;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private TableView tblProductosHasPlatos;
    @FXML
    private TableColumn colCodigoProducto;
    @FXML
    private TableColumn colCodigoPlato;

    
    
    
    
    
    
    
    
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductos_Has_Platos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_Has_Platos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_Has_Platos, Integer>("codigoProducto"));
        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoProducto.setItems(getProductos());
    }
    
    
    public ObservableList <Productos_Has_Platos> getProductos_Has_Platos(){
        ArrayList <Productos_Has_Platos> lista = new ArrayList<Productos_Has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductosHasPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos_Has_Platos(resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductoHasPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Platos> getPlatos(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Platos(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("tipoPlato_codigoTipoPlato")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Productos> getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos(resultado.getInt("codigoProducto"), 
                resultado.getString("nombreProducto"),
                resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public Platos buscarPlato(int codigoPlato){
        Platos resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Platos(
                                registro.getInt("codigoPlato"),
                                registro.getInt("cantidad"),
                                registro.getString("nombrePlato"),
                                registro.getString("descripcionPlato"),
                                registro.getDouble("precioPlato"),
                                registro.getInt("tipoPlato_codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Productos buscarProducto(int codigoProducto){
        Productos resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(
                                registro.getInt("codigoProducto"), 
                                registro.getString("nombreProducto"),
                                registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_Has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_Has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public void regresar(){
        imgRegresar.setStyle("-fx-opacity: 0.60;");
    }
    public void botonesNormales(){
        imgRegresar.setStyle("-fx-opacity: 1;");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
