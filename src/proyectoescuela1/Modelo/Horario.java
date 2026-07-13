/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modelo de Horario.
 * Representa el horario de un curso en un día
 * y hora específica, asignado a un aula.
 * Implementa Serializable para persistencia.
 */
public class Horario implements Serializable {

    // Necesario para serialización
    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    /**
     * Genera código automático tipo HOR0001, HOR0002...
     */
    private static String generarCodigo() {
        return "HOR" + String.format("%04d", contador++);
    }

    // ── ATRIBUTOS ─────────────────────────────────
    private String idHorario;
    private String dia;
    // dia: Lunes | Martes | Miércoles | Jueves | Viernes
    private String horaInicio;
    private String horaFin;
    private String aula;
    // tipo aula: Regular | Cómputo | Ciencias | Audiovisual
    private String nivel;
    // nivel: Inicial | Primaria | Secundaria

    /**
     * Curso asignado a este horario.
     * Un horario pertenece a un curso.
     */
    private Curso curso;

    /**
     * Docente asignado a este horario.
     */
    private Docente docente;

    /**
     * Lista de secciones que tienen este horario.
     * Una misma hora puede tener varias secciones
     * en distintas aulas.
     */
    private List<String> secciones = new ArrayList<>();

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Crea un nuevo horario con código automático.
     */
    public Horario(String dia, String horaInicio,
                   String horaFin, String aula,
                   String nivel) {
        this.idHorario  = generarCodigo();
        this.dia        = dia;
        this.horaInicio = horaInicio;
        this.horaFin    = horaFin;
        this.aula       = aula;
        this.nivel      = nivel;
    }

    // ── ITERATOR ──────────────────────────────────
    /**
     * Permite recorrer las secciones asignadas
     * a este horario de forma controlada.
     */
    public Iterator<String> iteratorSecciones() {
        return secciones.iterator();
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    /**
     * Verifica si este horario tiene conflicto
     * con otro horario.
     * Hay conflicto si es el mismo día, misma aula
     * y los horarios se superponen.
     */
    public boolean hayConflicto(Horario otro) {
        // verifica mismo día y misma aula
        if (!this.dia.equals(otro.dia)) return false;
        if (!this.aula.equals(otro.aula)) return false;

        // convierte horas a minutos para comparar
        int inicioA = convertirAMinutos(this.horaInicio);
        int finA    = convertirAMinutos(this.horaFin);
        int inicioB = convertirAMinutos(otro.horaInicio);
        int finB    = convertirAMinutos(otro.horaFin);

        // hay conflicto si los rangos se superponen
        return inicioA < finB && finA > inicioB;
    }

    /**
     * Convierte una hora en formato HH:MM
     * a minutos totales para comparar.
     * Ejemplo: "08:30" → 510 minutos
     */
    private int convertirAMinutos(String hora) {
        String[] partes = hora.split(":");
        int horas    = Integer.parseInt(partes[0]);
        int minutos  = Integer.parseInt(partes[1]);
        return horas * 60 + minutos;
    }

    /**
     * Calcula la duración del horario en minutos.
     */
    public int getDuracion() {
        return convertirAMinutos(horaFin)
             - convertirAMinutos(horaInicio);
    }

    /**
     * Agrega una sección a este horario.
     */
    public void agregarSeccion(String seccion) {
        if (!secciones.contains(seccion)) {
            secciones.add(seccion);
        }
    }

    /**
     * Elimina una sección usando Iterator.
     * Forma segura de eliminar mientras se recorre.
     */
    public void eliminarSeccion(String seccion) {
        Iterator<String> it = secciones.iterator();
        while (it.hasNext()) {
            if (it.next().equals(seccion)) {
                it.remove(); // eliminación segura
                return;
            }
        }
    }

    // ── GETTERS ───────────────────────────────────
    public String  getIdHorario()  { return idHorario; }
    public String  getDia()        { return dia; }
    public String  getHoraInicio() { return horaInicio; }
    public String  getHoraFin()    { return horaFin; }
    public String  getAula()       { return aula; }
    public String  getNivel()      { return nivel; }
    public Curso   getCurso()      { return curso; }
    public Docente getDocente()    { return docente; }
    public List<String> getSecciones() { return secciones; }

    // ── SETTERS ───────────────────────────────────
    /** Cambia el día del horario */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /** Cambia la hora de inicio */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /** Cambia la hora de fin */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /** Cambia el aula asignada */
    public void setAula(String aula) {
        this.aula = aula;
    }

    /** Cambia el nivel educativo */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /** Asigna el curso a este horario */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /** Asigna el docente a este horario */
    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public void setSecciones(List<String> secciones) {
        this.secciones = secciones;
    }

    @Override
    public String toString() {
        return "Horario{" +
               "id='" + idHorario + "'" +
               ", dia='" + dia + "'" +
               ", inicio='" + horaInicio + "'" +
               ", fin='" + horaFin + "'" +
               ", aula='" + aula + "'" +
               ", nivel='" + nivel + "'" +
               "}";
    }
}