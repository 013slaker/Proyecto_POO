/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.AsistenciaControlador;
import proyectoescuela1.Controlador.RegistroAsistenciaControlador;
import proyectoescuela1.Modelo.Alumno;


public class AsistenciaVista extends JPanel {

    // -- CONTROLADORES ---------------------------------------------
    // Comparten el mismo RegistroAsistenciaControlador para que ambas
    // pantallas (esta y RegistroAsistenciaVista) lean/escriban siempre
    // el mismo archivo de datos y no se genere informacion duplicada.
    private RegistroAsistenciaControlador registroCtrl =
            new RegistroAsistenciaControlador();
    private AsistenciaControlador controlador =
            new AsistenciaControlador(registroCtrl);
    private AlumnoControlador alumnoCtrl =
            new AlumnoControlador();

    // -- SELECTOR DE ALUMNO ------------------------------------------
    private JTextField txtBuscarAlumno = new JTextField(15);
    private JButton btnBuscarAlumno = new JButton("Buscar Alumno");
    private JLabel lblAlumno = new JLabel("Alumno: (ninguno seleccionado)");

    // -- PANEL DE JUSTIFICACION (solo se habilita con una fila elegida) --
    private JLabel lblRegistroSeleccionado =
            new JLabel("Seleccione una fila del historial (Falta/Tardanza).");
    private JTextField txtObservacion = new JTextField(25);
    private JButton btnJustificar = new JButton("Justificar falta seleccionada");
    private JButton btnGuardarObservacion = new JButton("Guardar solo observación");

    // -- LABELS ESTADISTICAS ------------------------------------------
    private JLabel lblPresencias = new JLabel("Presencias: -");
    private JLabel lblFaltas = new JLabel("Faltas: -");
    private JLabel lblPorcentaje = new JLabel("Asistencia: -");

    // -- TABLA (HISTORIAL) --------------------------------------------
    private String[] columnas = {"Código", "Fecha", "Estado", "Observación"};
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false; // el historial es de solo lectura; se edita con los botones
                }
            };
    private JTable tabla = new JTable(modeloTabla);

    private Alumno alumnoSeleccionado = null;
    private String idAsistenciaSeleccionada = null;
    private String estadoSeleccionado = null;

    // -- CONSTRUCTOR ---------------------------------------------------
    public AsistenciaVista() {
        setLayout(new BorderLayout(10, 10));
        btnJustificar.setEnabled(false);
        btnGuardarObservacion.setEnabled(false);
        initUI();
        initEventos();
    }

    // -- INTERFAZ --------------------------------------------------------
    private void initUI() {

        // BUSCAR ALUMNO
        JPanel panelAlumno = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAlumno.setBorder(BorderFactory.createTitledBorder("Seleccionar Alumno"));
        panelAlumno.add(new JLabel("Nombre/Código:"));
        panelAlumno.add(txtBuscarAlumno);
        panelAlumno.add(btnBuscarAlumno);
        panelAlumno.add(lblAlumno);

        // JUSTIFICACION
        JPanel form = new JPanel(new GridLayout(3, 1, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Justificar / Anotar observación"));
        form.add(lblRegistroSeleccionado);

        JPanel filaObs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaObs.add(new JLabel("Observación:"));
        filaObs.add(txtObservacion);
        form.add(filaObs);

        JPanel filaBotonesJustif = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaBotonesJustif.add(btnJustificar);
        filaBotonesJustif.add(btnGuardarObservacion);
        form.add(filaBotonesJustif);

        // ESTADISTICAS
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 5, 5));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        panelStats.add(lblPresencias);
        panelStats.add(lblFaltas);
        panelStats.add(lblPorcentaje);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(panelAlumno, BorderLayout.NORTH);
        panelSuperior.add(form, BorderLayout.CENTER);
        panelSuperior.add(panelStats, BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Historial de Asistencia (haga clic en una fila para justificarla)"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // -- EVENTOS -------------------------------------------------------
    private void initEventos() {

        // BUSCAR ALUMNO
        btnBuscarAlumno.addActionListener(e -> {
            String texto = txtBuscarAlumno.getText().trim();
            if (texto.isEmpty()) return;

            Alumno encontrado = alumnoCtrl.buscarPorCodigo(texto);

            if (encontrado == null) {
                List<Alumno> lista = alumnoCtrl.buscarPorNombre(texto);
                if (!lista.isEmpty()) {
                    encontrado = lista.get(0);
                }
            }

            if (encontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "Alumno no encontrado.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            alumnoSeleccionado = encontrado;
            lblAlumno.setText("Alumno: " + encontrado.getNombreCompleto()
                    + " (" + encontrado.getCodigoAlumno() + ")");

            limpiarSeleccion();
            cargarAsistencias();
            actualizarEstadisticas();
        });

        // JUSTIFICAR (cambia el estado a Justificada + guarda observación)
        btnJustificar.addActionListener(e -> {
            if (idAsistenciaSeleccionada == null) return;

            if (!"Falta".equals(estadoSeleccionado)
                    && !"Tardanza".equals(estadoSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                        "Solo se pueden justificar registros marcados "
                        + "como Falta o Tardanza.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = controlador.justificar(
                    idAsistenciaSeleccionada,
                    txtObservacion.getText().trim()
            );

            if (ok) {
                cargarAsistencias();
                actualizarEstadisticas();
                limpiarSeleccion();
                JOptionPane.showMessageDialog(this,
                        "Falta justificada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo encontrar el registro original.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // GUARDAR SOLO OBSERVACION (sin cambiar el estado, ej. anotar una anomalia)
        btnGuardarObservacion.addActionListener(e -> {
            if (idAsistenciaSeleccionada == null) return;

            boolean ok = controlador.actualizarObservacion(
                    idAsistenciaSeleccionada,
                    txtObservacion.getText().trim()
            );

            if (ok) {
                cargarAsistencias();
                limpiarSeleccion();
                JOptionPane.showMessageDialog(this,
                        "Observación guardada.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // CLICK EN TABLA -- habilita los botones de edicion
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;

                idAsistenciaSeleccionada =
                        modeloTabla.getValueAt(fila, 0).toString();
                estadoSeleccionado =
                        modeloTabla.getValueAt(fila, 2).toString();
                txtObservacion.setText(
                        modeloTabla.getValueAt(fila, 3).toString());

                lblRegistroSeleccionado.setText(
                        "Editando: " + idAsistenciaSeleccionada
                        + " (" + estadoSeleccionado + ")");

                btnJustificar.setEnabled(true);
                btnGuardarObservacion.setEnabled(true);
            }
        });
    }

    // -- CARGA ASISTENCIAS ----------------------------------------------
    /**
     * Carga en la tabla el historial del alumno seleccionado,
     * leyendo desde todos los registros de lista (Lambda: forEach).
     */
    private void cargarAsistencias() {
        if (alumnoSeleccionado == null) return;
        modeloTabla.setRowCount(0);

        controlador.buscarPorAlumno(alumnoSeleccionado.getCodigoAlumno())
                .forEach(a -> modeloTabla.addRow(new Object[]{
                    a.getIdAsistencia(),
                    a.getFechaFormateada(),
                    a.getNombreEstado(),
                    a.getObservacion()
                }));

        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // -- ACTUALIZAR ESTADISTICAS -----------------------------------------
    private void actualizarEstadisticas() {
        if (alumnoSeleccionado == null) return;
        String codigo = alumnoSeleccionado.getCodigoAlumno();

        long presencias = controlador.contarPresencias(codigo);
        long faltas = controlador.contarFaltas(codigo);
        double porcentaje = controlador.porcentajeAsistencia(codigo);

        lblPresencias.setText("Presencias: " + presencias);
        lblFaltas.setText("Faltas: " + faltas);
        lblPorcentaje.setText(String.format("Asistencia: %.1f%%", porcentaje));

        lblPorcentaje.setForeground(
                porcentaje >= 70 ? Color.GREEN.darker() : Color.RED);
    }

    // -- LIMPIAR SELECCION -------------------------------------------------
    private void limpiarSeleccion() {
        txtObservacion.setText("");
        tabla.clearSelection();
        idAsistenciaSeleccionada = null;
        estadoSeleccionado = null;
        lblRegistroSeleccionado.setText(
                "Seleccione una fila del historial (Falta/Tardanza).");
        btnJustificar.setEnabled(false);
        btnGuardarObservacion.setEnabled(false);
    }
}
