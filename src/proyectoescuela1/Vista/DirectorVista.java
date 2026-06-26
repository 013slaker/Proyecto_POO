/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;
import javax.swing.*;
import java.awt.*;

public class DirectorVista extends JFrame {

    private JButton btnGestionar = new JButton("Gestionar Sistema");
    private JLabel lblBienvenida = new JLabel("Bienvenido, Director", SwingConstants.CENTER);

    public DirectorVista() {
        setTitle("Panel del Director");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponentes();
        initEventos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Bienvenida arriba
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // Botones en el centro
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        panelBotones.add(btnGestionar);
        add(panelBotones, BorderLayout.CENTER);
    }

    private void initEventos() {
        btnGestionar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Sistema gestionado correctamente.",
                "Gestión", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
