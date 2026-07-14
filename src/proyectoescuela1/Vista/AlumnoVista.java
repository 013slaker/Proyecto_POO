/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Modelo.Alumno;

public class AlumnoVista extends JPanel {

    private AlumnoControlador controlador = new AlumnoControlador();

    // ── CAMPOS DEL FORMULARIO ─────────────────────
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);
    private String[] niveles = {"Primaria", "Secundaria"};
    private JComboBox<String> comboGrado = new JComboBox<>();

    private JComboBox<String> comboNivel = new JComboBox<>(niveles);

    private String[] secciones = {"A", "B", "C"};
    private JComboBox<String> comboSeccion = new JComboBox<>(secciones);

    private final String[] gradosPrimaria = {
        "1°", "2°", "3°", "4°", "5°", "6°"
    };

    private final String[] gradosSecundaria = {
        "1°", "2°", "3°", "4°", "5°"
    };

    // ── BOTONES ───────────────────────────────────
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnBuscar = new JButton("Buscar");

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Nombre", "Apellidos", "DNI",
        "Grado", "Sección", "Nivel", "Estado"
    };
    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {
        // hace que las celdas no sean editables
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private JTable tabla = new JTable(modeloTabla);

    // ── BÚSQUEDA ──────────────────────────────────
    private JTextField txtBuscar = new JTextField(15);

    // Guarda el código del alumno seleccionado para actualizar
    private String codigoSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public AlumnoVista() {
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        initEventos();
        // carga datos guardados al abrir
        actualizarTabla(controlador.listarTodos());
    }

    // ── INTERFAZ GRÁFICA ──────────────────────────
    private void initComponentes() {
        cargarGrados();

        // FORMULARIO
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                "Datos del Alumno"
        ));
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Apellidos:"));
        panelForm.add(txtApellidos);
        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new JLabel("Email:"));
        panelForm.add(txtEmail);
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Dirección:"));
        panelForm.add(txtDireccion);
        panelForm.add(new JLabel("Nivel:"));
        panelForm.add(comboNivel);
        panelForm.add(new JLabel("Grado:"));
        panelForm.add(comboGrado);
        panelForm.add(new JLabel("Sección:"));
        panelForm.add(comboSeccion);

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
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA CON SCROLL
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Lista de Alumnos"
        ));

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {
        comboNivel.addActionListener(e -> cargarGrados());

        // GUARDAR — registra nuevo alumno
        btnGuardar.addActionListener(e -> {

            if (!validarCampos()) {
                return;
            }

            // Verificar DNI repetido
            if (controlador.buscarPorDni(txtDni.getText().trim()) != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ya existe un alumno registrado con ese DNI.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            Alumno alumno = new Alumno(
                    0,
                    txtNombre.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtDni.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtDireccion.getText().trim(),
                    new Date(),
                    comboNivel.getSelectedItem().toString(),
                    comboGrado.getSelectedItem().toString(),
                    comboSeccion.getSelectedItem().toString(),
                    new Date()
            );

            controlador.registrarAlumno(alumno);

            actualizarTabla(controlador.listarTodos());

            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Alumno registrado correctamente.");
        });

        // ELIMINAR — elimina alumno seleccionado en tabla
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un alumno de la tabla.",
                        "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String codigo = (String) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar al alumno " + codigo + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminarAlumno(codigo);
                actualizarTabla(controlador.listarTodos());
                limpiarCampos();
            }
        });

        // ACTUALIZAR — modifica datos del alumno seleccionado
        btnActualizar.addActionListener(e -> {
            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un alumno de la tabla primero.",
                        "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!validarCampos()) {
                return;
            }

            // busca el alumno original para mantener su código
            Alumno original = controlador
                    .buscarPorCodigo(codigoSeleccionado);
            if (original == null) {
                return;
            }

            // actualiza sus datos
            original.setNivel(
                    (String) comboNivel.getSelectedItem()
            );
            original.setGrado((String) comboGrado.getSelectedItem());
            original.setSeccion(
                    (String) comboSeccion.getSelectedItem()
            );

            controlador.actualizarAlumno(original);
            actualizarTabla(controlador.listarTodos());
            limpiarCampos();
            JOptionPane.showMessageDialog(this,
                    "Alumno actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // LIMPIAR — vacía los campos del formulario
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // BUSCAR — filtra por nombre en la tabla
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                actualizarTabla(controlador.listarTodos());
            } else {
                actualizarTabla(
                        controlador.buscarPorNombre(texto)
                );
            }
        });

        // CLICK EN TABLA — carga datos del alumno en el formulario
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) {
                    return;
                }

                // guarda el código del alumno seleccionado
                codigoSeleccionado = (String) modeloTabla
                        .getValueAt(fila, 0);

                // busca el alumno y carga sus datos en el form
                Alumno a = controlador
                        .buscarPorCodigo(codigoSeleccionado);
                if (a != null) {
                    txtNombre.setText(a.getNombre());
                    txtApellidos.setText(a.getApellidos());
                    txtDni.setText(a.getDni());
                    txtEmail.setText(a.getEmail());
                    txtTelefono.setText(a.getTelefono());
                    txtDireccion.setText(a.getDireccion());
                    comboNivel.setSelectedItem(a.getNivel());
                    comboGrado.setSelectedItem(a.getGrado());
                    comboSeccion.setSelectedItem(a.getSeccion());
                }
            }
        });
    }

    // ── VALIDACIÓN DE CAMPOS ──────────────────────
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
        if (comboGrado.getSelectedItem() == null || comboGrado.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El grado es obligatorio.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    // ── ACTUALIZAR TABLA ──────────────────────────
    private void actualizarTabla(List<Alumno> lista) {
        modeloTabla.setRowCount(0);
        for (Alumno a : lista) {
            modeloTabla.addRow(new Object[]{
                a.getCodigoAlumno(),
                a.getNombre(),
                a.getApellidos(),
                a.getDni(),
                a.getNivel(),
                a.getGrado(),
                a.getSeccion(),
                a.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    // ── LIMPIAR CAMPOS ────────────────────────────
    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        comboNivel.setSelectedIndex(0);
        comboGrado.setSelectedIndex(0);
        comboSeccion.setSelectedIndex(0);
        codigoSeleccionado = null;
        tabla.clearSelection();
    }

    private void cargarGrados() {
        comboGrado.removeAllItems();
        comboGrado.addItem("Seleccione un grado");

        String nivel = comboNivel.getSelectedItem().toString();

        if (nivel.equals("Primaria")) {

            for (String grado : gradosPrimaria) {
                comboGrado.addItem(grado);
            }

        } else {

            for (String grado : gradosSecundaria) {
                comboGrado.addItem(grado);
            }

        }

        comboGrado.setSelectedIndex(0);
    }
}
