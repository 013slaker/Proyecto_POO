/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import proyectoescuela1.Controlador.PeriodoControlador;
import proyectoescuela1.Modelo.AnioEscolar;
import proyectoescuela1.Modelo.Bimestre;

/**
 * Vista del módulo Administrativo -> Período Académico.
 *
 * Aquí (y solo aquí) se configura el año escolar, las
 * fechas de cada bimestre y cuál está activo. Notas y
 * Asistencia solo LEEN esta configuración; no la modifican.
 */
public class PeriodoVista extends JPanel {

    private PeriodoControlador controlador = new PeriodoControlador();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // -- CREAR AÑO --
    private JTextField txtAnio = new JTextField(6);
    private JButton btnCrearAnio = new JButton("Crear Año Escolar");
    private JLabel lblAnioActual = new JLabel();

    // -- DEFINIR FECHAS DE UN BIMESTRE --
    private String[] numeros = {"1", "2", "3", "4"};
    private JComboBox<String> comboBimestre = new JComboBox<>(numeros);
    private JTextField txtInicio = new JTextField("dd/MM/yyyy", 10);
    private JTextField txtFin    = new JTextField("dd/MM/yyyy", 10);
    private JButton btnGuardarFechas = new JButton("Guardar Fechas");
    private JButton btnActivar = new JButton("Activar este Bimestre");
    private JButton btnCerrar  = new JButton("Cerrar este Bimestre");

    // -- TABLA DE BIMESTRES --
    private String[] columnas = {"Bimestre", "Inicio", "Fin", "Estado"};
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private JTable tabla = new JTable(modeloTabla);

    public PeriodoVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPanelBimestre(), BorderLayout.SOUTH);

        btnCrearAnio.addActionListener(e -> crearAnio());
        btnGuardarFechas.addActionListener(e -> guardarFechas());
        btnActivar.addActionListener(e -> activarBimestre());
        btnCerrar.addActionListener(e -> cerrarBimestre());

        refrescarTabla();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Año (ej. 2026):"));
        panel.add(txtAnio);
        panel.add(btnCrearAnio);
        panel.add(lblAnioActual);
        return panel;
    }

    private JPanel crearPanelBimestre() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Bimestre:"));
        panel.add(comboBimestre);
        panel.add(new JLabel("Inicio:"));
        panel.add(txtInicio);
        panel.add(new JLabel("Fin:"));
        panel.add(txtFin);
        panel.add(btnGuardarFechas);
        panel.add(btnActivar);
        panel.add(btnCerrar);
        return panel;
    }

    // -- ACCIONES --

    private void crearAnio() {
        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            controlador.crearAnioEscolar(anio);
            refrescarTabla();
            JOptionPane.showMessageDialog(this,
                "Año escolar " + anio + " creado. " +
                "Ahora define las fechas de cada bimestre.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Ingresa un año válido, ej. 2026.");
        }
    }

    private void guardarFechas() {
        try {
            int numero = Integer.parseInt(
                    (String) comboBimestre.getSelectedItem());
            Date inicio = sdf.parse(txtInicio.getText().trim());
            Date fin    = sdf.parse(txtFin.getText().trim());
            controlador.definirFechasBimestre(numero, inicio, fin);
            refrescarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Revisa el formato de fecha (dd/MM/yyyy) y que " +
                "exista un Año Escolar creado.\n" + ex.getMessage());
        }
    }

    private void activarBimestre() {
        try {
            int numero = Integer.parseInt(
                    (String) comboBimestre.getSelectedItem());
            // Al activar, el controlador cierra automáticamente
            // cualquier otro bimestre que estuviera activo:
            // nunca hay dos bimestres activos a la vez.
            controlador.activarBimestre(numero);
            refrescarTabla();
            JOptionPane.showMessageDialog(this,
                "Bimestre " + numero + " activado. Los demás " +
                "quedan bloqueados para registro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void cerrarBimestre() {
        int numero = Integer.parseInt(
                (String) comboBimestre.getSelectedItem());
        controlador.cerrarBimestre(numero);
        refrescarTabla();
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        AnioEscolar anio = controlador.getAnioEscolar();
        if (anio == null) {
            lblAnioActual.setText("(sin año escolar configurado)");
            return;
        }
        lblAnioActual.setText("Año escolar activo: " + anio.getAnio());
        for (Bimestre b : anio.getBimestres()) {
            modeloTabla.addRow(new Object[]{
                "Bimestre " + b.getNumero(),
                b.getFechaInicio() == null ? "-" : sdf.format(b.getFechaInicio()),
                b.getFechaFin() == null ? "-" : sdf.format(b.getFechaFin()),
                b.getEstado()
            });
        }
    }
}
