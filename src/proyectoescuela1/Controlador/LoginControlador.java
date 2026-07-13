
package proyectoescuela1.Controlador;

import java.util.List;
import proyectoescuela1.Modelo.Cuenta;

public class LoginControlador {

    private List<Cuenta> cuentas;

    public LoginControlador(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    // devuelve la cuenta, no el rol
    public Cuenta validarLogin(String usuario, String contrasena) {
        for (Cuenta c : cuentas) {
            if (c.getUsuario().equals(usuario) &&
                c.getContrasena().equals(contrasena)) {
                return c;
            }
        }
        return null;
    }
}    
