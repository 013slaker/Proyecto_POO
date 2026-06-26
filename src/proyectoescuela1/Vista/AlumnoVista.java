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
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.*;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Modelo.Alumno;

public class AlumnoVista extends JFrame {

    private AlumnoControlador controlador = new AlumnoControlador();

    //componentes del frame
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);
    private JTextField txtGrado = new JTextField(20);

    private String[] niveles = {"Primaria", "Secundaria"};
    private JComboBox<String> comboNivel = new JComboBox<>(niveles);

    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnBuscar = new JButton("Buscar");

    // Tabla reporte de alumnos
    private String[] columnas = {"Código", "Nombre", "DNI", "Grado", "Nivel", "Estado"};

    private DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    private JTable tabla = new JTable(modeloTabla);

    // búsqueda
    private JTextField txtBuscar = new JTextField(15);

    
    public AlumnoVista() {
        setTitle("Gestión de Alumnos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponentes();
        initEventos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        // ── PANEL FORMULARIO (NORTE) ──────────────
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 5, 5));
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
        panelForm.add(new JLabel("Grado:"));
        panelForm.add(txtGrado);
        panelForm.add(new JLabel("Nivel:"));
        panelForm.add(comboNivel);

        // BOTONES 
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        //  PANEL BÚSQUEDA 
        JPanel panelBuscar = new JPanel(new FlowLayout(
                FlowLayout.LEFT
        ));
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        //  PANEL SUPERIOR 
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        //  TABLA 
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Lista de Alumnos"
        ));

        //  PANEL INFERIOR 
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll, BorderLayout.CENTER);

        //  AGREGAR AL FRAME 
        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    private void initEventos() {

        // ── GUARDAR ───────────────────────────────
        btnGuardar.addActionListener(e -> {
            if (!validarCampos()) {
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
                    txtGrado.getText().trim(),
                    (String) comboNivel.getSelectedItem(),
                    new Date()
            );

            controlador.registrarAlumno(alumno);
            actualizarTabla(controlador.listarTodos());
            limpiarCampos();
            JOptionPane.showMessageDialog(this,
                    "Alumno registrado con código: "
                    + alumno.getCodigoAlumno(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ── ELIMINAR ──────────────────────────────
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un alumno de la tabla.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String codigo = (String) modeloTabla.getValueAt(
                    fila, 0
            );
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar al alumno " + codigo + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminarAlumno(codigo);
                actualizarTabla(controlador.listarTodos());
            }
        });

        // ── LIMPIAR ───────────────────────────────
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // ── BUSCAR ────────────────────────────────
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
    }

    // ── MÉTODOS AUXILIARES ────────────────────────
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
        return true;
    }

    private void actualizarTabla(List<Alumno> lista) {
        modeloTabla.setRowCount(0); // limpiar tabla
        for (Alumno a : lista) {
            modeloTabla.addRow(new Object[]{
                a.getCodigoAlumno(),
                a.getNombreCompleto(),
                a.getDni(),
                a.getNivel(),
                a.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        comboNivel.setSelectedIndex(0);
    }

      // Main temporal para probar
    public static void main(String[] args) {
        new AlumnoVista().setVisible(true);
    }

    
}
