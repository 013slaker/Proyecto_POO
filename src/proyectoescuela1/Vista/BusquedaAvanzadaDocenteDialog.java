/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import proyectoescuela1.Controlador.DocenteControlador;
import proyectoescuela1.Modelo.Docente;

public class BusquedaAvanzadaDocenteDialog extends JDialog {
//==================================================
    // CONTROLADOR
    //==================================================

    private DocenteControlador controlador;

    //==================================================
    // RADIO BUTTONS — criterio de búsqueda
    //==================================================
    private JRadioButton rbCodigo = new JRadioButton("Código");
    private JRadioButton rbNombre = new JRadioButton("Nombre");
    private JRadioButton rbEspecialidad = new JRadioButton("Especialidad");
    private JRadioButton rbNivel = new JRadioButton("Nivel");

    // Grupo — solo permite seleccionar uno a la vez
    private ButtonGroup grupoCriterio = new ButtonGroup();

    //==================================================
    // CAMPO DE TEXTO — valor a buscar
    //==================================================
    private JTextField txtValor = new JTextField(25);

    //==================================================
    // COMBOS — filtros adicionales
    //==================================================
    private JComboBox<String> comboNivel = new JComboBox<>(
            new String[]{"Todos", "Inicial", "Primaria", "Secundaria"}
    );

    private JComboBox<String> comboEspecialidad = new JComboBox<>(
            new String[]{
                "Todas",
                "Matemática",
                "Comunicación",
                "Ciencias",
                "Historia",
                "Inglés",
                "Educación Física",
                "Arte",
                "Computación"
            }
    );

    //==================================================
    // BOTONES
    //==================================================
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnMostrarTodos = new JButton("Mostrar Todos");
    private JButton btnCerrar = new JButton("Cerrar");

    //==================================================
    // TABLA DE RESULTADOS
    //==================================================
    private String[] columnas = {
        "Código", "Nombre", "DNI",
        "Especialidad", "Nivel", "Estado"
    };

    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int fila, int col) {
            return false;
        }
    };

    private JTable tablaResultados = new JTable(modeloTabla);

    //==================================================
    // LABEL DE RESULTADOS
    //==================================================
    private JLabel lblResultados = new JLabel("Resultados: 0");

    //==================================================
    // CONSTRUCTOR
    //==================================================
    public BusquedaAvanzadaDocenteDialog(
            Frame padre,
            DocenteControlador controlador) {

        // true = modal, bloquea la ventana principal
        super(padre, "Búsqueda Avanzada de Docentes", true);

        this.controlador = controlador;

        setSize(700, 580);
        setLocationRelativeTo(padre);
        setResizable(false);

        initComponentes();
        initEventos();

        // muestra todos al abrir
        actualizarTabla(controlador.listarTodos());
    }

    //==================================================
    // INTERFAZ GRÁFICA
    //==================================================
    private void initComponentes() {

        setLayout(new BorderLayout(10, 10));

        //================================================
        // PANEL SUPERIOR — criterios de búsqueda
        //================================================
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 5, 10
        ));

        //--- PANEL RADIO BUTTONS ---
        JPanel panelRadio = new JPanel(new GridLayout(2, 2, 5, 5));
        panelRadio.setBorder(BorderFactory.createTitledBorder(
                "Buscar por:"
        ));

        // agrega al grupo — solo uno activo a la vez
        grupoCriterio.add(rbCodigo);
        grupoCriterio.add(rbNombre);
        grupoCriterio.add(rbEspecialidad);
        grupoCriterio.add(rbNivel);

        // selecciona nombre por defecto
        rbNombre.setSelected(true);

        panelRadio.add(rbCodigo);
        panelRadio.add(rbNombre);
        panelRadio.add(rbEspecialidad);
        panelRadio.add(rbNivel);

        //--- PANEL VALOR ---
        JPanel panelValor = new JPanel(new FlowLayout(
                FlowLayout.LEFT
        ));
        panelValor.add(new JLabel("Valor:"));
        panelValor.add(txtValor);

        //--- PANEL FILTROS ---
        JPanel panelFiltros = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
                "Filtros adicionales:"
        ));
        panelFiltros.add(new JLabel("Nivel:"));
        panelFiltros.add(comboNivel);
        panelFiltros.add(new JLabel("Especialidad:"));
        panelFiltros.add(comboEspecialidad);

        //--- PANEL BOTONES BUSCAR ---
        JPanel panelBotonesBuscar = new JPanel(new FlowLayout());
        panelBotonesBuscar.add(btnBuscar);
        panelBotonesBuscar.add(btnMostrarTodos);

        //--- ARMAR PANEL SUPERIOR ---
        JPanel panelCriterios = new JPanel(new BorderLayout(5, 5));
        panelCriterios.add(panelRadio, BorderLayout.NORTH);
        panelCriterios.add(panelValor, BorderLayout.CENTER);
        panelCriterios.add(panelFiltros, BorderLayout.SOUTH);

        panelSuperior.add(panelCriterios, BorderLayout.CENTER);
        panelSuperior.add(panelBotonesBuscar, BorderLayout.SOUTH);

        //================================================
        // PANEL CENTRAL — tabla de resultados
        //================================================
        JScrollPane scroll = new JScrollPane(tablaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Resultados"
        ));

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(
                0, 10, 5, 10
        ));
        panelCentral.add(lblResultados, BorderLayout.NORTH);
        panelCentral.add(scroll, BorderLayout.CENTER);

        //================================================
        // PANEL INFERIOR — botones cerrar
        //================================================
        JPanel panelInferior = new JPanel(new FlowLayout(
                FlowLayout.RIGHT
        ));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(
                0, 10, 10, 10
        ));
        panelInferior.add(btnCerrar);

        //================================================
        // AGREGAR AL DIALOG
        //================================================
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    //==================================================
    // EVENTOS
    //==================================================
    private void initEventos() {

        //--- BOTÓN BUSCAR ---
        btnBuscar.addActionListener(e -> realizarBusqueda());

        //--- ENTER en el campo de texto también busca ---
        txtValor.addActionListener(e -> realizarBusqueda());

        //--- BOTÓN MOSTRAR TODOS ---
        btnMostrarTodos.addActionListener(e -> {
            txtValor.setText("");
            comboNivel.setSelectedIndex(0);
            comboEspecialidad.setSelectedIndex(0);
            actualizarTabla(controlador.listarTodos());
        });

        //--- BOTÓN CERRAR ---
        btnCerrar.addActionListener(e -> dispose());

        //--- RADIO BUTTONS — activa/desactiva campo valor ---
        rbNivel.addActionListener(e -> {
            // si busca por nivel, el combo es suficiente
            txtValor.setEnabled(false);
            txtValor.setText("");
        });

        rbCodigo.addActionListener(e
                -> txtValor.setEnabled(true));
        rbNombre.addActionListener(e
                -> txtValor.setEnabled(true));
        rbEspecialidad.addActionListener(e
                -> txtValor.setEnabled(true));
    }

    //==================================================
    // LÓGICA DE BÚSQUEDA — Lambda
    //==================================================
    private void realizarBusqueda() {

        String valor = txtValor.getText().trim();
        String nivel = comboNivel.getSelectedItem().toString();
        String especialidad = comboEspecialidad.getSelectedItem()
                .toString();

        List<Docente> resultado;

        // busca según el radio button seleccionado
        if (rbCodigo.isSelected()) {
            // Lambda: busca por código exacto
            resultado = controlador.listarTodos().stream()
                    .filter(d -> d.getCodigoDocente()
                    .equalsIgnoreCase(valor))
                    .collect(java.util.stream.Collectors.toList());

        } else if (rbNombre.isSelected()) {
            // Lambda: busca por nombre parcial
            resultado = controlador.buscarPorNombre(valor);

        } else if (rbEspecialidad.isSelected()) {
            // Lambda: busca por especialidad
            resultado = controlador.buscarPorEspecialidad(valor);

        } else {
            // busca por nivel usando el combo
            resultado = controlador.listarTodos().stream()
                    .filter(d -> d.getNivel()
                    .equalsIgnoreCase(nivel))
                    .collect(java.util.stream.Collectors.toList());
        }

        // aplica filtro de nivel si no es "Todos"
        if (!nivel.equals("Todos") && !rbNivel.isSelected()) {
            String nivelFinal = nivel;
            resultado = resultado.stream()
                    .filter(d -> d.getNivel()
                    .equalsIgnoreCase(nivelFinal))
                    .collect(java.util.stream.Collectors.toList());
        }

        // aplica filtro de especialidad si no es "Todas"
        if (!especialidad.equals("Todas")) {
            String espFinal = especialidad;
            resultado = resultado.stream()
                    .filter(d -> d.getEspecialidad()
                    .equalsIgnoreCase(espFinal))
                    .collect(java.util.stream.Collectors.toList());
        }

        actualizarTabla(resultado);
    }

    //==================================================
    // ACTUALIZAR TABLA
    //==================================================
    private void actualizarTabla(List<Docente> lista) {
        modeloTabla.setRowCount(0);
        for (Docente d : lista) {
            modeloTabla.addRow(new Object[]{
                d.getCodigoDocente(),
                d.getNombreCompleto(),
                d.getDni(),
                d.getEspecialidad(),
                d.getNivel(),
                d.isEstadoActivo() ? "Activo" : "Inactivo"
            });
        }
        // actualiza el contador de resultados
        lblResultados.setText("Resultados: " + lista.size());
    }
}
