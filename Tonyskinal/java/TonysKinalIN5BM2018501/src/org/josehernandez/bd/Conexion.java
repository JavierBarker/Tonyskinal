package org.josehernandez.bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
    private Connection conexion;//se crea la variable de tipo Connection para poder hacer despues el Patrón Singletón
    private static Conexion instancia;
    public Conexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();//carga el Diver
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtonyskinal2018501?useSSL=false","root","root");//indica la base de datos a la cual se hara la conexión y se le agrega el usuario y contraseña
            
        }catch(ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e){
            
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    //metodo estatico para el patron singleton, que solo dara una sola conexion
    public static Conexion getInstance(){
        if (instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
}            
