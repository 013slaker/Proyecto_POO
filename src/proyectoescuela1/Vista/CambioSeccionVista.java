package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;

import proyectoescuela1.Controlador.AlumnoControlador;
import proyectoescuela1.Controlador.CambioSeccionControlador;
import proyectoescuela1.Controlador.MatriculaControlador;
import proyectoescuela1.Controlador.PeriodoControlador;
import proyectoescuela1.Modelo.Alumno;

/**
 * ==========================================================
 * MÓDULO ADMINISTRATIVO -> CAMBIO DE SECCIÓN
 * ----------------------------------------------------------
 * Permite trasladar a uno o varios alumnos de una sección de
 * origen (grado + sección) hacia una sección de destino.
 * ==========================================================
 */
public class CambioSeccionVista extends JPanel {

    //==========================================================
    // CONTROLADORES
    //==========================================================
    private final AlumnoControlador alumnoCtrl = new AlumnoControlador();
    private final MatriculaControlador matriculaCtrl = new MatriculaControlador();
    private final PeriodoControlador periodoCtrl = new PeriodoControlador();
    private final CambioSeccionControlador cambioCtrl =
            new CambioSeccionControlador(alumnoCtrl, matriculaCtrl, periodoCtrl);

    //==========================================================
    // ORIGEN
    //==========================================================
    private final JTextField txtGradoOrigen = new JTextField(8);
    private final JTextField txtSeccionOrigen = new JTextField(8);
    private final JButton btnBuscar = new JButton("Buscar Alumnos");

    //==========================================================
    // LISTA DE ALUMNOS DE LA SECCIÓN DE ORIGEN
    //==========================================================
    private final DefaultListModel<Alumno> modeloLista = new DefaultListModel<>();
    private final JList<Alumno> listaAlumnos = new JList<>(modeloLista);

    //==========================================================
    // DESTINO
    //==========================================================
    private final JTextField txtGradoDestino = new JTextField(8);
    private final JTextField txtSeccionDestino = new JTextField(8);
    private final JButton btnTrasladar = new JButton("Trasladar Seleccionados");

    public CambioSeccionVista() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelOrigen(), BorderLayout.NORTH);
        add(crearPanelLista(), BorderLayout.CENTER);
        add(crearPanelDestino(), BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarAlumnosDeOrigen());
        btnTrasladar.addActionListener(e -> trasladarSeleccionados());

        listaAlumnos.setSelectionMode(
                javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private JPanel crearPanelOrigen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(
                "Sección de Origen"));
        panel.add(new JLabel("Grado:"));
        panel.add(txtGradoOrigen);
        panel.add(new JLabel("Sección:"));
        panel.add(txtSeccionOrigen);
        panel.add(btnBuscar);
        return panel;
    }

    private JScrollPane crearPanelLista() {
        listaAlumnos.setCellRenderer((list, alumno, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(alumno.getCodigoAlumno()
                    + " - " + alumno.getNombreCompleto());
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            return label;
        });
        JScrollPane scroll = new JScrollPane(listaAlumnos);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Alumnos en la sección de origen (selecciona uno o varios)"));
        return scroll;
    }

    private JPanel crearPanelDestino() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campos.setBorder(BorderFactory.createTitledBorder(
                "Sección de Destino"));
        campos.add(new JLabel("Nuevo Grado:"));
        campos.add(txtGradoDestino);
        campos.add(new JLabel("Nueva Sección:"));
        campos.add(txtSeccionDestino);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnTrasladar);

        panel.add(campos);
        panel.add(botones);
        return panel;
    }

    //==========================================================
    // ACCIONES
    //==========================================================
    private void buscarAlumnosDeOrigen() {
        String grado = txtGradoOrigen.getText().trim();
        String seccion = txtSeccionOrigen.getText().trim();

        if (grado.isEmpty() || seccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa el grado y la sección de origen.");
            return;
        }

        modeloLista.clear();
        List<Alumno> alumnos = cambioCtrl.listarAlumnosDeSeccion(grado, seccion);

        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron alumnos activos en "
                    + grado + " \"" + seccion + "\".");
            return;
        }

        for (Alumno a : alumnos) {
            modeloLista.addElement(a);
        }
    }

    private void trasladarSeleccionados() {
        List<Alumno> seleccionados = listaAlumnos.getSelectedValuesList();

        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona al menos un alumno de la lista.");
            return;
        }

        String gradoDestino = txtGradoDestino.getText().trim();
        String seccionDestino = txtSeccionDestino.getText().trim();

        if (gradoDestino.isEmpty() || seccionDestino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa el grado y la sección de destino.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Trasladar " + seleccionados.size() + " alumno(s) a "
                + gradoDestino + " \"" + seccionDestino + "\"?",
                "Confirmar traslado", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int exitosos = cambioCtrl.trasladarAlumnos(
                seleccionados, gradoDestino, seccionDestino);

        JOptionPane.showMessageDialog(this,
                exitosos + " de " + seleccionados.size()
                + " alumno(s) trasladados correctamente.");

        // Refresca la lista de origen (ya no deberían aparecer)
        buscarAlumnosDeOrigen();
    }
}
