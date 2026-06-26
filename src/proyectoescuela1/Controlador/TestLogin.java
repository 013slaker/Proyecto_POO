/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import proyectoescuela1.Modelo.*;



public class TestLogin {

    
   public void IniciarSesion(ArrayList<Cuenta> testCuentas) {
    String usuario = JOptionPane.showInputDialog(null, "Ingrese usuario");
    String contraseña = JOptionPane.showInputDialog(null, "Ingrese contraseña");

    for (Cuenta cuenta : testCuentas) {
        
        
        if (cuenta.validarCreedenciales(usuario, contraseña)) {
            
    
            Usuario tipoUsuario = cuenta.getasociacion();
            
            if (tipoUsuario instanceof Director) {
                JOptionPane.showMessageDialog(null, "Bienvenido, Director");
            } else if (tipoUsuario instanceof Secretaria) {
                JOptionPane.showMessageDialog(null, "Bienvenido, Secretaria");
            }
            
            return; 
        }
    }
    
   
    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
}
    
    
    
    
    




    
}
