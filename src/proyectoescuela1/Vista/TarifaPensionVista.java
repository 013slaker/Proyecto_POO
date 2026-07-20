/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import proyectoescuela1.Controlador.TarifaPensionControlador;
import proyectoescuela1.Modelo.TarifaPension;

/**
 * Vista del submódulo "Pensiones": aquí se configura el
 * monto mensual según nivel y grado. Deudas y Pagos usan
 * estos montos automáticamente.
 */
public class TarifaPensionVista extends JPanel {

    private TarifaPensionControlador controlador =
            new TarifaPensionControlador();

    private String[] niveles = {"Primaria", "Secundaria"};
    private String[] gradosPrimaria =
            {"1°", "2°", "3°", "4°", "5°", "6°"};
    private String[] gradosSecundaria =
            {"1°", "2°", "3°", "4°", "5°"};

    private JComboBox<String> comboNivel = new JComboBox<>(niveles);
    private JComboBox<String> comboGrado =
            new JComboBox<>(gradosPrimaria);
    private JTextField txtMonto = new JTextField(8);
    private JButton btnGuardar = new JButton("Guardar Tarifa");

    private String[] columnas = {"Nivel", "Grado", "Monto Mensual (S/)"};
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
    private JTable tabla = new JTable(modeloTabla);

    public TarifaPensionVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initUI();
        cargarTabla();
    }

    private void initUI() {

        JLabel titulo = new JLabel("Pensiones — Tarifa por Nivel y Grado");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder(
                "Configurar monto mensual"));
        form.add(new JLabel("Nivel:"));
        form.add(comboNivel);
        form.add(new JLabel("Grado:"));
        form.add(comboGrado);
        form.add(new JLabel("Monto S/:"));
        form.add(txtMonto);
        form.add(btnGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(form, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Tarifas configuradas"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        comboNivel.addActionListener(e -> {
            comboGrado.setModel(new DefaultComboBoxModel<>(
                    comboNivel.getSelectedItem().equals("Primaria")
                            ? gradosPrimaria : gradosSecundaria));
        });

        btnGuardar.addActionListener(e -> guardar());

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;
                comboNivel.setSelectedItem(
                        modeloTabla.getValueAt(fila, 0));
                comboGrado.setSelectedItem(
                        modeloTabla.getValueAt(fila, 1));
                txtMonto.setText(
                        modeloTabla.getValueAt(fila, 2).toString());
            }
        });
    }

    private void guardar() {
        if (txtMonto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa el monto mensual.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double monto = Double.parseDouble(txtMonto.getText().trim());
            controlador.guardar(
                    comboNivel.getSelectedItem().toString(),
                    comboGrado.getSelectedItem().toString(),
                    monto);
            cargarTabla();
            txtMonto.setText("");
            JOptionPane.showMessageDialog(this,
                    "Tarifa guardada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El monto debe ser un número.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (TarifaPension t : controlador.listar()) {
            modeloTabla.addRow(new Object[]{
                t.getNivel(), t.getGrado(),
                String.format("%.2f", t.getMonto())
            });
        }
    }
}
