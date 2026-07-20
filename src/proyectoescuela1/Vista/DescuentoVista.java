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
import proyectoescuela1.Controlador.DescuentoControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Descuento;

/**
 * Vista del submódulo "Descuentos": becas, hermanos,
 * convenios, etc. Cada descuento es un porcentaje sobre la
 * pensión del alumno seleccionado.
 */
public class DescuentoVista extends JPanel {

    private DescuentoControlador controlador =
            new DescuentoControlador();
    private AlumnoControlador alumnoCtrl = new AlumnoControlador();

    private JTextField txtBuscarAlumno = new JTextField(15);
    private JButton btnBuscarAlumno = new JButton("Buscar Alumno");
    private JLabel lblAlumno =
            new JLabel("Alumno: (ninguno seleccionado)");

    private String[] tipos = {"Beca", "Hermano", "Convenio", "Trabajador", "Otro"};
    private JComboBox<String> comboTipo = new JComboBox<>(tipos);
    private JTextField txtPorcentaje = new JTextField(5);
    private JTextField txtMotivo = new JTextField(20);

    private JButton btnGuardar = new JButton("Agregar Descuento");
    private JButton btnActivar = new JButton("Activar/Desactivar");
    private JButton btnEliminar = new JButton("Eliminar");

    private JLabel lblTotal = new JLabel("Descuento total: 0%");

    private String[] columnas = {"Código", "Tipo", "Porcentaje", "Motivo", "Estado"};
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
    private JTable tabla = new JTable(modeloTabla);

    private Alumno alumnoSeleccionado = null;
    private String idDescuentoSeleccionado = null;

    public DescuentoVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btnGuardar.setEnabled(false);
        btnActivar.setEnabled(false);
        btnEliminar.setEnabled(false);
        initUI();
    }

    private void initUI() {

        JPanel panelAlumno = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAlumno.setBorder(BorderFactory.createTitledBorder(
                "Seleccionar Alumno"));
        panelAlumno.add(new JLabel("Nombre/Código:"));
        panelAlumno.add(txtBuscarAlumno);
        panelAlumno.add(btnBuscarAlumno);
        panelAlumno.add(lblAlumno);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Nuevo Descuento"));
        form.add(new JLabel("Tipo:"));
        form.add(comboTipo);
        form.add(new JLabel("Porcentaje (0-100):"));
        form.add(txtPorcentaje);
        form.add(new JLabel("Motivo:"));
        form.add(txtMotivo);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActivar);
        panelBotones.add(btnEliminar);
        panelBotones.add(lblTotal);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(panelAlumno, BorderLayout.NORTH);
        panelSuperior.add(form, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Descuentos del Alumno"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        initEventos();
    }

    private void initEventos() {

        btnBuscarAlumno.addActionListener(e -> {
            String texto = txtBuscarAlumno.getText().trim();
            if (texto.isEmpty()) return;

            Alumno encontrado = alumnoCtrl.buscarPorCodigo(texto);
            if (encontrado == null) {
                List<Alumno> lista = alumnoCtrl.buscarPorNombre(texto);
                if (!lista.isEmpty()) encontrado = lista.get(0);
            }
            if (encontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "Alumno no encontrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            alumnoSeleccionado = encontrado;
            lblAlumno.setText("Alumno: "
                    + encontrado.getNombreCompleto()
                    + " (" + encontrado.getCodigoAlumno() + ")");
            btnGuardar.setEnabled(true);
            cargarDescuentos();
        });

        btnGuardar.addActionListener(e -> {
            if (alumnoSeleccionado == null) return;
            if (txtPorcentaje.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingresa el porcentaje.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double porcentaje = Double.parseDouble(
                        txtPorcentaje.getText().trim());
                Descuento d = new Descuento(
                        alumnoSeleccionado.getCodigoAlumno(),
                        comboTipo.getSelectedItem().toString(),
                        porcentaje,
                        txtMotivo.getText().trim());
                controlador.registrar(d);
                cargarDescuentos();
                txtPorcentaje.setText("");
                txtMotivo.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El porcentaje debe ser un número.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActivar.addActionListener(e -> {
            if (idDescuentoSeleccionado == null) return;
            Descuento d = controlador.listarPorAlumno(
                    alumnoSeleccionado.getCodigoAlumno())
                    .stream()
                    .filter(x -> x.getIdDescuento()
                            .equals(idDescuentoSeleccionado))
                    .findFirst().orElse(null);
            if (d == null) return;
            if (d.isActivo()) d.desactivar(); else d.activar();
            controlador.actualizar(d);
            cargarDescuentos();
        });

        btnEliminar.addActionListener(e -> {
            if (idDescuentoSeleccionado == null) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este descuento?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idDescuentoSeleccionado);
                idDescuentoSeleccionado = null;
                btnActivar.setEnabled(false);
                btnEliminar.setEnabled(false);
                cargarDescuentos();
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;
                idDescuentoSeleccionado =
                        modeloTabla.getValueAt(fila, 0).toString();
                btnActivar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });
    }

    private void cargarDescuentos() {
        if (alumnoSeleccionado == null) return;
        modeloTabla.setRowCount(0);
        String codigo = alumnoSeleccionado.getCodigoAlumno();

        controlador.listarPorAlumno(codigo).forEach(d ->
                modeloTabla.addRow(new Object[]{
                    d.getIdDescuento(), d.getTipo(),
                    d.getPorcentaje() + "%", d.getMotivo(),
                    d.isActivo() ? "Activo" : "Inactivo"
                }));

        lblTotal.setText("Descuento total: "
                + controlador.obtenerPorcentajeTotal(codigo) + "%");
        idDescuentoSeleccionado = null;
        btnActivar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
