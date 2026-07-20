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
 * origen (nivel + grado + sección) hacia una sección de
 * destino.
 *
 * Nivel/Grado/Sección son combos en cascada (no texto libre)
 * tanto para origen como para destino: así se evitan errores
 * de tipeo (ej. escribir "3" en vez de "3°", que antes hacía
 * que la búsqueda no encontrara a nadie sin avisar por qué) y
 * se evita mezclar un "1°" de Primaria con un "1°" de
 * Secundaria, que antes no se distinguían.
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
    // GRADOS POR NIVEL (igual que en AlumnoVista/MatriculaVista)
    //==========================================================
    private final String[] niveles = {"Primaria", "Secundaria"};
    private final String[] gradosPrimaria = {"1°", "2°", "3°", "4°", "5°", "6°"};
    private final String[] gradosSecundaria = {"1°", "2°", "3°", "4°", "5°"};
    private final String[] secciones = {"A", "B", "C"};

    //==========================================================
    // ORIGEN
    //==========================================================
    private final JComboBox<String> comboNivelOrigen = new JComboBox<>(niveles);
    private final JComboBox<String> comboGradoOrigen = new JComboBox<>();
    private final JComboBox<String> comboSeccionOrigen = new JComboBox<>(secciones);
    private final JButton btnBuscar = new JButton("Buscar Alumnos");

    //==========================================================
    // LISTA DE ALUMNOS DE LA SECCIÓN DE ORIGEN
    //==========================================================
    private final DefaultListModel<Alumno> modeloLista = new DefaultListModel<>();
    private final JList<Alumno> listaAlumnos = new JList<>(modeloLista);

    //==========================================================
    // DESTINO
    //==========================================================
    private final JComboBox<String> comboNivelDestino = new JComboBox<>(niveles);
    private final JComboBox<String> comboGradoDestino = new JComboBox<>();
    private final JComboBox<String> comboSeccionDestino = new JComboBox<>(secciones);
    private final JButton btnTrasladar = new JButton("Trasladar Seleccionados");

    //==========================================================
    // VOLVER
    //==========================================================
    private final JButton btnVolver = new JButton("Volver al menú");
    private Runnable onVolver = () -> {};

    public CambioSeccionVista() {
        this(null);
    }

    /** @param onVolver acción para regresar al menú anterior */
    public CambioSeccionVista(Runnable onVolver) {
        if (onVolver != null) {
            this.onVolver = onVolver;
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cargarGrados(comboNivelOrigen, comboGradoOrigen);
        cargarGrados(comboNivelDestino, comboGradoDestino);

        add(crearPanelOrigen(), BorderLayout.NORTH);
        add(crearPanelLista(), BorderLayout.CENTER);
        add(crearPanelDestino(), BorderLayout.SOUTH);

        comboNivelOrigen.addActionListener(e ->
                cargarGrados(comboNivelOrigen, comboGradoOrigen));
        comboNivelDestino.addActionListener(e ->
                cargarGrados(comboNivelDestino, comboGradoDestino));

        btnBuscar.addActionListener(e -> buscarAlumnosDeOrigen());
        btnTrasladar.addActionListener(e -> trasladarSeleccionados());
        btnVolver.addActionListener(e -> onVolver.run());

        listaAlumnos.setSelectionMode(
                javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private JPanel crearPanelOrigen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Sección de Origen"));
        panel.add(new JLabel("Nivel:"));
        panel.add(comboNivelOrigen);
        panel.add(new JLabel("Grado:"));
        panel.add(comboGradoOrigen);
        panel.add(new JLabel("Sección:"));
        panel.add(comboSeccionOrigen);
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
        campos.setBorder(BorderFactory.createTitledBorder("Sección de Destino"));
        campos.add(new JLabel("Nuevo Nivel:"));
        campos.add(comboNivelDestino);
        campos.add(new JLabel("Nuevo Grado:"));
        campos.add(comboGradoDestino);
        campos.add(new JLabel("Nueva Sección:"));
        campos.add(comboSeccionDestino);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnTrasladar);
        botones.add(btnVolver);

        panel.add(campos);
        panel.add(botones);
        return panel;
    }

    //==========================================================
    // CARGA LOS GRADOS DE UN COMBO SEGÚN EL NIVEL ELEGIDO
    //==========================================================
    private void cargarGrados(JComboBox<String> comboNivel,
            JComboBox<String> comboGrado) {

        comboGrado.removeAllItems();
        comboGrado.addItem("Seleccione un grado");

        String[] grados = "Primaria".equals(comboNivel.getSelectedItem())
                ? gradosPrimaria : gradosSecundaria;

        for (String grado : grados) {
            comboGrado.addItem(grado);
        }
    }

    //==========================================================
    // ACCIONES
    //==========================================================
    private void buscarAlumnosDeOrigen() {

        if (comboGradoOrigen.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione el grado de origen.");
            return;
        }

        String nivel = (String) comboNivelOrigen.getSelectedItem();
        String grado = (String) comboGradoOrigen.getSelectedItem();
        String seccion = (String) comboSeccionOrigen.getSelectedItem();

        modeloLista.clear();
        List<Alumno> alumnos = cambioCtrl.listarAlumnosDeSeccion(nivel, grado, seccion);

        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron alumnos activos en "
                    + nivel + " " + grado + " \"" + seccion + "\".");
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

        if (comboGradoDestino.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione el grado de destino.");
            return;
        }

        String nivelDestino = (String) comboNivelDestino.getSelectedItem();
        String gradoDestino = (String) comboGradoDestino.getSelectedItem();
        String seccionDestino = (String) comboSeccionDestino.getSelectedItem();

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Trasladar " + seleccionados.size() + " alumno(s) a "
                + nivelDestino + " " + gradoDestino + " \"" + seccionDestino + "\"?",
                "Confirmar traslado", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int exitosos = cambioCtrl.trasladarAlumnos(
                seleccionados, nivelDestino, gradoDestino, seccionDestino);

        JOptionPane.showMessageDialog(this,
                exitosos + " de " + seleccionados.size()
                + " alumno(s) trasladados correctamente.");

        // Refresca la lista de origen (ya no deberían aparecer)
        buscarAlumnosDeOrigen();
    }
}
