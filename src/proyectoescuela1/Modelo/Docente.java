/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Modelo de Docente
 *
 * Hereda de Usuario e implementa Serializable para poder
 * guardar los datos en archivos (.dat).
 */
public class Docente extends Usuario implements Serializable {

    // Identificador para Serialización
    private static final long serialVersionUID = 1L;

    //==================================================
    // GENERADOR AUTOMÁTICO DE CÓDIGO
    //==================================================

    private static int contador = 1;

    private static String generarCodigoDocente() {
        return "DOC" + String.format("%04d", contador++);
    }

    //==================================================
    // ATRIBUTOS
    //==================================================

    private String codigoDocente;
    private String especialidad;
    private String nivel;
    private Date fechaIngreso;
    private boolean estadoActivo;

    //==================================================
    // COMPOSICIÓN
    //==================================================

    // Un docente puede dictar varios cursos
    private List<Curso> cursos = new ArrayList<>();

    // Un docente puede tener varios horarios
    private List<Horario> horarios = new ArrayList<>();

    // Registro de asistencias del docente
    private List<Asistencia> asistencias = new ArrayList<>();

    //==================================================
    // CONSTRUCTOR
    //==================================================

    public Docente(int id,
                   String nombre,
                   String apellidos,
                   String dni,
                   String email,
                   String telefono,
                   String direccion,
                   Date fechaNac,
                   String especialidad,
                   String nivel,
                   Date fechaIngreso) {

        super(id,
              nombre,
              apellidos,
              dni,
              email,
              telefono,
              direccion,
              fechaNac);

        this.codigoDocente = generarCodigoDocente();
        this.especialidad = especialidad;
        this.nivel = nivel;
        this.fechaIngreso = fechaIngreso;
        this.estadoActivo = true;
    }

    //==================================================
    // ITERATOR
    //==================================================

    /**
     * Permite recorrer la lista de cursos
     */
    public Iterator<Curso> iteratorCursos() {
        return cursos.iterator();
    }

    /**
     * Permite recorrer los horarios
     */
    public Iterator<Horario> iteratorHorarios() {
        return horarios.iterator();
    }

    /**
     * Permite recorrer las asistencias
     */
    public Iterator<Asistencia> iteratorAsistencias() {
        return asistencias.iterator();
    }

    //==================================================
    // GETTERS
    //==================================================

    public String getCodigoDocente() {
        return codigoDocente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getNivel() {
        return nivel;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    //==================================================
    // SETTERS
    //==================================================

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setNivel(String nivel) {

        if (!nivel.equals("Inicial")
                && !nivel.equals("Primaria")
                && !nivel.equals("Secundaria")) {

            throw new IllegalArgumentException(
                    "Nivel inválido."
            );
        }

        this.nivel = nivel;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    //==================================================
    // MÉTODOS AUXILIARES
    //==================================================

    /**
     * Agrega un curso al docente.
     */
    public void agregarCurso(Curso curso) {
        cursos.add(curso);
    }

    /**
     * Agrega un horario.
     */
    public void agregarHorario(Horario horario) {
        horarios.add(horario);
    }

    /**
     * Agrega una asistencia.
     */
    public void agregarAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia);
    }

   // Activa al docente.    
    public void activar() {
        estadoActivo = true;
    }

    // * Desactiva al docente.
     
    public void desactivar() {
        estadoActivo = false;
    }

    //Devuelve la cantidad de cursos asignados.
     
    public int cantidadCursos() {
        return cursos.size();
    }

    //  Devuelve la cantidad de horarios.
    
    public int cantidadHorarios() {
        return horarios.size();
    }

    // Devuelve la cantidad de asistencias.
    
    public int cantidadAsistencias() {
        return asistencias.size();
    }

    //==================================================
    // toString
    //==================================================

    @Override
    public String toString() {

        return "Docente{"
                + "codigo='" + codigoDocente + '\''
                + ", nombre='" + getNombreCompleto() + '\''
                + ", especialidad='" + especialidad + '\''
                + ", nivel='" + nivel + '\''
                + ", estado=" + (estadoActivo ? "Activo" : "Inactivo")
                + '}';
    }

}