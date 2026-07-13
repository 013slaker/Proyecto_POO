package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modelo de Curso.
 * Implementa Serializable para poder guardar
 * los datos en archivos .dat (persistencia).
 */
public class Curso implements Serializable {

    // Necesario para la serialización
    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    /**
     * Genera un código automático tipo CUR0001, CUR0002...
     */
    private static String generarCodigo() {
        return "CUR" + String.format("%04d", contador++);
    }

    // ── ATRIBUTOS ─────────────────────────────────
    private String idCurso;
    private String nombre;
    private String area;
    private int    horasSemanales;
    private String nivel;
    // nivel: Inicial | Primaria | Secundaria
    private boolean activo;

    /**
     * Lista de docentes asignados al curso.
     * Un curso puede tener varios docentes
     * (composición).
     */
    private List<Docente> docentes = new ArrayList<>();

    // ── CONSTRUCTOR ───────────────────────────────
    /**
     * Crea un nuevo curso con código generado
     * automáticamente.
     */
    public Curso(String nombre, String area,
                 int horasSemanales, String nivel) {
        this.idCurso        = generarCodigo();
        this.nombre         = nombre;
        this.area           = area;
        this.horasSemanales = horasSemanales;
        this.nivel          = nivel;
        this.activo         = true;
    }

    // ── ITERATOR ──────────────────────────────────
    /**
     * Permite recorrer la lista de docentes
     * asignados al curso de forma controlada.
     * Uso: Iterator<Docente> it = curso.iteratorDocentes()
     */
    public Iterator<Docente> iteratorDocentes() {
        return docentes.iterator();
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────
    /**
     * Agrega un docente al curso.
     * Verifica que no esté ya asignado.
     */
    public void agregarDocente(Docente docente) {
        // verifica duplicado antes de agregar
        boolean yaAsignado = docentes.stream()
                .anyMatch(d -> d.getCodigoDocente()
                        .equals(docente.getCodigoDocente()));
        if (!yaAsignado) {
            docentes.add(docente);
        }
    }

    /**
     * Elimina un docente del curso usando Iterator.
     * Forma segura de eliminar mientras se recorre.
     */
    public void eliminarDocente(String codigoDocente) {
        Iterator<Docente> it = docentes.iterator();
        while (it.hasNext()) {
            Docente d = it.next();
            if (d.getCodigoDocente().equals(codigoDocente)) {
                it.remove(); // elimina sin error
                return;
            }
        }
    }

    /**
     * Activa el curso.
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva el curso.
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Retorna la cantidad de docentes asignados.
     */
    public int cantidadDocentes() {
        return docentes.size();
    }

    // ── GETTERS ───────────────────────────────────
    public String  getIdCurso()        { return idCurso; }
    public String  getNombre()         { return nombre; }
    public String  getArea()           { return area; }
    public int     getHorasSemanales() { return horasSemanales; }
    public String  getNivel()          { return nivel; }
    public boolean isActivo()          { return activo; }
    public List<Docente> getDocentes() { return docentes; }

    // ── SETTERS ───────────────────────────────────
    /** Cambia el nombre del curso */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Cambia el área del curso */
    public void setArea(String area) {
        this.area = area;
    }

    /** Cambia las horas semanales */
    public void setHorasSemanales(int horasSemanales) {
        if (horasSemanales <= 0)
            throw new IllegalArgumentException(
                "Las horas deben ser mayor a 0"
            );
        this.horasSemanales = horasSemanales;
    }

    /** Cambia el nivel del curso */
    public void setNivel(String nivel) {
        if (!nivel.equals("Inicial") &&
            !nivel.equals("Primaria") &&
            !nivel.equals("Secundaria"))
            throw new IllegalArgumentException(
                "Nivel inválido."
            );
        this.nivel = nivel;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }

    @Override
    public String toString() {
        return "Curso{" +
               "id='" + idCurso + "'" +
               ", nombre='" + nombre + "'" +
               ", area='" + area + "'" +
               ", horas=" + horasSemanales +
               ", nivel='" + nivel + "'" +
               ", activo=" + activo + "}";
    }
}