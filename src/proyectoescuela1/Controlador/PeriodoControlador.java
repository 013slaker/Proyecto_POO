/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import java.io.*;
import java.util.Date;
import proyectoescuela1.Modelo.AnioEscolar;
import proyectoescuela1.Modelo.Bimestre;

/**
 * Controlador del módulo Administrativo → Período Académico.
 *
 * Es la única fuente de verdad sobre qué bimestre está
 * activo. NotaControlador y AsistenciaControlador consultan
 * este controlador antes de guardar cualquier registro,
 * para que no se pueda escribir en un bimestre fuera de fecha.
 */
public class PeriodoControlador {

    private AnioEscolar anioActivo;

    private static final String ARCHIVO = "periodoAcademico.dat";

    public PeriodoControlador() {
        cargarDatos();
    }

    // ══════════════════════════════════════════════
    //  CONFIGURACIÓN DEL AÑO ESCOLAR
    // ══════════════════════════════════════════════

    /**
     * Crea un nuevo año escolar con sus 4 bimestres en
     * blanco (estado Planificado). El director/secretaría
     * define luego las fechas de cada uno.
     */
    public AnioEscolar crearAnioEscolar(int anio) {
        anioActivo = new AnioEscolar(anio);
        guardarDatos();
        return anioActivo;
    }

    public AnioEscolar getAnioEscolar() {
        return anioActivo;
    }

    public void definirFechasBimestre(int numero, Date inicio, Date fin) {
        if (anioActivo == null) {
            throw new IllegalStateException(
                "Primero debe crear un Año Escolar."
            );
        }
        if (fin.before(inicio)) {
            throw new IllegalArgumentException(
                "La fecha de fin no puede ser anterior al inicio."
            );
        }
        Bimestre b = anioActivo.buscarPorNumero(numero);
        if (b == null) {
            b = new Bimestre(numero, inicio, fin);
            anioActivo.agregarBimestre(b);
        } else {
            b.setFechaInicio(inicio);
            b.setFechaFin(fin);
        }
        guardarDatos();
    }

    /**
     * Activa un bimestre (y cierra el que estuviera activo).
     * Esta es la única forma correcta de "cambiar de bimestre";
     * nunca se debe permitir editar notas de dos bimestres
     * activos al mismo tiempo.
     */
    public void activarBimestre(int numero) {
        if (anioActivo == null) {
            throw new IllegalStateException(
                "No hay un Año Escolar configurado."
            );
        }
        anioActivo.activarBimestre(numero);
        guardarDatos();
    }

    public void cerrarBimestre(int numero) {
        if (anioActivo != null) {
            anioActivo.cerrarBimestre(numero);
            guardarDatos();
        }
    }

    public Bimestre getBimestreActivo() {
        return anioActivo == null ? null : anioActivo.getBimestreActivo();
    }

    /**
     * Activa el bimestre cuyo rango de fechas contiene la fecha dada.
     * Útil para datos de prueba/históricos: en vez de forzar un
     * número de bimestre, se activa el que realmente corresponde
     * a esa fecha según la configuración del Año Escolar.
     */
    public boolean activarBimestreQueContiene(Date fecha) {
        if (anioActivo == null) return false;
        for (Bimestre b : anioActivo.getBimestres()) {
            if (b.contieneFecha(fecha)) {
                activarBimestre(b.getNumero());
                return true;
            }
        }
        return false;
    }

    // ══════════════════════════════════════════════
    //  VALIDACIONES — usadas por Notas y Asistencia
    // ══════════════════════════════════════════════

    /**
     * ¿Se puede registrar/editar una nota en este número
     * de bimestre? Solo si coincide con el bimestre activo.
     */
    public boolean puedeRegistrarEnBimestre(int numeroBimestre) {
        Bimestre activo = getBimestreActivo();
        return activo != null && activo.getNumero() == numeroBimestre;
    }

    /**
     * ¿Se puede registrar asistencia en esta fecha?
     * Válido solo si la fecha cae dentro del rango del
     * bimestre actualmente activo.
     */
    public boolean puedeRegistrarEnFecha(Date fecha) {
        Bimestre activo = getBimestreActivo();
        return activo != null && activo.contieneFecha(fecha);
    }

    /**
     * Lanza excepción si no se puede registrar en ese bimestre.
     * Pensado para usarse al inicio de registrar()/actualizar()
     * en NotaControlador.
     */
    public void validarBimestre(int numeroBimestre) {
        if (!puedeRegistrarEnBimestre(numeroBimestre)) {
            Bimestre activo = getBimestreActivo();
            String detalle = activo == null
                ? "no hay ningún bimestre activo."
                : "el bimestre activo es el " + activo.getNumero() + ".";
            throw new IllegalStateException(
                "No se puede registrar en el bimestre " + numeroBimestre +
                ": " + detalle
            );
        }
    }

    /**
     * Lanza excepción si la fecha no cae en el bimestre activo.
     * Pensado para usarse en AsistenciaControlador.
     */
    public void validarFecha(Date fecha) {
        if (!puedeRegistrarEnFecha(fecha)) {
            throw new IllegalStateException(
                "No se puede registrar asistencia: la fecha está " +
                "fuera del bimestre activo."
            );
        }
    }

    // ══════════════════════════════════════════════
    //  SERIALIZACIÓN
    // ══════════════════════════════════════════════

    public void guardarDatos() {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            out.writeObject(anioActivo);
        } catch (IOException e) {
            System.out.println("Error al guardar período académico: " +
                    e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            anioActivo = (AnioEscolar) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar período académico: " +
                    e.getMessage());
        }
    }
}
