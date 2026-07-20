/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;

/**
 * Descuento sobre la pensión de un alumno (beca, hermano
 * matriculado, convenio, etc.). Se expresa como porcentaje
 * sobre el monto de la pensión.
 */
public class Descuento implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contador = 1;

    private static String generarCodigo() {
        return "DESC" + String.format("%04d", contador++);
    }

    private String idDescuento;
    private String codigoAlumno;
    private String tipo;       // Beca | Hermano | Convenio | Trabajador | Otro
    private double porcentaje; // 0 a 100
    private String motivo;
    private boolean activo;

    public Descuento(String codigoAlumno, String tipo,
            double porcentaje, String motivo) {
        this.idDescuento = generarCodigo();
        this.codigoAlumno = codigoAlumno;
        this.tipo = tipo;
        setPorcentaje(porcentaje);
        this.motivo = motivo;
        this.activo = true;
    }

    public String getIdDescuento() { return idDescuento; }
    public String getCodigoAlumno() { return codigoAlumno; }
    public String getTipo() { return tipo; }
    public double getPorcentaje() { return porcentaje; }
    public String getMotivo() { return motivo; }
    public boolean isActivo() { return activo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public void setPorcentaje(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException(
                "El porcentaje debe estar entre 0 y 100");
        }
        this.porcentaje = porcentaje;
    }

    public void setMotivo(String motivo) { this.motivo = motivo; }

    public void activar() { this.activo = true; }
    public void desactivar() { this.activo = false; }

    @Override
    public String toString() {
        return tipo + " - " + porcentaje + "% "
                + (activo ? "(activo)" : "(inactivo)");
    }
}
