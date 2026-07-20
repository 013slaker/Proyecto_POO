/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Modelo de Libreta de Notas.
 * NO es una entidad que se registra manualmente: es un
 * objeto de SOLO LECTURA que resume, para UN alumno, UN
 * curso y UN bimestre, sus notas y el promedio calculado.
 *
 * Se usa para armar el reporte "Libretas de Notas"
 * (módulo Reportes), a diferencia de "Notas" (Académico),
 * que es donde el docente registra las calificaciones.
 */
public class LibretaNotas implements Serializable {

    private static final long serialVersionUID = 1L;

    private String curso;
    private int    bimestre;
    private List<Nota> notas;
    // detalle: Práctica 1, Práctica 2, Práctica 3,
    // Participación, Examen (lo que el docente registró)
    private double promedio;
    // SIEMPRE calculado, nunca ingresado a mano

    public LibretaNotas(String curso, int bimestre,
                         List<Nota> notas, double promedio) {
        this.curso    = curso;
        this.bimestre = bimestre;
        this.notas    = notas;
        this.promedio = promedio;
    }

    /**
     * Busca el valor de un tipo de nota específico
     * dentro del detalle (ej. "Práctica 1").
     * Retorna null si el docente aún no la registró.
     */
    public Double getValorPorTipo(String tipo) {
        return notas.stream()
                .filter(n -> n.getTipo().equalsIgnoreCase(tipo))
                .map(Nota::getValor)
                .findFirst()
                .orElse(null);
    }

    public String getLetra() {
        if (promedio >= 18) return "AD";
        if (promedio >= 14) return "A";
        if (promedio >= 11) return "B";
        return "C";
    }

    public String  getCurso()    { return curso; }
    public int     getBimestre() { return bimestre; }
    public List<Nota> getNotas() { return notas; }
    public double  getPromedio() { return promedio; }
}

