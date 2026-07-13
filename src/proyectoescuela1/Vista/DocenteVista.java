/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.DocenteControlador;

public class DocenteVista extends JPanel {

    //==================================================
    // CONTROLADOR
    //==================================================
    // Se comunica con el modelo.
    private DocenteControlador controlador = new DocenteControlador();

    //==================================================
    // CAJAS DE TEXTO
    //==================================================
    // Datos personales
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);

    // Datos laborales
    private JTextField txtEspecialidad = new JTextField(20);

    //==================================================
    // COMBOBOX
    //==================================================
    // El usuario elegirá uno de estos niveles
    private String[] niveles = {
        "Inicial",
        "Primaria",
        "Secundaria"
    };

    private JComboBox<String> comboNivel
            = new JComboBox<>(niveles);

    //==================================================
    // BOTONES
    //==================================================
    private JButton btnGuardar
            = new JButton("Guardar");

    private JButton btnActualizar
            = new JButton("Actualizar");

    private JButton btnEliminar
            = new JButton("Eliminar");

    private JButton btnLimpiar
            = new JButton("Limpiar");

    private JButton btnBuscar
            = new JButton("Buscar");

    //==================================================
    // CAMPO DE BÚSQUEDA
    //==================================================
    private JTextField txtBuscar
            = new JTextField(18);

    //==================================================
    // TABLA
    //==================================================
    // Encabezados
    private String[] columnas = {
        "Código",
        "Nombre",
        "DNI",
        "Especialidad",
        "Nivel",
        "Estado"

    };

    // Modelo de la tabla
    private DefaultTableModel modeloTabla
            = new DefaultTableModel(columnas, 0) {

        // Evita que el usuario edite la tabla
        @Override
        public boolean isCellEditable(int fila, int columna) {

            return false;

        }

    };

    // Tabla
    private JTable tabla
            = new JTable(modeloTabla);

    //==================================================
    // CONSTRUCTOR
    //==================================================
    public DocenteVista() {

        // BorderLayout divide el JPanel
        // en Norte, Sur, Este, Oeste y Centro.
        setLayout(new BorderLayout(10, 10));

        // Construye la interfaz
        initComponentes();

        // Los eventos se agregarán
        // en la segunda parte.
        // initEventos();
    }

    //==================================================
    // INTERFAZ GRÁFICA
    //==================================================
    private void initComponentes() {

        //==============================================
        // FORMULARIO
        //==============================================
        JPanel panelFormulario
                = new JPanel(new GridLayout(8, 2, 6, 6));

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del Docente")
        );

        //------------------------------------------------
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellidos:"));
        panelFormulario.add(txtApellidos);

        panelFormulario.add(new JLabel("DNI:"));
        panelFormulario.add(txtDni);

        panelFormulario.add(new JLabel("Correo Electrónico:"));
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Especialidad:"));
        panelFormulario.add(txtEspecialidad);

        panelFormulario.add(new JLabel("Nivel:"));
        panelFormulario.add(comboNivel);

        //==============================================
        // PANEL BOTONES
        //==============================================
        JPanel panelBotones
                = new JPanel(new FlowLayout());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        //==============================================
        // PANEL SUPERIOR
        //==============================================
        JPanel panelSuperior
                = new JPanel(new BorderLayout());

        panelSuperior.add(panelFormulario,
                BorderLayout.CENTER);

        panelSuperior.add(panelBotones,
                BorderLayout.SOUTH);

        //==============================================
        // PANEL DE BÚSQUEDA
        //==============================================
        JPanel panelBuscar
                = new JPanel(new FlowLayout(
                        FlowLayout.LEFT));

        panelBuscar.setBorder(
                BorderFactory.createTitledBorder(
                        "Buscar Docente")
        );

        panelBuscar.add(new JLabel("Nombre:"));

        panelBuscar.add(txtBuscar);

        panelBuscar.add(btnBuscar);

        //==============================================
        // TABLA
        //==============================================
        JScrollPane scroll
                = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Lista de Docentes")
        );

        // Hace que las columnas ocupen todo el ancho
        tabla.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Altura de las filas
        tabla.setRowHeight(25);

        // Sólo se puede seleccionar una fila
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        //==============================================
        // PANEL INFERIOR
        //==============================================
        JPanel panelInferior
                = new JPanel(new BorderLayout());

        panelInferior.add(panelBuscar,
                BorderLayout.NORTH);

        panelInferior.add(scroll,
                BorderLayout.CENTER);

        //==============================================
        // AGREGAR TODO AL PANEL PRINCIPAL
        //==============================================
        add(panelSuperior,
                BorderLayout.NORTH);

        add(panelInferior,
                BorderLayout.CENTER);

    }

}

// ======================================================
// CONSTRUCTOR
// ======================================================
public DocenteVista() {

    // Layout principal del JPanel
    setLayout(new BorderLayout());

    // Construye toda la interfaz
    initComponentes();

    // Asocia los botones con sus eventos
    initEventos();

    // Carga automáticamente los docentes almacenados
    actualizarTabla(controlador.listarTodos());
}


// ======================================================
// CONSTRUCCIÓN DE LA INTERFAZ
// ======================================================
private void initComponentes() {

    // Espacios entre paneles
    setLayout(new BorderLayout(10,10));

    //====================================================
    // PANEL DEL FORMULARIO
    //====================================================

    JPanel panelForm = new JPanel(new GridLayout(8,2,5,5));

    panelForm.setBorder(
            BorderFactory.createTitledBorder("Datos del Docente")
    );

    // Nombre
    panelForm.add(new JLabel("Nombre:"));
    panelForm.add(txtNombre);

    // Apellidos
    panelForm.add(new JLabel("Apellidos:"));
    panelForm.add(txtApellidos);

    // DNI
    panelForm.add(new JLabel("DNI:"));
    panelForm.add(txtDni);

    // Correo
    panelForm.add(new JLabel("Email:"));
    panelForm.add(txtEmail);

    // Teléfono
    panelForm.add(new JLabel("Teléfono:"));
    panelForm.add(txtTelefono);

    // Dirección
    panelForm.add(new JLabel("Dirección:"));
    panelForm.add(txtDireccion);

    // Especialidad
    panelForm.add(new JLabel("Especialidad:"));
    panelForm.add(txtEspecialidad);

    // Nivel
    panelForm.add(new JLabel("Nivel:"));
    panelForm.add(comboNivel);


    //====================================================
    // PANEL BOTONES
    //====================================================

    JPanel panelBotones = new JPanel(new FlowLayout());

    panelBotones.add(btnGuardar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnLimpiar);


    //====================================================
    // PANEL BUSCADOR
    //====================================================

    JPanel panelBuscar = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
    );

    panelBuscar.add(new JLabel("Buscar:"));
    panelBuscar.add(txtBuscar);
    panelBuscar.add(btnBuscar);


    //====================================================
    // PANEL SUPERIOR
    //====================================================

    JPanel panelSuperior = new JPanel(new BorderLayout());

    panelSuperior.add(panelForm, BorderLayout.CENTER);
    panelSuperior.add(panelBotones, BorderLayout.SOUTH);


    //====================================================
    // TABLA
    //====================================================

    JScrollPane scroll = new JScrollPane(tabla);

    scroll.setBorder(
            BorderFactory.createTitledBorder("Lista de Docentes")
    );


    //====================================================
    // PANEL INFERIOR
    //====================================================

    JPanel panelInferior = new JPanel(new BorderLayout());

    panelInferior.add(panelBuscar, BorderLayout.NORTH);
    panelInferior.add(scroll, BorderLayout.CENTER);
    
    // AGREGAR TODO AL PANEL PRINCIPAL
    
    add(panelSuperior, BorderLayout.NORTH);
    add(panelInferior, BorderLayout.CENTER);
}


