/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.CursoControlador;
import proyectoescuela1.Controlador.DocenteControlador;
import proyectoescuela1.Controlador.RegistroAsistenciaControlador;

import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.RegistroAsistencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * ========================================================== REGISTRO DE
 * ASISTENCIA ---------------------------------------------------------- Esta
 * ventana permite al docente registrar la asistencia completa de una sección.
 *
 * El docente selecciona: • Curso • Nivel • Grado • Sección
 *
 * Luego el sistema carga automáticamente todos los alumnos pertenecientes a esa
 * sección para registrar la asistencia de todo el salón en una sola operación.
 *
 * ==========================================================
 */
public class RegistroAsistenciaVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private RegistroAsistenciaControlador controlador = new RegistroAsistenciaControlador();

    private AlumnoControlador alumnoControlador = new AlumnoControlador();

    private CursoControlador cursoControlador = new CursoControlador();

    private DocenteControlador docenteControlador = new DocenteControlador();

    //==========================================================
    // FILTROS DEL REGISTRO
    //==========================================================
    /**
     * Curso que dictará el docente.
     */
    private JComboBox<String> comboCurso
            = new JComboBox<>();

    /**
     * Nivel educativo.
     */
    private JComboBox<String> comboNivel
            = new JComboBox<>(new String[]{
        "Primaria",
        "Secundaria"
    });

    /**
     * Grado. Se llenará automáticamente según el nivel.
     */
    private JComboBox<String> comboGrado
            = new JComboBox<>();

    /**
     * Sección.
     */
    private JComboBox<String> comboSeccion
            = new JComboBox<>(new String[]{
        "A",
        "B",
        "C"
    });

    /**
     * Fecha del registro.
     */
    private JSpinner spinnerFecha
            = new JSpinner(new SpinnerDateModel());

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCargar
            = new JButton("Cargar alumnos");

    private JButton btnGuardar
            = new JButton("Guardar asistencia");

    private JButton btnLimpiar
            = new JButton("Limpiar");

    private JButton btnCancelar
            = new JButton("Cancelar");

    //==========================================================
    // TABLA
    //==========================================================
    /**
     * Columnas de la tabla.
     */
    private String[] columnas = {
        "Código",
        "Alumno",
        "Estado",
        "Observación"

    };

    /**
     * Modelo de la tabla.
     */
    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {

            /*
                     * Solo podrán editarse:
                     *
                     * Estado
                     * Observación
             */
            return columna == 2 || columna == 3;

        }

    };

    /**
     * Tabla principal.
     */
    private JTable tabla
            = new JTable(modeloTabla);

    //==========================================================
    // LISTA DE ALUMNOS CARGADOS
    //==========================================================
    /**
     * Guarda temporalmente los alumnos que pertenecen a la sección
     * seleccionada.
     */
    private List<Alumno> alumnos;

    //==========================================================
    // GRADOS
    //==========================================================
    private final String[] gradosPrimaria = {
        "1°",
        "2°",
        "3°",
        "4°",
        "5°",
        "6°"

    };

    private final String[] gradosSecundaria = {
        "1°",
        "2°",
        "3°",
        "4°",
        "5°"

    };

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public RegistroAsistenciaVista() {

        setLayout(new BorderLayout(10, 10));

        initComponentes();
        cargarGrados();
        initEventos();
    }
    //==========================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    //==========================================================

    private void initComponentes() {

        // fecha
        spinnerFecha.setEditor(
                new JSpinner.DateEditor(
                        spinnerFecha,
                        "dd/MM/yyyy"));

        //------------------------------------------------------
        // PANEL DE FILTROS
        //------------------------------------------------------
        JPanel panelFiltros = new JPanel(
                new GridLayout(5, 2, 5, 5));

        panelFiltros.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del Registro"));

        panelFiltros.add(new JLabel("Curso:"));
        panelFiltros.add(comboCurso);

        panelFiltros.add(new JLabel("Nivel:"));
        panelFiltros.add(comboNivel);

        panelFiltros.add(new JLabel("Grado:"));
        panelFiltros.add(comboGrado);

        panelFiltros.add(new JLabel("Sección:"));
        panelFiltros.add(comboSeccion);

        panelFiltros.add(new JLabel("Fecha:"));
        panelFiltros.add(spinnerFecha);

        //------------------------------------------------------
        // PANEL DE BOTONES
        //------------------------------------------------------
        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        panelBotones.add(btnCargar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);

        //------------------------------------------------------
        // PANEL SUPERIOR
        //------------------------------------------------------
        JPanel panelSuperior = new JPanel(
                new BorderLayout(5, 5));

        panelSuperior.add(panelFiltros,
                BorderLayout.CENTER);

        panelSuperior.add(panelBotones,
                BorderLayout.SOUTH);

        //------------------------------------------------------
        // CONFIGURACIÓN DE TABLA
        //------------------------------------------------------
        tabla.setRowHeight(28);

        tabla.getTableHeader()
                .setReorderingAllowed(false);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        //------------------------------------------------------
        // Combo de Estado
        //------------------------------------------------------

        JComboBox<String> comboEstado
                = new JComboBox<>();

        comboEstado.addItem("Presente");
        comboEstado.addItem("Falta");
        comboEstado.addItem("Tardanza");
        comboEstado.addItem("Justificada");

        tabla.getColumnModel()
                .getColumn(2)
                .setCellEditor(
                        new DefaultCellEditor(comboEstado)
                );

        //------------------------------------------------------
        // SCROLL
        //------------------------------------------------------
        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Lista de alumnos"));

        //------------------------------------------------------
        // AGREGAR COMPONENTES
        //------------------------------------------------------
        add(panelSuperior, BorderLayout.NORTH);

        add(scroll, BorderLayout.CENTER);

    }

    //==========================================================
// CARGA LOS GRADOS SEGÚN EL NIVEL
//==========================================================
    private void cargarGrados() {

        comboGrado.removeAllItems();

        if (comboNivel.getSelectedItem()
                .equals("Primaria")) {

            comboGrado.addItem("Seleccionar grado");

            for (String grado : gradosPrimaria) {

                comboGrado.addItem(grado);

            }

        } else {

            comboGrado.addItem("Seleccionar grado");

            for (String grado : gradosSecundaria) {

                comboGrado.addItem(grado);

            }

        }

    }
//==========================================================
// EVENTOS
//==========================================================

    private void initEventos() {
        //------------------------------------------------------
        // Cuando cambia el nivel
        //------------------------------------------------------

        comboNivel.addActionListener(e -> cargarGrados());

        //------------------------------------------------------
        // Botón cargar alumnos
        //------------------------------------------------------
        btnCargar.addActionListener(e -> {

            cargarAlumnos();

        });

    }
    //==========================================================
// CARGA LOS ALUMNOS DE LA SECCIÓN
//==========================================================

    private void cargarAlumnos() {

        if (comboGrado.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un grado.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;

        }

        //--------------------------------------------------
        // Obtiene los alumnos
        //--------------------------------------------------
        alumnos = alumnoControlador.buscarPorSeccion(
                comboNivel.getSelectedItem().toString(),
                comboGrado.getSelectedItem().toString(),
                comboSeccion.getSelectedItem().toString()
        );

        modeloTabla.setRowCount(0);

        //--------------------------------------------------
        // Llena la tabla
        //--------------------------------------------------
        for (Alumno a : alumnos) {

            modeloTabla.addRow(
                    new Object[]{
                        a.getCodigoAlumno(),
                        a.getNombreCompleto(),
                        "Presente",
                        ""

                    }
            );

        }

    }

}
