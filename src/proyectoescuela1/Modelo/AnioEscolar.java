/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de Año Escolar.
 * Agrupa los 4 bimestres de un año lectivo.
 * Es la "raíz" de la configuración temporal del sistema:
 * todo lo que registra fechas (Notas, Asistencia) debe
 * consultar aquí cuál es el bimestre activo.
 */
public class AnioEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── ATRIBUTOS ─────────────────────────────────
    private int anio;
    private List<Bimestre> bimestres = new ArrayList<>();

    // ── CONSTRUCTOR ───────────────────────────────
    public AnioEscolar(int anio) {
        this.anio = anio;
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────

    public void agregarBimestre(Bimestre bimestre) {
        boolean yaExiste = bimestres.stream()
                .anyMatch(b -> b.getNumero() == bimestre.getNumero());
        if (yaExiste) {
            throw new IllegalArgumentException(
                "Ya existe el bimestre " + bimestre.getNumero() +
                " para el año " + anio
            );
        }
        bimestres.add(bimestre);
    }

    public Bimestre buscarPorNumero(int numero) {
        return bimestres.stream()
                .filter(b -> b.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna el bimestre activo, o null si ninguno
     * está activo todavía (por ejemplo, año recién creado).
     */
    public Bimestre getBimestreActivo() {
        return bimestres.stream()
                .filter(Bimestre::estaActivo)
                .findFirst()
                .orElse(null);
    }

    /**
     * Activa un bimestre y desactiva cualquier otro que
     * estuviera activo. Garantiza la regla de negocio:
     * "solo un bimestre activo a la vez".
     */
    public void activarBimestre(int numero) {
        Bimestre objetivo = buscarPorNumero(numero);
        if (objetivo == null) {
            throw new IllegalArgumentException(
                "No existe el bimestre " + numero + " en el año " + anio
            );
        }
        for (Bimestre b : bimestres) {
            if (b.estaActivo()) {
                b.setEstado(Bimestre.CERRADO);
            }
        }
        objetivo.setEstado(Bimestre.ACTIVO);
    }

    public void cerrarBimestre(int numero) {
        Bimestre objetivo = buscarPorNumero(numero);
        if (objetivo != null) {
            objetivo.setEstado(Bimestre.CERRADO);
        }
    }

    // ── GETTERS ───────────────────────────────────
    public int getAnio() { return anio; }
    public List<Bimestre> getBimestres() { return bimestres; }

    @Override
    public String toString() {
        return "Año Escolar " + anio;
    }
}
