/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package proyectoescuela1.Interfaces;

/**
 *
 * @author USER
 */
public interface Autenticable {
    boolean login(String usuario, String clave);
    void logout();
    void cambiarClave(String nueva);
}
