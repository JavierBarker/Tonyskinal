package org.josehernandez.bean;

import java.util.Date;

public class Servicios_Has_Empleados {
    private int servicios_codigoServicio;
    private int codigoServicio;
    private int empleados_codigoEmpleado;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;

    public Servicios_Has_Empleados() {
    }

    public Servicios_Has_Empleados(int servicios_codigoServicio, int codigoServicio, int empleados_codigoEmpleado, Date fechaEvento, String horaEvento, String lugarEvento) {
        this.servicios_codigoServicio = servicios_codigoServicio;
        this.codigoServicio = codigoServicio;
        this.empleados_codigoEmpleado = empleados_codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getServicios_codigoServicio() {
        return servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int servicios_codigoServicio) {
        this.servicios_codigoServicio = servicios_codigoServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getEmpleados_codigoEmpleado() {
        return empleados_codigoEmpleado;
    }

    public void setEmpleados_codigoEmpleado(int empleados_codigoEmpleado) {
        this.empleados_codigoEmpleado = empleados_codigoEmpleado;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    
    
    
}
