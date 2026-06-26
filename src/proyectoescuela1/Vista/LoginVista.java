/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import proyectoescuela1.Controlador.loginControlador;
import proyectoescuela1.Modelo.Cuenta;
import proyectoescuela1.Controlador.*;

/**
 *
 * @author ALEX
 */
public class LoginVista extends JFrame{
    private loginControlador controlador;
     
    private JTextField txtUsuario = new JTextField(20);
    private JPasswordField txtContrasena = new JPasswordField(20);
    private JButton btnIngresar = new JButton("Ingresar");
    
    
    
    
    private void initComponentes() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);
        panel.add(new JLabel(""));
        panel.add(btnIngresar);

        add(panel);
    }
    
    private void initEventos(){
        btnIngresar.addActionListener(e -> {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtContrasena.getPassword());
        String rol = controlador.validarLogin(usuario, clave);
        
        if (rol == null) {
                JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        dispose();
        
        if (rol.equals("DIRECTOR")) {
                new DirectorVista().setVisible(true);
            } //else if (rol.equals("SECRETARIA")){
            //JOptionPane.showMessageDialog(null, "Bienvenido, Secretaria"); // falta que sebas agregue xd
            //} else {
            //    JOptionPane.showMessageDialog(null, "Bienvenido: " + rol);
           // }
        
        
        });  
    }
    
    public LoginVista(List<Cuenta> cuentas) {
        this.controlador = new loginControlador(cuentas);

        setTitle("Inicio de Sesión");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponentes();
        initEventos();
    }
    
    
    
    
    
}
