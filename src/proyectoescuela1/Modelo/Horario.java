/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

/**
 *
 * @author ALEX
 */
public class Horario {
    private String dia;
    private String horaInicio;
    private String horaFin;

    public Horario(String dia, String horaInicio, String horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Horario{");
        sb.append("dia=").append(dia);
        sb.append(", horaInicio=").append(horaInicio);
        sb.append(", horaFin=").append(horaFin);
        sb.append('}');
        return sb.toString();
    }

    
}
