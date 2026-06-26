/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Alumno extends Usuario{
    
   private String codigoAlumno; 
   private String grado;
   private String nivel;
   private boolean estadoActivo;
   private Date fechaIngreso;
   
   private static int contador = 1; //contador para el codigo de alumno
    private static String generarCodigoAlumno() {
        return "ALUM" + String.format("%04d", contador++);
    }
   //constructor
    public Alumno(int id, String nombre, String apellidos,
                  String dni, String email, String telefono,
                  String direccion, Date fechaNac,
                  String grado, String nivel, Date fechaIngreso) {
        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);
        this.codigoAlumno = generarCodigoAlumno();
        this.grado = grado;
        this.nivel        = nivel;
        this.estadoActivo = true;
        this.fechaIngreso = fechaIngreso;
    }
   
   //arreglos como base Datos por el momento
   
    private List<Nota>       notas       = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();
    private List<Matricula>  matriculas  = new ArrayList<>();
    private List<Incidencia> incidencias = new ArrayList<>();

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
        if (!nivel.equals("Primaria") &&
            !nivel.equals("Secundaria")) {
            throw new IllegalArgumentException(
                "El Nivel ingresado es inválido. Use Primaria o Secundaria"
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

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
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
   
}
