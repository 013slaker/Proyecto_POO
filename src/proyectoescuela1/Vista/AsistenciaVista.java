/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.CursoControlador;
import proyectoescuela1.Controlador.DocenteControlador;
import proyectoescuela1.Controlador.AsistenciaControlador;

import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Asistencia;
import proyectoescuela1.Modelo.Curso;
import proyectoescuela1.Modelo.Docente;
import proyectoescuela1.Modelo.RegistroAsistencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ====================================================================
 * REGISTRO DE ASISTENCIA POR LISTA (POR SECCIÓN)
 * --------------------------------------------------------------------
 * Esta es la pantalla PRINCIPAL para pasar asistencia: el docente elige
 * Nivel, Grado, Sección, Curso y Fecha, carga a todos los alumnos de esa
 * sección de una sola vez (todos "Presente" por defecto) y solo marca
 * a los que faltaron. Al guardar, se crea (o actualiza) UN SOLO
 * RegistroAsistencia que contiene la Asistencia de cada alumno.
 *
 * El botón "Marcar tardanzas" filtra a los que quedaron como Falta y
 * permite indicar cuáles en realidad llegaron tarde, cambiando su
 * estado a Tardanza sin tener que buscarlos uno por uno en la tabla.
 *
 * El botón "Ver historial del alumno" abre HistorialAlumnoDialog con
 * el historial completo y las estadísticas del alumno seleccionado en
 * la tabla, y permite justificar una falta puntual.
 *
 * Esta es la ÚNICA pantalla donde se CREAN asistencias nuevas. El
 * diálogo de historial solo permite CONSULTAR y JUSTIFICAR — no crea
 * registros nuevos, para no duplicar información.
 * ====================================================================
 */
public class AsistenciaVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private AsistenciaControlador controlador = new AsistenciaControlador();

    private AlumnoControlador alumnoControlador = new AlumnoControlador();

    private CursoControlador cursoControlador = new CursoControlador();

    private DocenteControlador docenteControlador = new DocenteControlador();

    //==========================================================
    // FILTROS DEL REGISTRO
    //==========================================================
    /** Curso que dictará el docente (se llena según el nivel elegido). */
    private JComboBox<String> comboCurso = new JComboBox<>();

    /** Docente que toma la asistencia (se llena según el nivel elegido). */
    private JComboBox<String> comboDocente = new JComboBox<>();

    /** Nivel educativo. */
    private JComboBox<String> comboNivel
            = new JComboBox<>(new String[]{"Primaria", "Secundaria"});

    /** Grado. Se llena automáticamente según el nivel. */
    private JComboBox<String> comboGrado = new JComboBox<>();

    /** Sección. */
    private JComboBox<String> comboSeccion
            = new JComboBox<>(new String[]{"A", "B", "C"});

    /** Fecha del registro. */
    private JSpinner spinnerFecha = new JSpinner(new SpinnerDateModel());

    //==========================================================
    // LISTAS PARALELAS PARA LOS COMBOS (para recuperar el objeto elegido)
    //==========================================================
    private List<Curso> cursosDisponibles = new ArrayList<>();
    private List<Docente> docentesDisponibles = new ArrayList<>();

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCargar = new JButton("Cargar alumnos");
    private JButton btnGuardar = new JButton("Guardar asistencia");
    private JButton btnTardanzas = new JButton("Marcar tardanzas");
    private JButton btnVerHistorial = new JButton("Ver historial del alumno");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnCancelar = new JButton("Cancelar");

    //==========================================================
    // TABLA
    //==========================================================
    private String[] columnas = {"Código", "Alumno", "Estado", "Observación"};

    private DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            // Solo pueden editarse: Estado y Observación
            return columna == 2 || columna == 3;
        }
    };

    private JTable tabla = new JTable(modeloTabla);

    //==========================================================
    // ESTADO DE LA PANTALLA
    //==========================================================
    /** Alumnos de la sección actualmente cargada. */
    private List<Alumno> alumnos;

    /**
     * Si ya existía un registro guardado para esta fecha+curso+sección,
     * se guarda aquí para EDITARLO en vez de crear uno nuevo al guardar.
     * Si es null, significa que se va a crear un registro nuevo.
     */
    private RegistroAsistencia registroActual = null;

    /** Código y nombre del alumno actualmente seleccionado en la tabla
     *  (para poder abrir su historial con btnVerHistorial). */
    private String codigoAlumnoSeleccionado = null;
    private String nombreAlumnoSeleccionado = null;

    //==========================================================
    // GRADOS
    //==========================================================
    private final String[] gradosPrimaria = {"1°", "2°", "3°", "4°", "5°", "6°"};
    private final String[] gradosSecundaria = {"1°", "2°", "3°", "4°", "5°"};

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public AsistenciaVista() {
        setLayout(new BorderLayout(10, 10));

        initComponentes();
        cargarGrados();
        cargarCursosYDocentes();
        initEventos();
    }

    //==========================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    //==========================================================
    private void initComponentes() {

        // fecha
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));

        //------------------------------------------------------
        // PANEL DE FILTROS
        //------------------------------------------------------
        JPanel panelFiltros = new JPanel(new GridLayout(6, 2, 5, 5));

        panelFiltros.setBorder(BorderFactory.createTitledBorder("Datos del Registro"));

        panelFiltros.add(new JLabel("Nivel:"));
        panelFiltros.add(comboNivel);

        panelFiltros.add(new JLabel("Grado:"));
        panelFiltros.add(comboGrado);

        panelFiltros.add(new JLabel("Sección:"));
        panelFiltros.add(comboSeccion);

        panelFiltros.add(new JLabel("Curso:"));
        panelFiltros.add(comboCurso);

        panelFiltros.add(new JLabel("Docente:"));
        panelFiltros.add(comboDocente);

        panelFiltros.add(new JLabel("Fecha:"));
        panelFiltros.add(spinnerFecha);

        //------------------------------------------------------
        // PANEL DE BOTONES
        //------------------------------------------------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panelBotones.add(btnCargar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnTardanzas);
        panelBotones.add(btnVerHistorial);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);

        //------------------------------------------------------
        // PANEL SUPERIOR
        //------------------------------------------------------
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));

        panelSuperior.add(panelFiltros, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        //------------------------------------------------------
        // CONFIGURACIÓN DE TABLA
        //------------------------------------------------------
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //------------------------------------------------------
        // Combo de Estado dentro de la tabla
        // (por defecto se carga "Presente"; el docente cambia a
        // "Falta" solo a quien no llegó. "Tardanza" se asigna con
        // el botón "Marcar tardanzas" y "Justificada" se maneja en
        // el diálogo de historial)
        //------------------------------------------------------
        JComboBox<String> comboEstado = new JComboBox<>();
        comboEstado.addItem("Presente");
        comboEstado.addItem("Falta");
        comboEstado.addItem("Tardanza");
        comboEstado.addItem("Justificada");

        tabla.getColumnModel().getColumn(2)
                .setCellEditor(new DefaultCellEditor(comboEstado));

        // Al hacer clic en una fila, se habilita "Ver historial del alumno"
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;

                codigoAlumnoSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
                nombreAlumnoSeleccionado = modeloTabla.getValueAt(fila, 1).toString();
                btnVerHistorial.setEnabled(true);
            }
        });

        //------------------------------------------------------
        // SCROLL
        //------------------------------------------------------
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de alumnos"));

        //------------------------------------------------------
        // AGREGAR COMPONENTES
        //------------------------------------------------------
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnVerHistorial.setEnabled(false);
    }

    //==========================================================
    // CARGA LOS GRADOS SEGÚN EL NIVEL
    //==========================================================
    private void cargarGrados() {

        comboGrado.removeAllItems();

        String[] grados = comboNivel.getSelectedItem().equals("Primaria")
                ? gradosPrimaria : gradosSecundaria;

        comboGrado.addItem("Seleccionar grado");
        for (String grado : grados) {
            comboGrado.addItem(grado);
        }
    }

    //==========================================================
    // CARGA LOS CURSOS Y DOCENTES SEGÚN EL NIVEL
    //==========================================================
    private void cargarCursosYDocentes() {

        String nivel = comboNivel.getSelectedItem().toString();

        // -- CURSOS --
        comboCurso.removeAllItems();
        cursosDisponibles = cursoControlador.buscarPorNivel(nivel);
        for (Curso c : cursosDisponibles) {
            comboCurso.addItem(c.getIdCurso() + " - " + c.getNombre());
        }

        // -- DOCENTES --
        comboDocente.removeAllItems();
        docentesDisponibles = docenteControlador.buscarPorNivel(nivel);
        for (Docente d : docentesDisponibles) {
            comboDocente.addItem(d.getCodigoDocente() + " - " + d.getNombreCompleto());
        }
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void initEventos() {

        // Cuando cambia el nivel, se recargan grado/curso/docente
        comboNivel.addActionListener(e -> {
            cargarGrados();
            cargarCursosYDocentes();
        });

        // Botón cargar alumnos
        btnCargar.addActionListener(e -> cargarAlumnos());

        // Botón guardar
        btnGuardar.addActionListener(e -> guardarAsistencia());

        // Botón marcar tardanzas
        btnTardanzas.addActionListener(e -> marcarTardanzas());

        // Botón ver historial del alumno (abre el diálogo)
        btnVerHistorial.addActionListener(e -> verHistorialAlumno());

        // Botón limpiar
        btnLimpiar.addActionListener(e -> limpiar());

        // Botón cancelar (limpia todo, igual que limpiar, pero además
        // descarta cualquier registro que se estuviera editando)
        btnCancelar.addActionListener(e -> {
            registroActual = null;
            limpiar();
        });
    }

    //==========================================================
    // VALIDA QUE TODOS LOS FILTROS OBLIGATORIOS ESTÉN LLENOS
    //==========================================================
    private boolean filtrosValidos() {

        if (comboGrado.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un grado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (comboCurso.getSelectedIndex() < 0 || cursosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay cursos registrados para este nivel. "
                    + "Registre cursos antes de tomar asistencia.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (comboDocente.getSelectedIndex() < 0 || docentesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay docentes registrados para este nivel. "
                    + "Registre docentes antes de tomar asistencia.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    //==========================================================
    // CARGA LOS ALUMNOS DE LA SECCIÓN (o el registro ya existente)
    //==========================================================
    private void cargarAlumnos() {

        if (!filtrosValidos()) {
            return;
        }

        String nivel = comboNivel.getSelectedItem().toString();
        String grado = comboGrado.getSelectedItem().toString();
        String seccion = comboSeccion.getSelectedItem().toString();
        Curso curso = cursosDisponibles.get(comboCurso.getSelectedIndex());
        Date fecha = (Date) spinnerFecha.getValue();

        // Busca si YA existe un registro para esta fecha+curso+sección.
        // Si existe, se entra en modo edición (por ejemplo, para marcar
        // tardanzas de un registro ya guardado hoy).
        registroActual = controlador.buscarRegistro(
                fecha, curso.getIdCurso(), nivel, grado, seccion);

        modeloTabla.setRowCount(0);

        if (registroActual != null) {
            // MODO EDICIÓN: se carga lo que ya estaba guardado
            for (Asistencia a : registroActual.getListaAsistencias()) {
                modeloTabla.addRow(new Object[]{
                    a.getCodigoAlumno(),
                    nombreAlumno(a.getCodigoAlumno()),
                    a.getNombreEstado(),
                    a.getObservacion()
                });
            }
            JOptionPane.showMessageDialog(this,
                    "Ya existe un registro para esta fecha/curso/sección. "
                    + "Se cargó para editarlo.",
                    "Registro existente", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // MODO NUEVO: carga a los alumnos de la sección, todos
            // "Presente" por defecto. El docente solo cambia a los
            // que faltaron.
            alumnos = alumnoControlador.buscarPorSeccion(nivel, grado, seccion);

            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay alumnos registrados en " + grado + seccion + ".",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Alumno a : alumnos) {
                modeloTabla.addRow(new Object[]{
                    a.getCodigoAlumno(),
                    a.getNombreCompleto(),
                    "Presente",
                    ""
                });
            }
        }
    }

    /** Busca el nombre completo de un alumno a partir de su código. */
    private String nombreAlumno(String codigo) {
        Alumno a = alumnoControlador.buscarPorCodigo(codigo);
        return a != null ? a.getNombreCompleto() : codigo;
    }

    //==========================================================
    // CONVIERTE EL TEXTO DEL COMBO DE ESTADO A SU LETRA (P/T/F/J)
    //==========================================================
    private String letraEstado(String estadoTexto) {
        switch (estadoTexto) {
            case "Presente": return "P";
            case "Tardanza": return "T";
            case "Falta": return "F";
            case "Justificada": return "J";
            default: return "P";
        }
    }

    //==========================================================
    // GUARDA LA ASISTENCIA (crea el registro nuevo o actualiza uno existente)
    //==========================================================
    private void guardarAsistencia() {

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Primero cargue la lista de alumnos.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!filtrosValidos()) {
            return;
        }

        // Deja de editar la celda actual antes de leer la tabla
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        Date fecha = (Date) spinnerFecha.getValue();
        Curso curso = cursosDisponibles.get(comboCurso.getSelectedIndex());
        Docente docente = docentesDisponibles.get(comboDocente.getSelectedIndex());
        String nivel = comboNivel.getSelectedItem().toString();
        String grado = comboGrado.getSelectedItem().toString();
        String seccion = comboSeccion.getSelectedItem().toString();

        if (registroActual == null) {
            // -------- CREAR REGISTRO NUEVO --------
            RegistroAsistencia nuevo = new RegistroAsistencia(
                    fecha, curso.getIdCurso(), curso.getNombre(),
                    nivel, grado, seccion,
                    docente.getCodigoDocente(), docente.getNombreCompleto()
            );

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String codigoAlumno = modeloTabla.getValueAt(i, 0).toString();
                String estadoTexto = modeloTabla.getValueAt(i, 2).toString();
                String observacion = modeloTabla.getValueAt(i, 3).toString();

                nuevo.agregarAsistencia(new Asistencia(
                        fecha, letraEstado(estadoTexto), codigoAlumno, observacion
                ));
            }

            controlador.registrar(nuevo);
            registroActual = nuevo;

        } else {
            // -------- ACTUALIZAR REGISTRO EXISTENTE --------
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String codigoAlumno = modeloTabla.getValueAt(i, 0).toString();
                String estadoTexto = modeloTabla.getValueAt(i, 2).toString();
                String observacion = modeloTabla.getValueAt(i, 3).toString();

                // busca la Asistencia de ese alumno dentro del registro y la actualiza
                registroActual.getListaAsistencias().stream()
                        .filter(a -> a.getCodigoAlumno().equals(codigoAlumno))
                        .findFirst()
                        .ifPresent(a -> {
                            a.setEstado(letraEstado(estadoTexto));
                            a.setObservacion(observacion);
                        });
            }

            controlador.actualizar(registroActual);
        }

        JOptionPane.showMessageDialog(this,
                "Asistencia guardada correctamente ("
                + modeloTabla.getRowCount() + " alumnos).",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    //==========================================================
    // ABRE EL DIÁLOGO CON EL HISTORIAL DEL ALUMNO SELECCIONADO
    //==========================================================
    private void verHistorialAlumno() {

        if (codigoAlumnoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un alumno de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se le pasa el MISMO RegistroAsistenciaControlador que ya usa
        // esta pantalla, para que el diálogo trabaje con los mismos
        // datos (una sola fuente de verdad) y no vuelva a leer el
        // archivo .dat desde cero.
        HistorialAsistenciaAlumnoDialog dialogo = new HistorialAsistenciaAlumnoDialog(
                SwingUtilities.getWindowAncestor(this),
                controlador,
                codigoAlumnoSeleccionado,
                nombreAlumnoSeleccionado
        );

        dialogo.setVisible(true);
    }

    //==========================================================
    // MARCAR TARDANZAS: filtra a los que están como "Falta" y
    // permite elegir cuáles en realidad llegaron tarde.
    //==========================================================
    private void marcarTardanzas() {

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Primero cargue (o guarde) la lista de asistencia.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // busca las filas actualmente marcadas como Falta
        List<Integer> filasFalta = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ("Falta".equals(modeloTabla.getValueAt(i, 2))) {
                filasFalta.add(i);
            }
        }

        if (filasFalta.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay alumnos marcados como Falta en esta lista.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // arma un panel con una casilla (checkbox) por cada alumno en Falta
        JPanel panelCheckboxes = new JPanel(new GridLayout(filasFalta.size(), 1));
        List<JCheckBox> checkboxes = new ArrayList<>();

        for (int fila : filasFalta) {
            String nombre = modeloTabla.getValueAt(fila, 1).toString();
            JCheckBox check = new JCheckBox(nombre);
            checkboxes.add(check);
            panelCheckboxes.add(check);
        }

        int resultado = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(panelCheckboxes),
                "Marque quiénes llegaron tarde (el resto se queda en Falta)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        // aplica el cambio: los marcados pasan de Falta a Tardanza
        for (int i = 0; i < filasFalta.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                int fila = filasFalta.get(i);
                modeloTabla.setValueAt("Tardanza", fila, 2);
            }
        }

        JOptionPane.showMessageDialog(this,
                "Tardanzas actualizadas en la tabla. "
                + "Presione 'Guardar asistencia' para grabar los cambios.",
                "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    //==========================================================
    // LIMPIAR
    //==========================================================
    private void limpiar() {
        modeloTabla.setRowCount(0);
        registroActual = null;
        alumnos = null;
        codigoAlumnoSeleccionado = null;
        nombreAlumnoSeleccionado = null;
        btnVerHistorial.setEnabled(false);
        comboSeccion.setSelectedIndex(0);
        comboGrado.setSelectedIndex(0);
    }
}
