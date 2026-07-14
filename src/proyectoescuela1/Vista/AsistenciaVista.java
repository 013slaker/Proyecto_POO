/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.AsistenciaControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Asistencia;

/**
 * Vista del módulo Asistencia.
 * Primero seleccionas el alumno,
 * luego registras su asistencia.
 */
public class AsistenciaVista extends JPanel {

    // ── CONTROLADORES ─────────────────────────────
    private AsistenciaControlador controlador =
            new AsistenciaControlador();
    private AlumnoControlador alumnoCtrl =
            new AlumnoControlador();

    // ── SELECTOR DE ALUMNO ────────────────────────
    private JTextField txtBuscarAlumno =
            new JTextField(15);
    private JButton btnBuscarAlumno =
            new JButton("Buscar Alumno");
    private JLabel lblAlumno =
            new JLabel("Alumno: (ninguno seleccionado)");

    // ── CAMPOS DE ASISTENCIA ──────────────────────
    private String[] estados = {
        "P - Presente",
        "T - Tardanza",
        "F - Falta",
        "J - Justificada"
    };
    private JComboBox<String> comboEstado =
            new JComboBox<>(estados);

    private JTextField txtObservacion =
            new JTextField(20);

    // ── BOTONES ───────────────────────────────────
    private JButton btnGuardar  = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar  = new JButton("Limpiar");

    // ── LABELS ESTADÍSTICAS ───────────────────────
    private JLabel lblPresencias =
            new JLabel("Presencias: -");
    private JLabel lblFaltas =
            new JLabel("Faltas: -");
    private JLabel lblPorcentaje =
            new JLabel("Asistencia: -");

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Fecha", "Estado", "Observación"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    private Alumno alumnoSeleccionado = null;
    private String idSeleccionado     = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public AsistenciaVista() {
        setLayout(new BorderLayout(10, 10));
        btnGuardar.setEnabled(false);
        btnEliminar.setEnabled(false);
        initUI();
        initEventos();
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // BUSCAR ALUMNO
        JPanel panelAlumno = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );
        panelAlumno.setBorder(
            BorderFactory.createTitledBorder(
                "Seleccionar Alumno"
            )
        );
        panelAlumno.add(new JLabel("Nombre/Código:"));
        panelAlumno.add(txtBuscarAlumno);
        panelAlumno.add(btnBuscarAlumno);
        panelAlumno.add(lblAlumno);

        // FORMULARIO
        JPanel form = new JPanel(
            new GridLayout(2, 2, 5, 5)
        );
        form.setBorder(BorderFactory.createTitledBorder(
            "Registrar Asistencia"
        ));
        form.add(new JLabel("Estado:"));
        form.add(comboEstado);
        form.add(new JLabel("Observación:"));
        form.add(txtObservacion);

        // ESTADÍSTICAS
        JPanel panelStats = new JPanel(
            new GridLayout(1, 3, 5, 5)
        );
        panelStats.setBorder(
            BorderFactory.createTitledBorder(
                "Estadísticas"
            )
        );
        panelStats.add(lblPresencias);
        panelStats.add(lblFaltas);
        panelStats.add(lblPorcentaje);

        // BOTONES
        JPanel panelBotones = new JPanel(
            new FlowLayout()
        );
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
            new BorderLayout(5, 5)
        );
        panelSuperior.add(panelAlumno,  BorderLayout.NORTH);
        panelSuperior.add(form,         BorderLayout.CENTER);
        panelSuperior.add(panelStats,   BorderLayout.SOUTH);

        JPanel panelMedio = new JPanel(
            new BorderLayout()
        );
        panelMedio.add(panelSuperior, BorderLayout.CENTER);
        panelMedio.add(panelBotones,  BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Historial de Asistencia"
        ));

        add(panelMedio, BorderLayout.NORTH);
        add(scroll,     BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

        // BUSCAR ALUMNO
        btnBuscarAlumno.addActionListener(e -> {
            String texto =
                txtBuscarAlumno.getText().trim();
            if (texto.isEmpty()) return;

            Alumno encontrado =
                alumnoCtrl.buscarPorCodigo(texto);

            if (encontrado == null) {
                List<Alumno> lista =
                    alumnoCtrl.buscarPorNombre(texto);
                if (!lista.isEmpty()) {
                    encontrado = lista.get(0);
                }
            }

            if (encontrado == null) {
                JOptionPane.showMessageDialog(this,
                    "Alumno no encontrado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            alumnoSeleccionado = encontrado;
            lblAlumno.setText("Alumno: " +
                encontrado.getNombreCompleto() +
                " (" + encontrado.getCodigoAlumno() + ")"
            );

            btnGuardar.setEnabled(true);
            cargarAsistencias();
            actualizarEstadisticas();
        });

        // GUARDAR ASISTENCIA
        btnGuardar.addActionListener(e -> {
            if (alumnoSeleccionado == null) return;

            // obtiene solo la letra del estado
            String estadoCompleto = comboEstado
                .getSelectedItem().toString();
            String estado = estadoCompleto
                .substring(0, 1); // P, T, F o J

            Asistencia asistencia = new Asistencia(
                new Date(), // fecha actual
                estado,
                alumnoSeleccionado.getCodigoAlumno(),
                txtObservacion.getText().trim()
            );

            controlador.registrar(asistencia);
            cargarAsistencias();
            actualizarEstadisticas();
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Asistencia registrada: " +
                asistencia.getNombreEstado(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un registro de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este registro?",
                "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                cargarAsistencias();
                actualizarEstadisticas();
                idSeleccionado = null;
                btnEliminar.setEnabled(false);
            }
        });

        // LIMPIAR
        btnLimpiar.addActionListener(e -> limpiar());

        // CLICK EN TABLA
        tabla.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;
                    idSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();
                    btnEliminar.setEnabled(true);
                }
            }
        );
    }

    // ── CARGA ASISTENCIAS ─────────────────────────
    /**
     * Carga en la tabla las asistencias del alumno
     * seleccionado usando Lambda forEach.
     */
    private void cargarAsistencias() {
        if (alumnoSeleccionado == null) return;
        modeloTabla.setRowCount(0);

        // Lambda — recorre y carga en tabla
        controlador
            .buscarPorAlumno(
                alumnoSeleccionado.getCodigoAlumno()
            )
            .forEach(a -> modeloTabla.addRow(new Object[]{
                a.getIdAsistencia(),
                a.getFechaFormateada(),
                a.getNombreEstado(),
                a.getObservacion()
            }));

        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── ACTUALIZAR ESTADÍSTICAS ───────────────────
    private void actualizarEstadisticas() {
        if (alumnoSeleccionado == null) return;
        String codigo = alumnoSeleccionado
            .getCodigoAlumno();

        long presencias = controlador
            .contarPresencias(codigo);
        long faltas     = controlador
            .contarFaltas(codigo);
        double porcentaje = controlador
            .porcentajeAsistencia(codigo);

        lblPresencias.setText("Presencias: " + presencias);
        lblFaltas.setText("Faltas: " + faltas);
        lblPorcentaje.setText(String.format(
            "Asistencia: %.1f%%", porcentaje
        ));

        // color según porcentaje
        lblPorcentaje.setForeground(
            porcentaje >= 70
                ? Color.GREEN.darker()
                : Color.RED
        );
    }

    // ── LIMPIAR ───────────────────────────────────
    private void limpiar() {
        comboEstado.setSelectedIndex(0);
        txtObservacion.setText("");
        tabla.clearSelection();
        idSeleccionado = null;
        btnEliminar.setEnabled(false);
    }
}
