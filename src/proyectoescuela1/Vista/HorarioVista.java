/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import proyectoescuela1.Controlador.HorarioControlador;
import proyectoescuela1.Modelo.Horario;

/**
 * Vista del módulo Horarios.
 * Permite registrar, actualizar, eliminar
 * y buscar horarios.
 * Detecta conflictos automáticamente.
 */
public class HorarioVista extends JPanel {

    // ── CONTROLADOR ───────────────────────────────
    private HorarioControlador controlador =
            new HorarioControlador();

    // ── CAMPOS DEL FORMULARIO ─────────────────────

    /** Lista desplegable de días */
    private String[] dias = {
        "Lunes", "Martes", "Miércoles",
        "Jueves", "Viernes"
    };
    private JComboBox<String> comboDia =
            new JComboBox<>(dias);

    /** Lista desplegable de horas inicio */
    private String[] horas = {
        "07:00", "07:45", "08:30", "09:15",
        "10:00", "10:45", "11:30", "12:15",
        "13:00", "13:45", "14:00", "14:45",
        "15:30", "16:15", "17:00"
    };
    private JComboBox<String> comboHoraInicio =
            new JComboBox<>(horas);
    private JComboBox<String> comboHoraFin =
            new JComboBox<>(horas);

    /** Lista desplegable de aulas */
    private String[] aulas = {
        "Regular", "Cómputo",
        "Ciencias", "Audiovisual"
    };
    private JComboBox<String> comboAula =
            new JComboBox<>(aulas);

    /** Lista desplegable de niveles */
    private String[] niveles = {
        "Inicial", "Primaria", "Secundaria"
    };
    private JComboBox<String> comboNivel =
            new JComboBox<>(niveles);

    // ── BOTONES ───────────────────────────────────
    private JButton btnGuardar    = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar   = new JButton("Eliminar");
    private JButton btnLimpiar    = new JButton("Limpiar");
    private JButton btnBuscar     = new JButton("Buscar");

    // ── BÚSQUEDA ──────────────────────────────────
    /** Combo para filtrar por día en la búsqueda */
    private String[] diasBuscar = {
        "Todos", "Lunes", "Martes", "Miércoles",
        "Jueves", "Viernes"
    };
    private JComboBox<String> comboBuscarDia =
            new JComboBox<>(diasBuscar);

    // ── TABLA ─────────────────────────────────────
    private String[] columnas = {
        "Código", "Día", "Inicio",
        "Fin", "Duración", "Aula", "Nivel"
    };
    private DefaultTableModel modeloTabla =
        new DefaultTableModel(columnas, 0) {
            /** Evita que el usuario edite la tabla */
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };
    private JTable tabla = new JTable(modeloTabla);

    /** Guarda el id del horario seleccionado */
    private String idSeleccionado = null;

    // ── CONSTRUCTOR ───────────────────────────────
    public HorarioVista() {
        setLayout(new BorderLayout(10, 10));

        // botones inactivos hasta seleccionar
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        // hora fin por defecto una hora después
        comboHoraFin.setSelectedIndex(1);

        initUI();
        initEventos();

        // carga datos guardados al abrir
        actualizarTabla(controlador.listar());
    }

    // ── INTERFAZ ──────────────────────────────────
    private void initUI() {

        // FORMULARIO
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder(
            "Datos del Horario"
        ));
        form.add(new JLabel("Día:"));
        form.add(comboDia);
        form.add(new JLabel("Hora inicio:"));
        form.add(comboHoraInicio);
        form.add(new JLabel("Hora fin:"));
        form.add(comboHoraFin);
        form.add(new JLabel("Aula:"));
        form.add(comboAula);
        form.add(new JLabel("Nivel:"));
        form.add(comboNivel);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // BÚSQUEDA
        JPanel panelBuscar = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );
        panelBuscar.add(new JLabel("Filtrar por día:"));
        panelBuscar.add(comboBuscarDia);
        panelBuscar.add(btnBuscar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
            new BorderLayout()
        );
        panelSuperior.add(form,         BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Lista de Horarios"
        ));

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(
            new BorderLayout()
        );
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll,      BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    // ── EVENTOS ───────────────────────────────────
    private void initEventos() {

        // GUARDAR — registra nuevo horario
        btnGuardar.addActionListener(e -> {
            if (!validarCampos()) return;

            Horario horario = new Horario(
                comboDia.getSelectedItem().toString(),
                comboHoraInicio.getSelectedItem().toString(),
                comboHoraFin.getSelectedItem().toString(),
                comboAula.getSelectedItem().toString(),
                comboNivel.getSelectedItem().toString()
            );

            // intenta registrar — detecta conflictos
            boolean registrado = controlador
                .registrar(horario);

            if (!registrado) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un horario en ese día, " +
                    "hora y aula.\n" +
                    "Por favor elige otro horario o aula.",
                    "Conflicto de horario",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Horario registrado: " +
                horario.getIdHorario(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ACTUALIZAR — modifica horario seleccionado
        btnActualizar.addActionListener(e -> {
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un horario de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!validarCampos()) return;

            Horario original = controlador
                .buscarPorId(idSeleccionado);
            if (original == null) return;

            // actualiza los atributos
            original.setDia(
                comboDia.getSelectedItem().toString()
            );
            original.setHoraInicio(
                comboHoraInicio.getSelectedItem().toString()
            );
            original.setHoraFin(
                comboHoraFin.getSelectedItem().toString()
            );
            original.setAula(
                comboAula.getSelectedItem().toString()
            );
            original.setNivel(
                comboNivel.getSelectedItem().toString()
            );

            controlador.guardarDatos();
            actualizarTabla(controlador.listar());
            limpiar();
            JOptionPane.showMessageDialog(this,
                "Horario actualizado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ELIMINAR — elimina horario seleccionado
        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                    "Selecciona un horario de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el horario " +
                idSeleccionado + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                actualizarTabla(controlador.listar());
                limpiar();
            }
        });

        // LIMPIAR — vacía el formulario
        btnLimpiar.addActionListener(e -> limpiar());

        // BUSCAR — filtra por día
        btnBuscar.addActionListener(e -> {
            String dia = comboBuscarDia
                .getSelectedItem().toString();
            if (dia.equals("Todos")) {
                // Lambda — ordena por día y hora
                actualizarTabla(
                    controlador.ordenarPorDiaYHora()
                );
            } else {
                actualizarTabla(
                    controlador.buscarPorDia(dia)
                );
            }
        });

        // CLICK EN TABLA — carga datos en formulario
        tabla.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;

                    idSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();

                    // activa botones
                    btnActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);

                    // busca y carga en formulario
                    Horario h = controlador
                        .buscarPorId(idSeleccionado);
                    if (h != null) {
                        comboDia.setSelectedItem(h.getDia());
                        comboHoraInicio.setSelectedItem(
                            h.getHoraInicio()
                        );
                        comboHoraFin.setSelectedItem(
                            h.getHoraFin()
                        );
                        comboAula.setSelectedItem(h.getAula());
                        comboNivel.setSelectedItem(
                            h.getNivel()
                        );
                    }
                }
            }
        );
    }

    // ── VALIDAR CAMPOS ────────────────────────────
    /**
     * Verifica que la hora de fin sea
     * posterior a la hora de inicio.
     */
    private boolean validarCampos() {
        String inicio = comboHoraInicio
            .getSelectedItem().toString();
        String fin    = comboHoraFin
            .getSelectedItem().toString();

        // compara las horas como texto (formato HH:MM)
        if (fin.compareTo(inicio) <= 0) {
            JOptionPane.showMessageDialog(this,
                "La hora de fin debe ser posterior " +
                "a la hora de inicio.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    // ── ACTUALIZAR TABLA ──────────────────────────
    /**
     * Limpia y recarga la tabla con la lista recibida.
     * Muestra la duración calculada en minutos.
     */
    private void actualizarTabla(List<Horario> lista) {
        modeloTabla.setRowCount(0);
        for (Horario h : lista) {
            modeloTabla.addRow(new Object[]{
                h.getIdHorario(),
                h.getDia(),
                h.getHoraInicio(),
                h.getHoraFin(),
                h.getDuracion() + " min",
                h.getAula(),
                h.getNivel()
            });
        }
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
    }

    // ── LIMPIAR CAMPOS ────────────────────────────
    /**
     * Vacía todos los campos del formulario
     * y desactiva los botones que requieren
     * selección.
     */
    private void limpiar() {
        comboDia.setSelectedIndex(0);
        comboHoraInicio.setSelectedIndex(0);
        comboHoraFin.setSelectedIndex(1);
        comboAula.setSelectedIndex(0);
        comboNivel.setSelectedIndex(0);
        comboBuscarDia.setSelectedIndex(0);
        tabla.clearSelection();
        idSeleccionado = null;
        // desactiva botones hasta nueva selección
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
