/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Horario;

/**
 * Controlador del módulo Horarios.
 * Maneja CRUD, búsquedas con Lambda,
 * eliminación con Iterator, detección de
 * conflictos y serialización.
 */
public class HorarioControlador {

    // ── BASE DE DATOS EN MEMORIA ──────────────────
    /** Lista donde se almacenan todos los horarios */
    private List<Horario> horarios = new ArrayList<>();

    /** Archivo de persistencia */
    private static final String ARCHIVO = "horarios.dat";

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Al iniciar carga los datos guardados
     * automáticamente.
     */
    public HorarioControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════════

    /**
     * Registra un nuevo horario.
     * Antes verifica que no haya conflicto
     * con otro horario existente.
     */
    public boolean registrar(Horario horario) {
        // verifica conflicto antes de registrar
        if (hayConflicto(horario)) {
            System.out.println(
                "Conflicto detectado: " +
                horario.getDia() + " " +
                horario.getHoraInicio() + " - " +
                horario.getHoraFin() +
                " en aula " + horario.getAula()
            );
            return false; // no registra si hay conflicto
        }
        horarios.add(horario);
        guardarDatos();
        System.out.println("Horario registrado: " +
                horario.getIdHorario());
        return true;
    }

    /**
     * Retorna toda la lista de horarios.
     */
    public List<Horario> listar() {
        return horarios;
    }

    /**
     * Actualiza los datos de un horario existente.
     */
    public void actualizar(Horario horario) {
        for (int i = 0; i < horarios.size(); i++) {
            if (horarios.get(i).getIdHorario()
                    .equals(horario.getIdHorario())) {
                horarios.set(i, horario);
                guardarDatos();
                System.out.println("Horario actualizado: " +
                        horario.getIdHorario());
                return;
            }
        }
        System.out.println("Horario no encontrado.");
    }

    /**
     * Elimina un horario usando Iterator.
     * Forma segura de eliminar mientras
     * se recorre la lista.
     */
    public void eliminar(String idHorario) {
        Iterator<Horario> it = horarios.iterator();
        while (it.hasNext()) {
            Horario h = it.next();
            if (h.getIdHorario().equals(idHorario)) {
                it.remove(); // eliminación segura
                guardarDatos();
                System.out.println("Horario eliminado: " +
                        idHorario);
                return;
            }
        }
        System.out.println("No encontrado: " + idHorario);
    }

    // ══════════════════════════════════════════════
    //  DETECCIÓN DE CONFLICTOS
    // ══════════════════════════════════════════════

    /**
     * Verifica si un horario tiene conflicto
     * con alguno ya registrado.
     * Lambda: anyMatch para buscar conflictos
     */
    public boolean hayConflicto(Horario nuevo) {
        return horarios.stream()
                .anyMatch(h -> h.hayConflicto(nuevo));
    }

    /**
     * Verifica conflicto excluyendo el propio horario
     * (usado al actualizar).
     */
    public boolean hayConflictoExcluyendo(
            Horario nuevo, String idExcluir) {
        return horarios.stream()
                .filter(h -> !h.getIdHorario()
                        .equals(idExcluir))
                .anyMatch(h -> h.hayConflicto(nuevo));
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /**
     * Busca un horario por su código exacto.
     * Lambda: filter + findFirst
     */
    public Horario buscarPorId(String id) {
        return horarios.stream()
                .filter(h -> h.getIdHorario().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filtra horarios por día.
     * Lambda: filter por día
     */
    public List<Horario> buscarPorDia(String dia) {
        return horarios.stream()
                .filter(h -> h.getDia()
                        .equalsIgnoreCase(dia))
                .collect(Collectors.toList());
    }

    /**
     * Filtra horarios por nivel educativo.
     * Lambda: filter por nivel
     */
    public List<Horario> buscarPorNivel(String nivel) {
        return horarios.stream()
                .filter(h -> h.getNivel()
                        .equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
    }

    /**
     * Filtra horarios por aula.
     * Lambda: filter por aula
     */
    public List<Horario> buscarPorAula(String aula) {
        return horarios.stream()
                .filter(h -> h.getAula()
                        .equalsIgnoreCase(aula))
                .collect(Collectors.toList());
    }

    /**
     * Ordena los horarios por día y hora de inicio.
     * Lambda: sorted con comparador
     */
    public List<Horario> ordenarPorDiaYHora() {
        // orden de días de la semana
        List<String> ordenDias = Arrays.asList(
            "Lunes", "Martes", "Miércoles",
            "Jueves", "Viernes"
        );
        return horarios.stream()
                .sorted((a, b) -> {
                    // primero compara por día
                    int difDia = ordenDias.indexOf(a.getDia())
                               - ordenDias.indexOf(b.getDia());
                    if (difDia != 0) return difDia;
                    // si el día es igual compara por hora
                    return a.getHoraInicio()
                            .compareTo(b.getHoraInicio());
                })
                .collect(Collectors.toList());
    }

    /** Total de horarios registrados */
    public int total() {
        return horarios.size();
    }

    /**
     * Cuenta horarios por día.
     * Lambda: filter + count
     */
    public long contarPorDia(String dia) {
        return horarios.stream()
                .filter(h -> h.getDia()
                        .equalsIgnoreCase(dia))
                .count();
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
            out.writeObject(horarios);
            System.out.println("Horarios guardados.");
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
                    "Sin datos previos de horarios.");
            return;
        }
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            horarios = (List<Horario>) in.readObject();
            System.out.println(horarios.size() +
                    " horarios cargados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar: " +
                    e.getMessage());
        }
    }
}