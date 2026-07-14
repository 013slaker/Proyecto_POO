/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.AsignacionCurso;
import proyectoescuela1.Modelo.Curso;
import proyectoescuela1.Modelo.Docente;
import proyectoescuela1.Modelo.Horario;

/**
 * Controlador del módulo AsignacionCurso.
 * Maneja CRUD, búsquedas con Lambda,
 * eliminación con Iterator y serialización.
 */
public class AsignacionCursoControlador {

    // ── BASE DE DATOS EN MEMORIA ──────────────────
    /** Lista de todas las asignaciones */
    private List<AsignacionCurso> asignaciones =
            new ArrayList<>();

    /** Archivo de persistencia */
    private static final String ARCHIVO =
            "asignaciones.dat";

    /** Controladores auxiliares */
    private DocenteControlador docenteCtrl =
            new DocenteControlador();
    private CursoControlador cursoCtrl =
            new CursoControlador();
    private HorarioControlador horarioCtrl =
            new HorarioControlador();

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Carga los datos guardados al iniciar.
     */
    public AsignacionCursoControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    /**
     * Registra una nueva asignación y guarda.
     */
    public void registrar(AsignacionCurso asignacion) {
        asignaciones.add(asignacion);
        guardarDatos();
        System.out.println("Asignación registrada: " +
                asignacion.getIdAsignacion());
    }

    /**
     * Retorna todas las asignaciones.
     */
    public List<AsignacionCurso> listar() {
        return asignaciones;
    }

    /**
     * Elimina una asignación usando Iterator.
     * Forma segura de eliminar mientras
     * se recorre la lista.
     */
    public void eliminar(String idAsignacion) {
        Iterator<AsignacionCurso> it =
                asignaciones.iterator();
        while (it.hasNext()) {
            AsignacionCurso a = it.next();
            if (a.getIdAsignacion()
                    .equals(idAsignacion)) {
                it.remove();
                guardarDatos();
                System.out.println(
                        "Asignación eliminada: " +
                        idAsignacion);
                return;
            }
        }
        System.out.println("No encontrada: " +
                idAsignacion);
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Busca por id exacto.
     * Lambda: filter + findFirst
     */
    public AsignacionCurso buscarPorId(String id) {
        return asignaciones.stream()
                .filter(a -> a.getIdAsignacion()
                        .equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filtra asignaciones por nivel.
     * Lambda: filter por nivel
     */
    public List<AsignacionCurso> buscarPorNivel(
            String nivel) {
        return asignaciones.stream()
                .filter(a -> a.getNivel()
                        .equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
    }

    /**
     * Filtra asignaciones por sección.
     * Lambda: filter por nombre de sección
     */
    public List<AsignacionCurso> buscarPorSeccion(
            String seccion) {
        return asignaciones.stream()
                .filter(a -> a.getNombreSeccion()
                        .equalsIgnoreCase(seccion))
                .collect(Collectors.toList());
    }

    /**
     * Busca asignaciones de un docente específico.
     * Lambda: filter con anyMatch en items
     */
    public List<AsignacionCurso> buscarPorDocente(
            String codigoDocente) {
        return asignaciones.stream()
                .filter(a -> {
                    // verifica si el docente es titular
                    if (a.getDocenteTitular() != null &&
                        a.getDocenteTitular()
                         .getCodigoDocente()
                         .equals(codigoDocente)) {
                        return true;
                    }
                    // verifica si está en algún item
                    return a.getItems().stream()
                            .anyMatch(i -> i.getDocente()
                                    .getCodigoDocente()
                                    .equals(codigoDocente));
                })
                .collect(Collectors.toList());
    }

    /**
     * Retorna solo las asignaciones válidas.
     * Lambda: filter por esValida()
     */
    public List<AsignacionCurso> listarValidas() {
        return asignaciones.stream()
                .filter(AsignacionCurso::esValida)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta asignaciones por nivel.
     * Lambda: filter + count
     */
    public long contarPorNivel(String nivel) {
        return asignaciones.stream()
                .filter(a -> a.getNivel()
                        .equalsIgnoreCase(nivel))
                .count();
    }

    /**
     * Retorna lista de secciones únicas.
     * Lambda: map + distinct
     */
    public List<String> listarSecciones() {
        return asignaciones.stream()
                .map(AsignacionCurso::getNombreSeccion)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // ── ACCESO A CONTROLADORES AUXILIARES ─────────
    public DocenteControlador getDocenteCtrl() {
        return docenteCtrl;
    }
    public CursoControlador getCursoCtrl() {
        return cursoCtrl;
    }
    public HorarioControlador getHorarioCtrl() {
        return horarioCtrl;
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    /**
     * Guarda la lista en archivo .dat.
     * Serialización: convierte objetos a bytes.
     */
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(asignaciones);
            System.out.println(
                    "Asignaciones guardadas.");
        } catch (IOException e) {
            System.out.println("Error al guardar: " +
                    e.getMessage());
        }
    }

    /**
     * Carga la lista desde archivo .dat.
     * Deserialización: convierte bytes a objetos.
     */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println(
                    "Sin datos previos de asignaciones.");
            return;
        }
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            asignaciones =
                (List<AsignacionCurso>) in.readObject();
            System.out.println(asignaciones.size() +
                    " asignaciones cargadas.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " +
                    e.getMessage());
        }
    }
}
