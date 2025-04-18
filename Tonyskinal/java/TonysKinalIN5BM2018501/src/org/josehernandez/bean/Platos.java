package org.josehernandez.bean;

public class Platos {
    private int codigoPlato;
    private int cantidad;
    private String nombrePlato;
    private String descripcionPlato;
    private double precioPlato;
    private int tipoPlato_codigoTipoPlato;

    public Platos() {
    }

    public Platos(int codigoPlato, int cantidad, String nombrePlato, String descripcionPlato, double precioPlato, int tipoPlato_codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.tipoPlato_codigoTipoPlato = tipoPlato_codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getTipoPlato_codigoTipoPlato() {
        return tipoPlato_codigoTipoPlato;
    }

    public void setTipoPlato_codigoTipoPlato(int tipoPlato_codigoTipoPlato) {
        this.tipoPlato_codigoTipoPlato = tipoPlato_codigoTipoPlato;
    }

    @Override
    public String toString() {
        return codigoPlato + " | " + nombrePlato;
    }
    
    
}
