/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Apoderado extends Usuario {

    // ── ATRIBUTOS PROPIOS ─────────────────────────────
    private String codigoApoderado;
    private String parentesco;
    private String ocupacion;
    private boolean estadoActivo;

    // código automático
    private static int contador = 1;

    private static String generarCodigo() {
        return "APO" + String.format("%04d", contador++);
    }

    // alumnos asociados (opcional pero útil)
    private List<Alumno> alumnos = new ArrayList<>();

    // CONSTRUCTOR
    public Apoderado(int id, String nombre, String apellidos,
            String dni, String email, String telefono,
            String direccion, Date fechaNac,
            String parentesco, String ocupacion) {

        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);

        this.codigoApoderado = generarCodigo();
        this.parentesco = parentesco;
        this.ocupacion = ocupacion;
        this.estadoActivo = true;
    }

    // GETTERS Y SETTERS
    public String getCodigoApoderado() {
        return codigoApoderado;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    // RELACIÓN
    public void agregarAlumno(Alumno alumno) {
        alumnos.add(alumno);
        alumno.setApoderado(this);
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    @Override
    public String toString() {
        return "Apoderado{"
                + "codigo='" + codigoApoderado + '\''
                + ", nombre='" + getNombreCompleto() + '\''
                + ", parentesco='" + parentesco + '\''
                + ", activo=" + estadoActivo + '\''
                + ", ocupacion=" + ocupacion
                + '}';
    }
}
