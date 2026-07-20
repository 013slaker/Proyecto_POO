/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import proyectoescuela1.Controlador.MatriculaControlador;
import proyectoescuela1.Modelo.Matricula;

/**
 * ====================================================================
 * DIÁLOGO — HISTORIAL DE MATRÍCULAS DE UN ALUMNO
 * --------------------------------------------------------------------
 * Muestra TODAS las matrículas de un alumno a lo largo de los años
 * (Matriculado / Retirado / Trasladado, con su motivo si lo tiene),
 * sin mezclarse con la tabla principal de MatriculaVista (que muestra
 * las matrículas de TODOS los alumnos). Solo consulta — no edita nada.
 * ====================================================================
 */
public class HistorialMatriculaDialog extends JDialog {

    private final String[] columnas = {
        "Código", "Año", "Nivel", "Grado", "Sección",
        "Fecha matrícula", "Estado", "Motivo retiro"
    };
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
    private final JTable tabla = new JTable(modeloTabla);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public HistorialMatriculaDialog(Window owner,
                                     MatriculaControlador matriculaCtrl,
                                     String codigoAlumno,
                                     String nombreAlumno) {

        super(owner, "Historial de matrículas — " + nombreAlumno,
                ModalityType.APPLICATION_MODAL);

        setSize(700, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Todas las matrículas de este alumno"));
        add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnCerrar);
        add(panelInferior, BorderLayout.SOUTH);

        List<Matricula> historial = matriculaCtrl.buscarPorAlumno(codigoAlumno);
        for (Matricula m : historial) {
            modeloTabla.addRow(new Object[]{
                m.getCodigoMatricula(),
                m.getAnio(),
                m.getNivel(),
                m.getGrado(),
                m.getSeccion(),
                sdf.format(m.getFechaMatricula()),
                m.getEstado(),
                m.getMotivoRetiro() != null ? m.getMotivoRetiro() : "-"
            });
        }

        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Este alumno todavía no tiene matrículas registradas.",
                    "Sin historial", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
