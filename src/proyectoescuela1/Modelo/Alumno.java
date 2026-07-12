package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// Serializable permite guardar el objeto en archivo .dat
public class Alumno extends Usuario implements Serializable {

    // Necesario para serialización — identificador de versión
    private static final long serialVersionUID = 1L;

    // Contador automático de código
    private static int contador = 1;

    private static String generarCodigoAlumno() {
        return "ALUM" + String.format("%04d", contador++);
    }

    // Atributos
    private String  codigoAlumno;
    private String  grado;
    private String  nivel;
    private boolean estadoActivo;
    private Date    fechaIngreso;

    // Composición — listas internas
    private List<Nota>       notas       = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();
    private List<Matricula>  matriculas  = new ArrayList<>();
    private List<Incidencia> incidencias = new ArrayList<>();

    // Constructor
    public Alumno(int id, String nombre, String apellidos,
                  String dni, String email, String telefono,
                  String direccion, Date fechaNac,
                  String grado, String nivel, Date fechaIngreso) {
        super(id, nombre, apellidos, dni, email,
              telefono, direccion, fechaNac);
        this.codigoAlumno = generarCodigoAlumno();
        this.grado        = grado;
        this.nivel        = nivel;
        this.estadoActivo = true;
        this.fechaIngreso = fechaIngreso;
    }

    // ── ITERATOR — recorrer notas de forma controlada ──
    public Iterator<Nota> iteratorNotas() {
        return notas.iterator();
    }

    // ── ITERATOR — recorrer asistencias ───────────────
    public Iterator<Asistencia> iteratorAsistencias() {
        return asistencias.iterator();
    }

    @Override
    public String toString() {
        return "Alumno{" +
               "codigo='" + codigoAlumno + "'" +
               ", nombre='" + getNombreCompleto() + "'" +
               ", grado='" + grado + "'" +
               ", nivel='" + nivel + "'" +
               ", activo=" + estadoActivo +
               "}";
    }

    // Getters
    public String  getCodigoAlumno() { return codigoAlumno; }
    public String  getGrado()        { return grado; }
    public String  getNivel()        { return nivel; }
    public boolean isEstadoActivo()  { return estadoActivo; }
    public Date    getFechaIngreso() { return fechaIngreso; }
    public List<Nota>       getNotas()       { return notas; }
    public List<Asistencia> getAsistencias() { return asistencias; }
    public List<Matricula>  getMatriculas()  { return matriculas; }
    public List<Incidencia> getIncidencias() { return incidencias; }

    // Setters
    public void setGrado(String grado) { this.grado = grado; }
    public void setNivel(String nivel) {
        if (!nivel.equals("Primaria") && !nivel.equals("Secundaria"))
            throw new IllegalArgumentException(
                "Nivel inválido. Use Primaria o Secundaria"
            );
        this.nivel = nivel;
    }
    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }
    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }
    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
}