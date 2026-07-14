/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Asistencia;

import proyectoescuela1.Modelo.RegistroAsistencia;

/**
 * Controlador del módulo de Asistencia.
 *
 * Administra los registros diarios de asistencia, búsquedas, estadísticas y
 * persistencia mediante serialización.
 */
public class AsistenciaControlador {

    //====================================================
    // LISTA DE REGISTROS (Base de datos en memoria)
    //====================================================
    private List<RegistroAsistencia> registros
            = new ArrayList<>();

    // Archivo de persistencia
    private static final String ARCHIVO
            = "registroAsistencia.dat";

    //====================================================
    // CONSTRUCTOR
    //====================================================
    public AsistenciaControlador() {
        cargarDatos();
    }

    //====================================================
    // CRUD
    //====================================================
    /**
     * Registra un nuevo registro de asistencia.
     */
    public void registrar(RegistroAsistencia registro) {

        registros.add(registro);

        guardarDatos();

        System.out.println(
                "Registro creado: "
                + registro.getCodigoRegistro()
        );

    }

    /**
     * Elimina un registro utilizando Iterator.
     */
    public void eliminar(String codigoRegistro) {

        Iterator<RegistroAsistencia> it
                = registros.iterator();

        while (it.hasNext()) {

            RegistroAsistencia r = it.next();

            if (r.getCodigoRegistro()
                    .equals(codigoRegistro)) {

                it.remove();

                guardarDatos();

                System.out.println(
                        "Registro eliminado."
                );

                return;
            }

        }

    }

    /**
     * Actualiza un registro existente.
     */
    public void actualizar(RegistroAsistencia registro) {

        for (int i = 0; i < registros.size(); i++) {

            if (registros.get(i)
                    .getCodigoRegistro()
                    .equals(registro.getCodigoRegistro())) {

                registros.set(i, registro);

                guardarDatos();

                return;

            }

        }

    }

    /**
     * Devuelve todos los registros.
     */
    public List<RegistroAsistencia> listar() {

        return registros;

    }

    //====================================================
    // BÚSQUEDAS (LAMBDA)
    //====================================================
    /**
     * Buscar por código.
     */
    public RegistroAsistencia buscarPorCodigo(
            String codigo) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoRegistro()
                        .equals(codigo))
                .findFirst()
                .orElse(null);

    }

    /**
     * Buscar registros por docente.
     */
    public List<RegistroAsistencia> buscarPorDocente(
            String codigoDocente) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoDocente()
                        .equals(codigoDocente))
                .collect(Collectors.toList());

    }

    /**
     * Buscar registros por curso.
     */
    public List<RegistroAsistencia> buscarPorCurso(
            String codigoCurso) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoCurso()
                        .equals(codigoCurso))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por grado.
     */
    public List<RegistroAsistencia> buscarPorGrado(
            String grado) {

        return registros.stream()
                .filter(r
                        -> r.getGrado()
                        .equalsIgnoreCase(grado))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por sección.
     */
    public List<RegistroAsistencia> buscarPorSeccion(
            String seccion) {

        return registros.stream()
                .filter(r
                        -> r.getSeccion()
                        .equalsIgnoreCase(seccion))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por fecha.
     */
    public List<RegistroAsistencia> buscarPorFecha(
            Date fecha) {

        return registros.stream()
                .filter(r
                        -> r.getFecha().equals(fecha))
                .collect(Collectors.toList());

    }

    //====================================================
    // ESTADÍSTICAS (LAMBDA)
    //====================================================
    /**
     * Total de registros.
     */
    public int totalRegistros() {

        return registros.size();

    }

    /**
     * Total de registros realizados por un docente.
     */
    public long totalPorDocente(
            String codigoDocente) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoDocente()
                        .equals(codigoDocente))
                .count();

    }

    /**
     * Total de registros por curso.
     */
    public long totalPorCurso(
            String codigoCurso) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoCurso()
                        .equals(codigoCurso))
                .count();

    }

    /**
     * Buscar registros por nivel.
     */
    public List<RegistroAsistencia> buscarPorNivel(String nivel) {

        return registros.stream()
                .filter(r -> r.getNivel().equalsIgnoreCase(nivel))
                .collect(Collectors.toList());

    }

    /**
     * Busca los registros de una sección específica.
     */
    public List<RegistroAsistencia> buscarPorSeccion(
            String nivel,
            String grado,
            String seccion) {

        return registros.stream()
                .filter(r
                        -> r.getNivel().equalsIgnoreCase(nivel)
                && r.getGrado().equalsIgnoreCase(grado)
                && r.getSeccion().equalsIgnoreCase(seccion))
                .collect(Collectors.toList());

    }

    /**
     * Busca un registro por curso y fecha.
     */
    public RegistroAsistencia buscarRegistro(
            String codigoCurso,
            Date fecha) {

        return registros.stream()
                .filter(r
                        -> r.getCodigoCurso().equals(codigoCurso)
                && r.getFecha().equals(fecha))
                .findFirst()
                .orElse(null);

    }

    /**
     * Busca un registro de asistencia de una sección en una fecha determinada.
     */
    public RegistroAsistencia buscarRegistro(
            Date fecha,
            String codigoCurso,
            String nivel,
            String grado,
            String seccion) {

        return registros.stream()
                .filter(r
                        -> r.getFecha().equals(fecha)
                && r.getCodigoCurso().equals(codigoCurso)
                && r.getNivel().equalsIgnoreCase(nivel)
                && r.getGrado().equalsIgnoreCase(grado)
                && r.getSeccion().equalsIgnoreCase(seccion))
                .findFirst()
                .orElse(null);

    }

    /**
     * Total de registros por nivel.
     */
    public long totalPorNivel(String nivel) {

        return registros.stream()
                .filter(r -> r.getNivel().equalsIgnoreCase(nivel))
                .count();

    }

    /**
     * Total de registros por grado.
     */
    public long totalPorGrado(
            String grado) {

        return registros.stream()
                .filter(r
                        -> r.getGrado()
                        .equalsIgnoreCase(grado))
                .count();

    }

    /**
     * Total de registros por sección.
     */
    public long totalPorSeccion(
            String nivel,
            String grado,
            String seccion) {

        return registros.stream()
                .filter(r
                        -> r.getNivel().equalsIgnoreCase(nivel)
                && r.getGrado().equalsIgnoreCase(grado)
                && r.getSeccion().equalsIgnoreCase(seccion))
                .count();

    }
    
    //====================================================
    // CONSULTAS Y EDICIÓN POR ALUMNO (ficha individual)
    //====================================================
    // Estos métodos antes vivían en una clase aparte llamada
    // AsistenciaControlador, pero como esa clase no guardaba nada
    // por su cuenta (solo consultaba estos mismos registros), se
    // movieron aquí para no tener una clase extra sin datos propios.

    /**
     * "Aplana" todos los registros de asistencia (uno por sección/curso/
     * fecha) en una sola lista con TODAS las Asistencias individuales.
     * Lambda: flatMap
     */
    private List<Asistencia> todasLasAsistencias() {
        return registros.stream()
                .flatMap(r -> r.getListaAsistencias().stream())
                .collect(Collectors.toList());
    }

    /** Retorna todas las asistencias de un alumno (su historial completo). */
    public List<Asistencia> buscarAsistenciasDeAlumno(String codigoAlumno) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigoAlumno))
                .collect(Collectors.toList());
    }

    /** Cuenta las faltas (incluye tardanzas) de un alumno. */
    public long contarFaltasDeAlumno(String codigoAlumno) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigoAlumno) && a.esFalta())
                .count();
    }

    /** Cuenta las presencias de un alumno. */
    public long contarPresenciasDeAlumno(String codigoAlumno) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigoAlumno) && a.esPresente())
                .count();
    }

    /** Calcula el porcentaje de asistencia de un alumno. */
    public double porcentajeAsistenciaDeAlumno(String codigoAlumno) {
        long total = todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigoAlumno))
                .count();
        if (total == 0) return 0.0;
        return (contarPresenciasDeAlumno(codigoAlumno) * 100.0) / total;
    }

    /**
     * Busca la Asistencia por su id (dentro de todos los registros) y la
     * marca como Justificada, agregando la observación indicada.
     * Devuelve true si la encontró y actualizó.
     */
    public boolean justificarAsistencia(String idAsistencia, String observacion) {
        for (RegistroAsistencia r : registros) {
            for (Asistencia a : r.getListaAsistencias()) {
                if (a.getIdAsistencia().equals(idAsistencia)) {
                    a.setEstado("J");
                    a.setObservacion(observacion);
                    actualizar(r); // vuelve a guardar en disco
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Actualiza solo la observación de una Asistencia, sin cambiar su estado.
     */
    public boolean actualizarObservacionAsistencia(String idAsistencia, String observacion) {
        for (RegistroAsistencia r : registros) {
            for (Asistencia a : r.getListaAsistencias()) {
                if (a.getIdAsistencia().equals(idAsistencia)) {
                    a.setObservacion(observacion);
                    actualizar(r);
                    return true;
                }
            }
        }
        return false;
    }
    

    //====================================================
    // SERIALIZACIÓN
    //====================================================
    /**
     * Guarda todos los registros en archivo .dat
     */
    public void guardarDatos() {

        try (ObjectOutputStream out
                = new ObjectOutputStream(
                        new FileOutputStream(
                                ARCHIVO))) {

            out.writeObject(registros);

            System.out.println(
                    "Registros guardados."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al guardar: "
                    + e.getMessage()
            );

        }

    }

    /**
     * Carga el archivo .dat
     */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {

        File archivo
                = new File(ARCHIVO);

        if (!archivo.exists()) {

            System.out.println(
                    "No existen registros previos."
            );

            return;

        }

        try (ObjectInputStream in
                = new ObjectInputStream(
                        new FileInputStream(
                                ARCHIVO))) {

            registros
                    = (List<RegistroAsistencia>) in.readObject();

            System.out.println(
                    registros.size()
                    + " registros cargados."
            );

        } catch (IOException
                | ClassNotFoundException e) {

            System.out.println(
                    "Error al cargar: "
                    + e.getMessage()
            );

        }

    }

}
