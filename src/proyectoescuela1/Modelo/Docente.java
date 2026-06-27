/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.util.Date;

/**
 *
 * @author USER
 */
public class Docente extends Usuario {

    private String codigoDocente;
    private String especialidad;
    private String nivel; // Inicial, Primaria, Secundaria
    private Date fechaIngreso;

    private static int contador = 1;

    private static String generarCodigo() {
        return "DOC" + String.format("%04d", contador++);
    }

    public Docente(int id, String nombre, String apellidos,
            String dni, String email, String telefono,
            String direccion, Date fechaNac,
            String especialidad, String nivel, Date fechaIngreso) {

        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);

        this.codigoDocente = generarCodigo();
        this.especialidad = especialidad;
        this.nivel = nivel;
        this.fechaIngreso = fechaIngreso;
    }

    public String getCodigoDocente() {
        return codigoDocente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getNivel() {
        return nivel;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    @Override
public String toString() {
    return "Docente{" +
            "codigo='" + codigoDocente + '\'' +
            ", nombre='" + getNombreCompleto() + '\'' +
            ", especialidad='" + especialidad + '\'' +
            ", nivel='" + nivel + '\'' +
            '}';
}
}
