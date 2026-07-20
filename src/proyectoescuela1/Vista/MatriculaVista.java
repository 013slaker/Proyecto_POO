package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.MatriculaControlador;
import proyectoescuela1.Controlador.PeriodoControlador;

import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.AnioEscolar;
import proyectoescuela1.Modelo.Matricula;

/**
 * ====================================================================
 * MATRÍCULA
 * -------------------------------------------------------------------- -
 * "Buscar" filtra la tabla de abajo por nombre o código de alumno (antes solo
 * aceptaba el código exacto y parecía no hacer nada). - "Ver historial del
 * alumno" abre un diálogo aparte con TODAS las matrículas del alumno elegido en
 * el combo de arriba, sin tapar la tabla principal. - "Actualizar" ahora sí
 * cambia algo: los combos de Nivel/Grado/ Sección quedan habilitados y reflejan
 * la fila seleccionada de la tabla, así que cambiarlos y presionar Actualizar
 * reasigna de verdad al alumno de sección/grado (antes estaban bloqueados, por
 * eso "actualizar" no modificaba nada). - Se agregó el botón "Volver" para
 * regresar al menú anterior.
 * ====================================================================
 */
public class MatriculaVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private MatriculaControlador matriculaCtrl = new MatriculaControlador();
    private AlumnoControlador alumnoCtrl = new AlumnoControlador();
    private PeriodoControlador periodoCtrl = new PeriodoControlador();

    //==========================================================
    // FORMULARIO
    //==========================================================
    private JTextField txtBuscarAlumno = new JTextField(15);
    private JButton btnBuscarAlumno = new JButton("Buscar");
    private JLabel lblAlumnoSeleccionado = new JLabel("(ningún alumno seleccionado)");
    private Alumno alumnoSeleccionado = null;
    private JTextField txtAnio = new JTextField(8);

    private final String[] niveles = {"Primaria", "Secundaria"};
    private JComboBox<String> comboNivel = new JComboBox<>(niveles);

    private JComboBox<String> comboGrado = new JComboBox<>();
    private final String[] gradosPrimaria = {"1°", "2°", "3°", "4°", "5°", "6°"};
    private final String[] gradosSecundaria = {"1°", "2°", "3°", "4°", "5°"};

    private final String[] secciones = {"A", "B", "C"};
    private JComboBox<String> comboSeccion = new JComboBox<>(secciones);

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnMatricular = new JButton("Matricular");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnRetirar = new JButton("Retirar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnVolver = new JButton("Volver al menú");

    //==========================================================
    // BUSCADOR (filtra la tabla de abajo)
    //==========================================================
    private JTextField txtBuscar = new JTextField(20);
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnHistorial = new JButton("Ver historial del alumno");

    //==========================================================
    // TABLA
    //==========================================================
    private String columnas[] = {
        "Código", "Alumno", "Año", "Nivel", "Grado",
        "Sección", "Fecha", "Estado"
    };

    private DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tabla = new JTable(modeloTabla);

    //==========================================================
    // VARIABLES AUXILIARES
    //==========================================================
    private String codigoSeleccionado = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Acción a ejecutar al presionar "Volver" (la asigna quien abre esta
     * pantalla, típicamente MenuPrincipalVista). Si no se asigna, el botón
     * simplemente no hace nada.
     */
    private Runnable onVolver = () -> {
    };

    //==========================================================
    // CONSTRUCTORES
    //==========================================================
    public MatriculaVista() {
        this(null);
    }

    /**
     * @param onVolver acción para regresar al menú anterior (puede ser null si
     * esta pantalla se usa sin ese botón)
     */
    public MatriculaVista(Runnable onVolver) {
        if (onVolver != null) {
            this.onVolver = onVolver;
        }

        setLayout(new BorderLayout(10, 10));

        initComponentes();
        initEventos();
        cargarAnioActual();
        actualizarTabla();
    }

    //==========================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    //==========================================================
    private void initComponentes() {

        cargarGrados();

        //------------------------------------------------------
        // PANEL DEL FORMULARIO
        //------------------------------------------------------
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 6, 6));
        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Datos de la Matrícula"));

        JPanel panelBuscarAlumno = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscarAlumno.add(txtBuscarAlumno);
        panelBuscarAlumno.add(btnBuscarAlumno);
        panelBuscarAlumno.add(lblAlumnoSeleccionado);

        panelFormulario.add(new JLabel("Alumno:"));
        panelFormulario.add(panelBuscarAlumno);

        panelFormulario.add(new JLabel("Año Escolar:"));
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Nivel:"));
        panelFormulario.add(comboNivel);

        panelFormulario.add(new JLabel("Grado:"));
        panelFormulario.add(comboGrado);

        panelFormulario.add(new JLabel("Sección:"));
        panelFormulario.add(comboSeccion);

        // El año escolar viene del módulo "Período Académico"; no se
        // escribe a mano para evitar matrículas con años inventados.
        txtAnio.setEditable(false);

        //------------------------------------------------------
        // PANEL DE BOTONES
        //------------------------------------------------------
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnMatricular);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnRetirar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        //------------------------------------------------------
        // PANEL SUPERIOR
        //------------------------------------------------------
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        //------------------------------------------------------
        // PANEL DE BÚSQUEDA
        //------------------------------------------------------
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.setBorder(
                BorderFactory.createTitledBorder(
                        "Buscar en la tabla (por nombre o código)"));
        panelBuscar.add(new JLabel("Alumno:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnHistorial);

        //------------------------------------------------------
        // TABLA
        //------------------------------------------------------
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(
                BorderFactory.createTitledBorder("Matrículas Registradas"));

        //------------------------------------------------------
        // PANEL INFERIOR
        //------------------------------------------------------
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll, BorderLayout.CENTER);

        //------------------------------------------------------
        // AGREGAR TODO AL PANEL PRINCIPAL
        //------------------------------------------------------
        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void initEventos() {

        //------------------------------------------------------
        // CUANDO CAMBIA EL ALUMNO — precarga su nivel/grado/sección
        // ACTUALES como punto de partida (editable, por si se le va
        // a matricular en un grado distinto, p.ej. el año siguiente)
        //------------------------------------------------------
        btnBuscarAlumno.addActionListener(e -> {
            String texto = txtBuscarAlumno.getText().trim();
            if (texto.isEmpty()) {
                return;
            }

            Alumno encontrado = alumnoCtrl.buscarPorCodigo(texto);
            if (encontrado == null) {
                List<Alumno> lista = alumnoCtrl.buscarPorNombre(texto);
                if (!lista.isEmpty()) {
                    encontrado = lista.get(0);
                }
            }
            if (encontrado == null) {
                JOptionPane.showMessageDialog(this, "Alumno no encontrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            alumnoSeleccionado = encontrado;
            lblAlumnoSeleccionado.setText(encontrado.getNombreCompleto()
                    + " (" + encontrado.getCodigoAlumno() + ")");

            comboNivel.setSelectedItem(encontrado.getNivel());
            cargarGrados();
            comboGrado.setSelectedItem(encontrado.getGrado());
            comboSeccion.setSelectedItem(encontrado.getSeccion());
        });

        // Al cambiar el nivel, se recargan los grados disponibles
        comboNivel.addActionListener(e -> cargarGrados());

        //------------------------------------------------------
        // BOTÓN MATRICULAR
        //------------------------------------------------------
        btnMatricular.addActionListener(e -> {

            try {
                Alumno alumno = alumnoSeleccionado;

                if (alumno == null) {
                    JOptionPane.showMessageDialog(this, "Seleccione un alumno.");
                    return;
                }

                if (!validarCombos()) {
                    return;
                }

                if (txtAnio.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No hay un Año Escolar configurado. "
                            + "Vaya primero a Período Académico.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int anio = Integer.parseInt(txtAnio.getText());

                Matricula matricula = new Matricula(
                        alumno,
                        anio,
                        (String) comboNivel.getSelectedItem(),
                        (String) comboGrado.getSelectedItem(),
                        (String) comboSeccion.getSelectedItem(),
                        new java.util.Date()
                );

                matriculaCtrl.registrar(matricula);

                // El alumno también refleja el nivel/grado/sección
                // con el que quedó matriculado.
                alumno.setNivel((String) comboNivel.getSelectedItem());
                alumno.setGrado((String) comboGrado.getSelectedItem());
                alumno.setSeccion((String) comboSeccion.getSelectedItem());
                alumnoCtrl.actualizarAlumno(alumno);

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                        "Matrícula registrada correctamente.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //------------------------------------------------------
        // BOTÓN BUSCAR — filtra la tabla por nombre o código
        //------------------------------------------------------
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();

            if (texto.isEmpty()) {
                actualizarTabla();
                return;
            }

            modeloTabla.setRowCount(0);

            // Busca coincidencias por nombre (parcial) o por código exacto
            List<Alumno> coincidencias = alumnoCtrl.buscarPorNombre(texto);
            Alumno porCodigo = alumnoCtrl.buscarPorCodigo(texto);
            if (porCodigo != null && !coincidencias.contains(porCodigo)) {
                coincidencias = new ArrayList<>(coincidencias);
                coincidencias.add(porCodigo);
            }

            if (coincidencias.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ningún alumno con ese nombre o código.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Alumno alumno : coincidencias) {
                for (Matricula m : matriculaCtrl.buscarPorAlumno(
                        alumno.getCodigoAlumno())) {
                    agregarFila(m);
                }
            }
        });

        //------------------------------------------------------
        // BOTÓN "VER HISTORIAL DEL ALUMNO" — abre un diálogo aparte
        // con TODAS las matrículas del alumno elegido en el combo
        // de arriba (no depende de txtBuscar ni toca la tabla
        // principal, para no generar confusión).
        //------------------------------------------------------
        btnHistorial.addActionListener(e -> {
            Alumno alumno = alumnoSeleccionado;

            if (alumno == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un alumno.");
                return;
            }

            HistorialMatriculaDialog dialogo = new HistorialMatriculaDialog(
                    SwingUtilities.getWindowAncestor(this),
                    matriculaCtrl,
                    alumno.getCodigoAlumno(),
                    alumno.getNombreCompleto()
            );
            dialogo.setVisible(true);
        });

        //------------------------------------------------------
        // SELECCIONAR FILA DE LA TABLA — carga esa matrícula en el
        // formulario (incluyendo el alumno) para poder actualizarla
        //------------------------------------------------------
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                return;
            }

            codigoSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
            Matricula matricula = matriculaCtrl.buscarPorCodigo(codigoSeleccionado);
            if (matricula == null) {
                return;
            }

            alumnoSeleccionado = matricula.getAlumno();
            lblAlumnoSeleccionado.setText(alumnoSeleccionado.getNombreCompleto()
                    + " (" + alumnoSeleccionado.getCodigoAlumno() + ")");
            txtAnio.setText(String.valueOf(matricula.getAnio()));
            comboNivel.setSelectedItem(matricula.getNivel());
            cargarGrados();
            comboGrado.setSelectedItem(matricula.getGrado());
            comboSeccion.setSelectedItem(matricula.getSeccion());
        });

        //------------------------------------------------------
        // BOTÓN ACTUALIZAR — reasigna nivel/grado/sección de la
        // matrícula seleccionada en la tabla (y del alumno) según
        // lo que se elija en los combos.
        //------------------------------------------------------
        btnActualizar.addActionListener(e -> {

            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una matrícula de la tabla primero.");
                return;
            }

            if (!validarCombos()) {
                return;
            }

            try {
                matriculaCtrl.reasignarNivelGradoSeccion(
                        codigoSeleccionado,
                        (String) comboNivel.getSelectedItem(),
                        (String) comboGrado.getSelectedItem(),
                        (String) comboSeccion.getSelectedItem()
                );

                actualizarTabla();

                JOptionPane.showMessageDialog(this,
                        "Matrícula actualizada: nivel/grado/sección "
                        + "reasignados correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //------------------------------------------------------
        // BOTÓN RETIRAR
        //------------------------------------------------------
        btnRetirar.addActionListener(e -> {

            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una matrícula de la tabla primero.");
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Desea retirar al alumno?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {

                String motivo = JOptionPane.showInputDialog(
                        this, "Motivo del retiro:");

                if (motivo == null) {
                    return; // canceló
                }

                matriculaCtrl.retirarAlumno(codigoSeleccionado, "Retirado", motivo);

                actualizarTabla();
                limpiarCampos();
            }
        });

        //------------------------------------------------------
        // BOTÓN LIMPIAR
        //------------------------------------------------------
        btnLimpiar.addActionListener(e -> limpiarCampos());

        //------------------------------------------------------
        // BOTÓN VOLVER
        //------------------------------------------------------
        btnVolver.addActionListener(e -> onVolver.run());
    }

    //==========================================================
    // VALIDA QUE LOS COMBOS TENGAN UNA OPCIÓN REAL SELECCIONADA
    //==========================================================
    private boolean validarCombos() {
        if (comboGrado.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un grado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    //==========================================================
    // CARGAR GRADOS SEGÚN EL NIVEL SELECCIONADO
    //==========================================================
    private void cargarGrados() {
        comboGrado.removeAllItems();
        comboGrado.addItem("Seleccione un grado");

        String[] grados = "Primaria".equals(comboNivel.getSelectedItem())
                ? gradosPrimaria : gradosSecundaria;

        for (String grado : grados) {
            comboGrado.addItem(grado);
        }
    }

    //==========================================================
    // CARGAR AÑO ESCOLAR ACTUAL
    //==========================================================
    private void cargarAnioActual() {
        AnioEscolar anio = periodoCtrl.getAnioEscolar();

        if (anio != null) {
            txtAnio.setText(String.valueOf(anio.getAnio()));
        } else {
            txtAnio.setText("");
            JOptionPane.showMessageDialog(this,
                    "No existe un Año Escolar configurado.\n"
                    + "Debe crear uno primero en el módulo "
                    + "Periodo Académico.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    //==========================================================
    // ACTUALIZAR TABLA (todas las matrículas)
    //==========================================================
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Matricula m : matriculaCtrl.listarTodas()) {
            agregarFila(m);
        }
    }

    private void agregarFila(Matricula m) {
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

    private void limpiarCampos() {
        codigoSeleccionado = null;
        txtBuscar.setText("");
        tabla.clearSelection();
        alumnoSeleccionado = null;
        txtBuscarAlumno.setText("");
        lblAlumnoSeleccionado.setText("(ningún alumno seleccionado)");
        actualizarTabla();
    }
}
