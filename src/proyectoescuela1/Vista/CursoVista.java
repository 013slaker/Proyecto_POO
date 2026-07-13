/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import proyectoescuela1.Controlador.CursoControlador;
import proyectoescuela1.Modelo.Curso;

/**
 * Vista del módulo Cursos.
 * Permite registrar, actualizar, eliminar
 * y buscar cursos.
 */
public class CursoVista extends JPanel {

    // ── CONTROLADOR ───────────────────────────────
    private CursoControlador controlador =
            new CursoControlador();

    // ── CAMPOS DEL FORMULARIO ─────────────────────
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtArea   = new JTextField(20);
    private JTextField txtHoras  = new JTextField(5);

    /** Lista desplegable de niveles */
    private String[] niveles = {
        "Inicial", "Primaria", "Secundaria"
    };
    private JComboBox<String> comboNivel =
            new JComboBox<>(niveles);

    /** Lista desplegable de áreas */
    private String[] areas = {
        "Comunicación", "Matemática", "Ciencias",
        "Historia", "Inglés", "Arte",
        "Educación Física", "Computación", "Religión"
    };
    private JComboBox<String> comboArea =
            new JComboBox<>(areas);

    // ── BOTONES ───────────────────────────────────
    private JButton btnGuardar    = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar   = new JButton("Eliminar");
    private JButton btnLimpiar    = new JButton("Limpiar");
    private JButton btnBuscar     = new JButton("Buscar");

    // ── BÚSQUEDA ──────────────────────────────────
    private JTextField txtBuscar = new JTextField(15);

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Nombre", "Área",
        "Horas/Sem", "Nivel", "Estado"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            /** Evita que el usuario edite la tabla */
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    /** Guarda el id del curso seleccionado */
    private String idSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public CursoVista() {
        setLayout(new BorderLayout(10, 10));

        // botones inactivos hasta seleccionar
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        initUI();
        initEventos();

        // carga datos guardados al abrir
        actualizarTabla(controlador.listar());
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // FORMULARIO
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder(
            "Datos del Curso"
        ));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Área:"));
        form.add(comboArea);
        form.add(new JLabel("Horas semanales:"));
        form.add(txtHoras);
        form.add(new JLabel("Nivel:"));
        form.add(comboNivel);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // BÚSQUEDA
        JPanel panelBuscar = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
            new BorderLayout()
        );
        panelSuperior.add(form,         BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Lista de Cursos"
        ));

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(
            new BorderLayout()
        );
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll,      BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

        // GUARDAR — registra nuevo curso
        btnGuardar.addActionListener(e -> {
            if (!validarCampos()) return;

            Curso curso = new Curso(
                txtNombre.getText().trim(),
                comboArea.getSelectedItem().toString(),
                Integer.parseInt(txtHoras.getText().trim()),
                comboNivel.getSelectedItem().toString()
            );

            controlador.registrar(curso);
            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Curso registrado: " + curso.getIdCurso(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ACTUALIZAR — modifica curso seleccionado
        btnActualizar.addActionListener(e -> {
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un curso de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!validarCampos()) return;

            // busca el curso original
            Curso original = controlador
                .buscarPorId(idSeleccionado);
            if (original == null) return;

            // actualiza sus atributos
            original.setNombre(txtNombre.getText().trim());
            original.setArea(
                comboArea.getSelectedItem().toString()
            );
            original.setHorasSemanales(
                Integer.parseInt(txtHoras.getText().trim())
            );
            original.setNivel(
                comboNivel.getSelectedItem().toString()
            );

            controlador.guardarDatos();
            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Curso actualizado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR — elimina curso seleccionado
        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un curso de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el curso " +
                idSeleccionado + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                actualizarTabla(controlador.listar());
                limpiar();
            }
        });

        // LIMPIAR — vacía el formulario
        btnLimpiar.addActionListener(e -> limpiar());

        // BUSCAR — filtra por nombre
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                actualizarTabla(controlador.listar());
            } else {
                actualizarTabla(
                    controlador.buscarPorNombre(texto)
                );
            }
        });

        // CLICK EN TABLA — carga datos en formulario
        tabla.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;

                    // guarda el id seleccionado
                    idSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();

                    // activa botones
                    btnActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);

                    // busca y carga en formulario
                    Curso c = controlador
                        .buscarPorId(idSeleccionado);
                    if (c != null) {
                        txtNombre.setText(c.getNombre());
                        comboArea.setSelectedItem(c.getArea());
                        txtHoras.setText(String.valueOf(
                            c.getHorasSemanales()
                        ));
                        comboNivel.setSelectedItem(
                            c.getNivel()
                        );
                    }
                }
            }
        );
    }

    // ── VALIDAR CAMPOS ────────────────────────────
    /**
     * Verifica que los campos obligatorios
     * estén completos y sean válidos.
     */
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if (txtHoras.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Las horas semanales son obligatorias.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        // verifica que las horas sean un número válido
        try {
            int horas = Integer.parseInt(
                txtHoras.getText().trim()
            );
            if (horas <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Las horas deben ser mayor a 0.",
                    "Error", JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Las horas deben ser un número entero.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    // ── ACTUALIZAR TABLA ──────────────────────────
    /**
     * Limpia y recarga la tabla con la lista recibida.
     */
    private void actualizarTabla(List<Curso> lista) {
        modeloTabla.setRowCount(0);
        for (Curso c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getIdCurso(),
                c.getNombre(),
                c.getArea(),
                c.getHorasSemanales() + " hrs",
                c.getNivel(),
                c.isActivo() ? "Activo" : "Inactivo"
            });
        }
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── LIMPIAR CAMPOS ────────────────────────────
    /**
     * Vacía todos los campos del formulario
     * y desactiva los botones que requieren
     * selección.
     */
    private void limpiar() {
        txtNombre.setText("");
        txtHoras.setText("");
        txtBuscar.setText("");
        comboArea.setSelectedIndex(0);
        comboNivel.setSelectedIndex(0);
        tabla.clearSelection();
        idSeleccionado = null;
        // desactiva botones hasta nueva selección
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}