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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.josehernandez.bd.Conexion;
import org.josehernandez.bean.Platos;
import org.josehernandez.bean.Servicios;
import org.josehernandez.bean.Servicios_Has_Platos;
import org.josehernandez.system.MainApp;

public class Servicios_Has_PlatosViewController implements Initializable {
    private ObservableList <Servicios> listaServicio;
    private ObservableList <Platos> listaPlato;
    private ObservableList <Servicios_Has_Platos>listaServicioHasPlato;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private TableView tblServiciosHasPlatos;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoPlato;
    
    
    
    public void cargarDatos(){
        tblServiciosHasPlatos.setItems(getServicios_Has_Platos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Platos, Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Platos, Integer>("codigoPlato"));
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlato.setItems(getPlatos());
    }
    
    
    public ObservableList <Servicios_Has_Platos> getServicios_Has_Platos(){
        ArrayList <Servicios_Has_Platos> lista = new ArrayList<Servicios_Has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServiciosHasPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_Has_Platos(resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Servicios> getServicio(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("empresas_codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
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
    
    public Servicios buscarServicio(int codigoServicio){
        Servicios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicios(
                                registro.getInt("codigoServicio"),
                                registro.getDate("fechaServicio"),
                                registro.getString("tipoServicio"),
                                registro.getString("horaServicio"),
                                registro.getString("lugarServicio"),
                                registro.getString("telefonoContacto"),
                                registro.getInt("empresas_codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public void seleccionarElemento(){
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_Has_Platos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_Has_Platos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
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
