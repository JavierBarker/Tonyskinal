package org.josehernandez.bean;

public class Productos_Has_Platos {
    private int codigoPlato;
    private int codigoProducto;

    public Productos_Has_Platos() {
    }

    public Productos_Has_Platos(int codigoPlato, int codigoProducto) {
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    
}
