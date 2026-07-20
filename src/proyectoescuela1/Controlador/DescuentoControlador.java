/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Descuento;

/**
 * Controlador del submódulo "Descuentos" (becas, hermanos,
 * convenios, etc.). Cada descuento es un porcentaje sobre
 * la pensión; DeudaControlador usa
 * obtenerPorcentajeTotal() para calcular el monto final
 * que le corresponde pagar a cada alumno.
 */
public class DescuentoControlador {

    private List<Descuento> descuentos = new ArrayList<>();

    private static final String ARCHIVO = "descuentos.dat";

    public DescuentoControlador() {
        cargarDatos();
    }

    public void registrar(Descuento descuento) {
        descuentos.add(descuento);
        guardarDatos();
    }

    public void eliminar(String idDescuento) {
        descuentos.removeIf(d -> d.getIdDescuento().equals(idDescuento));
        guardarDatos();
    }

    public void actualizar(Descuento descuento) {
        for (int i = 0; i < descuentos.size(); i++) {
            if (descuentos.get(i).getIdDescuento()
                    .equals(descuento.getIdDescuento())) {
                descuentos.set(i, descuento);
                guardarDatos();
                return;
            }
        }
    }

    public List<Descuento> listar() {
        return descuentos;
    }

    public List<Descuento> listarPorAlumno(String codigoAlumno) {
        return descuentos.stream()
                .filter(d -> d.getCodigoAlumno().equals(codigoAlumno))
                .collect(Collectors.toList());
    }

    /**
     * Suma los porcentajes de todos los descuentos ACTIVOS
     * de un alumno, con tope de 100%.
     */
    public double obtenerPorcentajeTotal(String codigoAlumno) {
        double total = descuentos.stream()
                .filter(d -> d.getCodigoAlumno().equals(codigoAlumno)
                        && d.isActivo())
                .mapToDouble(Descuento::getPorcentaje)
                .sum();
        return Math.min(total, 100.0);
    }

    // ── SERIALIZACIÓN ─────────────────────────────
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(descuentos);
        } catch (IOException e) {
            System.out.println(
                "Error al guardar descuentos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            descuentos = (List<Descuento>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                "Error al cargar descuentos: " + e.getMessage());
        }
    }
}
