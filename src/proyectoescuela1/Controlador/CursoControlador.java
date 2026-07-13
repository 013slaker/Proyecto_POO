/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Curso;

/**
 * Controlador del módulo Cursos.
 * Maneja CRUD, búsquedas con Lambda,
 * eliminación con Iterator y persistencia
 * con Serialización.
 */
public class CursoControlador {

    // ── BASE DE DATOS EN MEMORIA ──────────────────
    /** Lista donde se almacenan todos los cursos */
    private List<Curso> cursos = new ArrayList<>();

    /** Archivo de persistencia */
    private static final String ARCHIVO = "cursos.dat";

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Al iniciar carga los datos guardados
     * automáticamente.
     */
    public CursoControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    /**
     * Registra un nuevo curso y guarda en archivo.
     */
    public void registrar(Curso curso) {
        cursos.add(curso);
        guardarDatos();
        System.out.println("Curso registrado: " +
                curso.getIdCurso());
    }

    /**
     * Retorna toda la lista de cursos.
     */
    public List<Curso> listar() {
        return cursos;
    }

    /**
     * Actualiza los datos de un curso existente
     * y guarda los cambios.
     */
    public void actualizar(Curso curso) {
        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getIdCurso()
                    .equals(curso.getIdCurso())) {
                cursos.set(i, curso);
                guardarDatos();
                System.out.println("Curso actualizado: " +
                        curso.getIdCurso());
                return;
            }
        }
        System.out.println("Curso no encontrado.");
    }

    /**
     * Elimina un curso usando Iterator.
     * Forma segura de eliminar mientras
     * se recorre la lista.
     */
    public void eliminar(String idCurso) {
        // Iterator evita ConcurrentModificationException
        Iterator<Curso> it = cursos.iterator();
        while (it.hasNext()) {
            Curso c = it.next();
            if (c.getIdCurso().equals(idCurso)) {
                it.remove(); // eliminación segura
                guardarDatos();
                System.out.println("Curso eliminado: " +
                        idCurso);
                return;
            }
        }
        System.out.println("No encontrado: " + idCurso);
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Busca un curso por su código exacto.
     * Lambda: filter + findFirst
     */
    public Curso buscarPorId(String id) {
        return cursos.stream()
                .filter(c -> c.getIdCurso().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca cursos cuyo nombre contenga
     * el texto ingresado (no distingue mayúsculas).
     * Lambda: filter con contains
     */
    public List<Curso> buscarPorNombre(String nombre) {
        return cursos.stream()
                .filter(c -> c.getNombre()
                        .toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra cursos por área.
     * Lambda: filter por área exacta
     */
    public List<Curso> buscarPorArea(String area) {
        return cursos.stream()
                .filter(c -> c.getArea()
                        .equalsIgnoreCase(area))
                .collect(Collectors.toList());
    }

    /**
     * Filtra cursos por nivel educativo.
     * Lambda: filter por nivel
     */
    public List<Curso> buscarPorNivel(String nivel) {
        return cursos.stream()
                .filter(c -> c.getNivel()
                        .equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
    }

    /**
     * Retorna solo los cursos activos.
     * Lambda: filter por estado activo
     */
    public List<Curso> listarActivos() {
        return cursos.stream()
                .filter(Curso::isActivo)
                .collect(Collectors.toList());
    }

    /**
     * Ordena los cursos por nombre alfabéticamente.
     * Lambda: sorted con comparador
     */
    public List<Curso> ordenarPorNombre() {
        return cursos.stream()
                .sorted((a, b) -> a.getNombre()
                        .compareToIgnoreCase(b.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Ordena los cursos por horas semanales
     * de mayor a menor.
     * Lambda: sorted con comparador inverso
     */
    public List<Curso> ordenarPorHoras() {
        return cursos.stream()
                .sorted((a, b) -> b.getHorasSemanales()
                        - a.getHorasSemanales())
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  ESTADÍSTICAS — Lambda
    // ══════════════════════════════════════════════

    /** Total de cursos registrados */
    public int total() {
        return cursos.size();
    }

    /**
     * Cuenta cursos por nivel.
     * Lambda: filter + count
     */
    public long contarPorNivel(String nivel) {
        return cursos.stream()
                .filter(c -> c.getNivel()
                        .equalsIgnoreCase(nivel))
                .count();
    }

    /**
     * Suma total de horas semanales de todos
     * los cursos.
     * Lambda: mapToInt + sum
     */
    public int totalHorasSemanales() {
        return cursos.stream()
                .mapToInt(Curso::getHorasSemanales)
                .sum();
    }

    /**
     * Promedio de horas semanales.
     * Lambda: mapToInt + average
     */
    public double promedioHoras() {
        return cursos.stream()
                .mapToInt(Curso::getHorasSemanales)
                .average()
                .orElse(0.0);
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    /**
     * Guarda la lista completa en archivo .dat.
     * Serialización: convierte objetos a bytes.
     */
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(cursos);
            System.out.println("Cursos guardados.");
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
        // si no existe el archivo inicia vacío
        if (!archivo.exists()) {
            System.out.println(
                    "Sin datos previos de cursos.");
            return;
        }
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            cursos = (List<Curso>) in.readObject();
            System.out.println(cursos.size() +
                    " cursos cargados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " +
                    e.getMessage());
        }
    }
}