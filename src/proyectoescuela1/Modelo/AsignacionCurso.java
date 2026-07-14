/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * AsignacionCurso une Docente + Curso + Seccion.
 * Aplica reglas distintas según el nivel:
 *
 * PRIMARIA:
 *   - 1 docente titular para todos los cursos
 *   - Docentes especiales para Ed.Física y Cómputo
 *
 * SECUNDARIA:
 *   - 1 docente distinto por cada curso
 */
public class AsignacionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    private static String generarCodigo() {
        return "ASG" + String.format("%04d", contador++);
    }

    // ── CURSOS ESPECIALES DE PRIMARIA ─────────────
    // Estos cursos siempre tienen docente propio
    // aunque sea primaria
    private static final List<String> CURSOS_ESPECIALES =
        List.of(
            "Educación Física",
            "Computación",
            "Arte",
            "Inglés"
        );

    // ── ATRIBUTOS ─────────────────────────────────
    private String idAsignacion;
    private String nivel;
    // PRIMARIA | SECUNDARIA
    private String nombreSeccion;
    // ejemplo: 3°A, 4°B

    /**
     * Docente titular — solo en primaria.
     * Dicta todos los cursos no especiales.
     */
    private Docente docenteTitular;

    /**
     * Lista de asignaciones específicas.
     * En secundaria: un docente por cada curso.
     * En primaria: solo cursos especiales.
     */
    private List<ItemAsignacion> items = new ArrayList<>();

    // ── CONSTRUCTOR ───────────────────────────────
    public AsignacionCurso(String nivel,
                           String nombreSeccion) {
        this.idAsignacion  = generarCodigo();
        this.nivel         = nivel;
        this.nombreSeccion = nombreSeccion;
    }

    // ── CLASE INTERNA ─────────────────────────────
    /**
     * Representa un par Curso-Docente.
     * Usado en secundaria para cada materia,
     * y en primaria para cursos especiales.
     */
    public static class ItemAsignacion
            implements Serializable {

        private static final long serialVersionUID = 1L;

        private Curso   curso;
        private Docente docente;
        private Horario horario;

        public ItemAsignacion(Curso curso,
                              Docente docente,
                              Horario horario) {
            this.curso   = curso;
            this.docente = docente;
            this.horario = horario;
        }

        public Curso   getCurso()   { return curso; }
        public Docente getDocente() { return docente; }
        public Horario getHorario() { return horario; }

        public void setHorario(Horario horario) {
            this.horario = horario;
        }

        @Override
        public String toString() {
            return curso.getNombre() + " → " +
                   docente.getNombreCompleto();
        }
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    /**
     * Asigna el docente titular para primaria.
     * Solo aplica si el nivel es PRIMARIA.
     * Lanza excepción si se intenta en secundaria.
     */
    public void asignarDocenteTitular(Docente docente) {
        if (!nivel.equals("Primaria")) {
            throw new IllegalStateException(
                "El docente titular solo aplica " +
                "en Primaria."
            );
        }
        this.docenteTitular = docente;
        System.out.println("Docente titular asignado: " +
            docente.getNombreCompleto() +
            " → " + nombreSeccion);
    }

    /**
     * Agrega una asignación Curso-Docente.
     *
     * En SECUNDARIA: cualquier curso puede tener
     * su propio docente.
     *
     * En PRIMARIA: solo se permite para cursos
     * especiales (Ed.Física, Cómputo, Arte, Inglés).
     * Los demás los dicta el titular.
     */
    public void agregarItem(Curso curso,
                            Docente docente,
                            Horario horario) {

        if (nivel.equals("Primaria")) {
            // en primaria solo acepta cursos especiales
            if (!esCursoEspecial(curso.getNombre())) {
                throw new IllegalArgumentException(
                    "En Primaria, '" + curso.getNombre() +
                    "' lo dicta el docente titular.\n" +
                    "Solo puedes asignar docentes a: " +
                    CURSOS_ESPECIALES
                );
            }
        }

        // verifica que el curso no esté ya asignado
        boolean yaExiste = items.stream()
            .anyMatch(i -> i.getCurso().getIdCurso()
                           .equals(curso.getIdCurso()));
        if (yaExiste) {
            throw new IllegalArgumentException(
                "El curso '" + curso.getNombre() +
                "' ya tiene docente asignado en " +
                nombreSeccion
            );
        }

        items.add(new ItemAsignacion(
            curso, docente, horario
        ));
        System.out.println("Asignado: " +
            curso.getNombre() + " → " +
            docente.getNombreCompleto() +
            " en " + nombreSeccion);
    }

    /**
     * Elimina una asignación por nombre de curso
     * usando Iterator — forma segura.
     */
    public void eliminarItem(String nombreCurso) {
        Iterator<ItemAsignacion> it = items.iterator();
        while (it.hasNext()) {
            ItemAsignacion item = it.next();
            if (item.getCurso().getNombre()
                    .equals(nombreCurso)) {
                it.remove();
                System.out.println("Asignación eliminada: "
                    + nombreCurso);
                return;
            }
        }
    }

    /**
     * Verifica si un curso es especial en primaria.
     * Lambda: anyMatch para buscar en la lista fija
     */
    private boolean esCursoEspecial(String nombre) {
        return CURSOS_ESPECIALES.stream()
                .anyMatch(c -> c.equalsIgnoreCase(nombre));
    }

    /**
     * Retorna el docente que dicta un curso.
     * En primaria retorna el titular salvo que
     * sea un curso especial.
     * En secundaria busca en los items.
     */
    public Docente getDocenteDeCurso(String nombreCurso) {

        if (nivel.equals("Primaria") &&
            !esCursoEspecial(nombreCurso)) {
            // en primaria el titular dicta este curso
            return docenteTitular;
        }

        // busca en los items — Lambda
        return items.stream()
                .filter(i -> i.getCurso().getNombre()
                             .equalsIgnoreCase(nombreCurso))
                .map(ItemAsignacion::getDocente)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna el iterator de items para recorrerlos.
     */
    public Iterator<ItemAsignacion> iteratorItems() {
        return items.iterator();
    }

    /**
     * Verifica si la asignación es válida.
     * Primaria: debe tener docente titular
     * Secundaria: debe tener al menos un item
     */
    public boolean esValida() {
        if (nivel.equals("Primaria")) {
            return docenteTitular != null;
        }
        return !items.isEmpty();
    }

    // ── GETTERS ───────────────────────────────────
    public String getIdAsignacion()  { return idAsignacion; }
    public String getNivel()         { return nivel; }
    public String getNombreSeccion() { return nombreSeccion; }
    public Docente getDocenteTitular() {
        return docenteTitular;
    }
    public List<ItemAsignacion> getItems() { return items; }

    // ── SETTERS ───────────────────────────────────
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    public void setNombreSeccion(String seccion) {
        this.nombreSeccion = seccion;
    }

    @Override
    public String toString() {
        return "AsignacionCurso{" +
               "id='" + idAsignacion + "'" +
               ", nivel='" + nivel + "'" +
               ", seccion='" + nombreSeccion + "'" +
               ", items=" + items.size() +
               "}";
    }
}