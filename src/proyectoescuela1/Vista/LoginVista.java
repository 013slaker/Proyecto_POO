package proyectoescuela1.Vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import proyectoescuela1.Controlador.*;
import proyectoescuela1.Modelo.Cuenta;

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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // construir UI y eventos
        initComponentes();
        initEventos();
    }

    //para añadir imagen y darle formato
  private JLabel crearLogo() {
    
    // Ruta absoluta desde src/ (con / al inicio)
    java.net.URL url = getClass().getResource("/proyectoescuela1/iconos/insignia.png");
    
    // Si no la encuentra, prueba relativa al paquete (sin /)
    if (url == null) {
        url = getClass().getResource("iconos/insignia.png");
    }
    
    JLabel lblLogo = new JLabel();
    lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
    
    if (url != null) {
        ImageIcon icono = new ImageIcon(url);
        Image imagen = icono.getImage().getScaledInstance(220, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(imagen));
    } else {
        // Fallback: texto o imagen por defecto
        lblLogo.setText(" ESCUELA");
        lblLogo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        lblLogo.setForeground(java.awt.Color.DARK_GRAY);
        System.err.println("ADVERTENCIA: No se encontró insignia.png. Verifica la ruta.");
    }
    
    return lblLogo;
}

    // -----------------------------
    // INTERFAZ GRÁFICA
    // -----------------------------
    private void initComponentes() {
        setLayout(new BorderLayout());

        // ---------- panel del LOGO ----------
        JPanel panelLogo = new JPanel();

        panelLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelLogo.add(crearLogo());

        // ---------- panel del  FORMULARIO ----------
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelForm.add(new JLabel("Usuario:"));
        panelForm.add(txtUsuario);
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtContrasena);

        // ---------- BOTÓN CENTRADO ----------
        JPanel panelBoton = new JPanel();
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBoton.add(btnIngresar);

        // PANEL QUE UNE FORMULARIO + BOTÓN
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.add(panelForm, BorderLayout.CENTER);
        panelContenido.add(panelBoton, BorderLayout.SOUTH);

        add(panelLogo, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

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
                txtContrasena.setText("");
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
