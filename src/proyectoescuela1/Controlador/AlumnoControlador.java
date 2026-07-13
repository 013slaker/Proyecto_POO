package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Nota;

public class AlumnoControlador {

    // ── BASE DE DATOS EN MEMORIA (ArrayList) ──────
    private List<Alumno> alumnos = new ArrayList<>();

    // Archivo para persistencia
    private static final String ARCHIVO = "alumnos.dat";

    // Constructor — carga datos al iniciar
    public AlumnoControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD BÁSICO
    // ══════════════════════════════════════════════

    // Registra un alumno y guarda en archivo
    public void registrarAlumno(Alumno alumno) {
        alumnos.add(alumno);
        guardarDatos(); // serialización automática
        System.out.println("Alumno registrado: " +
                alumno.getCodigoAlumno() + " - " +
                alumno.getNombreCompleto());
    }

    // Elimina usando Iterator — forma segura de eliminar
    // mientras se recorre la lista
    public void eliminarAlumno(String codigo) {
        Iterator<Alumno> it = alumnos.iterator();
        while (it.hasNext()) {
            Alumno a = it.next();
            if (a.getCodigoAlumno().equals(codigo)) {
                it.remove(); // elimina sin ConcurrentModificationException
                guardarDatos();
                System.out.println("Alumno eliminado: " + codigo);
                return;
            }
        }
        System.out.println("Alumno no encontrado: " + codigo);
    }

    // Actualiza datos de un alumno existente
    public void actualizarAlumno(Alumno alumno) {
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getCodigoAlumno()
                    .equals(alumno.getCodigoAlumno())) {
                alumnos.set(i, alumno);
                guardarDatos();
                System.out.println("Alumno actualizado: " +
                        alumno.getCodigoAlumno());
                return;
            }
        }
        System.out.println("Alumno no encontrado para actualizar.");
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS
    // ══════════════════════════════════════════════

    // Retorna todos los alumnos
    public List<Alumno> listarTodos() {
        return alumnos;
    }

    // Busca por código usando Lambda
    public Alumno buscarPorCodigo(String codigo) {
        return alumnos.stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // Busca por DNI usando Lambda
    public Alumno buscarPorDni(String dni) {
        return alumnos.stream()
                .filter(a -> a.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    // Busca por nombre usando Lambda — no distingue mayúsculas
    public List<Alumno> buscarPorNombre(String nombre) {
        return alumnos.stream()
                .filter(a -> a.getNombreCompleto()
                        .toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Filtra por nivel usando Lambda
    public List<Alumno> buscarPorNivel(String nivel) {
        return alumnos.stream()
                .filter(a -> a.getNivel().equals(nivel))
                .collect(Collectors.toList());
    }

    // Filtra solo alumnos activos usando Lambda
    public List<Alumno> listarActivos() {
        return alumnos.stream()
                .filter(a -> a.isEstadoActivo())
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  MÉTODOS DE NOTAS — Lambda
    // ══════════════════════════════════════════════

    // Agrega nota a un alumno
    public void agregarNota(String codigoAlumno, Nota nota) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno != null) {
            alumno.getNotas().add(nota);
            guardarDatos();
            System.out.println("Nota agregada: " + codigoAlumno);
        }
    }
/*
    // Calcula promedio general usando Lambda mapToDouble
    public double calcularPromedioGeneral(String codigoAlumno) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno == null || alumno.getNotas().isEmpty())
            return 0.0;
        // Lambda: suma todos los valores y calcula promedio
        return alumno.getNotas().stream()
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0.0);
    }

    // Calcula promedio por bimestre usando Lambda
    public double calcularPromedioBimestre(
            String codigoAlumno, int bimestre) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno == null) return 0.0;
        // Lambda: filtra por bimestre y calcula promedio
        return alumno.getNotas().stream()
                .filter(n -> n.getBimestre() == bimestre)
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0.0);
    }

    // Verifica si está aprobado — promedio >= 11
    public boolean estaAprobado(String codigoAlumno) {
        return calcularPromedioGeneral(codigoAlumno) >= 11;
    }

    // Lista alumnos desaprobados usando Lambda
    public List<Alumno> alumnosDesaprobados() {
        return alumnos.stream()
                .filter(a -> !estaAprobado(a.getCodigoAlumno()))
                .collect(Collectors.toList());
    }

    // Ranking de promedios usando Lambda y Map
    public Map<String, Double> rankingPromedios() {
        Map<String, Double> ranking = new HashMap<>();
        // Lambda: recorre todos y calcula promedio de cada uno
        alumnos.forEach(a -> ranking.put(
                a.getNombreCompleto(),
                calcularPromedioGeneral(a.getCodigoAlumno())
        ));
        return ranking;
    }

    // Ordena alumnos por apellido usando Lambda
    public List<Alumno> ordenarPorApellido() {
        return alumnos.stream()
                .sorted((a, b) -> a.getApellidos()
                        .compareTo(b.getApellidos()))
                .collect(Collectors.toList());
    }
*/
    // ══════════════════════════════════════════════
    //  ESTADÍSTICAS
    // ══════════════════════════════════════════════

    // Total de alumnos registrados
    public int totalAlumnos() {
        return alumnos.size();
    }

    // Total por nivel usando Lambda
    public long totalPorNivel(String nivel) {
        return alumnos.stream()
                .filter(a -> a.getNivel().equals(nivel))
                .count();
    }

    // Elimina todos los inactivos usando Iterator
    public void eliminarInactivos() {
        Iterator<Alumno> it = alumnos.iterator();
        while (it.hasNext()) {
            Alumno a = it.next();
            if (!a.isEstadoActivo()) {
                it.remove();
                System.out.println("Inactivo eliminado: " +
                        a.getCodigoAlumno());
            }
        }
        guardarDatos();
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN — guardar y cargar datos
    // ══════════════════════════════════════════════

    // Guarda la lista en archivo .dat (serialización)
    public void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO))) {
            out.writeObject(alumnos);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    // Carga la lista desde archivo .dat (deserialización)
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Sin datos previos. Iniciando vacío.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARCHIVO))) {
            alumnos = (List<Alumno>) in.readObject();
            System.out.println("Datos cargados: " +
                    alumnos.size() + " alumnos.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }
}
