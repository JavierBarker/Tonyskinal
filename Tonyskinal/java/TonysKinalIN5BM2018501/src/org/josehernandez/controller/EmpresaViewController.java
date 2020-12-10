package org.josehernandez.controller;
import org.josehernandez.system.MainApp;
import org.josehernandez.bean.Empresas;
import org.josehernandez.bd.Conexion;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.josehernandez.report.GenerarReporte;


public class EmpresaViewController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO };
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private MainApp escenarioPrincipal;
    private ObservableList<Empresas>listaEmpresa;//el ObservableList controla los objetos independientes y el otro toma la linea
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccionEmpresa;
    @FXML
    private TableColumn colTelefonoEmpresa;
    @FXML
    private TextField txtCodigoEmpresa;
    @FXML
    private TextField txtNombreEmpresa; 
    @FXML
    private TextField txtDireccionEmpresa;
    @FXML
    private TextField txtTelefonoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnPresupuestos;
    
    //metodo para desactivar los controles
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    
    //metodo para activar controles
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    //metodo para limpiar los controles
    public void limpiarControles(){
        txtCodigoEmpresa.setText(null);
        txtNombreEmpresa.setText(null);
        txtDireccionEmpresa.setText(null);
        txtTelefonoEmpresa.setText(null);
    }
    //metodo para el boton nuevo o insertar
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
                if(tblEmpresas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el Registro? \n ADVERTENCIA: Al elimiar la Empresa, eliminará registros de Presupuesto y Servicios\n que tenga esa Empresa.", "EliminarEmpresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresa(?)}");
                            procedimiento.setInt(1, ((Empresas)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            //cargarDatos();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Empresa eliminada con exito!");
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Actualizar el Registro?", "EditarEmpresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
                imprimirReporte();
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
        parametros.put("codigoEmpresa", null);//en codigo empresa, se puede colocar cualquier atributo, por que no tiene parametros para enviar
        GenerarReporte.mostrarReporte("ReporteEmpresa.jasper", "Reporte de Empresa", parametros);
    }
    
    //metodo para guardar los datos
    public void guardar(){
        Empresas empresaNueva = new Empresas();
        empresaNueva.setNombreEmpresa(txtNombreEmpresa.getText());
        empresaNueva.setDireccion(txtDireccionEmpresa.getText());
        empresaNueva.setTelefono(txtTelefonoEmpresa.getText());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresa(?,?,?)}");
            sp.setString(1,empresaNueva.getNombreEmpresa());
            sp.setString(2,empresaNueva.getDireccion());
            sp.setString(3,empresaNueva.getTelefono());
            sp.execute();
            listaEmpresa.add(empresaNueva);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresa(?,?,?,?)}");
            Empresas empresaActualizada = ((Empresas)tblEmpresas.getSelectionModel().getSelectedItem());
            //obteiendo los datos de la vista al modelo de java
            empresaActualizada.setNombreEmpresa(txtNombreEmpresa.getText());
            empresaActualizada.setDireccion(txtDireccionEmpresa.getText());
            empresaActualizada.setTelefono(txtTelefonoEmpresa.getText());
            //enviando los datos actualizados a ejecutar en el objeto procedimiento almacenado
            sp.setInt(1, empresaActualizada.getCodigoEmpresa());
            sp.setString(2, empresaActualizada.getNombreEmpresa());
            sp.setString(3, empresaActualizada.getDireccion());
            sp.setString(4, empresaActualizada.getTelefono());
            sp.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }//aqui me quede
    }
    //Método para cargar los datos a la vista
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresas, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresas, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresas, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresas, String>("telefono"));
        desactivarControles();
    }
    //Método para ejecutar el procedimiento almacenado y llenar una lista del resultado
    public ObservableList<Empresas> getEmpresa(){
        ArrayList<Empresas> lista = new ArrayList<Empresas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");   
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
    
        //método para seleccionar elementos de la tabla y mostrarlos en los camposde textos
    public void seleccionarElemento(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresas)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(String.valueOf(((Empresas)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa()));
        txtDireccionEmpresa.setText(String.valueOf(((Empresas)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTelefonoEmpresa.setText(String.valueOf(((Empresas)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));
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
    
    public void botonServicios(){
        btnServicios.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void botonPresupuestos(){
        btnPresupuestos.setStyle("-fx-background-color: #B38D6B;");
    }
    
    public void regresar(){
        imgRegresar.setStyle("-fx-opacity: 0.60;");
    }
    public void botonesNormales(){
        btnNuevo.setStyle("-fx-background-color: #d1baa7;");
        btnEliminar.setStyle("-fx-background-color: #d1baa7;");
        btnEditar.setStyle("-fx-background-color: #d1baa7;");
        btnReporte.setStyle("-fx-background-color: #d1baa7;");
        btnServicios.setStyle("-fx-background-color: #d1baa7;");
        btnPresupuestos.setStyle("-fx-background-color: #d1baa7;");
        imgRegresar.setStyle("-fx-opacity: 1;");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
 //aqui se sejecutaran las ventanas desde esta ventana
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void presupuestos(){
        escenarioPrincipal.presupuestos();
    }
    
    public void servicios(){
        escenarioPrincipal.servicios();
    }
}
