/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.util.Date;


public class Director extends Usuario{

    public Director(int id, String nombre, String apellidos, String dni, String email, String telefono, String direccion, Date fechaNac) {
        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);
    }

    public void gestionarSistema(){
    System.out.println("sistema gestionado...");
    }
    
    public void aprobarComunicado(){
    System.out.println("Comunicado aprobado.");
    
    }
    
    public void aprobarMatricula(){
    System.out.println("Matricula aprobada");
    }

    @Override
    public String toString() {
        return "Director{id=" + id + ", nombre='" + getNombreCompleto() + "'}";
    }
    
    
    
    
    
}
