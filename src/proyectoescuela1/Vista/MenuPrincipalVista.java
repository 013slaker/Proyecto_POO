/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import proyectoescuela1.Modelo.Cuenta;

public class MenuPrincipalVista extends JFrame {

    private JPanel panelMenu;
    private JPanel panelContenido;
    private JPanel panelSuperior;
    private JPanel panelInferior;

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
        configurarPermisos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());
        crearPanelSuperior();
        crearPanelMenu();
        crearPanelContenido();
        crearPanelInferior();
    }

    // ── PANEL SUPERIOR ──────────────────────────────
    private void crearPanelSuperior() {
        panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN ESCOLAR");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usuario = new JLabel("Usuario: " + cuenta.getUsuario() + " | Rol: " + cuenta.getRol());
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaFormateada = formato.format(fecha);
        JLabel fechaIngreso = new JLabel("Ingreso: " + fechaFormateada);

        panelSuperior.add(titulo, BorderLayout.WEST);
        panelSuperior.add(usuario, BorderLayout.EAST);
        panelSuperior.add(fechaIngreso, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
    }

    // ── PANEL MENÚ ──────────────────────────────────
    private void crearPanelMenu() {
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(8, 1, 10, 15));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelMenu.setPreferredSize(new Dimension(250, 0));

        btnAcademico = new JButton("Académico");
        asignarImagenBoton(btnAcademico, "/proyectoescuela1/iconos/areaacademica.png", 40, 40);
        btnAcademico.addActionListener(e -> {
            System.out.println("CLICK MODULO ACADEMICO");
            mostrarPanel(crearSubMenuAcademico());
        });

        btnAdministrativo = new JButton("Administrativo");
        asignarImagenBoton(btnAdministrativo, "/proyectoescuela1/iconos/administrativo.png", 40, 40);
        btnAdministrativo.addActionListener(e -> {
            System.out.println("CLICK MODULO ADMINISTRATIVO");
            mostrarPanel(crearSubMenuAdministrativo());
        });

        btnFinanciero = new JButton("Financiero");
        asignarImagenBoton(btnFinanciero, "/proyectoescuela1/iconos/financiero.png", 40, 40);
        btnFinanciero.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnReportes = new JButton("Reportes");
        asignarImagenBoton(btnReportes, "/proyectoescuela1/iconos/reportes.png", 40, 40);
        btnReportes.addActionListener(e -> {
            System.out.println("CLICK MODULO REPORTES");
            mostrarPanel(crearSubMenuReportes());
        });

        btnSalir = new JButton("Salir");
        asignarImagenBoton(btnSalir, "/proyectoescuela1/iconos/salir.png", 40, 40);
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas salir?", "Salir", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panelMenu.add(btnAcademico);
        panelMenu.add(btnAdministrativo);
        panelMenu.add(btnFinanciero);
        panelMenu.add(btnReportes);
        panelMenu.add(btnSalir);
        panelMenu.add(new JLabel(""));

        add(panelMenu, BorderLayout.WEST);
    }

    // ── CONFIGURACIÓN DE PERMISOS (DESHABILITAR BOTONES) ──
    private void configurarPermisos() {
        String rol = cuenta.getRol();
        // Por defecto todos habilitados, deshabilitamos según rol

        // Deshabilitar y poner tooltip para cada caso
        switch (rol) {
            case "DIRECTOR":
                // Todo habilitado
                break;
            case "SECRETARIA":
                btnFinanciero.setEnabled(false);
                btnFinanciero.setToolTipText("Acceso restringido para Secretaria");
                break;
            case "DOCENTE":
                btnAdministrativo.setEnabled(false);
                btnAdministrativo.setToolTipText("Acceso restringido para Docente");
                btnFinanciero.setEnabled(false);
                btnFinanciero.setToolTipText("Acceso restringido para Docente");
                break;
            case "TESORERO":
                btnAcademico.setEnabled(false);
                btnAcademico.setToolTipText("Acceso restringido para Tesorero");
                btnAdministrativo.setEnabled(false);
                btnAdministrativo.setToolTipText("Acceso restringido para Tesorero");
                break;
            case "AUXILIAR":
                btnAcademico.setEnabled(false);
                btnAcademico.setToolTipText("Acceso restringido para Auxiliar");
                btnAdministrativo.setEnabled(false);
                btnAdministrativo.setToolTipText("Acceso restringido para Auxiliar");
                btnFinanciero.setEnabled(false);
                btnFinanciero.setToolTipText("Acceso restringido para Auxiliar");
                btnReportes.setEnabled(false);
                btnReportes.setToolTipText("Acceso restringido para Auxiliar");
                break;
            default:
                btnAcademico.setEnabled(false);
                btnAdministrativo.setEnabled(false);
                btnFinanciero.setEnabled(false);
                btnReportes.setEnabled(false);
                break;
        }
    }

    // ── PANEL CENTRAL ──────────────────────────────
    private void crearPanelContenido() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 10, 20, 20),
                BorderFactory.createTitledBorder("Área de Trabajo")
        ));

        JLabel bienvenida = new JLabel("Bienvenido al Sistema", SwingConstants.CENTER);
        bienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        panelContenido.add(bienvenida, BorderLayout.CENTER);

        add(panelContenido, BorderLayout.CENTER);
    }

    // ── PANEL INFERIOR ─────────────────────────────
    private void crearPanelInferior() {
        panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.add(new JLabel(obtenerTextoPeriodoActivo()));
        add(panelInferior, BorderLayout.SOUTH);
    }

    private String obtenerTextoPeriodoActivo() {
        proyectoescuela1.Controlador.PeriodoControlador periodoCtrl = new proyectoescuela1.Controlador.PeriodoControlador();
        proyectoescuela1.Modelo.AnioEscolar anio = periodoCtrl.getAnioEscolar();
        proyectoescuela1.Modelo.Bimestre activo = periodoCtrl.getBimestreActivo();

        if (anio == null) {
            return "Año escolar: sin configurar (ir a Administrativo → Período Académico)";
        }
        if (activo == null) {
            return "Año escolar " + anio.getAnio() + " — sin bimestre activo";
        }
        return "Año escolar " + anio.getAnio() + " — Bimestre " + activo.getNumero() + " activo";
    }

    // ── MÉTODOS AUXILIARES ──────────────────────────
    private void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public void asignarImagenBoton(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
                Image imagen = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                boton.setIconTextGap(10);
            } else {
                System.out.println("Imagen no encontrada: " + ruta);
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + ruta);
        }
    }

    // ── SUBMENÚ ACADÉMICO (con filtro por rol) ─────
    private JPanel crearSubMenuAcademico() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String rol = cuenta.getRol();

        JButton btnAlumnos = new JButton("Gestión Alumnos");
        asignarImagenBoton(btnAlumnos, "/proyectoescuela1/iconos/alumno.png", 40, 40);
        btnAlumnos.setVerticalTextPosition(JButton.BOTTOM);
        btnAlumnos.setHorizontalTextPosition(JButton.CENTER);
        btnAlumnos.addActionListener(e -> mostrarPanel(new AlumnoVista()));

        JButton btnApoderado = new JButton("Gestión Apoderado");
        asignarImagenBoton(btnApoderado, "/proyectoescuela1/iconos/apoderado.png", 40, 40);
        btnApoderado.setVerticalTextPosition(JButton.BOTTOM);
        btnApoderado.setHorizontalTextPosition(JButton.CENTER);
        btnApoderado.addActionListener(e -> mostrarPanel(new ApoderadoVista()));

        JButton btnDocentes = new JButton("Gestión Docentes");
        asignarImagenBoton(btnDocentes, "/proyectoescuela1/iconos/docente.png", 40, 40);
        btnDocentes.setVerticalTextPosition(JButton.BOTTOM);
        btnDocentes.setHorizontalTextPosition(JButton.CENTER);
        btnDocentes.addActionListener(e -> mostrarPanel(new DocenteVista()));

        JButton btnCursos = new JButton("Cursos");
        asignarImagenBoton(btnCursos, "/proyectoescuela1/iconos/cursos.png", 40, 40);
        btnCursos.setVerticalTextPosition(JButton.BOTTOM);
        btnCursos.setHorizontalTextPosition(JButton.CENTER);
        btnCursos.addActionListener(e -> mostrarPanel(new CursoVista()));

        JButton btnHorarios = new JButton("Horarios");
        asignarImagenBoton(btnHorarios, "/proyectoescuela1/iconos/horario.png", 40, 40);
        btnHorarios.setVerticalTextPosition(JButton.BOTTOM);
        btnHorarios.setHorizontalTextPosition(JButton.CENTER);
        btnHorarios.addActionListener(e -> mostrarPanel(new HorarioVista()));

        JButton btnAsistencia = new JButton("Asistencia");
        asignarImagenBoton(btnAsistencia, "/proyectoescuela1/iconos/asistencia.png", 40, 40);
        btnAsistencia.setVerticalTextPosition(JButton.BOTTOM);
        btnAsistencia.setHorizontalTextPosition(JButton.CENTER);
        btnAsistencia.addActionListener(e -> mostrarPanel(new AsistenciaVista()));

        JButton btnNotas = new JButton("Notas");
        asignarImagenBoton(btnNotas, "/proyectoescuela1/iconos/notas.png", 40, 40);
        btnNotas.setVerticalTextPosition(JButton.BOTTOM);
        btnNotas.setHorizontalTextPosition(JButton.CENTER);
        btnNotas.addActionListener(e -> mostrarPanel(new NotaVista()));

        JButton btnAsignaciones = new JButton("Asignación Cursos");
        asignarImagenBoton(btnAsignaciones, "/proyectoescuela1/iconos/asignacioncursos.png", 40, 40);
        btnAsignaciones.setVerticalTextPosition(JButton.BOTTOM);
        btnAsignaciones.setHorizontalTextPosition(JButton.CENTER);
        btnAsignaciones.addActionListener(e -> mostrarPanel(new AsignacionCursoVista()));

        // Deshabilitar según rol y poner tooltip
        if (rol.equals("DOCENTE")) {
            btnAlumnos.setEnabled(false);
            btnAlumnos.setToolTipText("Acceso restringido para Docente");
            btnApoderado.setEnabled(false);
            btnApoderado.setToolTipText("Acceso restringido para Docente");
            btnDocentes.setEnabled(false);
            btnDocentes.setToolTipText("Acceso restringido para Docente");
            btnCursos.setEnabled(false);
            btnCursos.setToolTipText("Acceso restringido para Docente");
            btnAsignaciones.setEnabled(false);
            btnAsignaciones.setToolTipText("Acceso restringido para Docente");
            // Horarios, Asistencia, Notas habilitados
        } else if (rol.equals("SECRETARIA")) {
            btnDocentes.setEnabled(false);
            btnDocentes.setToolTipText("Acceso restringido para Secretaria");
            btnAsistencia.setEnabled(false);
            btnAsistencia.setToolTipText("Acceso restringido para Secretaria");
            btnNotas.setEnabled(false);
            btnNotas.setToolTipText("Acceso restringido para Secretaria");
            // Alumnos, Apoderados, Cursos, Horarios, Asignaciones habilitados
        } else if (rol.equals("AUXILIAR")) {
            btnAlumnos.setEnabled(false);
            btnApoderado.setEnabled(false);
            btnDocentes.setEnabled(false);
            btnCursos.setEnabled(false);
            btnHorarios.setEnabled(false);
            btnNotas.setEnabled(false);
            btnAsignaciones.setEnabled(false);
            // solo Asistencia habilitado
            btnAsistencia.setEnabled(true);
            btnAsistencia.setToolTipText(null);
        }
        // DIRECTOR: todo habilitado

        panel.add(btnAsignaciones);
        panel.add(btnAlumnos);
        panel.add(btnApoderado);
        panel.add(btnDocentes);
        panel.add(btnCursos);
        panel.add(btnHorarios);
        panel.add(btnAsistencia);
        panel.add(btnNotas);

        return panel;
    }

    // ── SUBMENÚ REPORTES (filtrado) ────────────────
    private JPanel crearSubMenuReportes() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String rol = cuenta.getRol();

        JButton btnLibretas = new JButton("Libretas de Notas");
        asignarImagenBoton(btnLibretas, "/proyectoescuela1/iconos/asignacioncursos.png", 40, 40);
        btnLibretas.setVerticalTextPosition(JButton.BOTTOM);
        btnLibretas.setHorizontalTextPosition(JButton.CENTER);
        btnLibretas.addActionListener(e -> mostrarPanel(new LibretaNotasVista()));

        // Deshabilitar si no tiene permiso
        if (!rol.equals("DIRECTOR") && !rol.equals("SECRETARIA") && !rol.equals("DOCENTE")) {
            btnLibretas.setEnabled(false);
            btnLibretas.setToolTipText("Acceso restringido para " + rol);
        }

        panel.add(btnLibretas);
        return panel;
    }

    // ── SUBMENÚ ADMINISTRATIVO (filtrado) ──────────
    private JPanel crearSubMenuAdministrativo() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String rol = cuenta.getRol();

        JButton btnPeriodo = new JButton("Período Académico");
        asignarImagenBoton(btnPeriodo, "/proyectoescuela1/iconos/administrativo.png", 40, 40);
        btnPeriodo.setVerticalTextPosition(JButton.BOTTOM);
        btnPeriodo.setHorizontalTextPosition(JButton.CENTER);
        btnPeriodo.addActionListener(e -> mostrarPanel(new PeriodoVista()));

        // Solo DIRECTOR y SECRETARIA pueden gestionar período
        if (!rol.equals("DIRECTOR") && !rol.equals("SECRETARIA")) {
            btnPeriodo.setEnabled(false);
            btnPeriodo.setToolTipText("Acceso restringido para " + rol);
        }

        panel.add(btnPeriodo);
        return panel;
    }
}