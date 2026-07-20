/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Deuda;

/**
 * Controlador del submódulo "Deudas".
 * Las deudas NUNCA se registran a mano: se generan
 * automáticamente comparando, para cada alumno activo, si
 * ya venció el plazo de pago de un mes y no existe un Pago
 * registrado para ese mes. Esto evita errores humanos al
 * marcar deudas manualmente.
 */
public class DeudaControlador {

    private List<Deuda> deudas = new ArrayList<>();

    private static final String ARCHIVO = "deudas.dat";

    /** Día del mes en que vence la pensión (ej. 5 = vence el 5 de cada mes). */
    private static final int DIA_VENCIMIENTO = 5;

    private AlumnoControlador alumnoControlador = new AlumnoControlador();
    private TarifaPensionControlador tarifaControlador = new TarifaPensionControlador();
    private DescuentoControlador descuentoControlador = new DescuentoControlador();
    private PagoControlador pagoControlador = new PagoControlador();

    public DeudaControlador() {
        cargarDatos();
    }

    /**
     * Recorre a todos los alumnos activos, mes por mes desde
     * enero hasta el mes actual, y genera una Deuda por cada
     * mes ya vencido que no tenga un Pago registrado y que
     * todavía no tenga una Deuda generada. Se debe llamar al
     * abrir Deudas o Pagos para mantener todo al día.
     */
    public void actualizarDeudasAutomaticas() {
        LocalDate hoy = LocalDate.now();
        int anioActual = hoy.getYear();
        int mesActual = hoy.getMonthValue();

        for (Alumno alumno : alumnoControlador.listarActivos()) {
            for (int mes = 1; mes <= mesActual; mes++) {

                LocalDate vencimiento =
                        LocalDate.of(anioActual, mes, DIA_VENCIMIENTO);

                // todavía no vence este mes -> no se genera deuda
                if (hoy.isBefore(vencimiento)) continue;

                String codigo = alumno.getCodigoAlumno();

                // ya pagó ese mes
                if (pagoControlador.buscarPago(codigo, anioActual, mes) != null) {
                    continue;
                }
                // ya existe la deuda generada
                if (buscarDeuda(codigo, anioActual, mes) != null) {
                    continue;
                }

                double tarifa = tarifaControlador.obtenerMonto(
                        alumno.getNivel(), alumno.getGrado());
                // no se generan deudas para niveles/grados sin tarifa configurada
                if (tarifa <= 0) continue;

                double descuentoPct = descuentoControlador
                        .obtenerPorcentajeTotal(codigo);
                double montoFinal = tarifa * (1 - descuentoPct / 100.0);

                deudas.add(new Deuda(codigo, anioActual, mes,
                        montoFinal, vencimiento));
            }
        }
        guardarDatos();
    }

    public Deuda buscarDeuda(String codigoAlumno, int anio, int mes) {
        return deudas.stream()
                .filter(d -> d.getCodigoAlumno().equals(codigoAlumno)
                        && d.getAnio() == anio
                        && d.getMes() == mes)
                .findFirst()
                .orElse(null);
    }

    /** Marca como pagada la deuda de ese alumno/mes/año, si existe. Lo llama PagoControlador. */
    public void marcarPagada(String codigoAlumno, int anio, int mes) {
        Deuda d = buscarDeuda(codigoAlumno, anio, mes);
        if (d != null) {
            d.marcarPagada();
            guardarDatos();
        }
    }

    public List<Deuda> listar() {
        return deudas;
    }

    public List<Deuda> listarPendientes() {
        return deudas.stream()
                .filter(d -> !d.isPagada())
                .collect(Collectors.toList());
    }

    public List<Deuda> listarPorAlumno(String codigoAlumno) {
        return deudas.stream()
                .filter(d -> d.getCodigoAlumno().equals(codigoAlumno))
                .collect(Collectors.toList());
    }

    /** Suma de las deudas pendientes (no pagadas) de un alumno. */
    public double totalDeudaAlumno(String codigoAlumno) {
        return listarPorAlumno(codigoAlumno).stream()
                .filter(d -> !d.isPagada())
                .mapToDouble(Deuda::getMontoPension)
                .sum();
    }

    // ── SERIALIZACIÓN ─────────────────────────────
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(deudas);
        } catch (IOException e) {
            System.out.println(
                "Error al guardar deudas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            deudas = (List<Deuda>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                "Error al cargar deudas: " + e.getMessage());
        }
    }
}
