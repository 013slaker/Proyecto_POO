package proyectoescuela1;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import proyectoescuela1.Modelo.*;
import javax.swing.JOptionPane;
import proyectoescuela1.Controlador.*;
import proyectoescuela1.Datos.DatosPrueba;
import proyectoescuela1.Vista.*;

public class ProyectoEscuela1 {

    public static void main(String[] args) {
        // ── CARGA DATOS DE PRUEBA ─────────────────
        // Comenta esta línea cuando entregues
        new DatosPrueba().cargarTodo();

        // ── CUENTAS ───────────────────────────────
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta(
            1, "director1", "admin123", "DIRECTOR"
        ));
        cuentas.add(new Cuenta(
            2, "secretaria1", "admin123", "SECRETARIA"
        ));
        cuentas.add(new Cuenta(
            3, "tesorero1", "admin123", "TESORERO"
        ));
        cuentas.add(new Cuenta(
            4, "docente1", "admin123", "DOCENTE"
        ));
        cuentas.add(new Cuenta(
            5, "auxiliar1", "admin123", "AUXILIAR"
        ));
         cuentas.add(new Cuenta(
            6, "", "", "PRUEBAS"
        ));

        // ── INICIA EL SISTEMA ─────────────────────
        new LoginVista(cuentas).setVisible(true);
    }  
}