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
import org.josehernandez.bean.Servicios;
import org.josehernandez.report.GenerarReporte;
import org.josehernandez.system.MainApp;

public class ServicioViewController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Empresas> listaEmpresa;
    private ObservableList<Servicios> listaServicio;
    private DatePicker fechaSolicitud;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private GridPane grpFechaServicio;
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtHoraServicio;
    @FXML
    private TextField txtLugarServicio;
    @FXML
    private TextField txtTelefonoContacto;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblServicios;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn colTelefonoContacto;
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
        txtCodigoServicio.setEditable(false);
        grpFechaServicio.setDisable(true);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        grpFechaServicio.setDisable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.setText(null);
        fechaSolicitud.selectedDateProperty().set(null);
        txtTipoServicio.setText(null);
        txtHoraServicio.setText(null);
        txtLugarServicio.setText(null);
        txtTelefonoContacto.setText(null);
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
                guardar();
                desactivarControles();
                limpiarControles();
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
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro? \n ADVERTENCIA: Al eliminar el Servicio, eliminará registros de Servicios_Has_Empleados \n y Servicios_Has_Platos que tenga ese Servicio.", "EliminarServicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                            procedimiento.setInt(1, ((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            //cargarDatos();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Servicio eliminado con exito!");
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
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarServicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
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
        int codServicio = Integer.valueOf(txtCodigoServicio.getText());
        parametros.put("codServicio", codServicio);
        GenerarReporte.mostrarReporte("ReporteServicioFinal.jasper", "Reporte Servicio", parametros);
    }
    
    
    public void guardar(){
        Servicios registro = new Servicios();
        registro.setFechaServicio(fechaSolicitud.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setEmpresas_codigoEmpresa(((Empresas)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getEmpresas_codigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?)}");
            Servicios registro = (Servicios)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fechaSolicitud.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setString(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("empresas_codigoEmpresa"));
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    
    public ObservableList<Servicios> getServicio(){
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
        txtCodigoServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fechaSolicitud.selectedDateProperty().set(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
        txtHoraServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
        txtLugarServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefonoContacto.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getEmpresas_codigoEmpresa()));
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
        grpFechaServicio.add(fechaSolicitud, 0, 0);
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
