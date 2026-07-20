/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Modelo de Bimestre.
 * Representa uno de los 4 periodos académicos del
 * Año Escolar, con sus fechas y su estado.
 *
 * Reglas de negocio:
 * - Solo un bimestre puede estar "Activo" a la vez
 *   (esa validación vive en AnioEscolar/PeriodoControlador).
 * - Un bimestre "Planificado" o "Cerrado" no admite
 *   registro de notas ni asistencia.
 */
public class Bimestre implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PLANIFICADO = "Planificado";
    public static final String ACTIVO      = "Activo";
    public static final String CERRADO     = "Cerrado";

    // ── ATRIBUTOS ─────────────────────────────────
    private int    numero;
    // numero: 1 | 2 | 3 | 4
    private Date   fechaInicio;
    private Date   fechaFin;
    private String estado;
    // estado: Planificado | Activo | Cerrado

    // ── CONSTRUCTOR ───────────────────────────────
    public Bimestre(int numero, Date fechaInicio, Date fechaFin) {
        if (numero < 1 || numero > 4) {
            throw new IllegalArgumentException(
                "El bimestre debe ser entre 1 y 4"
            );
        }
        this.numero      = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.estado      = PLANIFICADO;
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    /**
     * Verifica si una fecha cae dentro del rango
     * de este bimestre (inclusive).
     */
    public boolean contieneFecha(Date fecha) {
        if (fecha == null) return false;
        return !fecha.before(fechaInicio) && !fecha.after(fechaFin);
    }

    public boolean estaActivo() {
        return ACTIVO.equals(estado);
    }

    // ── GETTERS ───────────────────────────────────
    public int    getNumero()      { return numero; }
    public Date   getFechaInicio() { return fechaInicio; }
    public Date   getFechaFin()    { return fechaFin; }
    public String getEstado()      { return estado; }

    // ── SETTERS ───────────────────────────────────
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEstado(String estado) {
        if (!estado.equals(PLANIFICADO) &&
            !estado.equals(ACTIVO) &&
            !estado.equals(CERRADO)) {
            throw new IllegalArgumentException("Estado de bimestre inválido.");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Bimestre " + numero + " [" + estado + "]";
    }
}
