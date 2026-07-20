/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Pago;

/**
 * Controlador del submódulo "Pagos".
 * Al registrar un pago, marca automáticamente como pagada
 * la Deuda de ese mismo alumno/mes/año (si existía),
 * para que Deudas y Pagos nunca queden desincronizados.
 */
public class PagoControlador {

    private List<Pago> pagos = new ArrayList<>();

    private static final String ARCHIVO = "pagos.dat";

    public PagoControlador() {
        cargarDatos();
    }

    public void registrar(Pago pago) {
        pagos.add(pago);
        guardarDatos();

        // sincroniza con Deudas: si existía una deuda de ese
        // mes, queda marcada como pagada
        new DeudaControlador().marcarPagada(
                pago.getCodigoAlumno(), pago.getAnio(), pago.getMes());
    }

    public Pago buscarPago(String codigoAlumno, int anio, int mes) {
        return pagos.stream()
                .filter(p -> p.getCodigoAlumno().equals(codigoAlumno)
                        && p.getAnio() == anio
                        && p.getMes() == mes)
                .findFirst()
                .orElse(null);
    }

    public List<Pago> listar() {
        return pagos;
    }

    public List<Pago> listarPorAlumno(String codigoAlumno) {
        return pagos.stream()
                .filter(p -> p.getCodigoAlumno().equals(codigoAlumno))
                .collect(Collectors.toList());
    }

    /** Total recaudado en un mes/año (para reportes). */
    public double totalRecaudado(int anio, int mes) {
        return pagos.stream()
                .filter(p -> p.getAnio() == anio && p.getMes() == mes)
                .mapToDouble(Pago::getMontoPagado)
                .sum();
    }

    // ── SERIALIZACIÓN ─────────────────────────────
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(pagos);
        } catch (IOException e) {
            System.out.println(
                "Error al guardar pagos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            pagos = (List<Pago>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                "Error al cargar pagos: " + e.getMessage());
        }
    }
}
