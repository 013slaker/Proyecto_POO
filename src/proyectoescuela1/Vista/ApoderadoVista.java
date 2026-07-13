package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import proyectoescuela1.Controlador.ApoderadoControlador;
import proyectoescuela1.Modelo.Apoderado;

public class ApoderadoVista extends JPanel {

    // ── CONTROLADOR ───────────────────────────────
    private ApoderadoControlador controlador =
            new ApoderadoControlador();

    // ── CAMPOS ────────────────────────────────────
    private JTextField txtNombre    = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni       = new JTextField(20);
    private JTextField txtEmail     = new JTextField(20);
    private JTextField txtTelefono  = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);
    private JTextField txtOcupacion = new JTextField(20);

    private String[] parentescos = {"Padre", "Madre", "Tutor"};
    private JComboBox<String> comboParentesco =
            new JComboBox<>(parentescos);

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
        "Código", "Nombre", "DNI",
        "Parentesco", "Ocupación", "Estado"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    // Código del apoderado seleccionado
    private String codigoSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public ApoderadoVista() {
        setLayout(new BorderLayout(10, 10));
        initUI();
        initEventos();
        actualizarTabla(controlador.listar());
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // FORMULARIO
        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder(
            "Datos del Apoderado"
        ));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Apellidos:"));
        form.add(txtApellidos);
        form.add(new JLabel("DNI:"));
        form.add(txtDni);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelefono);
        form.add(new JLabel("Dirección:"));
        form.add(txtDireccion);
        form.add(new JLabel("Parentesco:"));
        form.add(comboParentesco);
        form.add(new JLabel("Ocupación:"));
        form.add(txtOcupacion);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // BÚSQUEDA
        JPanel panelBuscar = new JPanel(new FlowLayout(
            FlowLayout.LEFT
        ));
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(form,          BorderLayout.CENTER);
        panelSuperior.add(panelBotones,  BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Lista de Apoderados"
        ));

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll,      BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

        // GUARDAR
        btnGuardar.addActionListener(e -> {
            if (!validarCampos()) return;

            Apoderado a = new Apoderado(
                0,
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtDni.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefono.getText().trim(),
                txtDireccion.getText().trim(),
                new Date(),
                comboParentesco.getSelectedItem().toString(),
                txtOcupacion.getText().trim()
            );

            controlador.registrar(a);
            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Apoderado registrado: " + a.getCodigoApoderado(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ACTUALIZAR
        btnActualizar.addActionListener(e -> {
            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un apoderado de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!validarCampos()) return;

            Apoderado original = controlador
                .buscarPorCodigo(codigoSeleccionado);

            if (original == null) return;

            // actualiza atributos de Usuario (padre)
            original.setNombre(txtNombre.getText().trim());
            original.setApellidos(txtApellidos.getText().trim());
            original.setDni(txtDni.getText().trim());
            original.setEmail(txtEmail.getText().trim());
            original.setTelefono(txtTelefono.getText().trim());
            original.setDireccion(txtDireccion.getText().trim());

            // actualiza atributos propios
            original.setParentesco(
                comboParentesco.getSelectedItem().toString()
            );
            original.setOcupacion(txtOcupacion.getText().trim());

            controlador.guardarDatos();
            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Apoderado actualizado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un apoderado de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String codigo = modeloTabla
                .getValueAt(fila, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al apoderado " + codigo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(codigo);
                actualizarTabla(controlador.listar());
                limpiar();
            }
        });

        // LIMPIAR
        btnLimpiar.addActionListener(e -> limpiar());

        // BUSCAR
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
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;

                codigoSeleccionado = modeloTabla
                    .getValueAt(fila, 0).toString();

                Apoderado a = controlador
                    .buscarPorCodigo(codigoSeleccionado);

                if (a != null) {
                    txtNombre.setText(a.getNombre());
                    txtApellidos.setText(a.getApellidos());
                    txtDni.setText(a.getDni());
                    txtEmail.setText(a.getEmail());
                    txtTelefono.setText(a.getTelefono());
                    txtDireccion.setText(a.getDireccion());
                    comboParentesco.setSelectedItem(
                        a.getParentesco()
                    );
                    txtOcupacion.setText(a.getOcupacion());
                }
            }
        });
    }

    // ── VALIDAR CAMPOS ────────────────────────────
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los apellidos son obligatorios.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if (txtDni.getText().trim().length() != 8) {
            JOptionPane.showMessageDialog(this,
                "El DNI debe tener 8 dígitos.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if (txtOcupacion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La ocupación es obligatoria.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    // ── ACTUALIZAR TABLA ──────────────────────────
    private void actualizarTabla(List<Apoderado> lista) {
        modeloTabla.setRowCount(0);
        for (Apoderado a : lista) {
            modeloTabla.addRow(new Object[]{
                a.getCodigoApoderado(),
                a.getNombreCompleto(),
                a.getDni(),
                a.getParentesco(),
                a.getOcupacion(),
                a.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── LIMPIAR CAMPOS ────────────────────────────
    private void limpiar() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        comboParentesco.setSelectedIndex(0);
        txtBuscar.setText("");
        tabla.clearSelection();
        codigoSeleccionado = null;
    }
}