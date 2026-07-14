/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import proyectoescuela1.Controlador.AsistenciaControlador;


public class HistorialAsistenciaAlumnoDialog extends JDialog {

    
    // RegistroAsistenciaControlador que ya tenía abierto la pantalla que
    // lo invoca, para no leer el archivo .dat de nuevo y ver siempre los
    // mismos datos actualizados.
    private final AsistenciaControlador controlador;
    private final String codigoAlumno;

    // ── ESTADÍSTICAS ───────────────────────────────
    private final JLabel lblPresencias = new JLabel("Presencias: -");
    private final JLabel lblFaltas = new JLabel("Faltas: -");
    private final JLabel lblPorcentaje = new JLabel("Asistencia: -");

    // ── PANEL DE JUSTIFICACIÓN ─────────────────────
    private final JLabel lblRegistroSeleccionado =
            new JLabel("Seleccione una fila del historial (Falta/Tardanza).");
    private final JTextField txtObservacion = new JTextField(25);
    private final JButton btnJustificar = new JButton("Justificar falta seleccionada");
    private final JButton btnGuardarObservacion = new JButton("Guardar solo observación");
    private final JButton btnCerrar = new JButton("Cerrar");

    // ── TABLA (HISTORIAL) ──────────────────────────
    private final String[] columnas = {"Código", "Fecha", "Estado", "Observación"};
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false; // de solo lectura; se edita con los botones
                }
            };
    private final JTable tabla = new JTable(modeloTabla);

    private String idAsistenciaSeleccionada = null;
    private String estadoSeleccionado = null;

    /**
     * @param owner        ventana padre (para centrar el diálogo)
     * @param registroCtrl el MISMO RegistroAsistenciaControlador que ya
     *                     usa la pantalla que abre este diálogo
     * @param codigoAlumno código del alumno a mostrar
     * @param nombreAlumno nombre del alumno (solo para el título)
     */
    public HistorialAsistenciaAlumnoDialog(Window owner,
                                  AsistenciaControlador registroCtrl,
                                  String codigoAlumno,
                                  String nombreAlumno) {

        super(owner, "Historial de asistencia — " + nombreAlumno,
                ModalityType.APPLICATION_MODAL);

        this.controlador = registroCtrl;
        this.codigoAlumno = codigoAlumno;

        setSize(600, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        initUI();
        initEventos();

        cargarAsistencias();
        actualizarEstadisticas();
    }

    // ── INTERFAZ ───────────────────────────────────
    private void initUI() {

        // ESTADÍSTICAS
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 5, 5));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        panelStats.add(lblPresencias);
        panelStats.add(lblFaltas);
        panelStats.add(lblPorcentaje);

        // JUSTIFICACIÓN
        JPanel form = new JPanel(new GridLayout(3, 1, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Justificar / Anotar observación"));
        form.add(lblRegistroSeleccionado);

        JPanel filaObs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaObs.add(new JLabel("Observación:"));
        filaObs.add(txtObservacion);
        form.add(filaObs);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaBotones.add(btnJustificar);
        filaBotones.add(btnGuardarObservacion);
        form.add(filaBotones);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(panelStats, BorderLayout.NORTH);
        panelSuperior.add(form, BorderLayout.CENTER);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Historial (haga clic en una fila para justificarla)"));

        // PIE
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnJustificar.setEnabled(false);
        btnGuardarObservacion.setEnabled(false);
    }

    // ── EVENTOS ────────────────────────────────────
    private void initEventos() {

        // CLICK EN TABLA — habilita los botones de edición
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;

                idAsistenciaSeleccionada = modeloTabla.getValueAt(fila, 0).toString();
                estadoSeleccionado = modeloTabla.getValueAt(fila, 2).toString();
                txtObservacion.setText(modeloTabla.getValueAt(fila, 3).toString());

                lblRegistroSeleccionado.setText(
                        "Editando: " + idAsistenciaSeleccionada
                        + " (" + estadoSeleccionado + ")");

                btnJustificar.setEnabled(true);
                btnGuardarObservacion.setEnabled(true);
            }
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

            boolean ok = controlador.justificarAsistencia(
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

        // GUARDAR SOLO OBSERVACIÓN (sin cambiar el estado, ej. anotar una anomalía)
        btnGuardarObservacion.addActionListener(e -> {
            if (idAsistenciaSeleccionada == null) return;

            boolean ok = controlador.actualizarObservacionAsistencia(
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

        btnCerrar.addActionListener(e -> dispose());
    }

    // ── CARGA ASISTENCIAS ─────────────────────────
    private void cargarAsistencias() {
        modeloTabla.setRowCount(0);

        controlador.buscarAsistenciasDeAlumno(codigoAlumno)
                .forEach(a -> modeloTabla.addRow(new Object[]{
                    a.getIdAsistencia(),
                    a.getFechaFormateada(),
                    a.getNombreEstado(),
                    a.getObservacion()
                }));
    }

    // ── ACTUALIZAR ESTADÍSTICAS ───────────────────
    private void actualizarEstadisticas() {
        long presencias = controlador.contarPresenciasDeAlumno(codigoAlumno);
        long faltas = controlador.contarFaltasDeAlumno(codigoAlumno);
        double porcentaje = controlador.porcentajeAsistenciaDeAlumno(codigoAlumno);

        lblPresencias.setText("Presencias: " + presencias);
        lblFaltas.setText("Faltas: " + faltas);
        lblPorcentaje.setText(String.format("Asistencia: %.1f%%", porcentaje));

        lblPorcentaje.setForeground(
                porcentaje >= 70 ? Color.GREEN.darker() : Color.RED);
    }

    // ── LIMPIAR SELECCIÓN ─────────────────────────
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