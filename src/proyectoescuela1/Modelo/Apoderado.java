package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Apoderado extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── CÓDIGO AUTOMÁTICO ─────────────────────────
    private static int contador = 1;

    private static String generarCodigo() {
        return "APO" + String.format("%04d", contador++);
    }

    // ── ATRIBUTOS ─────────────────────────────────
    private String  codigoApoderado;
    private String  parentesco;
    private String  ocupacion;
    private boolean estadoActivo;

    // Alumnos asociados al apoderado
    private List<Alumno> alumnos = new ArrayList<>();

    // ── CONSTRUCTOR ───────────────────────────────
    public Apoderado(int id, String nombre, String apellidos,
                     String dni, String email, String telefono,
                     String direccion, Date fechaNac,
                     String parentesco, String ocupacion) {
        super(id, nombre, apellidos, dni, email,
              telefono, direccion, fechaNac);
        this.codigoApoderado = generarCodigo();
        this.parentesco      = parentesco;
        this.ocupacion       = ocupacion;
        this.estadoActivo    = true;
    }

    // ── ITERATOR — recorrer alumnos ───────────────
    public Iterator<Alumno> iteratorAlumnos() {
        return alumnos.iterator();
    }

    // ── GETTERS ───────────────────────────────────
    public String  getCodigoApoderado() { return codigoApoderado; }
    public String  getParentesco()      { return parentesco; }
    public String  getOcupacion()       { return ocupacion; }
    public boolean isEstadoActivo()     { return estadoActivo; }
    public List<Alumno> getAlumnos()    { return alumnos; }

    // ── SETTERS ───────────────────────────────────
    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }
    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }
    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    // Agrega un alumno al apoderado
    public void agregarAlumno(Alumno alumno) {
        alumnos.add(alumno);
    }

    @Override
    public String toString() {
        return "Apoderado{" +
               "codigo='" + codigoApoderado + "'" +
               ", nombre='" + getNombreCompleto() + "'" +
               ", parentesco='" + parentesco + "'" +
               ", ocupacion='" + ocupacion + "'" +
               ", activo=" + estadoActivo + "}";
    }
}