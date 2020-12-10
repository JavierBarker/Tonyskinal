package org.josehernandez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.josehernandez.bd.Conexion;
import org.josehernandez.bean.Empresas;
import org.josehernandez.bean.Presupuestos;
import org.josehernandez.report.GenerarReporte;
import org.josehernandez.system.MainApp;

public class PresupuestoViewController implements Initializable {
    private MainApp escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Empresas> listaEmpresa;
    private ObservableList<Presupuestos> listaPresupuesto;
    private DatePicker fechaSolicitud;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TextField txtCodigoPresupuesto;
    @FXML
    private TextField txtCantidadPresupuesto;
    @FXML
    private GridPane grpFechaSolicitud;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblPresupuestos;
    @FXML
    private TableColumn colCodigoPresupuesto;
    @FXML
    private TableColumn colFechaSolicitud;
    @FXML
    private TableColumn colCantidadPresupuesto;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.setText(null);
        fechaSolicitud.selectedDateProperty().set(null);
        txtCantidadPresupuesto.setText(null);
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
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
                //ejecutar metodos para guardar los datos
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                //cargar los nuevos daos en la tabla
                cargarDatos();
            break;
        }
    }

    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Registro para Editar");
                }
            break;
            
            case ACTUALIZAR:
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarPresupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
            
            default:
                //verificar que tenga seleccionado el reguistro de la tabla
                if(tblPresupuestos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro?", "EliminarPresupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1, ((Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            //cargarDatos();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Presupuesto eliminado con exito!");
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
    
    public void reporte(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Registro para el Reporte");
                }
            break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresas)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuestoFinal.jasper", "Reporte Presupuesto", parametros);
    }
    
    public void guardar(){
        Presupuestos registro = new Presupuestos();
        registro.setFechaSolicitud(fechaSolicitud.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setEmpresas_codigoEmpresa(((Empresas)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getEmpresas_codigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");
            Presupuestos registro = (Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fechaSolicitud.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuestos,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuestos, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuestos, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuestos, Integer>("Empresas_codigoEmpresa"));
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    
    public ObservableList<Presupuestos> getPresupuesto(){
        ArrayList<Presupuestos> lista = new ArrayList<Presupuestos>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuestos()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Presupuestos(resultado.getInt("codigoPresupuesto"),
                       resultado.getDate("fechaSolicitud"),
                       resultado.getDouble("cantidadPresupuesto"),
                       resultado.getInt("Empresas_codigoEmpresa")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresas> getEmpresa(){
        ArrayList<Empresas> lista = new ArrayList<Empresas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarEmpresas()}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Empresas(resultado.getInt("codigoEmpresa"), 
                        resultado.getString("nombreEmpresa"), 
                        resultado.getString("direccion"), 
                        resultado.getString("telefono")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public Empresas buscarEmpresa(int codigoEmpresa){
        Empresas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresas(
                                registro.getInt("codigoEmpresa"),
                                registro.getString("nombreEmpresa"),
                                registro.getString("direccion"),
                                registro.getString("telefono"));
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fechaSolicitud.selectedDateProperty().set(((Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuestos)tblPresupuestos.getSelectionModel().getSelectedItem()).getEmpresas_codigoEmpresa()));
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
        fechaSolicitud = new DatePicker(Locale.ENGLISH);
        fechaSolicitud.setMaxHeight(32);
        fechaSolicitud.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fechaSolicitud.getCalendarView().todayButtonTextProperty().set("Today");
        fechaSolicitud.getCalendarView().setShowWeeks(false);
        fechaSolicitud.getStylesheets().add("org/josehernandez/resource/DatePicker.css");
        grpFechaSolicitud.add(fechaSolicitud, 0, 0);
        cargarDatos();
    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    public void empresas(){
        escenarioPrincipal.empresas();
    }
}
