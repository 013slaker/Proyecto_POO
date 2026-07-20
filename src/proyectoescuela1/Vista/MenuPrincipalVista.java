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
        btnAdministrativo.addActionListener(e -> {
            System.out.println("CLICK MODULO ADMINISTRATIVO");
            mostrarPanel(crearSubMenuAdministrativo());
        });

        btnFinanciero = new JButton("Financiero");
        asignarImagenBoton(btnFinanciero, "/proyectoescuela1/iconos/financiero.png", 40, 40);
        btnFinanciero.addActionListener(e -> {
            mostrarPanel(crearSubMenuFinanciero());
        });

        btnReportes = new JButton("Reportes");
        asignarImagenBoton(btnReportes, "/proyectoescuela1/iconos/reportes.png", 40, 40);
        btnReportes.addActionListener(e -> {
            System.out.println("CLICK MODULO REPORTES");
            mostrarPanel(crearSubMenuReportes());
        });

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

        panelInferior.add(new JLabel(obtenerTextoPeriodoActivo()));

        add(panelInferior, BorderLayout.SOUTH);

    }

    /**
     * Consulta el bimestre activo real (Administrativo → Período
     * Académico) para mostrarlo en la barra inferior.
     */
    private String obtenerTextoPeriodoActivo() {
        proyectoescuela1.Controlador.PeriodoControlador periodoCtrl =
                new proyectoescuela1.Controlador.PeriodoControlador();
        proyectoescuela1.Modelo.AnioEscolar anio = periodoCtrl.getAnioEscolar();
        proyectoescuela1.Modelo.Bimestre activo = periodoCtrl.getBimestreActivo();

        if (anio == null) {
            return "Año escolar: sin configurar (ir a Administrativo → Período Académico)";
        }
        if (activo == null) {
            return "Año escolar " + anio.getAnio() + " — sin bimestre activo";
        }
        return "Año escolar " + anio.getAnio() +
                " — Bimestre " + activo.getNumero() + " activo";
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
        btnAlumnos.setVerticalTextPosition(JButton.BOTTOM);
        btnAlumnos.setHorizontalTextPosition(JButton.CENTER);
        btnAlumnos.addActionListener(e -> {
            System.out.println("CLICK ALUMNOS");
            mostrarPanel(new AlumnoVista());
        });

        JButton btnApoderado = new JButton("Gestión Apoderado");
        asignarImagenBoton(btnApoderado, "/proyectoescuela1/iconos/apoderado.png", 40, 40);
        btnApoderado.setVerticalTextPosition(JButton.BOTTOM);
        btnApoderado.setHorizontalTextPosition(JButton.CENTER);
        btnApoderado.addActionListener(e -> {
            System.out.println("CLICK apoderados");
            mostrarPanel(new ApoderadoVista());
        });

        JButton btnDocentes = new JButton("Gestión Docentes");
        asignarImagenBoton(btnDocentes, "/proyectoescuela1/iconos/docente.png", 40, 40);
        btnDocentes.setVerticalTextPosition(JButton.BOTTOM);
        btnDocentes.setHorizontalTextPosition(JButton.CENTER);
        btnDocentes.addActionListener(e -> {
            mostrarPanel(new DocenteVista());
        });

        JButton btnCursos = new JButton("Cursos");
        asignarImagenBoton(btnCursos, "/proyectoescuela1/iconos/cursos.png", 40, 40);
        btnCursos.setVerticalTextPosition(JButton.BOTTOM);
        btnCursos.setHorizontalTextPosition(JButton.CENTER);
        btnCursos.addActionListener(e -> {
            mostrarPanel(new CursoVista());
        });

        JButton btnHorarios = new JButton("Horarios");
        asignarImagenBoton(btnHorarios, "/proyectoescuela1/iconos/horario.png", 40, 40);
        btnHorarios.setVerticalTextPosition(JButton.BOTTOM);
        btnHorarios.setHorizontalTextPosition(JButton.CENTER);
        btnHorarios.addActionListener(e -> {
            mostrarPanel(new HorarioVista());
        });
        JButton btnAsistencia = new JButton("Asistencia");
        asignarImagenBoton(btnAsistencia, "/proyectoescuela1/iconos/asistencia.png", 40, 40);
        btnAsistencia.setVerticalTextPosition(JButton.BOTTOM);
        btnAsistencia.setHorizontalTextPosition(JButton.CENTER);
        btnAsistencia.addActionListener(e -> {
            mostrarPanel(new AsistenciaVista());
        });
        JButton btnNotas = new JButton("Notas");
        asignarImagenBoton(btnNotas, "/proyectoescuela1/iconos/notas.png", 40, 40);
        btnNotas.setVerticalTextPosition(JButton.BOTTOM);
        btnNotas.setHorizontalTextPosition(JButton.CENTER);
        btnNotas.addActionListener(e -> {
            mostrarPanel(new NotaVista());
        });
        // asignacion de cursos al docente
        JButton btnAsignaciones = new JButton("Asignación Cursos");
        asignarImagenBoton(btnAsignaciones, "/proyectoescuela1/iconos/asignacioncursos.png", 40, 40);
        btnAsignaciones.setVerticalTextPosition(JButton.BOTTOM);
        btnAsignaciones.setHorizontalTextPosition(JButton.CENTER);
        btnAsignaciones.addActionListener(e -> {
            mostrarPanel(new AsignacionCursoVista());
        });

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

    //Modulo de reportes
    private JPanel crearSubMenuReportes() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JButton btnLibretas = new JButton("Libretas de Notas");
        panel.add(btnLibretas);

        asignarImagenBoton(btnLibretas, "/proyectoescuela1/iconos/asignacioncursos.png", 40, 40);
        btnLibretas.setVerticalTextPosition(JButton.BOTTOM);
        btnLibretas.setHorizontalTextPosition(JButton.CENTER);
        btnLibretas.addActionListener(e -> {
            mostrarPanel(new LibretaNotasVista());
        });
        return panel;

    }

    // Modulo administrativo
    private JPanel crearSubMenuAdministrativo() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // -- Periodo Academico (crear año, bimestres, activar/cerrar) --
        JButton btnPeriodo = new JButton("Periodo Academico");
        asignarImagenBoton(btnPeriodo, "/proyectoescuela1/iconos/periodo.png", 40, 40);
        btnPeriodo.setVerticalTextPosition(JButton.BOTTOM);
        btnPeriodo.setHorizontalTextPosition(JButton.CENTER);
        btnPeriodo.addActionListener(e -> {
            mostrarPanel(new PeriodoVista());
        });

        // -- Matrículas (registrar, buscar, historial, reasignar) --
        JButton btnMatriculas = new JButton("Matrículas");
        asignarImagenBoton(btnMatriculas, "/proyectoescuela1/iconos/matricula.png", 40, 40);
        btnMatriculas.setVerticalTextPosition(JButton.BOTTOM);
        btnMatriculas.setHorizontalTextPosition(JButton.CENTER);
        btnMatriculas.addActionListener(e -> {
            mostrarPanel(new MatriculaVista(
                    () -> mostrarPanel(crearSubMenuAdministrativo())));
        });

        // -- Cambio de Sección (trasladar alumnos entre secciones) --
        JButton btnCambioSeccion = new JButton("Cambio de Sección");
        asignarImagenBoton(btnCambioSeccion, "/proyectoescuela1/iconos/horario.png", 40, 40);
        btnCambioSeccion.setVerticalTextPosition(JButton.BOTTOM);
        btnCambioSeccion.setHorizontalTextPosition(JButton.CENTER);
        btnCambioSeccion.addActionListener(e -> {
            mostrarPanel(new CambioSeccionVista(
                    () -> mostrarPanel(crearSubMenuAdministrativo())));
        });

        // -- Retiro de Alumno (cambiar estado, registrar motivo) --
        JButton btnRetiro = new JButton("Retiro de Alumno");
        asignarImagenBoton(btnRetiro, "/proyectoescuela1/iconos/alumno.png", 40, 40);
        btnRetiro.setVerticalTextPosition(JButton.BOTTOM);
        btnRetiro.setHorizontalTextPosition(JButton.CENTER);
        btnRetiro.addActionListener(e -> {
            mostrarPanel(new RetiroAlumnoVista());
        });

        panel.add(btnPeriodo);
        panel.add(btnMatriculas);
        panel.add(btnCambioSeccion);
        panel.add(btnRetiro);

        return panel;
    }

    // Modulo financiero
    private JPanel crearSubMenuFinanciero() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // -- Pensiones (configurar tarifa por nivel/grado) --
        JButton btnPensiones = new JButton("Pensiones");
        asignarImagenBoton(btnPensiones, "/proyectoescuela1/iconos/pension.png", 40, 40);
        btnPensiones.setVerticalTextPosition(JButton.BOTTOM);
        btnPensiones.setHorizontalTextPosition(JButton.CENTER);
        btnPensiones.addActionListener(e -> {
            mostrarPanel(new TarifaPensionVista());
        });

        // -- Pagos (registrar pago, ver deudas del alumno) --
        JButton btnPagos = new JButton("Pagos");
        asignarImagenBoton(btnPagos, "/proyectoescuela1/iconos/pagos.png", 50, 50);
        btnPagos.setVerticalTextPosition(JButton.BOTTOM);
        btnPagos.setHorizontalTextPosition(JButton.CENTER);
        btnPagos.addActionListener(e -> {
            mostrarPanel(new PagoVista());
        });

        // -- Deudas (cobranza general, se generan automáticamente) --
        JButton btnDeudas = new JButton("Deudas");
        asignarImagenBoton(btnDeudas, "/proyectoescuela1/iconos/deuda.png", 50, 50);
        btnDeudas.setVerticalTextPosition(JButton.BOTTOM);
        btnDeudas.setHorizontalTextPosition(JButton.CENTER);
        btnDeudas.addActionListener(e -> {
            mostrarPanel(new DeudaVista());
        });

        // -- Descuentos (becas, hermanos, convenios) --
        JButton btnDescuentos = new JButton("Descuentos");
        asignarImagenBoton(btnDescuentos, "/proyectoescuela1/iconos/descuento.png", 50, 50);
        btnDescuentos.setVerticalTextPosition(JButton.BOTTOM);
        btnDescuentos.setHorizontalTextPosition(JButton.CENTER);
        btnDescuentos.addActionListener(e -> {
            mostrarPanel(new DescuentoVista());
        });

        panel.add(btnPensiones);
        panel.add(btnPagos);
        panel.add(btnDeudas);
        panel.add(btnDescuentos);

        return panel;
    }

}
