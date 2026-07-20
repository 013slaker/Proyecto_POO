/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import proyectoescuela1.Modelo.TarifaPension;

/**
 * Controlador del submódulo "Pensiones": configura el
 * monto mensual de pensión por nivel y grado. El resto del
 * módulo Financiero (Deudas, Pagos) solo lo consulta.
 */
public class TarifaPensionControlador {

    private List<TarifaPension> tarifas = new ArrayList<>();

    private static final String ARCHIVO = "tarifas_pension.dat";

    public TarifaPensionControlador() {
        cargarDatos();
    }

    /** Crea la tarifa si no existe, o actualiza el monto si ya existe. */
    public void guardar(String nivel, String grado, double monto) {
        TarifaPension existente = buscar(nivel, grado);
        if (existente != null) {
            existente.setMonto(monto);
        } else {
            tarifas.add(new TarifaPension(nivel, grado, monto));
        }
        guardarDatos();
    }

    public TarifaPension buscar(String nivel, String grado) {
        return tarifas.stream()
                .filter(t -> t.getNivel().equalsIgnoreCase(nivel)
                        && t.getGrado().equalsIgnoreCase(grado))
                .findFirst()
                .orElse(null);
    }

    /** Retorna el monto configurado, o 0 si no se ha configurado ese nivel/grado. */
    public double obtenerMonto(String nivel, String grado) {
        TarifaPension t = buscar(nivel, grado);
        return t != null ? t.getMonto() : 0.0;
    }

    public List<TarifaPension> listar() {
        return tarifas;
    }

    public List<TarifaPension> listarPorNivel(String nivel) {
        return tarifas.stream()
                .filter(t -> t.getNivel().equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
    }

    // ── SERIALIZACIÓN ─────────────────────────────
    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO))) {
            out.writeObject(tarifas);
        } catch (IOException e) {
            System.out.println(
                "Error al guardar tarifas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO))) {
            tarifas = (List<TarifaPension>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                "Error al cargar tarifas: " + e.getMessage());
        }
    }
}
