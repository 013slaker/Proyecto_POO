package proyectoescuela1;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import proyectoescuela1.Modelo.*;
import javax.swing.JOptionPane;
import proyectoescuela1.Controlador.*;
import proyectoescuela1.Vista.*;

public class ProyectoEscuela1 {

    public static void main(String[] args) {
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta(1, "director1", "admin123", "DIRECTOR"));
        cuentas.add(new Cuenta(2, "secretaria1", "secre123", "SECRETARIA"));
        cuentas.add(new Cuenta(3, "tesoreria1", "teso123", "TESORERO"));
        
        new LoginVista(cuentas).setVisible(true);
        
        
        
     

    }  
}