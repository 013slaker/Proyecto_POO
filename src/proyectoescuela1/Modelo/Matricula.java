package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.Date;

public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoMatricula;
    private Alumno alumno;
    private int anio;
    private String nivel;
    private String grado;
    private String seccion;
    private Date fechaMatricula;
    private String estado;
    private String motivoRetiro;
    private static int contador = 1;
    
    private static String generarCodigo() {
        return "MAT" + String.format("%04d", contador++);
    }

    public Matricula(
            Alumno alumno,
            int anio,
            String nivel,
            String grado,
            String seccion,
            Date fechaMatricula) {

        this.codigoMatricula = generarCodigo();

        this.alumno = alumno;

        this.anio = anio;

        this.nivel = nivel;

        this.grado = grado;

        this.seccion = seccion;

        this.fechaMatricula = fechaMatricula;

        this.estado = "Matriculado";
    }

    //=========================
    // GETTERS
    //=========================
    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public int getAnio() {
        return anio;
    }

    public String getNivel() {
        return nivel;
    }

    public String getGrado() {
        return grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public Date getFechaMatricula() {
        return fechaMatricula;
    }

    public String getEstado() {
        return estado;
    }

    public String getMotivoRetiro() {
        return motivoRetiro;
    }

    //=========================
    // SETTERS
    //=========================
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMotivoRetiro(String motivoRetiro) {
        this.motivoRetiro = motivoRetiro;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    @Override
    public String toString() {
        return codigoMatricula + " - " + alumno.getNombreCompleto();
    }

}
