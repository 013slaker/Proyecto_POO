package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.MatriculaControlador;
import proyectoescuela1.Controlador.PeriodoControlador;

import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.AnioEscolar;
import proyectoescuela1.Modelo.Matricula;


public class MatriculaVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private MatriculaControlador matriculaCtrl
            = new MatriculaControlador();

    private AlumnoControlador alumnoCtrl
            = new AlumnoControlador();

    private PeriodoControlador periodoCtrl
            = new PeriodoControlador();

    //==========================================================
    // FORMULARIO
    //==========================================================
    private JComboBox<Alumno> comboAlumno
            = new JComboBox<>();

    private JTextField txtAnio
            = new JTextField(8);

    private JTextField txtNivel
            = new JTextField(12);

    private JTextField txtGrado
            = new JTextField(10);

    private JTextField txtSeccion
            = new JTextField(10);

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnMatricular
            = new JButton("Matricular");

    private JButton btnActualizar
            = new JButton("Actualizar");

    private JButton btnRetirar
            = new JButton("Retirar");

    private JButton btnLimpiar
            = new JButton("Limpiar");

    //==========================================================
    // BUSCADOR
    //==========================================================
    private JTextField txtBuscar
            = new JTextField(20);

    private JButton btnBuscar
            = new JButton("Buscar");

    private JButton btnHistorial
            = new JButton("Historial del Alumno");

    //==========================================================
    // TABLA
    //==========================================================
    private String columnas[] = {
        "Código",
        "Alumno",
        "Año",
        "Nivel",
        "Grado",
        "Sección",
        "Fecha",
        "Estado"

    };

    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }

    };

    private JTable tabla
            = new JTable(modeloTabla);

    //==========================================================
    // VARIABLES AUXILIARES
    //==========================================================
    private String codigoSeleccionado = null;

    private SimpleDateFormat sdf
            = new SimpleDateFormat("dd/MM/yyyy");

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public MatriculaVista() {

        setLayout(new BorderLayout(10, 10));

        initComponentes();

        initEventos();

        cargarAlumnos();

        cargarAnioActual();

        actualizarTabla();

    }

    //==========================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    //==========================================================
    private void initComponentes() {

        //------------------------------------------------------
        // PANEL DEL FORMULARIO
        //------------------------------------------------------
        JPanel panelFormulario
                = new JPanel(new GridLayout(5, 2, 6, 6));

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de la Matrícula"));

        panelFormulario.add(new JLabel("Alumno:"));
        panelFormulario.add(comboAlumno);

        panelFormulario.add(new JLabel("Año Escolar:"));
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Nivel:"));
        panelFormulario.add(txtNivel);

        panelFormulario.add(new JLabel("Grado:"));
        panelFormulario.add(txtGrado);

        panelFormulario.add(new JLabel("Sección:"));
        panelFormulario.add(txtSeccion);

        //------------------------------------------------------
        // Estos campos solamente muestran información
        //------------------------------------------------------
        txtAnio.setEditable(false);
        txtNivel.setEditable(false);
        txtGrado.setEditable(false);
        txtSeccion.setEditable(false);

        //------------------------------------------------------
        // PANEL DE BOTONES
        //------------------------------------------------------
        JPanel panelBotones
                = new JPanel(new FlowLayout());

        panelBotones.add(btnMatricular);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnRetirar);
        panelBotones.add(btnLimpiar);

        //------------------------------------------------------
        // PANEL SUPERIOR
        //------------------------------------------------------
        JPanel panelSuperior
                = new JPanel(new BorderLayout());

        panelSuperior.add(panelFormulario,
                BorderLayout.CENTER);

        panelSuperior.add(panelBotones,
                BorderLayout.SOUTH);

        //------------------------------------------------------
        // PANEL DE BÚSQUEDA
        //------------------------------------------------------
        JPanel panelBuscar
                = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelBuscar.setBorder(
                BorderFactory.createTitledBorder(
                        "Buscar Matrícula"));

        panelBuscar.add(new JLabel("Alumno:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnHistorial);

        //------------------------------------------------------
        // TABLA
        //------------------------------------------------------
        JScrollPane scroll
                = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Matrículas Registradas"));

        //------------------------------------------------------
        // PANEL INFERIOR
        //------------------------------------------------------
        JPanel panelInferior
                = new JPanel(new BorderLayout());

        panelInferior.add(panelBuscar,
                BorderLayout.NORTH);

        panelInferior.add(scroll,
                BorderLayout.CENTER);

        //------------------------------------------------------
        // AGREGAR TODO AL PANEL PRINCIPAL
        //------------------------------------------------------
        add(panelSuperior,
                BorderLayout.NORTH);

        add(panelInferior,
                BorderLayout.CENTER);

    }

//==========================================================
// EVENTOS
//==========================================================
    private void initEventos() {

        //------------------------------------------------------
        // CUANDO CAMBIA EL ALUMNO
        //------------------------------------------------------
        comboAlumno.addActionListener(e -> {

            Alumno alumno
                    = (Alumno) comboAlumno.getSelectedItem();

            if (alumno != null) {

                txtNivel.setText(alumno.getNivel());

                txtGrado.setText(alumno.getGrado());

                txtSeccion.setText(alumno.getSeccion());

            }

        });

        //------------------------------------------------------
        // BOTÓN MATRICULAR
        //------------------------------------------------------
        btnMatricular.addActionListener(e -> {

            try {

                Alumno alumno
                        = (Alumno) comboAlumno.getSelectedItem();

                if (alumno == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Seleccione un alumno."
                    );

                    return;
                }

                int anio
                        = Integer.parseInt(txtAnio.getText());

                Matricula matricula
                        = new Matricula(
                                alumno,
                                anio,
                                txtNivel.getText(),
                                txtGrado.getText(),
                                txtSeccion.getText(),
                                new java.util.Date()
                        );

                matriculaCtrl.registrar(matricula);

                actualizarTabla();

                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Matrícula registrada correctamente."
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

            }

        });

        //------------------------------------------------------
        // BOTÓN BUSCAR
        //------------------------------------------------------
        btnBuscar.addActionListener(e -> {

            String texto
                    = txtBuscar.getText().trim();

            if (texto.isEmpty()) {

                actualizarTabla();

                return;
            }

            modeloTabla.setRowCount(0);

            List<Matricula> lista
                    = matriculaCtrl.buscarPorAlumno(texto);

            for (Matricula m : lista) {

                modeloTabla.addRow(new Object[]{
                    m.getCodigoMatricula(),
                    m.getAlumno().getNombreCompleto(),
                    m.getAnio(),
                    m.getNivel(),
                    m.getGrado(),
                    m.getSeccion(),
                    sdf.format(m.getFechaMatricula()),
                    m.getEstado()

                });

            }

        });

        //------------------------------------------------------
        // BOTÓN HISTORIAL DEL ALUMNO
        //------------------------------------------------------
        btnHistorial.addActionListener(e -> verHistorialAlumnoSeleccionado());

        //------------------------------------------------------
        // SELECCIONAR FILA DE LA TABLA
        //------------------------------------------------------
        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (e.getValueIsAdjusting()) {
                return;
            }

            int fila
                    = tabla.getSelectedRow();

            if (fila == -1) {
                return;
            }

            codigoSeleccionado
                    = modeloTabla.getValueAt(fila, 0).toString();

            Matricula matricula
                    = matriculaCtrl.buscarPorCodigo(codigoSeleccionado);

            if (matricula == null) {
                return;
            }

            comboAlumno.setSelectedItem(
                    matricula.getAlumno());

            txtAnio.setText(
                    String.valueOf(matricula.getAnio()));

            txtNivel.setText(
                    matricula.getNivel());

            txtGrado.setText(
                    matricula.getGrado());

            txtSeccion.setText(
                    matricula.getSeccion());

        });

        //------------------------------------------------------
        // BOTÓN ACTUALIZAR
        //------------------------------------------------------
        btnActualizar.addActionListener(e -> {

            if (codigoSeleccionado == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una matrícula."
                );

                return;
            }

            Matricula matricula
                    = matriculaCtrl.buscarPorCodigo(codigoSeleccionado);

            if (matricula == null) {
                return;
            }

            matricula.setGrado(txtGrado.getText());

            matricula.setSeccion(txtSeccion.getText());

            matriculaCtrl.guardarDatos();

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Matrícula actualizada."
            );

        });

        //------------------------------------------------------
        // BOTÓN RETIRAR
        //------------------------------------------------------
        btnRetirar.addActionListener(e -> {

            if (codigoSeleccionado == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una matrícula."
                );

                return;
            }

            int opcion
                    = JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea retirar al alumno?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                    );

            if (opcion == JOptionPane.YES_OPTION) {

                String motivo = JOptionPane.showInputDialog(
                        this,
                        "Motivo del retiro:"
                );

                if (motivo == null) {
                    // El usuario canceló el diálogo de motivo
                    return;
                }

                matriculaCtrl.retirarAlumno(
                        codigoSeleccionado, "Retirado", motivo);

                actualizarTabla();

                limpiarCampos();

            }

        });

        //------------------------------------------------------
        // BOTÓN LIMPIAR
        //------------------------------------------------------
        btnLimpiar.addActionListener(e -> {

            limpiarCampos();

        });

    }
    //==================================================
    // CARGAR ALUMNOS EN EL COMBO
    //==================================================

    private void cargarAlumnos() {

        comboAlumno.removeAllItems();

        List<Alumno> alumnos = alumnoCtrl.listarTodos();

        for (Alumno alumno : alumnos) {

            comboAlumno.addItem(alumno);

        }

        // Muestra automáticamente los datos del primer alumno
        if (comboAlumno.getItemCount() > 0) {

            comboAlumno.setSelectedIndex(0);

        }

    }
//==================================================
// CARGAR AÑO ESCOLAR ACTUAL
//==================================================

    private void cargarAnioActual() {

        AnioEscolar anio = periodoCtrl.getAnioEscolar();

        if (anio != null) {

            txtAnio.setText(String.valueOf(anio.getAnio()));

        } else {

            txtAnio.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    "No existe un Año Escolar configurado.\n"
                    + "Debe crear uno primero en el módulo "
                    + "Periodo Académico.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

        }

    }

//==================================================
// ACTUALIZAR TABLA
//==================================================
    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        List<Matricula> lista = matriculaCtrl.listarTodas();

        for (Matricula m : lista) {

            modeloTabla.addRow(new Object[]{
                m.getCodigoMatricula(),
                m.getAlumno().getNombreCompleto(),
                m.getAnio(),
                m.getNivel(),
                m.getGrado(),
                m.getSeccion(),
                sdf.format(m.getFechaMatricula()),
                m.getEstado()
            });

        }

    }

    private void limpiarCampos() {

        codigoSeleccionado = null;

        txtBuscar.setText("");

        tabla.clearSelection();

        if (comboAlumno.getItemCount() > 0) {
            comboAlumno.setSelectedIndex(0);
        }

    }

    //==========================================================
    // HISTORIAL DE MATRÍCULAS DE UN ALUMNO
    // Muestra en la tabla únicamente las matrículas del alumno
    // seleccionado en el combo (todas sus matrículas, de
    // cualquier año/estado). Se usa el botón "Buscar" con el
    // código del alumno seleccionado.
    //==========================================================
    private void verHistorialAlumnoSeleccionado() {

        Alumno alumno = (Alumno) comboAlumno.getSelectedItem();

        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno.");
            return;
        }

        modeloTabla.setRowCount(0);

        List<Matricula> historial =
                matriculaCtrl.buscarPorAlumno(alumno.getCodigoAlumno());

        for (Matricula m : historial) {

            modeloTabla.addRow(new Object[]{
                m.getCodigoMatricula(),
                m.getAlumno().getNombreCompleto(),
                m.getAnio(),
                m.getNivel(),
                m.getGrado(),
                m.getSeccion(),
                sdf.format(m.getFechaMatricula()),
                m.getEstado()
            });

        }
    }

}
