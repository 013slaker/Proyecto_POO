/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import proyectoescuela1.Controlador.ApoderadoControlador;
import proyectoescuela1.Modelo.Apoderado;

public class ApoderadoVista extends JPanel {

    private ApoderadoControlador controlador = new ApoderadoControlador();

    // ── CAMPOS ─────────────────────────────
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);
    private JTextField txtOcupacion = new JTextField(20);

    private String[] parentescos = {"Padre", "Madre", "Tutor"};
    private JComboBox<String> comboParentesco = new JComboBox<>(parentescos);

    // ── BOTONES ────────────────────────────
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");

    // ── TABLA ──────────────────────────────
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "DNI", "Parentesco", "Ocupación"}, 0
    );

    private JTable tabla = new JTable(modelo);

    public ApoderadoVista() {
        setLayout(new BorderLayout(10, 10));
        initUI();
        initEventos();
    }

    // ── INTERFAZ ───────────────────────────
    private void initUI() {

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Datos del Apoderado"));

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

        form.add(new JLabel("Parentesco:"));
        form.add(comboParentesco);

        form.add(new JLabel("Ocupación:"));
        form.add(txtOcupacion);

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(tabla), BorderLayout.SOUTH);
    }

    // ── EVENTOS ────────────────────────────
    private void initEventos() {

        btnGuardar.addActionListener(e -> guardar());

        btnEliminar.addActionListener(e -> eliminar());

        btnLimpiar.addActionListener(e -> limpiar());
    }

    // ── GUARDAR ────────────────────────────
    private void guardar() {

       /* Apoderado a = new Apoderado(
                0,
                txtNombre.getText(),
                txtApellidos.getText(),
                txtDni.getText(),
                txtEmail.getText(),
                txtTelefono.getText(),
                txtDireccion.getText(),
                new Date(),
                (String) comboParentesco.getSelectedItem(),
                txtOcupacion.getText()
        );

        controlador.registrar(a);
        actualizar();
        limpiar();
    }

    // ── ELIMINAR ───────────────────────────
    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un apoderado");
            return;
        }

        String codigo = (String) modelo.getValueAt(fila, 0);

        controlador.eliminar(codigo);
        actualizar();
    }

    // ── ACTUALIZAR TABLA ───────────────────
    private void actualizar() {
        modelo.setRowCount(0);

        for (Apoderado a : controlador.listar()) {
            modelo.addRow(new Object[]{
                a.getCodigoApoderado(),
                a.getNombreCompleto(),
                a.getDni(),
                a.getParentesco(),
                a.getOcupacion()
            });
        }
    }

    // ── LIMPIAR ────────────────────────────
    private void limpiar() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        comboParentesco.setSelectedIndex(0);
*/
    }
}
