/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author USER
 */
public class MenuPrincipalVista extends JFrame {
    // PANEL PRINCIPAL

    private JPanel panelMenu;
    private JPanel panelContenido;
    private JPanel panelSuperior;
    private JPanel panelInferior;

    // BOTONES MÓDULOS
    private JButton btnAlumnos;
    private JButton btnDocentes;
    private JButton btnAcademico;
    private JButton btnMatricula;
    private JButton btnPagos;
    private JButton btnReportes;
    private JButton btnSalir;

    public MenuPrincipalVista() {

        setTitle("Sistema de Gestión Escolar");
        setSize(1200, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        initComponentes();

    }

    private void initComponentes() {

        setLayout(new BorderLayout());

        crearPanelSuperior();

        crearPanelMenu();

        crearPanelContenido();

        crearPanelInferior();

    }

    // PANEL SUPERIOR
    private void crearPanelSuperior() {

        panelSuperior = new JPanel(
                new BorderLayout()
        );

        panelSuperior.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        JLabel titulo
                = new JLabel(
                        "SISTEMA DE GESTIÓN ESCOLAR"
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel usuario
                = new JLabel(
                        "Usuario: Secretaria"
                );

        panelSuperior.add(
                titulo,
                BorderLayout.WEST
        );

        panelSuperior.add(
                usuario,
                BorderLayout.EAST
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

    }
//IMAGEN PARA BOTONES

    public void asignarImagenBoton(JButton boton, String ruta, int ancho, int alto) {
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));

            Image imagen = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagen));
            boton.setIconTextGap(10);
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + ruta);
        }
    }

    // PANEL MENÚ IZQUIERDO
    private void crearPanelMenu() {

        panelMenu = new JPanel();

        panelMenu.setLayout(new GridLayout(8, 1, 10, 15));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));//margen
        panelMenu.setPreferredSize(new Dimension(250, 0));

        //BOTONES
        btnAlumnos = new JButton("Alumnos");
        asignarImagenBoton(btnAlumnos, "/iconos/alumno.png", 40, 40);

        btnDocentes = new JButton("Docentes");
        asignarImagenBoton(btnDocentes, "/iconos/docente.png", 40, 40);

        btnAcademico = new JButton("Académico");
        asignarImagenBoton(btnAcademico, "/iconos/areaacademica.png", 40, 40);

        btnMatricula = new JButton("Matrícula");
        asignarImagenBoton(btnMatricula, "/iconos/matricula.png", 40, 40);

        btnPagos = new JButton("Pagos");
        asignarImagenBoton(btnPagos, "/iconos/pagos.png", 40, 40);

        btnReportes = new JButton("Reportes");
        asignarImagenBoton(btnReportes, "/iconos/reportes.png", 40, 40);

        btnSalir = new JButton("Salir");
        asignarImagenBoton(btnSalir, "/iconos/salir.png", 40, 40);

        panelMenu.add(btnAlumnos);
        panelMenu.add(btnDocentes);
        panelMenu.add(btnAcademico);
        panelMenu.add(btnMatricula);
        panelMenu.add(btnPagos);
        panelMenu.add(btnReportes);

        panelMenu.add(
                new JLabel("")
        );

        panelMenu.add(btnSalir);

        add(
                panelMenu,
                BorderLayout.WEST
        );

    }

    // PANEL CENTRAL
    private void crearPanelContenido() {

        panelContenido = new JPanel(new BorderLayout());

        panelContenido.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(
                                15,
                                10,
                                20,
                                20),
                        BorderFactory.createTitledBorder("Área de Trabajo")
                )
        );

        JLabel bienvenida = new JLabel("Bienvenido al Sistema", SwingConstants.CENTER);

        bienvenida.setFont(new Font("Arial", Font.BOLD, 28));

        panelContenido.add(bienvenida, BorderLayout.CENTER);

        add(panelContenido, BorderLayout.CENTER);

    }

    // PANEL INFERIOR
    private void crearPanelInferior() {

        panelInferior
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panelInferior.add(
                new JLabel(
                        "Año escolar activo"
                )
        );

        add(
                panelInferior,
                BorderLayout.SOUTH
        );

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                    MenuPrincipalVista menu
                    = new MenuPrincipalVista();

                    menu.setVisible(true);

                }
        );

    }

}
