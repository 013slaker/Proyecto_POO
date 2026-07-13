/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Controlador del módulo Gestión de Docentes
 * Permite realizar operaciones CRUD, búsquedas,
 * estadísticas y persistencia mediante Serialización.
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import proyectoescuela1.Modelo.Docente;

public class DocenteControlador {

    // =====================================================
    // BASE DE DATOS EN MEMORIA
    // =====================================================
    // Lista donde se almacenan todos los docentes
    private List<Docente> docentes = new ArrayList<>();

    // Archivo donde se guardarán los datos
    private static final String ARCHIVO = "docentes.dat";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public DocenteControlador() {

        // Al iniciar el controlador se cargan los datos
        cargarDatos();

    }

    // =====================================================
    // CRUD
    // =====================================================
    /**
     * Registrar un nuevo docente.
     */
    public void registrarDocente(Docente docente) {

        docentes.add(docente);

        guardarDatos();

        System.out.println(
                "Docente registrado correctamente: "
                + docente.getCodigoDocente()
        );

    }

    /**
     * Devuelve toda la lista de docentes.
     */
    public List<Docente> listarTodos() {

        return docentes;

    }

    /**
     * Actualiza la información de un docente.
     */
    public void actualizarDocente(Docente docente) {

        for (int i = 0; i < docentes.size(); i++) {

            if (docentes.get(i)
                    .getCodigoDocente()
                    .equals(docente.getCodigoDocente())) {

                docentes.set(i, docente);

                guardarDatos();

                System.out.println(
                        "Docente actualizado correctamente."
                );

                return;

            }

        }

        System.out.println("No se encontró el docente.");

    }

    /**
     * Elimina un docente utilizando Iterator. (Requerimiento de POO)
     */
    public void eliminarDocente(String codigo) {

        Iterator<Docente> iterator = docentes.iterator();

        while (iterator.hasNext()) {

            Docente docente = iterator.next();

            if (docente.getCodigoDocente().equals(codigo)) {

                iterator.remove();

                guardarDatos();

                System.out.println(
                        "Docente eliminado correctamente."
                );

                return;

            }

        }

        System.out.println(
                "No existe un docente con código: "
                + codigo
        );

    }

    // =====================================================
    // SERIALIZACIÓN
    // =====================================================
    /**
     * Guarda la lista completa en un archivo .dat
     */
    public void guardarDatos() {

        try (ObjectOutputStream salida
                = new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {

            salida.writeObject(docentes);

            System.out.println(
                    "Docentes guardados correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al guardar docentes: "
                    + e.getMessage()
            );

        }

    }

    /**
     * Carga los docentes desde el archivo.
     */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {

        File archivo = new File(ARCHIVO);

        // Si todavía no existe el archivo,
        // simplemente inicia la lista vacía.
        if (!archivo.exists()) {

            System.out.println(
                    "No existen docentes registrados."
            );

            return;

        }

        try (ObjectInputStream entrada
                = new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {

            docentes = (List<Docente>) entrada.readObject();

            System.out.println(
                    docentes.size()
                    + " docentes cargados."
            );

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Error al cargar docentes: "
                    + e.getMessage()
            );

        }

    }

    // =====================================================
    // BÚSQUEDAS (LAMBDA - STREAM)
    // =====================================================

    /**
     * Busca un docente por su código.
     */
    public Docente buscarPorCodigo(String codigo) {

        return docentes.stream()
                .filter(d -> d.getCodigoDocente().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);

    }

    /**
     * Busca un docente por DNI.
     */
    public Docente buscarPorDni(String dni) {

        return docentes.stream()
                .filter(d -> d.getDni().equals(dni))
                .findFirst()
                .orElse(null);

    }

    /**
     * Busca docentes por nombre o apellidos.
     */
    public List<Docente> buscarPorNombre(String nombre) {

        return docentes.stream()
                .filter(d -> d.getNombreCompleto()
                .toLowerCase()
                .contains(nombre.toLowerCase()))
                .toList();

    }

    /**
     * Busca docentes por especialidad.
     */
    public List<Docente> buscarPorEspecialidad(String especialidad) {

        return docentes.stream()
                .filter(d -> d.getEspecialidad()
                .equalsIgnoreCase(especialidad))
                .toList();

    }

    /**
     * Busca docentes por nivel.
     */
    public List<Docente> buscarPorNivel(String nivel) {

        return docentes.stream()
                .filter(d -> d.getNivel()
                .equalsIgnoreCase(nivel))
                .toList();

    }

    /**
     * Devuelve únicamente los docentes activos.
     */
    public List<Docente> listarActivos() {

        return docentes.stream()
                .filter(Docente::isEstadoActivo)
                .toList();

    }

    /**
     * Devuelve únicamente los docentes inactivos.
     */
    public List<Docente> listarInactivos() {

        return docentes.stream()
                .filter(d -> !d.isEstadoActivo())
                .toList();

    }

    // =====================================================
    // ORDENAMIENTOS (LAMBDA)
    // =====================================================
    /**
     * Ordena por apellido.
     */
    public List<Docente> ordenarPorApellido() {

        return docentes.stream()
                .sorted((a, b)
                        -> a.getApellidos()
                        .compareToIgnoreCase(b.getApellidos()))
                .toList();

    }

    /**
     * Ordena por nombre.
     */
    public List<Docente> ordenarPorNombre() {

        return docentes.stream()
                .sorted((a, b)
                        -> a.getNombre()
                        .compareToIgnoreCase(b.getNombre()))
                .toList();

    }

    /**
     * Ordena por código.
     */
    public List<Docente> ordenarPorCodigo() {

        return docentes.stream()
                .sorted((a, b)
                        -> a.getCodigoDocente()
                        .compareToIgnoreCase(
                                b.getCodigoDocente()))
                .toList();

    }

    /**
     * Ordena por especialidad.
     */
    public List<Docente> ordenarPorEspecialidad() {

        return docentes.stream()
                .sorted((a, b)
                        -> a.getEspecialidad()
                        .compareToIgnoreCase(
                                b.getEspecialidad()))
                .toList();

    }

    // =====================================================
    // ESTADÍSTICAS
    // =====================================================
    /**
     * Total de docentes registrados.
     */
    public int totalDocentes() {

        return docentes.size();

    }

    /**
     * Total de docentes de Inicial.
     */
    public long totalInicial() {

        return docentes.stream()
                .filter(d -> d.getNivel().equalsIgnoreCase("Inicial"))
                .count();

    }

    /**
     * Total de docentes de Primaria.
     */
    public long totalPrimaria() {

        return docentes.stream()
                .filter(d -> d.getNivel().equalsIgnoreCase("Primaria"))
                .count();

    }

    /**
     * Total de docentes de Secundaria.
     */
    public long totalSecundaria() {

        return docentes.stream()
                .filter(d -> d.getNivel().equalsIgnoreCase("Secundaria"))
                .count();

    }

    /**
     * Total de docentes activos.
     */
    public long totalActivos() {

        return docentes.stream()
                .filter(Docente::isEstadoActivo)
                .count();

    }

    
}
