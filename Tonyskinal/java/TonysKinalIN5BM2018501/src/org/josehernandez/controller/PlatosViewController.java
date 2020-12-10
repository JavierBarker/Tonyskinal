package org.josehernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.josehernandez.bd.Conexion;
import org.josehernandez.bean.Platos;
import org.josehernandez.bean.TipoPlato;
import org.josehernandez.system.MainApp;

public class PlatosViewController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList <TipoPlato> listaTipoPlato;
    private ObservableList <Platos> listaPlatos;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtDescripcionPlato;
    @FXML
    private TextField txtPrecioPlato;
    @FXML
    private ComboBox cmbCodigoTipoPlato;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcionPlato;
    @FXML
    private TableColumn colPrecioPlato;
    @FXML
    private TableColumn colCodigoTipoPlato;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.setText(null);
        txtCantidad.setText(null);
        txtNombrePlato.setText(null);
        txtDescripcionPlato.setText(null);
        txtPrecioPlato.setText(null);
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
            
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro? \n ADVERTENCIA: Al eliminar el Plato, eliminará registros de Servicios_Has_Platos \n que tenga ese Plato.", "EliminarPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                            procedimiento.setInt(1, ((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            limpiarControles();
                            listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            JOptionPane.showMessageDialog(null, "Plato eliminado con exito!");
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un registro en la Tabla");
                }
            break;
        }
    
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Registro para Editar");
                }
            break;
            
            case ACTUALIZAR:
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }
            break;
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void guardar(){
        Platos registro = new Platos();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setTipoPlato_codigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getTipoPlato_codigoTipoPlato());
            procedimiento.execute();
            listaPlatos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?)}");
            Platos registro = ((Platos)tblPlatos.getSelectionModel().getSelectedItem());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Platos, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("tipoPlato_codigoTipoPlato"));
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        desactivarControles();
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
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTiposPlatos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(
                                registro.getInt("codigoTipoPlato"),
                                registro.getString("descripcionTipo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
        txtDescripcionPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
        txtPrecioPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getTipoPlato_codigoTipoPlato()));
    }
    
    public void botonNuevo(){
        btnNuevo.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void botonEliminar(){
        btnEliminar.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void botonEditar(){
        btnEditar.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void botonReporte(){
        btnReporte.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void regresar(){
        imgRegresar.setStyle("-fx-opacity: 0.60;");
    }
    public void botonesNormales(){
        btnNuevo.setStyle("-fx-background-color: #d1baa7;");
        btnEliminar.setStyle("-fx-background-color: #d1baa7;");
        btnEditar.setStyle("-fx-background-color: #d1baa7;");
        btnReporte.setStyle("-fx-background-color: #d1baa7;");
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

    
    
    
    public void tiposPlatos(){
        escenarioPrincipal.tiposPlatos();
    }
    
}
