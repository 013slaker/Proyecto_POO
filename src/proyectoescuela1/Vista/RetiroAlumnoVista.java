package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.MatriculaControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Matricula;

/**
 * ==========================================================
 * MÓDULO ADMINISTRATIVO -> RETIRO DE ALUMNO
 * ----------------------------------------------------------
 * Permite buscar a un alumno, ver su matrícula vigente,
 * cambiar su estado (Retirado / Trasladado a otro colegio /
 * Suspendido) y registrar el motivo del retiro.
 * ==========================================================
 */
public class RetiroAlumnoVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private final AlumnoControlador alumnoCtrl = new AlumnoControlador();
    private final MatriculaControlador matriculaCtrl = new MatriculaControlador();

    //==========================================================
    // BUSCADOR DE ALUMNO
    //==========================================================
    private final JTextField txtBuscar = new JTextField(20);
    private final JButton btnBuscar = new JButton("Buscar Alumno");

    //==========================================================
    // TABLA DE MATRÍCULAS DEL ALUMNO ENCONTRADO
    //==========================================================
    private final String[] columnas = {
        "Código", "Alumno", "Año", "Grado", "Sección", "Estado"
    };
    private final DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    //==========================================================
    // FORMULARIO DE RETIRO
    //==========================================================
    private final String[] estados = {"Retirado", "Trasladado", "Suspendido"};
    private final JComboBox<String> comboEstado = new JComboBox<>(estados);
    private final JTextArea txtMotivo = new JTextArea(3, 30);
    private final JButton btnConfirmar = new JButton("Confirmar Cambio de Estado");

    private String codigoMatriculaSeleccionada = null;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public RetiroAlumnoVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelBuscar(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarAlumno());
        btnConfirmar.addActionListener(e -> confirmarRetiro());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = tabla.getSelectedRow();
            if (fila == -1) return;
            codigoMatriculaSeleccionada =
                    modeloTabla.getValueAt(fila, 0).toString();
        });
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Alumno"));
        panel.add(new JLabel("Nombre o código:"));
        panel.add(txtBuscar);
        panel.add(btnBuscar);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                "Cambiar Estado / Registrar Motivo"));

        JPanel campos = new JPanel(new GridLayout(2, 2, 5, 5));
        campos.add(new JLabel("Nuevo estado:"));
        campos.add(comboEstado);
        campos.add(new JLabel("Motivo:"));
        campos.add(new JScrollPane(txtMotivo));

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnConfirmar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    //==========================================================
    // ACCIONES
    //==========================================================
    private void buscarAlumno() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un nombre o código.");
            return;
        }

        modeloTabla.setRowCount(0);
        codigoMatriculaSeleccionada = null;

        // Primero intenta por código exacto de alumno
        List<Matricula> matriculas = matriculaCtrl.buscarPorAlumno(texto);

        // Si no hay resultado, intenta por nombre
        if (matriculas.isEmpty()) {
            List<Alumno> encontrados = alumnoCtrl.buscarPorNombre(texto);
            for (Alumno a : encontrados) {
                matriculas.addAll(
                        matriculaCtrl.buscarPorAlumno(a.getCodigoAlumno()));
            }
        }

        if (matriculas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron matrículas para \"" + texto + "\".");
            return;
        }

        for (Matricula m : matriculas) {
            modeloTabla.addRow(new Object[]{
                m.getCodigoMatricula(),
                m.getAlumno().getNombreCompleto(),
                m.getAnio(),
                m.getGrado(),
                m.getSeccion(),
                m.getEstado()
            });
        }
    }

    private void confirmarRetiro() {
        if (codigoMatriculaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una matrícula de la tabla.");
            return;
        }

        String nuevoEstado = (String) comboEstado.getSelectedItem();
        String motivo = txtMotivo.getText().trim();

        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes registrar el motivo del cambio de estado.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Confirmar cambio de estado a \"" + nuevoEstado + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            matriculaCtrl.retirarAlumno(
                    codigoMatriculaSeleccionada, nuevoEstado, motivo);

            JOptionPane.showMessageDialog(this,
                    "Estado actualizado correctamente.");

            txtMotivo.setText("");
            buscarAlumno();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
