/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Asistencia;

/**
 * Controlador del módulo Asistencia.
 * Maneja registro, búsqueda con Lambda,
 * estadísticas y serialización.
 */
public class AsistenciaControlador {

    /** Lista de todas las asistencias */
    private List<Asistencia> asistencias = new ArrayList<>();

    /** Archivo de persistencia */
    private static final String ARCHIVO = "asistencias.dat";

    /** Controlador de alumnos */
    private AlumnoControlador alumnoControlador =
            new AlumnoControlador();

    // ── CONSTRUCTOR ───────────────────────────────
    public AsistenciaControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    /**
     * Registra una asistencia y la agrega
     * al alumno correspondiente.
     */
    public void registrar(Asistencia asistencia) {
        asistencias.add(asistencia);

        // también la agrega al alumno directamente
        Alumno alumno = alumnoControlador
            .buscarPorCodigo(asistencia.getCodigoAlumno());
        if (alumno != null) {
            alumno.getAsistencias().add(asistencia);
            alumnoControlador.guardarDatos();
        }

        guardarDatos();
        System.out.println("Asistencia registrada: " +
                asistencia.getIdAsistencia());
    }

    /**
     * Retorna todas las asistencias.
     */
    public List<Asistencia> listar() {
        return asistencias;
    }

    /**
     * Elimina una asistencia usando Iterator.
     */
    public void eliminar(String idAsistencia) {
        Iterator<Asistencia> it = asistencias.iterator();
        while (it.hasNext()) {
            Asistencia a = it.next();
            if (a.getIdAsistencia().equals(idAsistencia)) {
                it.remove();
                guardarDatos();
                return;
            }
        }
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Retorna todas las asistencias de un alumno.
     * Lambda: filter por código
     */
    public List<Asistencia> buscarPorAlumno(
            String codigo) {
        return asistencias.stream()
                .filter(a -> a.getCodigoAlumno()
                        .equals(codigo))
                .collect(Collectors.toList());
    }

    /**
     * Filtra por estado (P, T, F, J).
     * Lambda: filter por estado
     */
    public List<Asistencia> buscarPorEstado(
            String codigo, String estado) {
        return asistencias.stream()
                .filter(a -> a.getCodigoAlumno()
                        .equals(codigo) &&
                        a.getEstado().equals(estado))
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  ESTADÍSTICAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Cuenta las faltas de un alumno.
     * Lambda: filter + count
     */
    public long contarFaltas(String codigo) {
        return asistencias.stream()
                .filter(a -> a.getCodigoAlumno()
                        .equals(codigo) &&
                        a.esFalta())
                .count();
    }

    /**
     * Cuenta las presencias de un alumno.
     */
    public long contarPresencias(String codigo) {
        return asistencias.stream()
                .filter(a -> a.getCodigoAlumno()
                        .equals(codigo) &&
                        a.esPresente())
                .count();
    }

    /**
     * Calcula el porcentaje de asistencia.
     * Lambda: count para presencias y total
     */
    public double porcentajeAsistencia(String codigo) {
        long total = asistencias.stream()
                .filter(a -> a.getCodigoAlumno()
                        .equals(codigo))
                .count();
        if (total == 0) return 0.0;
        long presentes = contarPresencias(codigo);
        return (presentes * 100.0) / total;
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    /** Guarda la lista en archivo .dat */
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(asistencias);
        } catch (IOException e) {
            System.out.println("Error al guardar: " +
                    e.getMessage());
        }
    }

    /** Carga la lista desde archivo .dat */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            asistencias =
                (List<Asistencia>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " +
                    e.getMessage());
        }
    }
}