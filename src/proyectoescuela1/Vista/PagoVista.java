/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.DeudaControlador;
import proyectoescuela1.Controlador.PagoControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Deuda;
import proyectoescuela1.Modelo.Pago;

/**
 * Vista del submódulo "Pagos". Al abrir un alumno se
 * actualizan primero sus deudas (por si venció algún mes
 * nuevo) y se muestran las pendientes; al registrar un pago
 * la deuda de ese mes queda marcada como pagada
 * automáticamente (ver PagoControlador).
 */
public class PagoVista extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PagoControlador pagoControlador = new PagoControlador();
    private DeudaControlador deudaControlador = new DeudaControlador();
    private AlumnoControlador alumnoCtrl = new AlumnoControlador();

    private JTextField txtBuscarAlumno = new JTextField(15);
    private JButton btnBuscarAlumno = new JButton("Buscar Alumno");
    private JLabel lblAlumno =
            new JLabel("Alumno: (ninguno seleccionado)");

    private JLabel lblMesSeleccionado =
            new JLabel("Selecciona una deuda pendiente de la tabla ↓");
    private JTextField txtMonto = new JTextField(8);
    private String[] metodos = {"Efectivo", "Transferencia", "Tarjeta", "Yape/Plin"};
    private JComboBox<String> comboMetodo = new JComboBox<>(metodos);
    private JButton btnRegistrarPago = new JButton("Registrar Pago");

    private String[] columnasDeudas =
            {"Mes/Año", "Monto (S/)", "Vencimiento", "Días de atraso"};
    private DefaultTableModel modeloDeudas =
            new DefaultTableModel(columnasDeudas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) { return false; }
            };
    private JTable tablaDeudas = new JTable(modeloDeudas);

    private String[] columnasPagos =
            {"Código", "Mes/Año", "Monto Pagado", "Fecha", "Método"};
    private DefaultTableModel modeloPagos =
            new DefaultTableModel(columnasPagos, 0) {
                @Override
                public boolean isCellEditable(int f, int c) { return false; }
            };
    private JTable tablaPagos = new JTable(modeloPagos);

    private Alumno alumnoSeleccionado = null;
    private Deuda deudaSeleccionada = null;

    public PagoVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btnRegistrarPago.setEnabled(false);
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

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Registrar Pago"));
        form.add(lblMesSeleccionado);
        form.add(new JLabel("Monto S/:"));
        form.add(txtMonto);
        form.add(new JLabel("Método:"));
        form.add(comboMetodo);
        form.add(btnRegistrarPago);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(panelAlumno, BorderLayout.NORTH);
        panelSuperior.add(form, BorderLayout.CENTER);

        JScrollPane scrollDeudas = new JScrollPane(tablaDeudas);
        scrollDeudas.setBorder(BorderFactory.createTitledBorder(
                "Meses Pendientes de Pago"));

        JScrollPane scrollPagos = new JScrollPane(tablaPagos);
        scrollPagos.setBorder(BorderFactory.createTitledBorder(
                "Historial de Pagos"));

        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 10, 10));
        panelTablas.add(scrollDeudas);
        panelTablas.add(scrollPagos);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelTablas, BorderLayout.CENTER);

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

            // actualiza deudas por si venció un mes nuevo
            deudaControlador.actualizarDeudasAutomaticas();
            cargarDeudas();
            cargarPagos();
        });

        tablaDeudas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaDeudas.getSelectedRow();
                if (fila == -1 || alumnoSeleccionado == null) return;

                List<Deuda> pendientes = deudaControlador
                        .listarPorAlumno(alumnoSeleccionado.getCodigoAlumno())
                        .stream().filter(d -> !d.isPagada())
                        .collect(java.util.stream.Collectors.toList());

                deudaSeleccionada = pendientes.get(fila);
                lblMesSeleccionado.setText("Pagando: "
                        + deudaSeleccionada.getMes() + "/"
                        + deudaSeleccionada.getAnio());
                txtMonto.setText(String.format("%.2f",
                        deudaSeleccionada.getMontoPension()));
                btnRegistrarPago.setEnabled(true);
            }
        });

        btnRegistrarPago.addActionListener(e -> {
            if (alumnoSeleccionado == null || deudaSeleccionada == null) return;
            if (txtMonto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingresa el monto pagado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double monto = Double.parseDouble(txtMonto.getText().trim());
                Pago pago = new Pago(
                        alumnoSeleccionado.getCodigoAlumno(),
                        deudaSeleccionada.getAnio(),
                        deudaSeleccionada.getMes(),
                        monto,
                        comboMetodo.getSelectedItem().toString());
                pagoControlador.registrar(pago);

                JOptionPane.showMessageDialog(this,
                        "Pago registrado: " + pago.getIdPago(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                deudaSeleccionada = null;
                btnRegistrarPago.setEnabled(false);
                txtMonto.setText("");
                lblMesSeleccionado.setText(
                        "Selecciona una deuda pendiente de la tabla ↓");
                cargarDeudas();
                cargarPagos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El monto debe ser un número.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarDeudas() {
        modeloDeudas.setRowCount(0);
        deudaControlador.listarPorAlumno(alumnoSeleccionado.getCodigoAlumno())
                .stream()
                .filter(d -> !d.isPagada())
                .forEach(d -> modeloDeudas.addRow(new Object[]{
                    d.getMes() + "/" + d.getAnio(),
                    String.format("%.2f", d.getMontoPension()),
                    d.getFechaVencimiento().format(FORMATO_FECHA),
                    d.getDiasAtraso() + " días"
                }));
    }

    private void cargarPagos() {
        modeloPagos.setRowCount(0);
        pagoControlador.listarPorAlumno(alumnoSeleccionado.getCodigoAlumno())
                .forEach(p -> modeloPagos.addRow(new Object[]{
                    p.getIdPago(),
                    p.getMes() + "/" + p.getAnio(),
                    String.format("%.2f", p.getMontoPagado()),
                    p.getFechaPago().format(FORMATO_FECHA),
                    p.getMetodoPago()
                }));
    }
}
