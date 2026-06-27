/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Vista;

/**
 *
 * @author Gisela
 */
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.DocenteControlador;
import proyectoescuela1.Modelo.Docente;


public class DocenteVista extends JPanel {

       private DocenteControlador controlador = new DocenteControlador();

    // CAMPOS
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtDireccion = new JTextField(20);

    private JTextField txtEspecialidad = new JTextField(20);

    private String[] niveles = {"Inicial", "Primaria", "Secundaria"};
    private JComboBox<String> comboNivel = new JComboBox<>(niveles);

    // BOTONES
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnBuscar = new JButton("Buscar");

    // TABLA
    private String[] columnas = {"Código", "Nombre", "DNI", "Especialidad", "Nivel", "Estado"};
    private DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    private JTable tabla = new JTable(modeloTabla);

    private JTextField txtBuscar = new JTextField(15);

    // CONSTRUCTOR
    public DocenteVista() {
        System.out.println("DOCENTE VISTA CARGADA");
        setLayout(new BorderLayout());
        initComponentes();
    }

    private void initComponentes() {

        setLayout(new BorderLayout(10, 10));

        // ================= FORMULARIO =================
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Docente"));

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Apellidos:"));
        panelForm.add(txtApellidos);

        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);

        panelForm.add(new JLabel("Email:"));
        panelForm.add(txtEmail);

        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);

        panelForm.add(new JLabel("Dirección:"));
        panelForm.add(txtDireccion);

        panelForm.add(new JLabel("Especialidad:"));
        panelForm.add(txtEspecialidad);

        panelForm.add(new JLabel("Nivel:"));
        panelForm.add(comboNivel);

        // ================= BOTONES =================
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // ================= BUSCADOR =================
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        // ================= SUPERIOR =================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // ================= TABLA =================
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Docentes"));

        // ================= INFERIOR =================
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBuscar, BorderLayout.NORTH);
        panelInferior.add(scroll, BorderLayout.CENTER);

        // ================= ADD =================
        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    // ================= MAIN PARA PRUEBA =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Docentes");
            f.setContentPane(new DocenteVista());
            f.setSize(900, 600);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}