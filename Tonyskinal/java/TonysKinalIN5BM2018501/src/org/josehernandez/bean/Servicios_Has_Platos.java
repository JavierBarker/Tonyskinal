package org.josehernandez.bean;

public class Servicios_Has_Platos {
    private int codigoServicio;
    private int codigoPlato;

    public Servicios_Has_Platos() {
    }

    public Servicios_Has_Platos(int codigoServicio, int codigoPlato) {
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
    
}
