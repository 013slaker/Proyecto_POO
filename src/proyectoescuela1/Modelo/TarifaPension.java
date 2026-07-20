/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;

/**
 * Monto de la pensión mensual según nivel y grado.
 * Se configura una vez desde el submódulo "Pensiones" y
 * el resto del módulo Financiero (Deudas, Pagos) lo
 * consulta para saber cuánto debe pagar cada alumno.
 */
public class TarifaPension implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nivel; // Primaria | Secundaria
    private String grado; // "1°".."6°"
    private double monto;  // monto mensual en soles

    public TarifaPension(String nivel, String grado, double monto) {
        this.nivel = nivel;
        this.grado = grado;
        setMonto(monto);
    }

    public String getNivel() { return nivel; }
    public String getGrado() { return grado; }
    public double getMonto() { return monto; }

    public void setMonto(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException(
                "El monto no puede ser negativo");
        }
        this.monto = monto;
    }

    @Override
    public String toString() {
        return nivel + " " + grado + " - S/ "
                + String.format("%.2f", monto);
    }
}
