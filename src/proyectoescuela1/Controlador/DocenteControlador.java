/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Docente;

public class DocenteControlador {

    //==========================================
    // BASE DE DATOS EN MEMORIA
    //==========================================
    private List<Docente> docentes = new ArrayList<>();

    // Archivo de serialización
    private static final String ARCHIVO = "docentes.dat";

    //==========================================
    // CONSTRUCTOR
    //==========================================
    public DocenteControlador() {
        cargarDatos();
    }

    //==========================================
    // CRUD
    //==========================================
    /**
     * Registrar docente
     */
    public void registrar(Docente docente) {

        docentes.add(docente);

        guardarDatos();

        System.out.println("Docente registrado: "
                + docente.getCodigoDocente());
    }

    /**
     * Eliminar usando Iterator
     */
    public void eliminar(String codigo) {

        Iterator<Docente> it = docentes.iterator();

        while (it.hasNext()) {

            Docente d = it.next();

            if (d.getCodigoDocente().equals(codigo)) {

                it.remove();

                guardarDatos();

                System.out.println("Docente eliminado.");

                return;
            }
        }

        System.out.println("Docente no encontrado.");
    }

    /**
     * Actualizar información
     */
    public void actualizar(Docente docente) {

        for (int i = 0; i < docentes.size(); i++) {

            if (docentes.get(i).getCodigoDocente()
                    .equals(docente.getCodigoDocente())) {

                docentes.set(i, docente);

                guardarDatos();

                System.out.println("Docente actualizado.");

                return;
            }
        }
    }

    /**
     * Listar todos
     */
    public List<Docente> listar() {
        return docentes;
    }
    //==========================================
    // BÚSQUEDAS
    //==========================================

    /**
     * Buscar por código
     */
    public Docente buscarPorCodigo(String codigo) {

        return docentes.stream()
                .filter(d
                        -> d.getCodigoDocente().equals(codigo))
                .findFirst()
                .orElse(null);

    }

    /**
     * Buscar por DNI
     */
    public Docente buscarPorDni(String dni) {

        return docentes.stream()
                .filter(d
                        -> d.getDni().equals(dni))
                .findFirst()
                .orElse(null);

    }

    /**
     * Buscar por nombre
     */
    public List<Docente> buscarPorNombre(String nombre) {

        return docentes.stream()
                .filter(d
                        -> d.getNombreCompleto()
                        .toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por especialidad
     */
    public List<Docente> buscarPorEspecialidad(String especialidad) {

        return docentes.stream()
                .filter(d
                        -> d.getEspecialidad()
                        .equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());

    }

    /**
     * Buscar por nivel
     */
    public List<Docente> buscarPorNivel(String nivel) {

        return docentes.stream()
                .filter(d
                        -> d.getNivel()
                        .equalsIgnoreCase(nivel))
                .collect(Collectors.toList());

    }

    /**
     * Listar solo activos
     */
    public List<Docente> listarActivos() {

        return docentes.stream()
                .filter(Docente::isEstadoActivo)
                .collect(Collectors.toList());

    }

//==========================================
    // ORDENAMIENTOS
    //==========================================
    /**
     * Ordenar por apellido
     */
    public List<Docente> ordenarPorApellido() {

        return docentes.stream()
                .sorted(Comparator.comparing(Docente::getApellidos))
                .collect(Collectors.toList());

    }

    /**
     * Ordenar por especialidad
     */
    public List<Docente> ordenarPorEspecialidad() {

        return docentes.stream()
                .sorted(Comparator.comparing(Docente::getEspecialidad))
                .collect(Collectors.toList());

    }

    //==========================================
    // ESTADÍSTICAS
    //==========================================
    public int totalDocentes() {

        return docentes.size();

    }

    public long totalActivos() {

        return docentes.stream()
                .filter(Docente::isEstadoActivo)
                .count();

    }

    public long totalPorNivel(String nivel) {

        return docentes.stream()
                .filter(d
                        -> d.getNivel()
                        .equalsIgnoreCase(nivel))
                .count();

    }

    /**
     * Desactiva todos los docentes de una especialidad.
     */
    public void desactivarEspecialidad(String especialidad) {

        docentes.stream()
                .filter(d
                        -> d.getEspecialidad()
                        .equalsIgnoreCase(especialidad))
                .forEach(Docente::desactivar);

        guardarDatos();

    }

//==========================================
    // SERIALIZACIÓN
    //==========================================
    /**
     * Guarda la información en docentes.dat
     */
    public void guardarDatos() {

        try (ObjectOutputStream out
                = new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {

            out.writeObject(docentes);

            System.out.println("Docentes guardados.");

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }

    /**
     * Carga la información
     */
    @SuppressWarnings("unchecked")
    public void cargarDatos() {

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {

            return;

        }

        try (ObjectInputStream in
                = new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {

            docentes = (List<Docente>) in.readObject();

            System.out.println("Docentes cargados.");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

    }

}
