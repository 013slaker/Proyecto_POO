package proyectoescuela1.Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyectoescuela1.Controlador.DocenteControlador;
import proyectoescuela1.Modelo.Docente;

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
    private String[] niveles = {"Primaria", "Secundaria"};
    private JComboBox<String> comboNivel = new JComboBox<>(niveles);

    //==================================================
    // BOTONES
    //==================================================
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnBusquedaAvanzada = new JButton("Búsqueda Avanzada");

    //==================================================
    // CAMPO DE BÚSQUEDA
    //==================================================
    private JTextField txtBuscar = new JTextField(18);

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
    private JTable tabla = new JTable(modeloTabla);

//==================================================
    // CÓDIGO DEL DOCENTE SELECCIONADO  
    //==================================================
    private String codigoSeleccionado = null;

//==================================================
    // CONSTRUCTOR
    //==================================================
    public DocenteVista() {

        // BorderLayout divide el JPanel
        // en Norte, Sur, Este, Oeste y Centro.
        setLayout(new BorderLayout(10, 10));

        // Construye la interfaz
        initComponentes();

        // Asocia los botones con sus eventos
        initEventos();

        // Carga automáticamente los docentes almacenados
        actualizarTabla(controlador.listarTodos());
    }

    //==================================================
    // INTERFAZ GRÁFICA
    //==================================================
    private void initComponentes() {
        // Espacios entre paneles

        setLayout(new BorderLayout(10, 10));

        //====================================================
        // PANEL DEL FORMULARIO
        //====================================================
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 6, 6));

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
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        //====================================================
        // PANEL BUSCADOR
        //====================================================
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

        panelBuscar.add(btnBusquedaAvanzada);

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

        //====================================================
        // AGREGAR TODO AL PANEL PRINCIPAL
        //====================================================
        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

// ======================================================
// EVENTOS
// ======================================================
    private void initEventos() {

        // --------------------------------------------------
        // BOTÓN GUARDAR
        // --------------------------------------------------
        btnGuardar.addActionListener(e -> {

            // Primero valida que los datos sean correctos
            if (!validarCampos()) {
                return;
            }

            // Crea un nuevo objeto Docente con los datos del formulario
            Docente docente = new Docente(
                    0,
                    txtNombre.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtDni.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtDireccion.getText().trim(),
                    new Date(), // Fecha de nacimiento (temporal)
                    txtEspecialidad.getText().trim(),
                    comboNivel.getSelectedItem().toString(),
                    new Date() // Fecha de ingreso
            );

            // Registrar en el controlador
            controlador.registrarDocente(docente);

            // Actualizar la tabla
            actualizarTabla(controlador.listarTodos());

            // Limpiar formulario
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Docente registrado correctamente.\nCódigo: "
                    + docente.getCodigoDocente(),
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        });

        // --------------------------------------------------
        // BOTÓN ELIMINAR
        // --------------------------------------------------
        btnEliminar.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un docente de la tabla.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String codigo = modeloTabla.getValueAt(fila, 0).toString();

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este docente?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                controlador.eliminarDocente(codigo);

                actualizarTabla(controlador.listarTodos());

            }

        });

        // --------------------------------------------------
        // CLICK EN TABLA — carga datos en el formulario
        // --------------------------------------------------
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) {
                    return;
                }

                // guarda el código del docente seleccionado
                codigoSeleccionado = modeloTabla
                        .getValueAt(fila, 0).toString();

                // busca el docente y carga sus datos en el formulario
                Docente d = controlador
                        .buscarPorCodigo(codigoSeleccionado);

                if (d != null) {
                    txtNombre.setText(d.getNombre());
                    txtApellidos.setText(d.getApellidos());
                    txtDni.setText(d.getDni());
                    txtEmail.setText(d.getEmail());
                    txtTelefono.setText(d.getTelefono());
                    txtDireccion.setText(d.getDireccion());
                    txtEspecialidad.setText(d.getEspecialidad());
                    comboNivel.setSelectedItem(d.getNivel());
                }
            }
        });

        // --------------------------------------------------
        // BOTÓN Actualizar
        // --------------------------------------------------
        btnActualizar.addActionListener(e -> {

            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Primero selecciona un docente de la tabla.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!validarCampos()) {
                return;
            }

            // busca el docente en la lista
            Docente original = controlador.buscarPorCodigo(codigoSeleccionado);

            if (original == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el docente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // modifica los atributos
            original.setNombre(txtNombre.getText().trim());
            original.setApellidos(txtApellidos.getText().trim());
            original.setDni(txtDni.getText().trim());
            original.setEmail(txtEmail.getText().trim());
            original.setTelefono(txtTelefono.getText().trim());
            original.setDireccion(txtDireccion.getText().trim());

            // ── actualiza atributos propios de Docente ──
            original.setEspecialidad(txtEspecialidad.getText().trim());
            original.setNivel(comboNivel.getSelectedItem().toString());

            // guarda y refresca
            controlador.guardarDatos();
            actualizarTabla(controlador.listarTodos());
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Docente actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // --------------------------------------------------
        // BOTÓN LIMPIAR
        // --------------------------------------------------
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // --------------------------------------------------
        // BOTÓN BUSCAR
        // --------------------------------------------------
        btnBuscar.addActionListener(e -> {

            String texto = txtBuscar.getText().trim();

            if (texto.isEmpty()) {

                actualizarTabla(controlador.listarTodos());

            } else {

                actualizarTabla(
                        controlador.buscarPorNombre(texto)
                );

            }

        });
        // --------------------------------------------------
        // BOTÓN PARA ABRIR UNA VENTANA DE DIALOGO DE BUSQUEDA AVANZADA
        // --------------------------------------------------

        btnBusquedaAvanzada.addActionListener(e -> {
            // obtiene la ventana padre
            Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);

            // abre el dialog — bloquea la ventana principal
            BusquedaAvanzadaDocenteDialog dialog
                    = new BusquedaAvanzadaDocenteDialog(padre, controlador);

            dialog.setVisible(true);
        });

    }

// ======================================================
// VALIDAR CAMPOS
// ======================================================
    private boolean validarCampos() {

        if (txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el nombre del docente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        if (txtApellidos.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese los apellidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        if (txtDni.getText().trim().length() != 8) {

            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe contener 8 dígitos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        if (txtEspecialidad.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la especialidad.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        return true;
    }

// ======================================================
// ACTUALIZAR TABLA
// ======================================================
    private void actualizarTabla(List<Docente> lista) {

        // elimina todas las filas
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }

        // agrega las filas nuevas
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

        // fuerza el repintado
        modeloTabla.fireTableDataChanged();
        tabla.repaint();
        tabla.revalidate();
    }

// ======================================================
// LIMPIAR FORMULARIO
// ======================================================
    private void limpiarCampos() {

        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtEspecialidad.setText("");

        comboNivel.setSelectedIndex(0);

        txtBuscar.setText("");

        // Quitar selección de la tabla
        tabla.clearSelection();

    }

}
