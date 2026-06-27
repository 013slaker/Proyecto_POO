package proyectoescuela1.Vista;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import proyectoescuela1.Controlador.LoginControlador;
import proyectoescuela1.Modelo.Cuenta;
import proyectoescuela1.Vista.MenuPrincipalVista;

public class LoginVista extends JFrame {

    // CONTROLADOR DEL LOGIN
    private LoginControlador controlador;

    // CAMPOS DE TEXTO
    private JTextField txtUsuario = new JTextField(20);
    private JPasswordField txtContrasena = new JPasswordField(20);

    // BOTÓN
    private JButton btnIngresar = new JButton("Ingresar");

    // CONSTRUCTOR
    public LoginVista(List<Cuenta> cuentas) {

        // inicializa controlador con lista de cuentas
        this.controlador = new LoginControlador(cuentas);

        // configuración de ventana
        setTitle("Inicio de Sesión");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // construir UI y eventos
        initComponentes();
        initEventos();
    }

    // -----------------------------
    // INTERFAZ GRÁFICA
    // -----------------------------
    private void initComponentes() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // etiquetas y campos
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);

        panel.add(new JLabel("")); // espacio vacío
        panel.add(btnIngresar);

        add(panel);
    }

    // -----------------------------
    // EVENTOS DEL BOTÓN
    // -----------------------------
    private void initEventos() {

        btnIngresar.addActionListener(e -> {

            // obtener datos ingresados
            String usuario = txtUsuario.getText().trim();
            String clave = new String(txtContrasena.getPassword());

            // validar login (DEVUELVE CUENTA)
            Cuenta cuenta = controlador.validarLogin(usuario, clave);

            // si es incorrecto
            if (cuenta == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // cerrar login
            dispose();

            // abrir menú principal pasando la cuenta
            MenuPrincipalVista menu = new MenuPrincipalVista(cuenta);
            menu.setVisible(true);
        });
    }
}