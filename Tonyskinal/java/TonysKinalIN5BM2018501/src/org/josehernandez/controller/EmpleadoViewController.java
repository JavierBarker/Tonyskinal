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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.josehernandez.bd.Conexion;
import org.josehernandez.bean.Empleados;
import org.josehernandez.bean.TipoEmpleado;
import org.josehernandez.system.MainApp;

public class EmpleadoViewController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleados> listaEmpleado;
    private MainApp escenarioPrincipal;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNumeroEmpleado;
    @FXML
    private TextField txtApellidosEmpleado;
    @FXML
    private TextField txtNombresEmpleado;
    @FXML
    private TextField txtDireccionEmpleado;
    @FXML
    private TextField txtGradoCocinero;
    @FXML
    private ComboBox cmbCodigoTipoEmpleado;
    @FXML
    private TextField txtTelefonoContacto;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colNumeroEmpleado;
    @FXML
    private TableColumn colApellidosEmpleado;
    @FXML
    private TableColumn colNombresEmpleado;
    @FXML
    private TableColumn colDireccionEmpleado;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colGradoCocinero;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText(null);
        txtNumeroEmpleado.setText(null);
        txtApellidosEmpleado.setText(null);
        txtNombresEmpleado.setText(null);
        txtDireccionEmpleado.setText(null);
        txtTelefonoContacto.setText(null);
        txtGradoCocinero.setText(null);
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
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

    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
                if(tblEmpleados.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro? \n ADVERTENCIA: Al eliminar el Empleado, eliminará registros de Servicios_Has_Empleados \n que tenga el Empleado.", "EliminarEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            //cargarDatos();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Empleado eliminado con exito!");
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
    
    public void guardar(){
        Empleados registro = new Empleados();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registro.setNombresEmpleado(txtNombresEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setTipoEmpleado_codigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getTipoEmpleado_codigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?)}");
            Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados,Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleados, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("tipoEmpleado_codigoTipoEmpleado"));
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        desactivarControles();
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
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTiposEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"), 
                        resultado.getString("descripcion")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(
                                registro.getInt("codigoTipoEmpleado"),
                                registro.getString("descripcion"));
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidosEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
        txtNombresEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
        txtDireccionEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
        txtTelefonoContacto.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        txtGradoCocinero.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero()));
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTipoEmpleado_codigoTipoEmpleado()));
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
    
    
    
    
    
    
    
    
    public void tiposEmpleados(){
        escenarioPrincipal.tiposEmpleados();
    }
}
