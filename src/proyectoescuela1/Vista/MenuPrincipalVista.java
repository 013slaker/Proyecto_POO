/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
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
        Date fecha = new Date();
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
                = new JLabel("SISTEMA DE GESTIÓN ESCOLAR");

        titulo.setFont(new Font(
                "Arial", Font.BOLD, 24));

        JLabel usuario
                = new JLabel(
                        "Usuario: " + cuenta.getUsuario()
                        + " | Rol: " + cuenta.getRol()
                );
        //para formato de fecha
        Date fecha = new Date();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String fechaFormateada = formato.format(fecha);

        JLabel fechaIngreso = new JLabel("Ingreso: " + fechaFormateada);

        panelSuperior.add(titulo, BorderLayout.WEST);

        panelSuperior.add(usuario, BorderLayout.EAST);
        panelSuperior.add(fechaIngreso, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

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

        JButton btnApoderado = new JButton("Gestión Apoderado");
        asignarImagenBoton(btnApoderado, "/proyectoescuela1/iconos/apoderado.png", 40, 40);
        btnApoderado.addActionListener(e -> {
            System.out.println("CLICK apoderados");
            mostrarPanel(new ApoderadoVista());
        });

        JButton btnDocentes = new JButton("Gestión Docentes");
        asignarImagenBoton(btnDocentes, "/proyectoescuela1/iconos/docente.png", 40, 40);
        btnDocentes.addActionListener(e -> {
            mostrarPanel(new DocenteVista());
        });

        JButton btnCursos = new JButton("Cursos");
        asignarImagenBoton(btnCursos, "/proyectoescuela1/iconos/cursos.png", 40, 40);
        btnCursos.addActionListener(e -> {
            mostrarPanel(new CursoVista());
        });

        JButton btnHorarios = new JButton("Horarios");
        asignarImagenBoton(btnHorarios, "/proyectoescuela1/iconos/horario.png", 40, 40);
        btnHorarios.addActionListener(e -> {
            mostrarPanel(new HorarioVista());
        });
        JButton btnAsistencia = new JButton("Asistencia");
        asignarImagenBoton(btnAsistencia, "/proyectoescuela1/iconos/asistencia.png", 40, 40);
        btnAsistencia.addActionListener(e -> {
           mostrarPanel(new AsistenciaVista()); 
        });
        
        JButton btnNotas = new JButton("Notas");
        asignarImagenBoton(btnNotas, "/proyectoescuela1/iconos/notas.png", 40, 40);
        btnNotas.addActionListener(e -> {
            mostrarPanel(new NotaVista());
        });
        // En crearSubMenuAcademico()
JButton btnAsignaciones =
    new JButton("Asignación Cursos");
btnAsignaciones.addActionListener(e -> {
    mostrarPanel(new AsignacionCursoVista());
});
panel.add(btnAsignaciones);

        JButton btnLibretas = new JButton("Libretas de Notas");

        panel.add(btnAlumnos);
        panel.add(btnApoderado);
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
        btnApoderado.addActionListener(e -> {
            mostrarPanel(new ApoderadoVista());
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
