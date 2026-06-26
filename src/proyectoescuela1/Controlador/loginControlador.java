/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;
import java.util.List;
import proyectoescuela1.Modelo.*;

public class loginControlador {
  private List<Cuenta> cuentas;

    public loginControlador(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
  //devuelve el cargo si las credenciales son correctas//
  //si son incorrectas simplemente devuelve nulo
  public String validarLogin(String usuario, String contrasena){
  for(Cuenta c: cuentas){
      if(c.login(usuario, contrasena)){
      return c.getRol();
      
      }
      
  } 
      
  return null;
  }     
}
