package org.josehernandez.bean;

import java.util.Date;


public class Presupuestos {
    private int codigoPresupuesto;
    private Date fechaSolicitud;
    private double cantidadPresupuesto;
    private int empresas_codigoEmpresa;

    public Presupuestos() {
    }

    public Presupuestos(int codigoPresupuesto, Date fechaSolicitud, double cantidadPresupuesto, int empresas_codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.empresas_codigoEmpresa = empresas_codigoEmpresa;
    }
    
    
    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(double cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public int getEmpresas_codigoEmpresa() {
        return empresas_codigoEmpresa;
    }

    public void setEmpresas_codigoEmpresa(int empresas_codigoEmpresa) {
        this.empresas_codigoEmpresa = empresas_codigoEmpresa;
    }
    
    
    
}
