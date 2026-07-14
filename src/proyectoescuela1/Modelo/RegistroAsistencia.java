/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Registro de Asistencia.
 *
 * Representa el registro de asistencia realizado por un docente para un curso,
 * grado y sección en una fecha determinada.
 *
 * Un RegistroAsistencia contiene muchas Asistencias.
 *
 * Implementa Serializable para guardar la información en archivos.
 */
public class RegistroAsistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    //==================================================
    // GENERADOR DE CÓDIGO AUTOMÁTICO
    //==================================================
    private static int contador = 1;

    private static String generarCodigo() {
        return "REGASI" + String.format("%04d", contador++);
    }

    //==================================================
    // ATRIBUTOS
    //==================================================
    private String codigoRegistro;
    private Date fecha;
    private String codigoCurso;
    private String nombreCurso;
    private String nivel;
    private String grado;
    private String seccion;
    private String codigoDocente;
    private String nombreDocente;

    // Composición
    private List<Asistencia> listaAsistencias;

    //==================================================
    // CONSTRUCTOR
    //==================================================
    public RegistroAsistencia(
            Date fecha,
            String codigoCurso,
            String nombreCurso,
            String nivel,
            String grado,
            String seccion,
            String codigoDocente,
            String nombreDocente) {

        this.codigoRegistro = generarCodigo();
        this.fecha = fecha;
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.nivel = nivel;
        this.grado = grado;
        this.seccion = seccion;
        this.codigoDocente = codigoDocente;
        this.nombreDocente = nombreDocente;

        this.listaAsistencias = new ArrayList<>();
    }

    //==================================================
    // MÉTODOS DE NEGOCIO
    //==================================================
    /**
     * Agrega una asistencia al registro.
     */
    public void agregarAsistencia(Asistencia asistencia) {
        listaAsistencias.add(asistencia);
    }

    /**
     * Elimina una asistencia usando Iterator.
     */
    public void eliminarAsistencia(String idAsistencia) {

        Iterator<Asistencia> it = listaAsistencias.iterator();

        while (it.hasNext()) {

            Asistencia a = it.next();

            if (a.getIdAsistencia().equals(idAsistencia)) {

                it.remove();
                return;

            }

        }

    }

    /**
     * Retorna el Iterator de la lista.
     */
    public Iterator<Asistencia> iterator() {
        return listaAsistencias.iterator();
    }

    /**
     * Total de alumnos registrados.
     */
    public int totalAlumnos() {
        return listaAsistencias.size();
    }

    /**
     * Total de presentes.
     */
    public long totalPresentes() {

        return listaAsistencias.stream()
                .filter(Asistencia::esPresente)
                .count();

    }

    /**
     * Total de faltas.
     */
    public long totalFaltas() {

        return listaAsistencias.stream()
                .filter(a -> a.getEstado().equals("F"))
                .count();

    }

    /**
     * Total de tardanzas.
     */
    public long totalTardanzas() {

        return listaAsistencias.stream()
                .filter(a -> a.getEstado().equals("T"))
                .count();

    }

    /**
     * Total de justificadas.
     */
    public long totalJustificadas() {

        return listaAsistencias.stream()
                .filter(a -> a.getEstado().equals("J"))
                .count();

    }

    /**
     * Porcentaje de asistencia.
     */
    public double porcentajeAsistencia() {

        if (listaAsistencias.isEmpty()) {
            return 0;
        }

        return (totalPresentes() * 100.0)
                / listaAsistencias.size();

    }

    /**
     * Fecha en formato dd/MM/yyyy.
     */
    public String getFechaFormateada() {

        SimpleDateFormat sdf
                = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(fecha);

    }

    //==================================================
    // GETTERS
    //==================================================
    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public String getNivel() {
        return nivel;
    }

    public String getGrado() {
        return grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getCodigoDocente() {
        return codigoDocente;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public List<Asistencia> getListaAsistencias() {
        return listaAsistencias;
    }

    //==================================================
    // SETTERS
    //==================================================
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public void setCodigoDocente(String codigoDocente) {
        this.codigoDocente = codigoDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    //==================================================
    // TOSTRING
    //==================================================
    @Override
    public String toString() {

        return "RegistroAsistencia{"
                + "codigo='" + codigoRegistro + '\''
                + ", fecha=" + getFechaFormateada()+'\''
                + ", curso='" + nombreCurso + '\''
                + ", nivel='" + nivel + '\''
                + ", grado='" + grado + '\''
                + ", seccion='" + seccion + '\''
                + ", docente='" + nombreDocente + '\''
                + ", alumnos=" + totalAlumnos()
                + '}';

    }

}
