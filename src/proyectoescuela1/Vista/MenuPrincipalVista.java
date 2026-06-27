/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import proyectoescuela1.Modelo.Cuenta;

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
    private JButton btnAcademico;
    private JButton btnAdministrativo;
    private JButton btnFinanciero;
    private JButton btnReportes;
    private JButton btnSalir;
   private Cuenta cuenta;
   
    public MenuPrincipalVista(Cuenta cuenta) {
        this.cuenta = cuenta;

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
        btnAcademico = new JButton("Académico");
        asignarImagenBoton(btnAcademico, "/proyectoescuela1/iconos/areaacademica.png", 40, 40);
        btnAcademico.addActionListener(e -> {
            System.out.println("CLICK MODULO ACADEMICO");
            mostrarPanel(crearSubMenuAcademico());
        });

        btnAdministrativo = new JButton("Administrativo");
        asignarImagenBoton(btnAdministrativo, "/proyectoescuela1/iconos/administrativo.png", 40, 40);

        btnFinanciero = new JButton("Financiero");
        asignarImagenBoton(btnFinanciero, "/proyectoescuela1/iconos/financiero.png", 40, 40);

        btnReportes = new JButton("Reportes");
        asignarImagenBoton(btnReportes, "/proyectoescuela1/iconos/reportes.png", 40, 40);

        btnSalir = new JButton("Salir");
        asignarImagenBoton(btnSalir, "/proyectoescuela1/iconos/salir.png", 40, 40);

        panelMenu.add(btnAcademico);
        panelMenu.add(btnAdministrativo);
        panelMenu.add(btnFinanciero);
        panelMenu.add(btnReportes);
        panelMenu.add(btnSalir);

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

        panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelInferior.add(new JLabel("Año escolar activo"));

        add(panelInferior, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta(1, "director1", "admin123", "DIRECTOR"));
        cuentas.add(new Cuenta(2, "secretaria1", "admin123", "SECRETARIA"));

        new LoginVista(cuentas).setVisible(true);
    });
}
    //metodos para llamar los paneles con los botones
    private void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    //MOdulo academico
    private JPanel crearSubMenuAcademico() {

        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton btnAlumnos = new JButton("Gestión Alumnos");
        asignarImagenBoton(btnAlumnos, "/proyectoescuela1/iconos/alumno.png", 40, 40);
        btnAlumnos.addActionListener(e -> {
            System.out.println("CLICK ALUMNOS");
            mostrarPanel(new AlumnoVista());
        });

        JButton btnDocentes = new JButton("Gestión Docentes");
        asignarImagenBoton(btnDocentes, "/proyectoescuela1/iconos/docente.png", 40, 40);
        btnDocentes.addActionListener(e -> {
            mostrarPanel(new DocenteVista());
        });

        JButton btnCursos = new JButton("Cursos");
        asignarImagenBoton(btnCursos, "/proyectoescuela1/iconos/cursos.png", 40, 40);

        JButton btnHorarios = new JButton("Horarios");
        asignarImagenBoton(btnHorarios, "/proyectoescuela1/iconos/horario.png", 40, 40);

        JButton btnAsistencia = new JButton("Asistencia");
        asignarImagenBoton(btnAsistencia, "/proyectoescuela1/iconos/asistencia.png", 40, 40);

        JButton btnNotas = new JButton("Notas");
        asignarImagenBoton(btnNotas, "/proyectoescuela1/iconos/notas.png", 40, 40);

        JButton btnLibretas = new JButton("Libretas de Notas");

        panel.add(btnAlumnos);
        panel.add(btnDocentes);
        panel.add(btnCursos);
        panel.add(btnHorarios);
        panel.add(btnAsistencia);
        panel.add(btnNotas);
        panel.add(btnLibretas);

        // EVENTOS
        btnAlumnos.addActionListener(e -> {
            mostrarPanel(new AlumnoVista());
        });

        btnDocentes.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Docentes"));
        });

        btnCursos.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Cursos"));
        });

        btnHorarios.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Horarios"));
        });

        btnAsistencia.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Asistencia"));
        });

        btnNotas.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Notas"));
        });

        btnLibretas.addActionListener(e -> {
            mostrarPanel(crearPanelTemporal("Libretas de Notas"));
        });

        return panel;
    }

    //OJO TEMPORAL se borrara cuando teminemos todos las clases que falta
    private JPanel crearPanelTemporal(String nombre) {

        JPanel p = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel(
                "Módulo: " + nombre + " (en desarrollo)",
                SwingConstants.CENTER
        );

        lbl.setFont(new Font("Arial", Font.BOLD, 24));

        p.add(lbl, BorderLayout.CENTER);

        return p;
    }//HASTA AQUI SE DEBE QUITAR AL FINAL

}
