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
import proyectoescuela1.Controlador.NotaControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Nota;

/**
 * Vista del módulo Notas.
 * Primero seleccionas el alumno,
 * luego registras su nota.
 */
public class NotaVista extends JPanel {

    // ── CONTROLADORES ─────────────────────────────
    private NotaControlador    controlador =
            new NotaControlador();
    private AlumnoControlador  alumnoCtrl  =
            new AlumnoControlador();

    // ── SELECTOR DE ALUMNO ────────────────────────
    private JTextField txtBuscarAlumno =
            new JTextField(15);
    private JButton    btnBuscarAlumno =
            new JButton("Buscar Alumno");
    private JLabel     lblAlumno       =
            new JLabel("Alumno: (ninguno seleccionado)");

    // ── CAMPOS DE NOTA ────────────────────────────
    private String[] cursos = {
        "Comunicación", "Matemática", "Ciencias",
        "Historia", "Inglés", "Arte",
        "Educación Física", "Computación"
    };
    private JComboBox<String> comboCurso =
            new JComboBox<>(cursos);

    private String[] bimestres = {
        "1", "2", "3", "4"
    };
    private JComboBox<String> comboBimestre =
            new JComboBox<>(bimestres);

    private String[] tipos = {
        "Parcial", "Examen", "Trabajo", "Promedio"
    };
    private JComboBox<String> comboTipo =
            new JComboBox<>(tipos);

    private JTextField txtValor = new JTextField(5);

    // ── BOTONES ───────────────────────────────────
    private JButton btnGuardar  = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar  = new JButton("Limpiar");

    // ── LABELS DE ESTADÍSTICAS ────────────────────
    private JLabel lblPromedio =
            new JLabel("Promedio: -");
    private JLabel lblMaxima   =
            new JLabel("Nota máxima: -");
    private JLabel lblMinima   =
            new JLabel("Nota mínima: -");
    private JLabel lblEstado   =
            new JLabel("Estado: -");

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Curso", "Bimestre",
        "Tipo", "Nota", "Letra"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    // Alumno seleccionado actualmente
    private Alumno alumnoSeleccionado = null;
    // id de nota seleccionada en tabla
    private String idNotaSeleccionada = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public NotaVista() {
        setLayout(new BorderLayout(10, 10));
        btnGuardar.setEnabled(false);
        btnEliminar.setEnabled(false);
        initUI();
        initEventos();
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // PANEL BUSCAR ALUMNO
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

        // FORMULARIO DE NOTA
        JPanel form = new JPanel(
            new GridLayout(4, 2, 5, 5)
        );
        form.setBorder(BorderFactory.createTitledBorder(
            "Datos de la Nota"
        ));
        form.add(new JLabel("Curso:"));
        form.add(comboCurso);
        form.add(new JLabel("Bimestre:"));
        form.add(comboBimestre);
        form.add(new JLabel("Tipo:"));
        form.add(comboTipo);
        form.add(new JLabel("Nota (0-20):"));
        form.add(txtValor);

        // ESTADÍSTICAS
        JPanel panelStats = new JPanel(
            new GridLayout(2, 2, 5, 5)
        );
        panelStats.setBorder(
            BorderFactory.createTitledBorder(
                "Estadísticas"
            )
        );
        panelStats.add(lblPromedio);
        panelStats.add(lblMaxima);
        panelStats.add(lblEstado);
        panelStats.add(lblMinima);

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
        panelSuperior.add(panelStats,   BorderLayout.EAST);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Notas del Alumno"
        ));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll,        BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

        // BUSCAR ALUMNO
        btnBuscarAlumno.addActionListener(e -> {
            String texto = txtBuscarAlumno.getText().trim();
            if (texto.isEmpty()) return;

            // busca por código primero
            Alumno encontrado = alumnoCtrl
                .buscarPorCodigo(texto);

            // si no encuentra por código busca por nombre
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

            // guarda el alumno seleccionado
            alumnoSeleccionado = encontrado;
            lblAlumno.setText("Alumno: " +
                encontrado.getNombreCompleto() +
                " (" + encontrado.getCodigoAlumno() + ")"
            );

            // activa los botones
            btnGuardar.setEnabled(true);

            // carga las notas del alumno
            cargarNotasAlumno();
            actualizarEstadisticas();
        });

        // GUARDAR NOTA
        btnGuardar.addActionListener(e -> {
            if (alumnoSeleccionado == null) return;
            if (!validarCampos()) return;

            Nota nota = new Nota(
                Integer.parseInt(
                    comboBimestre.getSelectedItem()
                                 .toString()
                ),
                Double.parseDouble(txtValor.getText().trim()),
                comboTipo.getSelectedItem().toString(),
                comboCurso.getSelectedItem().toString(),
                alumnoSeleccionado.getCodigoAlumno()
            );

            controlador.registrar(nota);
            cargarNotasAlumno();
            actualizarEstadisticas();
            limpiarNota();
            JOptionPane.showMessageDialog(this,
                "Nota registrada: " + nota.getIdNota(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR NOTA
        btnEliminar.addActionListener(e -> {
            if (idNotaSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona una nota de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar esta nota?",
                "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idNotaSeleccionada);
                cargarNotasAlumno();
                actualizarEstadisticas();
                idNotaSeleccionada = null;
                btnEliminar.setEnabled(false);
            }
        });

        // LIMPIAR
        btnLimpiar.addActionListener(e -> limpiarNota());

        // CLICK EN TABLA
        tabla.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;
                    idNotaSeleccionada = modeloTabla
                        .getValueAt(fila, 0).toString();
                    btnEliminar.setEnabled(true);
                }
            }
        );
    }

    // ── CARGA NOTAS DEL ALUMNO ────────────────────
    /**
     * Carga en la tabla las notas del alumno
     * seleccionado usando Lambda.
     */
    private void cargarNotasAlumno() {
        if (alumnoSeleccionado == null) return;
        modeloTabla.setRowCount(0);

        // Lambda — filtra notas del alumno
        controlador
            .buscarPorAlumno(
                alumnoSeleccionado.getCodigoAlumno()
            )
            .forEach(n -> modeloTabla.addRow(new Object[]{
                n.getIdNota(),
                n.getCurso(),
                "Bimestre " + n.getBimestre(),
                n.getTipo(),
                n.getValor(),
                n.getLetra()
            }));

        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── ACTUALIZAR ESTADÍSTICAS ───────────────────
    /**
     * Actualiza los labels de estadísticas
     * con datos del alumno seleccionado.
     */
    private void actualizarEstadisticas() {
        if (alumnoSeleccionado == null) return;
        String codigo = alumnoSeleccionado
            .getCodigoAlumno();

        double promedio = controlador
            .calcularPromedio(codigo);
        double maxima   = controlador
            .notaMaxima(codigo);
        double minima   = controlador
            .notaMinima(codigo);
        boolean aprobado = controlador
            .estaAprobado(codigo);

        lblPromedio.setText(String.format(
            "Promedio: %.1f", promedio
        ));
        lblMaxima.setText(String.format(
            "Nota máxima: %.1f", maxima
        ));
        lblMinima.setText(String.format(
            "Nota mínima: %.1f", minima
        ));
        lblEstado.setText("Estado: " +
            (aprobado ? "✓ Aprobado" : "✗ Desaprobado")
        );

        // color según estado
        lblEstado.setForeground(
            aprobado ? Color.GREEN.darker() : Color.RED
        );
    }

    // ── VALIDAR CAMPOS ────────────────────────────
    private boolean validarCampos() {
        if (txtValor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingresa el valor de la nota.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        try {
            double valor = Double.parseDouble(
                txtValor.getText().trim()
            );
            if (valor < 0 || valor > 20) {
                JOptionPane.showMessageDialog(this,
                    "La nota debe ser entre 0 y 20.",
                    "Error", JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "La nota debe ser un número.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    // ── LIMPIAR NOTA ──────────────────────────────
    private void limpiarNota() {
        txtValor.setText("");
        comboCurso.setSelectedIndex(0);
        comboBimestre.setSelectedIndex(0);
        comboTipo.setSelectedIndex(0);
        tabla.clearSelection();
        idNotaSeleccionada = null;
        btnEliminar.setEnabled(false);
    }
}
