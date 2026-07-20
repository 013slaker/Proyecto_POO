/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.DeudaControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Deuda;

/**
 * Vista del submódulo "Deudas": listado general de todas
 * las deudas pendientes del colegio, para labores de
 * cobranza. Las deudas no se crean aquí manualmente: el
 * botón "Actualizar Deudas" solo dispara
 * DeudaControlador.actualizarDeudasAutomaticas(), que
 * revisa fechas de vencimiento vs. pagos registrados.
 */
public class DeudaVista extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DeudaControlador controlador = new DeudaControlador();
    private AlumnoControlador alumnoCtrl = new AlumnoControlador();

    private JButton btnActualizar = new JButton("Actualizar Deudas");
    private JTextField txtFiltro = new JTextField(15);
    private JButton btnFiltrar = new JButton("Filtrar por Alumno");
    private JButton btnVerTodas = new JButton("Ver Todas");
    private JLabel lblTotal = new JLabel("Total pendiente: S/ 0.00");

    private String[] columnas =
            {"Alumno", "Mes/Año", "Monto (S/)", "Vencimiento", "Días de atraso"};
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) { return false; }
            };
    private JTable tabla = new JTable(modeloTabla);

    public DeudaVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initUI();
        cargarTodas();
    }

    private void initUI() {

        JLabel titulo = new JLabel("Deudas — Pensiones Pendientes de Cobro");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnActualizar);
        panelBotones.add(new JLabel("   |   "));
        panelBotones.add(new JLabel("Alumno:"));
        panelBotones.add(txtFiltro);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnVerTodas);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);
        panelSuperior.add(lblTotal, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(tabla);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnActualizar.addActionListener(e -> {
            controlador.actualizarDeudasAutomaticas();
            cargarTodas();
            JOptionPane.showMessageDialog(this,
                    "Deudas actualizadas según fecha de vencimiento.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnFiltrar.addActionListener(e -> {
            String texto = txtFiltro.getText().trim();
            if (texto.isEmpty()) return;

            Alumno encontrado = alumnoCtrl.buscarPorCodigo(texto);
            if (encontrado == null) {
                var lista = alumnoCtrl.buscarPorNombre(texto);
                if (!lista.isEmpty()) encontrado = lista.get(0);
            }
            if (encontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "Alumno no encontrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cargarPorAlumno(encontrado);
        });

        btnVerTodas.addActionListener(e -> cargarTodas());
    }

    private void cargarTodas() {
        modeloTabla.setRowCount(0);
        double total = 0;
        for (Deuda d : controlador.listarPendientes()) {
            Alumno a = alumnoCtrl.buscarPorCodigo(d.getCodigoAlumno());
            String nombre = a != null
                    ? a.getNombreCompleto() + " (" + a.getCodigoAlumno() + ")"
                    : d.getCodigoAlumno();
            agregarFila(nombre, d);
            total += d.getMontoPension();
        }
        lblTotal.setText("Total pendiente: S/ " + String.format("%.2f", total));
    }

    private void cargarPorAlumno(Alumno alumno) {
        modeloTabla.setRowCount(0);
        double total = 0;
        var pendientes = controlador
                .listarPorAlumno(alumno.getCodigoAlumno())
                .stream().filter(d -> !d.isPagada())
                .toList();
        String nombre = alumno.getNombreCompleto()
                + " (" + alumno.getCodigoAlumno() + ")";
        for (Deuda d : pendientes) {
            agregarFila(nombre, d);
            total += d.getMontoPension();
        }
        lblTotal.setText("Total pendiente: S/ " + String.format("%.2f", total));
    }

    private void agregarFila(String nombreAlumno, Deuda d) {
        modeloTabla.addRow(new Object[]{
            nombreAlumno,
            d.getMes() + "/" + d.getAnio(),
            String.format("%.2f", d.getMontoPension()),
            d.getFechaVencimiento().format(FORMATO_FECHA),
            d.getDiasAtraso() + " días"
        });
    }
}
