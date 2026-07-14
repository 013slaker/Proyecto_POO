/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Asistencia;
import proyectoescuela1.Modelo.RegistroAsistencia;

/**
 * Controlador del módulo Asistencia — VISTA INDIVIDUAL POR ALUMNO.
 *
 * ── CAMBIO IMPORTANTE DE ARQUITECTURA ──────────────────────────────
 * Antes esta clase guardaba SU PROPIA lista de Asistencia en un archivo
 * separado ("asistencias.dat"), mientras que RegistroAsistenciaControlador
 * guardaba OTRA lista distinta ("registroAsistencia.dat"). Eran dos
 * "bases de datos" separadas para el mismo tipo de información, y por
 * eso el historial no coincidía entre pantallas: lo que el docente
 * registraba por lista (RegistroAsistenciaVista) no aparecía en la
 * ficha individual del alumno (AsistenciaVista), y viceversa.
 *
 * Ahora AsistenciaControlador YA NO GUARDA NADA POR SU CUENTA.
 * Solo consulta y edita las Asistencias que ya existen dentro de los
 * RegistroAsistencia creados al pasar lista (RegistroAsistenciaVista).
 * Así queda UNA SOLA fuente de verdad: RegistroAsistenciaControlador
 * (archivo registroAsistencia.dat).
 *
 * Esta clase actúa como una "fachada" (facade): simplifica el acceso
 * a los datos para la pantalla de ficha individual del alumno, sin
 * duplicar el almacenamiento.
 */
public class AsistenciaControlador {

    /** Controlador que realmente guarda los datos (única fuente de verdad). */
    private RegistroAsistenciaControlador registroCtrl;

    // ── CONSTRUCTORES ─────────────────────────────
    /** Crea su propio RegistroAsistenciaControlador (carga registroAsistencia.dat). */
    public AsistenciaControlador() {
        this.registroCtrl = new RegistroAsistenciaControlador();
    }

    /**
     * Reutiliza un RegistroAsistenciaControlador ya existente (por ejemplo,
     * el mismo que usa RegistroAsistenciaVista). Así se evita abrir y leer
     * el archivo .dat más de una vez y ambas pantallas ven siempre los
     * mismos datos actualizados.
     */
    public AsistenciaControlador(RegistroAsistenciaControlador registroCtrl) {
        this.registroCtrl = registroCtrl;
    }

    // ══════════════════════════════════════════════
    //  UTILIDAD INTERNA
    // ══════════════════════════════════════════════

    /**
     * "Aplana" todos los registros de asistencia (uno por sección/curso/
     * fecha) en una sola lista con TODAS las Asistencias individuales.
     * Lambda: flatMap
     */
    private List<Asistencia> todasLasAsistencias() {
        return registroCtrl.listar().stream()
                .flatMap(r -> r.getListaAsistencias().stream())
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  BÚSQUEDAS — Lambda
    // ══════════════════════════════════════════════

    /** Retorna todas las asistencias de un alumno (su historial completo). */
    public List<Asistencia> buscarPorAlumno(String codigo) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo))
                .collect(Collectors.toList());
    }

    /** Filtra el historial de un alumno por estado (P, T, F, J). */
    public List<Asistencia> buscarPorEstado(String codigo, String estado) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo)
                        && a.getEstado().equals(estado))
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════
    //  ESTADÍSTICAS — Lambda
    // ══════════════════════════════════════════════

    /** Cuenta las faltas (incluye tardanzas) de un alumno. */
    public long contarFaltas(String codigo) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo) && a.esFalta())
                .count();
    }

    /** Cuenta las presencias de un alumno. */
    public long contarPresencias(String codigo) {
        return todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo) && a.esPresente())
                .count();
    }

    /** Calcula el porcentaje de asistencia de un alumno. */
    public double porcentajeAsistencia(String codigo) {
        long total = todasLasAsistencias().stream()
                .filter(a -> a.getCodigoAlumno().equals(codigo))
                .count();
        if (total == 0) return 0.0;
        return (contarPresencias(codigo) * 100.0) / total;
    }

    // ══════════════════════════════════════════════
    //  EDICIÓN — justificar una falta / anotar observación
    // ══════════════════════════════════════════════

    /**
     * Busca la Asistencia por su id (dentro de todos los registros) y la
     * marca como Justificada, agregando la observación indicada
     * (ej. "Certificado médico"). Devuelve true si la encontró y actualizó.
     *
     * Esto reemplaza la vieja lógica de "crear una asistencia nueva":
     * ahora se EDITA el registro que el docente ya tomó por lista.
     */
    public boolean justificar(String idAsistencia, String observacion) {
        for (RegistroAsistencia r : registroCtrl.listar()) {
            for (Asistencia a : r.getListaAsistencias()) {
                if (a.getIdAsistencia().equals(idAsistencia)) {
                    a.setEstado("J");
                    a.setObservacion(observacion);
                    registroCtrl.actualizar(r); // vuelve a guardar en disco
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Actualiza solo la observación de una Asistencia, sin cambiar su estado.
     * Útil para anotar detalles sobre una anomalía sin necesidad de justificar.
     */
    public boolean actualizarObservacion(String idAsistencia, String observacion) {
        for (RegistroAsistencia r : registroCtrl.listar()) {
            for (Asistencia a : r.getListaAsistencias()) {
                if (a.getIdAsistencia().equals(idAsistencia)) {
                    a.setObservacion(observacion);
                    registroCtrl.actualizar(r);
                    return true;
                }
            }
        }
        return false;
    }
}
