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
    private String codigoAlumno;
    private String nivel;
    private String grado;
    private String seccion;
    private boolean estadoActivo;
    private Date fechaIngreso;

    // ── DATOS PERSONALES ADICIONALES (para la matrícula) ──
    // Se agregan como propiedades editables (con valores por
    // defecto) en vez de meterlas al constructor, para no romper
    // los más de 9 lugares del proyecto que ya construyen un
    // Alumno con el constructor actual.
    private String departamentoNacimiento = "";
    private String provinciaNacimiento = "";
    private String distritoNacimiento = "";
    /** SIS | EsSalud | Particular | Ninguno */
    private String tipoSeguro = "Ninguno";
    private boolean presentaDiscapacidad = false;
    /** Detalle de la discapacidad (solo si presentaDiscapacidad = true) */
    private String tipoDiscapacidad = "";

    // Composición — listas internas
    private List<Nota> notas = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();
    private List<Matricula> matriculas = new ArrayList<>();
    private List<Incidencia> incidencias = new ArrayList<>();

    // Constructor
    public Alumno(
            int id,
            String nombre,
            String apellidos,
            String dni,
            String email,
            String telefono,
            String direccion,
            Date fechaNac,
            String nivel,
            String grado,
            String seccion,
            Date fechaIngreso) {
        super(id, nombre, apellidos, dni, email,
                telefono, direccion, fechaNac);
        this.codigoAlumno = generarCodigoAlumno();
        this.nivel = nivel;
        this.grado = grado;
        this.seccion = seccion;
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

    
    // Getters
    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public String getGrado() {
        return grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getNivel() {
        return nivel;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

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

    public String getDepartamentoNacimiento() {
        return departamentoNacimiento;
    }

    public String getProvinciaNacimiento() {
        return provinciaNacimiento;
    }

    public String getDistritoNacimiento() {
        return distritoNacimiento;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public boolean isPresentaDiscapacidad() {
        return presentaDiscapacidad;
    }

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    // Setters
    public void setGrado(String grado) {
        this.grado = grado;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public void setNivel(String nivel) {
        if (!nivel.equals("Primaria") && !nivel.equals("Secundaria")) {
            throw new IllegalArgumentException(
                    "Nivel inválido. Use Primaria o Secundaria"
            );
        }
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

    public void setDepartamentoNacimiento(String departamentoNacimiento) {
        this.departamentoNacimiento = departamentoNacimiento;
    }

    public void setProvinciaNacimiento(String provinciaNacimiento) {
        this.provinciaNacimiento = provinciaNacimiento;
    }

    public void setDistritoNacimiento(String distritoNacimiento) {
        this.distritoNacimiento = distritoNacimiento;
    }

    /** SIS | EsSalud | Particular | Ninguno */
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public void setPresentaDiscapacidad(boolean presentaDiscapacidad) {
        this.presentaDiscapacidad = presentaDiscapacidad;
        if (!presentaDiscapacidad) {
            this.tipoDiscapacidad = ""; // limpia el detalle si ya no aplica
        }
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }
    @Override
public String toString() {

    return "Alumno{" +
            "codigo='" + codigoAlumno + '\'' +
            ", nombre='" + getNombreCompleto() + '\'' +
            ", nivel='" + nivel + '\'' +
            ", grado='" + grado + '\'' +
            ", seccion='" + seccion + '\'' +
            ", activo=" + estadoActivo +
            '}';
}
    
}
