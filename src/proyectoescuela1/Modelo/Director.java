/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

/**
 *
 * @author ALEX
 */
public class Director extends Usuario {

    public Director(int id, String nombre, String apellido, String dni, String correo, String telefono) {
        super(id, nombre, apellido, dni, correo, telefono);
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
    
    
    
    
    
}
