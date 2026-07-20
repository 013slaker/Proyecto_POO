/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Registro de un pago de pensión de un alumno
 * correspondiente a un mes/año específico.
 */
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contador = 1;

    private static String generarCodigo() {
        return "PAG" + String.format("%04d", contador++);
    }

    private String idPago;
    private String codigoAlumno;
    private int anio;
    private int mes; // 1 a 12
    private double montoPagado;
    private LocalDate fechaPago;
    private String metodoPago; // Efectivo | Transferencia | Tarjeta | Yape/Plin

    public Pago(String codigoAlumno, int anio, int mes,
            double montoPagado, String metodoPago) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException(
                "El mes debe estar entre 1 y 12");
        }
        this.idPago = generarCodigo();
        this.codigoAlumno = codigoAlumno;
        this.anio = anio;
        this.mes = mes;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.fechaPago = LocalDate.now();
    }

    public String getIdPago() { return idPago; }
    public String getCodigoAlumno() { return codigoAlumno; }
    public int getAnio() { return anio; }
    public int getMes() { return mes; }
    public double getMontoPagado() { return montoPagado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public String getMetodoPago() { return metodoPago; }

    @Override
    public String toString() {
        return idPago + " - " + codigoAlumno
                + " - " + mes + "/" + anio
                + " - S/ " + String.format("%.2f", montoPagado);
    }
}
