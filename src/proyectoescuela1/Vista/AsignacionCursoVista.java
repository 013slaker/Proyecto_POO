/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import proyectoescuela1.Controlador.AsignacionCursoControlador;
import proyectoescuela1.Modelo.AsignacionCurso;
import proyectoescuela1.Modelo.Curso;
import proyectoescuela1.Modelo.Docente;
import proyectoescuela1.Modelo.Horario;

/**
 * Vista del módulo AsignacionCurso. Permite crear asignaciones para primaria y
 * secundaria con sus respectivas reglas.
 */
public class AsignacionCursoVista extends JPanel {

    // ── CONTROLADOR ───────────────────────────────
    private AsignacionCursoControlador controlador
            = new AsignacionCursoControlador();

    // ── CAMPOS PRINCIPALES ────────────────────────
    /**
     * Grados disponibles
     */
    private String[] grados = {
        "1°", "2°", "3°", "4°", "5°", "6°"
    };
    private JComboBox<String> comboGrado
            = new JComboBox<>(grados);

    /**
     * Secciones disponibles
     */
    private String[] secciones = {
        "A", "B", "C", "D", "E"
    };
    private JComboBox<String> comboSeccion
            = new JComboBox<>(secciones);

    /**
     * Niveles
     */
    private String[] niveles = {
        "Primaria", "Secundaria"
    };
    private JComboBox<String> comboNivel
            = new JComboBox<>(niveles);

    // ── BOTONES PRINCIPALES ───────────────────────
    private JButton btnCrear = new JButton("Crear");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnDetalle
            = new JButton("Ver Detalle");

    // ── TABLA PRINCIPAL ───────────────────────────
    private String[] columnas = {
        "Código", "Sección", "Nivel",
        "Titular", "Cursos asignados", "¿Válida?"
    };
    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int f, int c) {
            return false;
        }
    };
    private JTable tabla = new JTable(modeloTabla);

    /**
     * Id de asignación seleccionada
     */
    private String idSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public AsignacionCursoVista() {
        setLayout(new BorderLayout(10, 10));

        // botones inactivos hasta seleccionar
        btnEliminar.setEnabled(false);
        btnDetalle.setEnabled(false);

        initUI();
        initEventos();
        actualizarTabla(controlador.listar());
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // FORMULARIO CREAR ASIGNACIÓN
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder(
                "Nueva Asignación"
        ));
        form.add(new JLabel("Nivel:"));
        form.add(comboNivel);
        form.add(new JLabel("Grado:"));
        form.add(comboGrado);
        form.add(new JLabel("Sección:"));
        form.add(comboSeccion);

        // BOTONES
        JPanel panelBotones = new JPanel(
                new FlowLayout()
        );
        panelBotones.add(btnCrear);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
                new BorderLayout()
        );
        panelSuperior.add(form, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Lista de Asignaciones"
        ));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

// cambia los grados disponibles
        comboNivel.addActionListener(e -> {
            String nivel = comboNivel
                    .getSelectedItem().toString();
            comboGrado.removeAllItems();

            if (nivel.equals("Primaria")) {
                // primaria: 1° a 6°
                String[] gradosPrimaria = {
                    "1°", "2°", "3°", "4°", "5°", "6°"
                };
                for (String g : gradosPrimaria) {
                    comboGrado.addItem(g);
                }
            } else {
                // secundaria: 1° a 5°
                String[] gradosSecundaria = {
                    "1°", "2°", "3°", "4°", "5°"
                };
                for (String g : gradosSecundaria) {
                    comboGrado.addItem(g);
                }
            }
        });
        // CREAR — crea nueva asignación vacía
        btnCrear.addActionListener(e -> {
            String nivel = comboNivel
                    .getSelectedItem().toString();
            String grado = comboGrado
                    .getSelectedItem().toString();
            String seccion = comboSeccion
                    .getSelectedItem().toString();

            // forma el nombre automáticamente: "3°A"
            String nombreSeccion = grado + seccion;

            // verifica que no exista ya esa sección
            boolean yaExiste = controlador.listar().stream()
                    .anyMatch(a
                            -> a.getNombreSeccion()
                            .equals(nombreSeccion)
                    && a.getNivel().equals(nivel)
                    );

            if (yaExiste) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una asignación para "
                        + nombreSeccion + " (" + nivel + ").",
                        "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            AsignacionCurso asignacion
                    = new AsignacionCurso(nivel, nombreSeccion);

            controlador.registrar(asignacion);
            actualizarTabla(controlador.listar());
            limpiar();

            JOptionPane.showMessageDialog(this,
                    "Asignación creada: "
                    + asignacion.getIdAsignacion()
                    + " → " + nombreSeccion
                    + "\nAbre el detalle para asignar docentes.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == null) {
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Eliminar esta asignación?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                actualizarTabla(controlador.listar());
                limpiar();
            }
        });

        // VER DETALLE — abre dialog de asignación
        btnDetalle.addActionListener(e -> {
            AsignacionCurso a = controlador
                    .buscarPorId(idSeleccionado);
            if (a == null) {
                return;
            }

            Frame padre = (Frame) SwingUtilities
                    .getWindowAncestor(this);
            abrirDetalle(padre, a);
        });

        // LIMPIAR
        btnLimpiar.addActionListener(e -> limpiar());

        // CLICK EN TABLA
        tabla.addMouseListener(
                new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) {
                    return;
                }

                idSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();

                // activa botones
                btnEliminar.setEnabled(true);
                btnDetalle.setEnabled(true);
            }
        }
        );
    }

    // ══════════════════════════════════════════════
    //  DIALOG DE DETALLE
    // ══════════════════════════════════════════════
    /**
     * Abre el dialog de detalle donde se asignan docentes y cursos según el
     * nivel.
     */
    private void abrirDetalle(Frame padre,
            AsignacionCurso a) {

        JDialog dialog = new JDialog(
                padre,
                "Detalle: " + a.getNombreSeccion()
                + " (" + a.getNivel() + ")",
                true
        );
        dialog.setSize(700, 550);
        dialog.setLocationRelativeTo(padre);
        dialog.setLayout(new BorderLayout(10, 10));

        // ── PANEL SEGÚN NIVEL ─────────────────────
        JPanel panelDetalle = new JPanel(
                new BorderLayout(5, 5)
        );

        if (a.getNivel().equals("Primaria")) {
            // panel especial para primaria
            panelDetalle.add(
                    crearPanelPrimaria(a, dialog),
                    BorderLayout.CENTER
            );
        } else {
            // panel para secundaria
            panelDetalle.add(
                    crearPanelSecundaria(a, dialog),
                    BorderLayout.CENTER
            );
        }

        // ── TABLA DE ITEMS ASIGNADOS ──────────────
        String[] colsItems = {
            "Curso", "Docente", "Horario"
        };
        DefaultTableModel modeloItems
                = new DefaultTableModel(colsItems, 0) {
            @Override
            public boolean isCellEditable(
                    int f, int c) {
                return false;
            }
        };
        JTable tablaItems = new JTable(modeloItems);

        // carga items existentes usando Iterator
        cargarItems(a, modeloItems);

        JScrollPane scrollItems
                = new JScrollPane(tablaItems);
        scrollItems.setBorder(
                BorderFactory.createTitledBorder(
                        "Cursos y Docentes Asignados"
                )
        );

        // ── BOTÓN QUITAR ITEM ─────────────────────
        JButton btnQuitarItem
                = new JButton("Quitar Seleccionado");
        btnQuitarItem.addActionListener(e -> {
            int fila = tablaItems.getSelectedRow();
            if (fila == -1) {
                return;
            }
            String nombreCurso = modeloItems
                    .getValueAt(fila, 0).toString();
            a.eliminarItem(nombreCurso);
            controlador.guardarDatos();
            cargarItems(a, modeloItems);
            actualizarTabla(controlador.listar());
        });

        JPanel panelItems = new JPanel(
                new BorderLayout()
        );
        panelItems.add(scrollItems, BorderLayout.CENTER);
        panelItems.add(btnQuitarItem, BorderLayout.SOUTH);

        // ── BOTÓN CERRAR ──────────────────────────
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            actualizarTabla(controlador.listar());
            dialog.dispose();
        });

        JPanel panelInferior = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        panelInferior.add(btnCerrar);

        dialog.add(panelDetalle, BorderLayout.NORTH);
        dialog.add(panelItems, BorderLayout.CENTER);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── PANEL PRIMARIA ────────────────────────────
    /**
     * Panel para asignar docente titular y cursos especiales en primaria.
     */
    private JPanel crearPanelPrimaria(
            AsignacionCurso a, JDialog dialog) {

        JPanel panel = new JPanel(
                new BorderLayout(5, 5)
        );
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Primaria — Asignación de Docentes"
                )
        );

        // info
        JLabel lblInfo = new JLabel(
                "<html><b>Regla Primaria:</b> "
                + "1 docente titular dicta todos los cursos.<br>"
                + "Cursos especiales (Ed.Física, Cómputo, "
                + "Arte, Inglés) tienen docente propio.</html>"
        );
        lblInfo.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        // selector titular
        JPanel panelTitular = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );
        panelTitular.setBorder(
                BorderFactory.createTitledBorder(
                        "Docente Titular"
                )
        );

        List<Docente> docentes = controlador
                .getDocenteCtrl().listarTodos();
        String[] nombresDocentes = docentes.stream()
                .map(d -> d.getCodigoDocente()
                + " - " + d.getNombreCompleto())
                .toArray(String[]::new);

        JComboBox<String> comboTitular
                = new JComboBox<>(nombresDocentes);

        // muestra el titular actual si existe
        if (a.getDocenteTitular() != null) {
            comboTitular.setSelectedItem(
                    a.getDocenteTitular().getCodigoDocente()
                    + " - "
                    + a.getDocenteTitular().getNombreCompleto()
            );
        }

        JButton btnAsignarTitular
                = new JButton("Asignar Titular");
        btnAsignarTitular.addActionListener(e -> {
            if (nombresDocentes.length == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "No hay docentes registrados.",
                        "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String sel = comboTitular
                    .getSelectedItem().toString();
            String codigo = sel.split(" - ")[0];
            Docente docente = controlador
                    .getDocenteCtrl()
                    .buscarPorCodigo(codigo);
            if (docente == null) {
                return;
            }

            a.asignarDocenteTitular(docente);
            controlador.guardarDatos();
            JOptionPane.showMessageDialog(dialog,
                    "Titular asignado: "
                    + docente.getNombreCompleto(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        panelTitular.add(new JLabel("Docente:"));
        panelTitular.add(comboTitular);
        panelTitular.add(btnAsignarTitular);

        // selector curso especial
        JPanel panelEspecial = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );
        panelEspecial.setBorder(
                BorderFactory.createTitledBorder(
                        "Agregar Curso Especial"
                )
        );

        List<Curso> cursos = controlador
                .getCursoCtrl().listar();
        String[] nombresCursos = cursos.stream()
                .map(c -> c.getIdCurso()
                + " - " + c.getNombre())
                .toArray(String[]::new);

        JComboBox<String> comboCursoEsp
                = new JComboBox<>(nombresCursos);
        JComboBox<String> comboDocenteEsp
                = new JComboBox<>(nombresDocentes);

        List<Horario> horarios = controlador
                .getHorarioCtrl().listar();
        String[] nombresHorarios = horarios.stream()
                .map(h -> h.getIdHorario() + " - "
                + h.getDia() + " "
                + h.getHoraInicio())
                .toArray(String[]::new);

        JComboBox<String> comboHorarioEsp
                = new JComboBox<>(nombresHorarios);

        JButton btnAgregarEsp
                = new JButton("Agregar");
        btnAgregarEsp.addActionListener(e -> {
            try {
                String idCurso = comboCursoEsp
                        .getSelectedItem().toString()
                        .split(" - ")[0];
                String idDoc = comboDocenteEsp
                        .getSelectedItem().toString()
                        .split(" - ")[0];
                String idHor = nombresHorarios.length > 0
                        ? comboHorarioEsp
                                .getSelectedItem().toString()
                                .split(" - ")[0]
                        : null;

                Curso curso = controlador
                        .getCursoCtrl()
                        .buscarPorId(idCurso);
                Docente docente = controlador
                        .getDocenteCtrl()
                        .buscarPorCodigo(idDoc);
                Horario horario = idHor != null
                        ? controlador.getHorarioCtrl()
                                .buscarPorId(idHor)
                        : null;

                if (curso == null || docente == null) {
                    return;
                }

                a.agregarItem(curso, docente, horario);
                controlador.guardarDatos();

                // refresca la tabla de items
                JScrollPane sp = (JScrollPane) ((JPanel) dialog.getContentPane()
                        .getComponent(1))
                        .getComponent(0);
                JTable t = (JTable) sp.getViewport().getView();
                cargarItems(a,
                        (DefaultTableModel) t.getModel());

                JOptionPane.showMessageDialog(dialog,
                        "Curso especial asignado.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panelEspecial.add(new JLabel("Curso:"));
        panelEspecial.add(comboCursoEsp);
        panelEspecial.add(new JLabel("Docente:"));
        panelEspecial.add(comboDocenteEsp);
        panelEspecial.add(new JLabel("Horario:"));
        panelEspecial.add(comboHorarioEsp);
        panelEspecial.add(btnAgregarEsp);

        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(panelTitular, BorderLayout.CENTER);
        panel.add(panelEspecial, BorderLayout.SOUTH);

        return panel;
    }

    // ── PANEL SECUNDARIA ──────────────────────────
    /**
     * Panel para asignar un docente por cada curso en secundaria.
     */
    private JPanel crearPanelSecundaria(
            AsignacionCurso a, JDialog dialog) {

        JPanel panel = new JPanel(
                new BorderLayout(5, 5)
        );
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Secundaria — Asignación de Docentes"
                )
        );

        JLabel lblInfo = new JLabel(
                "<html><b>Regla Secundaria:</b> "
                + "Cada curso tiene su propio docente.</html>"
        );
        lblInfo.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        // selectores
        JPanel panelForm = new JPanel(
                new GridLayout(3, 2, 5, 5)
        );
        panelForm.setBorder(
                BorderFactory.createTitledBorder(
                        "Agregar Curso-Docente"
                )
        );

        List<Curso> cursos = controlador
                .getCursoCtrl().listar();
        String[] nombresCursos = cursos.stream()
                .map(c -> c.getIdCurso()
                + " - " + c.getNombre())
                .toArray(String[]::new);

        List<Docente> docentes = controlador
                .getDocenteCtrl().listarTodos();
        String[] nombresDocentes = docentes.stream()
                .map(d -> d.getCodigoDocente()
                + " - " + d.getNombreCompleto())
                .toArray(String[]::new);

        List<Horario> horarios = controlador
                .getHorarioCtrl().listar();
        String[] nombresHorarios = horarios.stream()
                .map(h -> h.getIdHorario() + " - "
                + h.getDia() + " "
                + h.getHoraInicio())
                .toArray(String[]::new);

        JComboBox<String> comboCurso
                = new JComboBox<>(nombresCursos);
        JComboBox<String> comboDocente
                = new JComboBox<>(nombresDocentes);
        JComboBox<String> comboHorario
                = new JComboBox<>(nombresHorarios);

        panelForm.add(new JLabel("Curso:"));
        panelForm.add(comboCurso);
        panelForm.add(new JLabel("Docente:"));
        panelForm.add(comboDocente);
        panelForm.add(new JLabel("Horario:"));
        panelForm.add(comboHorario);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            try {
                String idCurso = comboCurso
                        .getSelectedItem().toString()
                        .split(" - ")[0];
                String idDoc = comboDocente
                        .getSelectedItem().toString()
                        .split(" - ")[0];
                String idHor = nombresHorarios.length > 0
                        ? comboHorario
                                .getSelectedItem().toString()
                                .split(" - ")[0]
                        : null;

                Curso curso = controlador
                        .getCursoCtrl()
                        .buscarPorId(idCurso);
                Docente docente = controlador
                        .getDocenteCtrl()
                        .buscarPorCodigo(idDoc);
                Horario horario = idHor != null
                        ? controlador.getHorarioCtrl()
                                .buscarPorId(idHor)
                        : null;

                if (curso == null || docente == null) {
                    return;
                }

                a.agregarItem(curso, docente, horario);
                controlador.guardarDatos();

                // refresca tabla de items
                JScrollPane sp = (JScrollPane) ((JPanel) dialog.getContentPane()
                        .getComponent(1))
                        .getComponent(0);
                JTable t = (JTable) sp.getViewport().getView();
                cargarItems(a,
                        (DefaultTableModel) t.getModel());

                JOptionPane.showMessageDialog(dialog,
                        "Asignación agregada.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(panelForm, BorderLayout.CENTER);
        panel.add(btnAgregar, BorderLayout.SOUTH);

        return panel;
    }

    // ── CARGA ITEMS CON ITERATOR ──────────────────
    /**
     * Carga los items de una asignación en la tabla usando Iterator.
     */
    private void cargarItems(AsignacionCurso a,
            DefaultTableModel m) {
        m.setRowCount(0);

        // si es primaria muestra el titular primero
        if (a.getNivel().equals("Primaria")
                && a.getDocenteTitular() != null) {
            m.addRow(new Object[]{
                "Todos los cursos",
                a.getDocenteTitular()
                .getNombreCompleto()
                + " (Titular)",
                "-"
            });
        }

        // recorre los items con Iterator
        Iterator<AsignacionCurso.ItemAsignacion> it
                = a.iteratorItems();
        while (it.hasNext()) {
            AsignacionCurso.ItemAsignacion item
                    = it.next();
            m.addRow(new Object[]{
                item.getCurso().getNombre(),
                item.getDocente().getNombreCompleto(),
                item.getHorario() != null
                ? item.getHorario().getDia() + " "
                + item.getHorario().getHoraInicio()
                : "Sin horario"
            });
        }

        m.fireTableDataChanged();
    }

    // ── ACTUALIZAR TABLA PRINCIPAL ────────────────
    /**
     * Recarga la tabla principal con la lista.
     */
    private void actualizarTabla(
            List<AsignacionCurso> lista) {
        modeloTabla.setRowCount(0);
        for (AsignacionCurso a : lista) {
            String titular = a.getDocenteTitular() != null
                    ? a.getDocenteTitular()
                            .getNombreCompleto()
                    : "Sin asignar";

            modeloTabla.addRow(new Object[]{
                a.getIdAsignacion(),
                a.getNombreSeccion(),
                a.getNivel(),
                titular,
                a.getItems().size() + " curso(s)",
                a.esValida() ? "✓ Válida" : "✗ Incompleta"
            });
        }
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── LIMPIAR ───────────────────────────────────
    private void limpiar() {
        comboNivel.setSelectedIndex(0);
        comboGrado.setSelectedIndex(0);
        comboSeccion.setSelectedIndex(0);
        tabla.clearSelection();
        idSeleccionado = null;
        btnEliminar.setEnabled(false);
        btnDetalle.setEnabled(false);
    }
}
