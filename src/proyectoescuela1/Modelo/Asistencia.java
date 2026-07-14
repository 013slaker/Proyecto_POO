/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modelo de Asistencia.
 * Representa el registro diario de asistencia
 * de un alumno.
 * Estados: P=Presente, T=Tardanza,
 *          F=Falta, J=Justificada
 * Implementa Serializable para persistencia.
 */
public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    /**
     * Genera código automático tipo ASI0001...
     */
    private static String generarCodigo() {
        return "ASI" + String.format("%04d", contador++);
    }

    // ── ATRIBUTOS ─────────────────────────────────
    private String idAsistencia;
    private Date   fecha;
    private String estado;
    // P=Presente T=Tardanza F=Falta J=Justificada
    private String codigoAlumno;
    // código del alumno al que pertenece
    private String observacion;
    // observación opcional

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Crea un nuevo registro de asistencia.
     */
    public Asistencia(Date fecha, String estado,
                      String codigoAlumno,
                      String observacion) {
        this.idAsistencia = generarCodigo();
        this.fecha        = fecha;
        this.estado       = estado;
        this.codigoAlumno = codigoAlumno;
        this.observacion  = observacion;
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    /**
     * Verifica si la asistencia es una falta
     * (incluye tardanza).
     */
    public boolean esFalta() {
        return estado.equals("F") || estado.equals("T");
    }

    /**
     * Verifica si la falta está justificada.
     */
    public boolean esJustificada() {
        return estado.equals("J");
    }

    /**
     * Verifica si estuvo presente.
     */
    public boolean esPresente() {
        return estado.equals("P");
    }

    /**
     * Retorna el nombre completo del estado.
     */
    public String getNombreEstado() {
        switch (estado) {
            case "P": return "Presente";
            case "T": return "Tardanza";
            case "F": return "Falta";
            case "J": return "Justificada";
            default:  return "Desconocido";
        }
    }

    /**
     * Retorna la fecha formateada dd/MM/yyyy.
     */
    public String getFechaFormateada() {
        SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    // ── GETTERS ───────────────────────────────────
    public String getIdAsistencia() { return idAsistencia; }
    public Date   getFecha()        { return fecha; }
    public String getEstado()       { return estado; }
    public String getCodigoAlumno() { return codigoAlumno; }
    public String getObservacion()  { return observacion; }

    // ── SETTERS ───────────────────────────────────
    /** Cambia el estado de asistencia */
    public void setEstado(String estado) {
        if (!estado.equals("P") && !estado.equals("T") &&
            !estado.equals("F") && !estado.equals("J"))
            throw new IllegalArgumentException(
                "Estado inválido. Use P, T, F o J"
            );
        this.estado = estado;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Asistencia{" +
               "id='" + idAsistencia + "'" +
               ", fecha='" + getFechaFormateada() + "'" +
               ", estado='" + getNombreEstado() + "'" +
               ", alumno='" + codigoAlumno + "'" +
               "}";
    }
}