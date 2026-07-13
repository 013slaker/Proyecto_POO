package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Apoderado;

public class ApoderadoControlador {

    // ── BASE DE DATOS EN MEMORIA ──────────────────
    private List<Apoderado> lista = new ArrayList<>();

    // Archivo de persistencia
    private static final String ARCHIVO = "apoderados.dat";

    // ── CONSTRUCTOR — carga datos al iniciar ──────
    public ApoderadoControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    // Registra un apoderado y guarda
    public void registrar(Apoderado a) {
        lista.add(a);
        guardarDatos();
        System.out.println("Apoderado registrado: " +
                a.getCodigoApoderado());
    }

    // Retorna toda la lista
    public List<Apoderado> listar() {
        return lista;
    }

    // Actualiza datos de un apoderado
    public void actualizar(Apoderado apoderado) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigoApoderado()
                    .equals(apoderado.getCodigoApoderado())) {
                lista.set(i, apoderado);
                guardarDatos();
                System.out.println("Apoderado actualizado: " +
                        apoderado.getCodigoApoderado());
                return;
            }
        }
        System.out.println("Apoderado no encontrado.");
    }

    // Elimina usando Iterator — forma segura
    public void eliminar(String codigo) {
        Iterator<Apoderado> it = lista.iterator();
        while (it.hasNext()) {
            Apoderado a = it.next();
            if (a.getCodigoApoderado().equals(codigo)) {
                it.remove(); // elimina sin error
                guardarDatos();
                System.out.println("Apoderado eliminado: " + codigo);
                return;
            }
        }
        System.out.println("No encontrado: " + codigo);
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    // Busca por código usando Lambda
    public Apoderado buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(a -> a.getCodigoApoderado().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // Busca por DNI usando Lambda
    public Apoderado buscarPorDni(String dni) {
        return lista.stream()
                .filter(a -> a.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    // Busca por nombre usando Lambda
    public List<Apoderado> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(a -> a.getNombreCompleto()
                        .toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Filtra por parentesco usando Lambda
    public List<Apoderado> buscarPorParentesco(String parentesco) {
        return lista.stream()
                .filter(a -> a.getParentesco()
                        .equalsIgnoreCase(parentesco))
                .collect(Collectors.toList());
    }

    // Lista solo activos usando Lambda
    public List<Apoderado> listarActivos() {
        return lista.stream()
                .filter(Apoderado::isEstadoActivo)
                .collect(Collectors.toList());
    }

    // Ordena por apellido usando Lambda
    public List<Apoderado> ordenarPorApellido() {
        return lista.stream()
                .sorted((a, b) -> a.getApellidos()
                        .compareToIgnoreCase(b.getApellidos()))
                .collect(Collectors.toList());
    }

    // Total de apoderados
    public int total() {
        return lista.size();
    }

    // Cuenta por parentesco usando Lambda
    public long contarPorParentesco(String parentesco) {
        return lista.stream()
                .filter(a -> a.getParentesco()
                        .equalsIgnoreCase(parentesco))
                .count();
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    // Guarda la lista en archivo .dat
    public void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO))) {
            out.writeObject(lista);
            System.out.println("Apoderados guardados.");
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    // Carga la lista desde archivo .dat
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Sin datos previos de apoderados.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARCHIVO))) {
            lista = (List<Apoderado>) in.readObject();
            System.out.println(lista.size() + " apoderados cargados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }
}