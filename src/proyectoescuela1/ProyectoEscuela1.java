package proyectoescuela1;
import java.util.ArrayList;
import java.time.LocalDateTime;
import proyectoescuela1.Modelo.*;
import javax.swing.JOptionPane;
import proyectoescuela1.Controlador.*;

public class ProyectoEscuela1 {

    public static void main(String[] args) {
        // Usuarios de prueba //
        
        //tipos de usuarios.
        Director testDirector = new Director(1,"nombre1","apellido2","123456789","correo@test","9999999999");
        Secretaria testSecretaria = new Secretaria(2,"nombre2","apellido2","987654321","correo2@test","4296853837953");
        
        ArrayList<Cuenta> testCuentas = new ArrayList<>();
        //cuentas
        testCuentas.add(new Cuenta("test1","admin123",LocalDateTime.now(),testDirector));
        testCuentas.add(new Cuenta("test2","admin123",LocalDateTime.now(),testSecretaria));
        
        //test xd//
        TestLogin login = new TestLogin();
        login.IniciarSesion(testCuentas);

    }  
}