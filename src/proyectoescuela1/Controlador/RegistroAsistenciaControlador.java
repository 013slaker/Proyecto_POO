/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import proyectoescuela1.Modelo.RegistroAsistencia;

/**
 * Controlador del módulo Registro de Asistencia.
 *
 * Administra los registros diarios de asistencia,
 * búsquedas, estadísticas y persistencia mediante
 * serialización.
 */
public class RegistroAsistenciaControlador {

    //====================================================
    // LISTA DE REGISTROS (Base de datos en memoria)
    //====================================================

    private List<RegistroAsistencia> registros =
            new ArrayList<>();

    // Archivo de persistencia
    private static final String ARCHIVO =
            "registroAsistencia.dat";

    //====================================================
    // CONSTRUCTOR
    //====================================================

    public RegistroAsistenciaControlador() {
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

        Iterator<RegistroAsistencia> it =
                registros.iterator();

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

                .filter(r ->
                        r.getCodigoRegistro()
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

                .filter(r ->
                        r.getCodigoDocente()
                                .equals(codigoDocente))

                .collect(Collectors.toList());

    }

    /**
     * Buscar registros por curso.
     */
    public List<RegistroAsistencia> buscarPorCurso(
            String codigoCurso) {

        return registros.stream()

                .filter(r ->
                        r.getCodigoCurso()
                                .equals(codigoCurso))

                .collect(Collectors.toList());

    }

    /**
     * Buscar por grado.
     */
    public List<RegistroAsistencia> buscarPorGrado(
            String grado) {

        return registros.stream()

                .filter(r ->
                        r.getGrado()
                                .equalsIgnoreCase(grado))

                .collect(Collectors.toList());

    }

    /**
     * Buscar por sección.
     */
    public List<RegistroAsistencia> buscarPorSeccion(
            String seccion) {

        return registros.stream()

                .filter(r ->
                        r.getSeccion()
                                .equalsIgnoreCase(seccion))

                .collect(Collectors.toList());

    }

    /**
     * Buscar por fecha.
     */
    public List<RegistroAsistencia> buscarPorFecha(
            Date fecha) {

        return registros.stream()

                .filter(r ->
                        r.getFecha().equals(fecha))

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
     * Total de registros realizados
     * por un docente.
     */
    public long totalPorDocente(
            String codigoDocente) {

        return registros.stream()

                .filter(r ->
                        r.getCodigoDocente()
                                .equals(codigoDocente))

                .count();

    }

    /**
     * Total de registros por curso.
     */
    public long totalPorCurso(
            String codigoCurso) {

        return registros.stream()

                .filter(r ->
                        r.getCodigoCurso()
                                .equals(codigoCurso))

                .count();

    }

    /**
     * Total de registros por grado.
     */
    public long totalPorGrado(
            String grado) {

        return registros.stream()

                .filter(r ->
                        r.getGrado()
                                .equalsIgnoreCase(grado))

                .count();

    }

    //====================================================
    // SERIALIZACIÓN
    //====================================================

    /**
     * Guarda todos los registros
     * en archivo .dat
     */
    public void guardarDatos() {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
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

        File archivo =
                new File(ARCHIVO);

        if (!archivo.exists()) {

            System.out.println(
                    "No existen registros previos."
            );

            return;

        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(
                                     ARCHIVO))) {

            registros =
                    (List<RegistroAsistencia>)
                            in.readObject();

            System.out.println(
                    registros.size()
                            + " registros cargados."
            );

        } catch (IOException |
                 ClassNotFoundException e) {

            System.out.println(
                    "Error al cargar: "
                            + e.getMessage()
            );

        }

    }

}
