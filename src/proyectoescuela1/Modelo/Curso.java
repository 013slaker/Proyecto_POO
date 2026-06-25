/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

/**
 *
 * @author ALEX
 */
public class Curso {
    private String idCurso;
    private String Nombre;
    private String area;
    private int horasSemanales;

    public Curso(String idCurso, String Nombre, String area, int horasSemanales) {
        this.idCurso = idCurso;
        this.Nombre = Nombre;
        this.area = area;
        this.horasSemanales = horasSemanales;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Curso{");
        sb.append("idCurso=").append(idCurso);
        sb.append(", Nombre=").append(Nombre);
        sb.append(", area=").append(area);
        sb.append(", horasSemanales=").append(horasSemanales);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
