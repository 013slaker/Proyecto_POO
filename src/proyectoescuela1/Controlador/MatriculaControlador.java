/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Matricula;

/**
 * ========================================================== CONTROLADOR DE
 * MATRÍCULAS ----------------------------------------------------------
 * Administra el registro de matrículas del colegio.
 *
 * Funciones principales: - Registrar matrícula - Actualizar matrícula -
 * Eliminar matrícula - Buscar matrículas - Generar estadísticas -
 * Guardar/Cargar datos mediante serialización
 * ==========================================================
 */
public class MatriculaControlador {

    //======================================================
    // BASE DE DATOS EN MEMORIA
    //======================================================
    // Lista donde se almacenan todas las matrículas.
    private List<Matricula> matriculas = new ArrayList<>();

    // Archivo donde se guardarán las matrículas.
    private static final String ARCHIVO = "matriculas.dat";

    //======================================================
    // CONSTRUCTOR
    //======================================================
    public MatriculaControlador() {
        cargarDatos();
    }

    //======================================================
    // CRUD
    //======================================================
    /**
     * Registra una nueva matrícula.
     */
    public void registrarMatricula(Matricula matricula) {

        // Verifica que el alumno no esté matriculado
        // en el mismo año escolar.
        if (buscarPorAlumno(matricula.getAlumno().getCodigoAlumno())
                .stream()
                .anyMatch(m -> m.getAnio() == matricula.getAnio())) {

            throw new IllegalArgumentException(
                    "El alumno ya está matriculado en el año "
                    + matricula.getAnio());
        }

        // Agrega la matrícula
        matriculas.add(matricula);

        // También la agrega al historial del alumno
        matricula.getAlumno()
                .getMatriculas()
                .add(matricula);

        guardarDatos();

        System.out.println("Matrícula registrada: "
                + matricula.getCodigoMatricula());
    }

    /**
     * Elimina una matrícula utilizando Iterator.
     */
    public void eliminar(String codigoMatricula) {

        Iterator<Matricula> it = matriculas.iterator();

        while (it.hasNext()) {

            Matricula m = it.next();

            if (m.getCodigoMatricula()
                    .equals(codigoMatricula)) {

                it.remove();

                guardarDatos();

                System.out.println(
                        "Matrícula eliminada.");

                return;

            }

        }

    }
    // Busca todas las matrículas de un alumno

    public List<Matricula> buscarPorAlumno(Alumno alumno) {

        return matriculas.stream()
                .filter(m -> m.getAlumno().equals(alumno))
                .toList();
    }

    /**
     * Actualiza una matrícula existente.
     */
    public void actualizar(Matricula matricula) {

        for (int i = 0; i < matriculas.size(); i++) {

            if (matriculas.get(i)
                    .getCodigoMatricula()
                    .equals(matricula.getCodigoMatricula())) {

                matriculas.set(i, matricula);

                guardarDatos();

                return;

            }

        }

    }

    /**
     * Devuelve todas las matrículas.
     */
    public List<Matricula> listarTodos() {

        return matriculas;

    }

    /**
     * Alias de listarTodos(), usado por las vistas.
     */
    public List<Matricula> listarTodas() {
        return listarTodos();
    }

    /**
     * Alias de registrarMatricula(), usado por las vistas.
     */
    public void registrar(Matricula matricula) {
        registrarMatricula(matricula);
    }

    //======================================================
    // CAMBIO DE SECCIÓN
    //======================================================
    /**
     * Reasigna el grado y/o sección de la matrícula vigente
     * de un alumno (usado por "Matrículas -> Reasignar grado
     * y sección" y por el módulo "Cambio de Sección").
     */
    public void reasignarGradoSeccion(String codigoMatricula,
            String nuevoGrado, String nuevaSeccion) {
        reasignarNivelGradoSeccion(codigoMatricula, null, nuevoGrado, nuevaSeccion);
    }

    /**
     * Igual que reasignarGradoSeccion, pero además permite cambiar el
     * nivel (Primaria/Secundaria). Si nuevoNivel es null, el nivel no
     * se modifica (para no romper el código que ya llamaba a la
     * versión de 3 parámetros).
     */
    public void reasignarNivelGradoSeccion(String codigoMatricula,
            String nuevoNivel, String nuevoGrado, String nuevaSeccion) {

        Matricula matricula = buscarPorCodigo(codigoMatricula);

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "No se encontró la matrícula " + codigoMatricula);
        }

        if (!"Matriculado".equalsIgnoreCase(matricula.getEstado())) {
            throw new IllegalStateException(
                    "Solo se puede reasignar grado/sección a una "
                    + "matrícula en estado 'Matriculado'.");
        }

        if (nuevoNivel != null) {
            matricula.setNivel(nuevoNivel);
            matricula.getAlumno().setNivel(nuevoNivel);
        }

        matricula.setGrado(nuevoGrado);
        matricula.setSeccion(nuevaSeccion);

        // El alumno también refleja su grado/sección actual
        matricula.getAlumno().setGrado(nuevoGrado);
        matricula.getAlumno().setSeccion(nuevaSeccion);

        guardarDatos();
    }

    /**
     * Busca la matrícula vigente (estado "Matriculado") de un
     * alumno para un año determinado. Útil para saber qué
     * matrícula actualizar al trasladar de sección o retirar.
     */
    public Matricula buscarMatriculaVigente(String codigoAlumno, int anio) {
        return matriculas.stream()
                .filter(m -> m.getAlumno().getCodigoAlumno().equals(codigoAlumno)
                        && m.getAnio() == anio
                        && "Matriculado".equalsIgnoreCase(m.getEstado()))
                .findFirst()
                .orElse(null);
    }

    //======================================================
    // RETIRO DE ALUMNO
    //======================================================
    /**
     * Cambia el estado de una matrícula (por ejemplo a
     * "Retirado" o "Trasladado") y registra el motivo.
     * El alumno queda marcado como inactivo, ya que deja
     * de pertenecer a una sección del colegio.
     */
    public void retirarAlumno(String codigoMatricula, String nuevoEstado,
            String motivo) {

        Matricula matricula = buscarPorCodigo(codigoMatricula);

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "No se encontró la matrícula " + codigoMatricula);
        }

        matricula.setEstado(nuevoEstado);
        matricula.setMotivoRetiro(motivo);
        matricula.getAlumno().setEstadoActivo(false);

        guardarDatos();
    }

    /**
     * Sobrecarga simple: retira con estado "Retirado" por
     * defecto y sin motivo (compatibilidad con código previo).
     */
    public void retirarAlumno(String codigoMatricula) {
        retirarAlumno(codigoMatricula, "Retirado", null);
    }

    //======================================================
    // BÚSQUEDAS (LAMBDA)
    //======================================================
    /**
     * Busca una matrícula por su código.
     */
    public Matricula buscarPorCodigo(String codigo) {

        return matriculas.stream()
                .filter(m
                        -> m.getCodigoMatricula()
                        .equals(codigo))
                .findFirst()
                .orElse(null);

    }

    /**
     * Busca las matrículas de un alumno.
     */
    public List<Matricula> buscarPorAlumno(String codigoAlumno) {

        return matriculas.stream()
                .filter(m
                        -> m.getAlumno()
                        .getCodigoAlumno()
                        .equals(codigoAlumno))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por año escolar.
     */
    public List<Matricula> buscarPorAnio(int anio) {

        return matriculas.stream()
                .filter(m
                        -> m.getAnio() == anio)
                .collect(Collectors.toList());

    }

    /**
     * Buscar por nivel.
     */
    public List<Matricula> buscarPorNivel(String nivel) {

        return matriculas.stream()
                .filter(m
                        -> m.getNivel()
                        .equalsIgnoreCase(nivel))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por grado.
     */
    public List<Matricula> buscarPorGrado(String grado) {

        return matriculas.stream()
                .filter(m
                        -> m.getGrado()
                        .equalsIgnoreCase(grado))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por sección.
     */
    public List<Matricula> buscarPorSeccion(String seccion) {

        return matriculas.stream()
                .filter(m
                        -> m.getSeccion()
                        .equalsIgnoreCase(seccion))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por estado.
     */
    public List<Matricula> buscarPorEstado(String estado) {

        return matriculas.stream()
                .filter(m
                        -> m.getEstado()
                        .equalsIgnoreCase(estado))
                .collect(Collectors.toList());

    }

    //======================================================
    // ESTADÍSTICAS
    //======================================================
    /**
     * Total de matrículas.
     */
    public int totalMatriculas() {

        return matriculas.size();

    }

    /**
     * Total por nivel.
     */
    public long totalPorNivel(String nivel) {

        return matriculas.stream()
                .filter(m
                        -> m.getNivel()
                        .equalsIgnoreCase(nivel))
                .count();

    }

    /**
     * Total por grado.
     */
    public long totalPorGrado(String grado) {

        return matriculas.stream()
                .filter(m
                        -> m.getGrado()
                        .equalsIgnoreCase(grado))
                .count();

    }

    /**
     * Total por año escolar.
     */
    public long totalPorAnio(int anio) {

        return matriculas.stream()
                .filter(m
                        -> m.getAnio() == anio)
                .count();

    }

    //======================================================
    // SERIALIZACIÓN
    //======================================================
    /**
     * Guarda todas las matrículas en el archivo .dat.
     */
    public void guardarDatos() {

        try (ObjectOutputStream out
                = new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {

            out.writeObject(matriculas);

            System.out.println(
                    "Matrículas guardadas.");

        } catch (IOException e) {

            System.out.println(
                    "Error al guardar: "
                    + e.getMessage());

        }

    }

    /**
     * Carga las matrículas almacenadas en el archivo .dat.
     */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {

            System.out.println(
                    "No existen matrículas registradas.");

            return;

        }

        try (ObjectInputStream in
                = new ObjectInputStream(
                        new FileInputStream(archivo))) {

            matriculas
                    = (List<Matricula>) in.readObject();

            System.out.println(
                    matriculas.size()
                    + " matrículas cargadas.");

        } catch (IOException
                | ClassNotFoundException e) {

            System.out.println(
                    "Error al cargar: "
                    + e.getMessage());

        }

    }

}
