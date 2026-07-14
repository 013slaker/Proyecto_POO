/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Nota;

/**
 * Controlador del módulo Notas.
 * Maneja registro, búsqueda con Lambda,
 * cálculo de promedios y serialización.
 */
public class NotaControlador {

    /** Lista general de todas las notas */
    private List<Nota> notas = new ArrayList<>();

    /** Archivo de persistencia */
    private static final String ARCHIVO = "notas.dat";

    /** Controlador de alumnos para acceder a sus datos */
    private AlumnoControlador alumnoControlador =
            new AlumnoControlador();

    // ── CONSTRUCTOR ───────────────────────────────
    public NotaControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    /**
     * Registra una nota y la agrega al alumno
     * correspondiente.
     */
    public void registrar(Nota nota) {
        notas.add(nota);

        // también la agrega al alumno directamente
        Alumno alumno = alumnoControlador
            .buscarPorCodigo(nota.getCodigoAlumno());
        if (alumno != null) {
            alumno.getNotas().add(nota);
            alumnoControlador.guardarDatos();
        }

        guardarDatos();
        System.out.println("Nota registrada: " +
                nota.getIdNota());
    }

    /**
     * Retorna todas las notas registradas.
     */
    public List<Nota> listar() {
        return notas;
    }

    /**
     * Actualiza una nota existente.
     */
    public void actualizar(Nota nota) {
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getIdNota()
                    .equals(nota.getIdNota())) {
                notas.set(i, nota);
                guardarDatos();
                return;
            }
        }
    }

    /**
     * Elimina una nota usando Iterator.
     */
    public void eliminar(String idNota) {
        Iterator<Nota> it = notas.iterator();
        while (it.hasNext()) {
            Nota n = it.next();
            if (n.getIdNota().equals(idNota)) {
                it.remove();
                guardarDatos();
                System.out.println("Nota eliminada: " +
                        idNota);
                return;
            }
        }
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Busca nota por id.
     */
    public Nota buscarPorId(String id) {
        return notas.stream()
                .filter(n -> n.getIdNota().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna todas las notas de un alumno.
     * Lambda: filter por código de alumno
     */
    public List<Nota> buscarPorAlumno(String codigo) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigo))
                .collect(Collectors.toList());
    }

    /**
     * Retorna notas de un alumno en un bimestre.
     * Lambda: filter por alumno y bimestre
     */
    public List<Nota> buscarPorBimestre(
            String codigo, int bimestre) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigo) &&
                        n.getBimestre() == bimestre)
                .collect(Collectors.toList());
    }

    /**
     * Retorna notas de un alumno en un curso.
     * Lambda: filter por alumno y curso
     */
    public List<Nota> buscarPorCurso(
            String codigo, String curso) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigo) &&
                        n.getCurso()
                        .equalsIgnoreCase(curso))
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  CÁLCULOS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Calcula el promedio general de un alumno.
     * Lambda: mapToDouble + average
     */
    public double calcularPromedio(String codigoAlumno) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigoAlumno))
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula el promedio de un bimestre.
     * Lambda: filter + mapToDouble + average
     */
    public double calcularPromedioBimestre(
            String codigo, int bimestre) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigo) &&
                        n.getBimestre() == bimestre)
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0.0);
    }

    /**
     * Verifica si el alumno está aprobado.
     * Lambda: promedio >= 11
     */
    public boolean estaAprobado(String codigoAlumno) {
        return calcularPromedio(codigoAlumno) >= 11;
    }

    /**
     * Retorna la nota más alta del alumno.
     * Lambda: mapToDouble + max
     */
    public double notaMaxima(String codigoAlumno) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigoAlumno))
                .mapToDouble(Nota::getValor)
                .max()
                .orElse(0.0);
    }

    /**
     * Retorna la nota más baja del alumno.
     * Lambda: mapToDouble + min
     */
    public double notaMinima(String codigoAlumno) {
        return notas.stream()
                .filter(n -> n.getCodigoAlumno()
                        .equals(codigoAlumno))
                .mapToDouble(Nota::getValor)
                .min()
                .orElse(0.0);
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    /** Guarda la lista en archivo .dat */
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(notas);
        } catch (IOException e) {
            System.out.println("Error al guardar notas: " +
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
            notas = (List<Nota>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar notas: " +
                    e.getMessage());
        }
    }
}
