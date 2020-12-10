package org.josehernandez.bean;

import java.util.Date;

public class Servicios {
    private int codigoServicio;
    private Date fechaServicio;
    private String tipoServicio;
    private String horaServicio;
    private String lugarServicio;
    private String telefonoContacto;
    private int empresas_codigoEmpresa;

    public Servicios() {
    }

    public Servicios(int codigoServicio, Date fechaServicio, String tipoServicio, String horaServicio, String lugarServicio, String telefonoContacto, int empresas_codigoEmpresa) {
        this.codigoServicio = codigoServicio;
        this.fechaServicio = fechaServicio;
        this.tipoServicio = tipoServicio;
        this.horaServicio = horaServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.empresas_codigoEmpresa = empresas_codigoEmpresa;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(String horaServicio) {
        this.horaServicio = horaServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getEmpresas_codigoEmpresa() {
        return empresas_codigoEmpresa;
    }

    public void setEmpresas_codigoEmpresa(int empresas_codigoEmpresa) {
        this.empresas_codigoEmpresa = empresas_codigoEmpresa;
    }

    @Override
    public String toString() {
        return codigoServicio + " | " + tipoServicio;
    }
    
    
    
}
