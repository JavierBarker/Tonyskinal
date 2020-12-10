package org.josehernandez.bean;
public class Empresas {//se crea una clase por cada tabla de la base de datos
    private int codigoEmpresa;// va una variable por cada campo en una tabla de base de datos
    private String nombreEmpresa;
    private String direccion;
    private String telefono;

    public Empresas() {//este constructor vacio sirve para poder usar la clase sin necesidad de poner los parámetros
    }

    public Empresas(int codigoEmpresa, String nombreEmpresa, String direccion, String telefono) {
        this.codigoEmpresa = codigoEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return codigoEmpresa + " | " + nombreEmpresa ;
    }

    
           
}
