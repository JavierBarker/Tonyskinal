package org.josehernandez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.josehernandez.bean.Empleados;
import org.josehernandez.bean.Servicios;
import org.josehernandez.bean.Servicios_Has_Empleados;
import org.josehernandez.system.MainApp;

public class Servicios_Has_EmpleadosViewController implements Initializable {
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList <Servicios> listaServicio;
    private ObservableList <Empleados> listaEmpleado;
    private ObservableList <Servicios_Has_Empleados> listaServicioHasEmpleado;
    private DatePicker fechaEvento;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TextField txtCodigoS_H_E;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private ComboBox cmbCodigoEmpleado;
    @FXML
    private GridPane grpFechaEvento;
    @FXML
    private TextField txtHoraEvento;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private TableView tblServiciosHasEmpleados;
    @FXML
    private TableColumn colCodigoS_H_E;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    public void desactivarControles(){
        txtCodigoS_H_E.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
        grpFechaEvento.setDisable(true);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoS_H_E.setEditable(false);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoS_H_E.setText(null);
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        fechaEvento.selectedDateProperty().set(null);
        txtHoraEvento.setText(null);
        txtLugarEvento.setText(null);
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
                if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro?", "EliminarServicioHasEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicioHasEmpleado(?)}");
                            procedimiento.setInt(1, ((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServicioHasEmpleado.remove(tblServiciosHasEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Servicio_Has_Empleado eliminado con exito!");
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
                if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarServicioHasEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
        Servicios_Has_Empleados registro = new Servicios_Has_Empleados();
        registro.setCodigoServicio(((Servicios)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setEmpleados_codigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fechaEvento.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicioHasEmpleado(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2, registro.getEmpleados_codigoEmpleado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4, registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
            procedimiento.execute();
            listaServicioHasEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicioHasEmpleado(?,?,?,?)}");
            Servicios_Has_Empleados registro = (Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(fechaEvento.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServicios_Has_Empleados());
        colCodigoS_H_E.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, Integer>("servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, Integer>("empleados_codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_Has_Empleados, String>("lugarEvento"));
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        desactivarControles();
    }
    
    public ObservableList <Servicios_Has_Empleados> getServicios_Has_Empleados(){
        ArrayList<Servicios_Has_Empleados> lista = new ArrayList<Servicios_Has_Empleados>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServiciosHasEmpleados()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Servicios_Has_Empleados(resultado.getInt("servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("empleados_codigoEmpleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getString("horaEvento"),
                        resultado.getString("lugarEvento")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicioHasEmpleado = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Empleados> getEmpleado(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                       resultado.getInt("numeroEmpleado"),
                       resultado.getString("apellidosEmpleado"),
                       resultado.getString("nombresEmpleado"),
                       resultado.getString("direccionEmpleado"),
                       resultado.getString("telefonoContacto"),
                       resultado.getString("gradoCocinero"),
                       resultado.getInt("tipoEmpleado_codigoTipoEmpleado")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
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
    
    public Empleados buscarEmpleado(int codigoEmpleado){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(
                                registro.getInt("codigoEmpleado"),
                                registro.getInt("numeroEmpleado"),
                                registro.getString("apellidosEmpleado"),
                                registro.getString("nombresEmpleado"),
                                registro.getString("direccionEmpleado"),
                                registro.getString("telefonoContacto"),
                                registro.getString("gradoCocinero"),
                                registro.getInt("tipoEmpleado_codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        txtCodigoS_H_E.setText(String.valueOf(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getEmpleados_codigoEmpleado()));
        fechaEvento.selectedDateProperty().set(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(String.valueOf(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
        txtLugarEvento.setText(String.valueOf(((Servicios_Has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
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
        fechaEvento = new DatePicker(Locale.ENGLISH);
        fechaEvento.setMaxHeight(32);
        fechaEvento.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fechaEvento.getCalendarView().todayButtonTextProperty().set("Today");
        fechaEvento.getCalendarView().setShowWeeks(false);
        fechaEvento.getStylesheets().add("org/josehernandez/resource/DatePicker.css");
        grpFechaEvento.add(fechaEvento, 0, 0);
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
