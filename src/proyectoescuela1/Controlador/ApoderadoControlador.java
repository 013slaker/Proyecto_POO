/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

/**
 *
 * @author USER
 */
import proyectoescuela1.Modelo.Apoderado;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ApoderadoControlador {

    private List<Apoderado> lista = new ArrayList<>();

    public void registrar(Apoderado a) {
        lista.add(a);
    }

    public List<Apoderado> listar() {
        return lista;
    }

    public void eliminar(String codigo) {
        lista.removeIf(a -> a.getCodigoApoderado().equals(codigo));
    }

    public List<Apoderado> buscarPorNombre(String nombre) {
        List<Apoderado> resultado = new ArrayList<>();
        for (Apoderado a : lista) {
            if (a.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(a);
            }
        }
        return resultado;
    }
}
🖥️ 3. VISTA: ApoderadoVista (tipo AlumnoVista)
package proyectoescuela1.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import proyectoescuela1.Controlador.ApoderadoControlador;
import proyectoescuela1.Modelo.Apoderado;
import java.util.Date;

public class ApoderadoVista extends JPanel {

    private ApoderadoControlador controlador = new ApoderadoControlador();

    private JTextField txtNombre = new JTextField(20);
    private JTextField txtApellidos = new JTextField(20);
    private JTextField txtDni = new JTextField(20);
    private JTextField txtTelefono = new JTextField(20);
    private JTextField txtOcupacion = new JTextField(20);

    private String[] parentescos = {"Padre", "Madre", "Tutor"};
    private JComboBox<String> comboParentesco = new JComboBox<>(parentescos);

    private JButton btnGuardar = new JButton("Guardar");

    private DefaultTableModel modelo =
            new DefaultTableModel(new String[]{"Código", "Nombre", "Parentesco", "Ocupación"}, 0);

    private JTable tabla = new JTable(modelo);

    public ApoderadoVista() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {

        JPanel form = new JPanel(new GridLayout(5, 2));

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);

        form.add(new JLabel("Apellidos:"));
        form.add(txtApellidos);

        form.add(new JLabel("DNI:"));
        form.add(txtDni);

        form.add(new JLabel("Parentesco:"));
        form.add(comboParentesco);

        form.add(new JLabel("Ocupación:"));
        form.add(txtOcupacion);

        JPanel botones = new JPanel();
        botones.add(btnGuardar);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(tabla), BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {

        Apoderado a = new Apoderado(
                0,
                txtNombre.getText(),
                txtApellidos.getText(),
                txtDni.getText(),
                "",
                txtTelefono.getText(),
                "",
                new Date(),
                (String) comboParentesco.getSelectedItem(),
                txtOcupacion.getText()
        );

        controlador.registrar(a);
        actualizar();
    }

    private void actualizar() {
        modelo.setRowCount(0);
        for (Apoderado a : controlador.listar()) {
            modelo.addRow(new Object[]{
                    a.getCodigoApoderado(),
                    a.getNombreCompleto(),
                    a.getParentesco(),
                    a.getOcupacion()
            });
        }
    }
}