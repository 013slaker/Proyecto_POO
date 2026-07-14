/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;

/**
 * Modelo de Nota.
 * Representa la calificación de un alumno
 * en un bimestre específico.
 * Escala vigesimal peruana: 0 a 20.
 * Implementa Serializable para persistencia.
 */
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    /**
     * Genera código automático tipo NOT0001...
     */
    private static String generarCodigo() {
        return "NOT" + String.format("%04d", contador++);
    }

    // ── ATRIBUTOS ─────────────────────────────────
    private String idNota;
    private int    bimestre;
    // bimestre: 1 | 2 | 3 | 4
    private double valor;
    // escala vigesimal: 0.0 a 20.0
    private String tipo;
    // tipo: Parcial | Examen | Trabajo | Promedio
    private String curso;
    // nombre del curso al que pertenece la nota
    private String codigoAlumno;
    // código del alumno al que pertenece

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Crea una nueva nota con código automático.
     */
    public Nota(int bimestre, double valor,
                String tipo, String curso,
                String codigoAlumno) {
        this.idNota       = generarCodigo();
        this.bimestre     = bimestre;
        this.valor        = valor;
        this.tipo         = tipo;
        this.curso        = curso;
        this.codigoAlumno = codigoAlumno;
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    /**
     * Verifica si la nota es aprobatoria.
     * En Perú se aprueba con 11 o más.
     */
    public boolean estaAprobado() {
        return valor >= 11;
    }

    /**
     * Retorna la letra correspondiente a la nota.
     * AD=Logro destacado, A=Logro esperado,
     * B=En proceso, C=En inicio
     */
    public String getLetra() {
        if (valor >= 18) return "AD";
        if (valor >= 14) return "A";
        if (valor >= 11) return "B";
        return "C";
    }

    // ── GETTERS ───────────────────────────────────
    public String getIdNota()       { return idNota; }
    public int    getBimestre()     { return bimestre; }
    public double getValor()        { return valor; }
    public String getTipo()         { return tipo; }
    public String getCurso()        { return curso; }
    public String getCodigoAlumno() { return codigoAlumno; }

    // ── SETTERS ───────────────────────────────────
    /** Cambia el valor de la nota */
    public void setValor(double valor) {
        if (valor < 0 || valor > 20)
            throw new IllegalArgumentException(
                "La nota debe estar entre 0 y 20"
            );
        this.valor = valor;
    }

    /** Cambia el tipo de nota */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /** Cambia el bimestre */
    public void setBimestre(int bimestre) {
        if (bimestre < 1 || bimestre > 4)
            throw new IllegalArgumentException(
                "El bimestre debe ser entre 1 y 4"
            );
        this.bimestre = bimestre;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "Nota{" +
               "id='" + idNota + "'" +
               ", bimestre=" + bimestre +
               ", valor=" + valor +
               ", tipo='" + tipo + "'" +
               ", curso='" + curso + "'" +
               ", letra='" + getLetra() + "'" +
               "}";
    }
}