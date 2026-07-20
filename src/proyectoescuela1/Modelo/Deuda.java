/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Deuda de pensión de un alumno en un mes/año específico.
 * Nunca se crea manualmente: la genera
 * DeudaControlador.actualizarDeudasAutomaticas() cuando
 * detecta que ya venció el plazo y no hay un Pago
 * registrado para ese mes.
 */
public class Deuda implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contador = 1;

    private static String generarCodigo() {
        return "DEU" + String.format("%04d", contador++);
    }

    private String idDeuda;
    private String codigoAlumno;
    private int anio;
    private int mes;
    private double montoPension; // ya con descuento aplicado
    private LocalDate fechaVencimiento;
    private boolean pagada;

    public Deuda(String codigoAlumno, int anio, int mes,
            double montoPension, LocalDate fechaVencimiento) {
        this.idDeuda = generarCodigo();
        this.codigoAlumno = codigoAlumno;
        this.anio = anio;
        this.mes = mes;
        this.montoPension = montoPension;
        this.fechaVencimiento = fechaVencimiento;
        this.pagada = false;
    }

    public void marcarPagada() { this.pagada = true; }

    /** Días de atraso respecto a la fecha de vencimiento (0 si aún no vence o ya está pagada). */
    public long getDiasAtraso() {
        if (pagada) return 0;
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(fechaVencimiento)) return 0;
        return ChronoUnit.DAYS.between(fechaVencimiento, hoy);
    }

    public String getIdDeuda() { return idDeuda; }
    public String getCodigoAlumno() { return codigoAlumno; }
    public int getAnio() { return anio; }
    public int getMes() { return mes; }
    public double getMontoPension() { return montoPension; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isPagada() { return pagada; }

    @Override
    public String toString() {
        return idDeuda + " - " + codigoAlumno
                + " - " + mes + "/" + anio
                + " - S/ " + String.format("%.2f", montoPension)
                + (pagada ? " (pagada)" : " (pendiente)");
    }
}
