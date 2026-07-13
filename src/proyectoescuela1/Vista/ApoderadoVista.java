package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.ApoderadoControlador;
import proyectoescuela1.Modelo.Apoderado;

public class ApoderadoVista extends JPanel {

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
    private JButton btnGuardar       = new JButton("Guardar");
    private JButton btnActualizar    = new JButton("Actualizar");
    private JButton btnEliminar      = new JButton("Eliminar");
    private JButton btnLimpiar       = new JButton("Limpiar");
    private JButton btnBuscar        = new JButton("Buscar");
    private JButton btnVerAlumnos    = new JButton("Ver Alumnos");
    private JButton btnAsociarAlumno = new JButton("Asociar Alumno");

    // ── BÚSQUEDA ──────────────────────────────────
    private JTextField txtBuscar = new JTextField(15);

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Nombre", "DNI",
        "Parentesco", "Ocupación", "Alumnos", "Estado"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    private String codigoSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public ApoderadoVista() {
        setLayout(new BorderLayout(10, 10));

        // botones inactivos al inicio
        btnVerAlumnos.setEnabled(false);
        btnAsociarAlumno.setEnabled(false);

        initUI();
        initEventos();
        actualizarTabla(controlador.listar());
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

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

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAsociarAlumno);
        panelBotones.add(btnVerAlumnos);

        JPanel panelBuscar = new JPanel(new FlowLayout(
            FlowLayout.LEFT
        ));
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(form,         BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Lista de Apoderados"
        ));

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
                "Apoderado registrado: " +
                a.getCodigoApoderado(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ACTUALIZAR
        btnActualizar.addActionListener(e -> {
            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un apoderado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!validarCampos()) return;

            Apoderado original = controlador
                .buscarPorCodigo(codigoSeleccionado);
            if (original == null) return;

            original.setNombre(txtNombre.getText().trim());
            original.setApellidos(txtApellidos.getText().trim());
            original.setDni(txtDni.getText().trim());
            original.setEmail(txtEmail.getText().trim());
            original.setTelefono(txtTelefono.getText().trim());
            original.setDireccion(txtDireccion.getText().trim());
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
                    "Selecciona un apoderado.",
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

        // CLICK EN TABLA
        tabla.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;

                    codigoSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();

                    // activa botones al seleccionar
                    btnVerAlumnos.setEnabled(true);
                    btnAsociarAlumno.setEnabled(true);

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
            }
        );

        // VER ALUMNOS
        btnVerAlumnos.addActionListener(e -> {
            Apoderado a = controlador
                .buscarPorCodigo(codigoSeleccionado);
            if (a == null) return;

            if (a.getAlumnos().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Este apoderado no tiene alumnos.",
                    "Sin alumnos",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            Frame padre = (Frame) SwingUtilities
                .getWindowAncestor(this);
            mostrarAlumnosDialog(padre, a);
        });

        // ASOCIAR ALUMNO
        btnAsociarAlumno.addActionListener(e -> {
            Apoderado a = controlador
                .buscarPorCodigo(codigoSeleccionado);
            if (a == null) return;

            Frame padre = (Frame) SwingUtilities
                .getWindowAncestor(this);
            mostrarAsociarAlumnoDialog(padre, a);
        });
    }

    // ── DIALOG VER ALUMNOS ────────────────────────
    private void mostrarAlumnosDialog(
            Frame padre, Apoderado apoderado) {

        JDialog dialog = new JDialog(
            padre,
            "Alumnos de " + apoderado.getNombreCompleto(),
            true
        );
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(padre);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {
            "Código", "Nombre", "Grado", "Nivel", "Estado"
        };
        DefaultTableModel modeloAlumnos =
            new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
        JTable tablaAlumnos = new JTable(modeloAlumnos);

        // Iterator — recorre la lista de alumnos
        java.util.Iterator<proyectoescuela1.Modelo.Alumno>
            it = apoderado.iteratorAlumnos();
        while (it.hasNext()) {
            proyectoescuela1.Modelo.Alumno al = it.next();
            modeloAlumnos.addRow(new Object[]{
                al.getCodigoAlumno(),
                al.getNombreCompleto(),
                al.getGrado(),
                al.getNivel(),
                al.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }

        JLabel lblTotal = new JLabel(
            "Total: " + apoderado.getAlumnos().size()
            + " alumno(s)",
            SwingConstants.LEFT
        );
        lblTotal.setBorder(BorderFactory.createEmptyBorder(
            5, 10, 5, 10
        ));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel panelInferior = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );
        panelInferior.add(btnCerrar);

        dialog.add(lblTotal, BorderLayout.NORTH);
        dialog.add(new JScrollPane(tablaAlumnos),
            BorderLayout.CENTER);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── DIALOG ASOCIAR ALUMNO ─────────────────────
    private void mostrarAsociarAlumnoDialog(
            Frame padre, Apoderado apoderado) {

        JDialog dialog = new JDialog(
            padre,
            "Asociar Alumno a " +
            apoderado.getNombreCompleto(),
            true
        );
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(padre);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelBuscar = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );
        JTextField txtBuscarAlumno = new JTextField(20);
        JButton btnBuscarAlumno    = new JButton("Buscar");
        panelBuscar.setBorder(
            BorderFactory.createTitledBorder("Buscar Alumno")
        );
        panelBuscar.add(new JLabel("Nombre o código:"));
        panelBuscar.add(txtBuscarAlumno);
        panelBuscar.add(btnBuscarAlumno);

        String[] cols = {
            "Código", "Nombre", "Grado", "Nivel"
        };
        DefaultTableModel modeloAlumnos =
            new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
        JTable tablaAlumnos = new JTable(modeloAlumnos);

        AlumnoControlador alumnoControlador =
            new AlumnoControlador();

        // Lambda — carga todos los alumnos
        alumnoControlador.listarTodos().forEach(al ->
            modeloAlumnos.addRow(new Object[]{
                al.getCodigoAlumno(),
                al.getNombreCompleto(),
                al.getGrado(),
                al.getNivel()
            })
        );

        JButton btnAsociar = new JButton("Asociar");
        JButton btnCerrar  = new JButton("Cerrar");
        btnAsociar.setEnabled(false);

        // activa btnAsociar al seleccionar
        tablaAlumnos.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    btnAsociar.setEnabled(
                        tablaAlumnos.getSelectedRow() != -1
                    );
                }
            }
        );

        // BUSCAR — Lambda
        btnBuscarAlumno.addActionListener(e -> {
            String texto = txtBuscarAlumno.getText().trim();
            modeloAlumnos.setRowCount(0);
            alumnoControlador.listarTodos().stream()
                .filter(al -> al.getNombreCompleto()
                        .toLowerCase()
                        .contains(texto.toLowerCase()) ||
                        al.getCodigoAlumno()
                        .toLowerCase()
                        .contains(texto.toLowerCase()))
                .forEach(al -> modeloAlumnos.addRow(
                    new Object[]{
                        al.getCodigoAlumno(),
                        al.getNombreCompleto(),
                        al.getGrado(),
                        al.getNivel()
                    })
                );
        });

        // ASOCIAR
        btnAsociar.addActionListener(e -> {
            int fila = tablaAlumnos.getSelectedRow();
            if (fila == -1) return;

            String codigoAlumno = modeloAlumnos
                .getValueAt(fila, 0).toString();

            proyectoescuela1.Modelo.Alumno alumno =
                alumnoControlador
                .buscarPorCodigo(codigoAlumno);

            if (alumno == null) return;

            // verifica duplicado — Lambda
            boolean yaAsociado = apoderado.getAlumnos()
                .stream()
                .anyMatch(a -> a.getCodigoAlumno()
                               .equals(codigoAlumno));

            if (yaAsociado) {
                JOptionPane.showMessageDialog(dialog,
                    "Este alumno ya está asociado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            apoderado.agregarAlumno(alumno);
            controlador.guardarDatos();
            actualizarTabla(controlador.listar());

            JOptionPane.showMessageDialog(dialog,
                "Alumno asociado correctamente.\n" +
                "Total: " + apoderado.getAlumnos().size()
                + " alumno(s)",
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose();
        });

        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );
        panelBotones.add(btnAsociar);
        panelBotones.add(btnCerrar);

        dialog.add(panelBuscar, BorderLayout.NORTH);
        dialog.add(new JScrollPane(tablaAlumnos),
            BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── VALIDAR ───────────────────────────────────
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los apellidos son obligatorios.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtDni.getText().trim().length() != 8) {
            JOptionPane.showMessageDialog(this,
                "El DNI debe tener 8 dígitos.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtOcupacion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La ocupación es obligatoria.",
                "Error", JOptionPane.ERROR_MESSAGE);
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
                a.getAlumnos().size(), // cantidad de alumnos
                a.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── LIMPIAR ───────────────────────────────────
    private void limpiar() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtBuscar.setText("");
        comboParentesco.setSelectedIndex(0);
        tabla.clearSelection();
        codigoSeleccionado = null;
        // desactiva botones al limpiar
        btnVerAlumnos.setEnabled(false);
        btnAsociarAlumno.setEnabled(false);
    }
}