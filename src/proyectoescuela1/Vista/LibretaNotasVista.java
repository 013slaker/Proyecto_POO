/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.NotaControlador;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.LibretaNotas;
import proyectoescuela1.Modelo.Nota;

/**
 * Vista del reporte "Libreta de Notas".
 * esta pantalla es de
 * SOLO LECTURA: consolida, por alumno y bimestre, todas
 * las notas por curso y el promedio ya calculado.
 */
public class LibretaNotasVista extends JPanel {

    // -- CONTROLADORES --
    private NotaControlador   notaControlador   = new NotaControlador();
    private AlumnoControlador alumnoControlador = new AlumnoControlador();

    // -- SELECTOR DE ALUMNO Y BIMESTRE --
    private JTextField txtBuscarAlumno = new JTextField(15);
    private JButton    btnBuscarAlumno = new JButton("Buscar Alumno");
    private JLabel      lblAlumno      =
            new JLabel("Alumno: (ninguno seleccionado)");

    private String[] bimestres = {"1", "2", "3", "4"};
    private JComboBox<String> comboBimestre = new JComboBox<>(bimestres);
    private JButton btnGenerar = new JButton("Generar Libreta");

    private Alumno alumnoSeleccionado;

    // -- TABLA --
    private String[] columnas = {
        "Curso", "Práctica 1", "Práctica 2", "Práctica 3",
        "Participación", "Examen", "Promedio", "Logro"
    };
    private DefaultTableModel modeloTabla =
            new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false; // toda la libreta es solo lectura
        }
    };
    private JTable tabla = new JTable(modeloTabla);

    private JLabel lblPromedioGeneral = new JLabel("Promedio general: -");

    // -- CONSTRUCTOR --
    public LibretaNotasVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(lblPromedioGeneral, BorderLayout.SOUTH);

        btnBuscarAlumno.addActionListener(e -> buscarAlumno());
        btnGenerar.addActionListener(e -> generarLibreta());
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Código o nombre:"));
        panel.add(txtBuscarAlumno);
        panel.add(btnBuscarAlumno);
        panel.add(lblAlumno);
        panel.add(new JLabel("Bimestre:"));
        panel.add(comboBimestre);
        panel.add(btnGenerar);
        return panel;
    }

    // -- BUSCAR ALUMNO --
    private void buscarAlumno() {
        String texto = txtBuscarAlumno.getText().trim();
        if (texto.isEmpty()) return;

        Alumno encontrado = alumnoControlador.buscarPorCodigo(texto);
        if (encontrado == null) {
            List<Alumno> lista = alumnoControlador.buscarPorNombre(texto);
            if (!lista.isEmpty()) {
                encontrado = lista.get(0);
            }
        }

        if (encontrado == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró al alumno.");
            return;
        }

        alumnoSeleccionado = encontrado;
        lblAlumno.setText("Alumno: " + encontrado.getNombreCompleto() +
                " (" + encontrado.getCodigoAlumno() + ")");
    }

    // -- GENERAR LIBRETA --
    /**
     * Arma la libreta consultando NotaControlador. No se
     * ingresa nada manualmente: solo se lee y se calcula.
     */
    private void generarLibreta() {
        if (alumnoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero selecciona un alumno.");
            return;
        }

        int bimestre = Integer.parseInt(
                (String) comboBimestre.getSelectedItem());
        String codigo = alumnoSeleccionado.getCodigoAlumno();

        List<Nota> notasBimestre =
                notaControlador.buscarPorBimestre(codigo, bimestre);

        // cursos distintos con notas registradas en ese bimestre
        Set<String> cursos = new LinkedHashSet<>();
        for (Nota n : notasBimestre) {
            cursos.add(n.getCurso());
        }

        modeloTabla.setRowCount(0);

        for (String curso : cursos) {
            List<Nota> notasCurso = notaControlador
                    .buscarPorCurso(codigo, curso).stream()
                    .filter(n -> n.getBimestre() == bimestre)
                    .collect(java.util.stream.Collectors.toList());

            double promedio = notaControlador
                    .calcularPromedioCurso(codigo, curso, bimestre);

            LibretaNotas fila =
                    new LibretaNotas(curso, bimestre, notasCurso, promedio);

            modeloTabla.addRow(new Object[]{
                fila.getCurso(),
                formatear(fila.getValorPorTipo("Práctica 1")),
                formatear(fila.getValorPorTipo("Práctica 2")),
                formatear(fila.getValorPorTipo("Práctica 3")),
                formatear(fila.getValorPorTipo("Participación")),
                formatear(fila.getValorPorTipo("Examen")),
                String.format("%.1f", fila.getPromedio()),
                fila.getLetra()
            });
        }

        double promedioGeneral =
                notaControlador.calcularPromedioBimestre(codigo, bimestre);
        lblPromedioGeneral.setText(String.format(
                "Promedio general del bimestre %d: %.1f",
                bimestre, promedioGeneral));
    }

    private String formatear(Double valor) {
        return valor == null ? "\u2014" : String.format("%.1f", valor);
    }
}
