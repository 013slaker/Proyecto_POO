package proyectoescuela1.Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alumno extends Usuario {

    private String codigoAlumno;
    private String grado;
    private String nivel;
    private boolean estadoActivo;
    private Date fechaIngreso;

    private Apoderado apoderado;

    // ── LISTAS 
    private List<Nota> notas = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();
    private List<Matricula> matriculas = new ArrayList<>();
    private List<Incidencia> incidencias = new ArrayList<>();

    private static int contador = 1;

    private static String generarCodigoAlumno() {
        return "ALUM" + String.format("%04d", contador++);
    }

    public Alumno(int id, String nombre,
            String apellidos, String dni,
            String email, String telefono,
            String direccion, Date fechaNac,
            String grado, String nivel,
            Date fechaIngreso, Apoderado apoderado) {
        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);

        this.codigoAlumno = generarCodigoAlumno();
        this.grado = grado;
        this.nivel = nivel;
        this.estadoActivo = true;

        
        this.fechaIngreso = fechaIngreso;

        this.apoderado = apoderado;
    }

   
    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        if (!nivel.equals("Primaria") && !nivel.equals("Secundaria")) {
            throw new IllegalArgumentException(
                    "Nivel inválido. Use Primaria o Secundaria"
            );
        }
        this.nivel = nivel;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // ── RELACIÓN APODERADO ────────────────────────────
    public Apoderado getApoderado() {
        return apoderado;
    }

    public void setApoderado(Apoderado apoderado) {
        this.apoderado = apoderado;
    }

    // ── LISTAS ─────────────────────────────────────────
    public List<Nota> getNotas() {
        return notas;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    // ── TO STRING ──────────────────────────────────────
    @Override
    public String toString() {
        return "Alumno{"
                + "codigo='" + codigoAlumno + '\''
                + ", nombre='" + getNombreCompleto() + '\''
                + ", grado='" + grado + '\''
                + ", nivel='" + nivel + '\''
                + ", apoderado='"
                + (apoderado != null ? apoderado.getNombreCompleto() : "Sin asignar")
                + '\''
                + ", activo=" + estadoActivo
                + '}';
    }
}
