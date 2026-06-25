/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;
import java.util.ArrayList;
import proyectoescuela1.Modelo.Curso;
import proyectoescuela1.Modelo.Horario;
import proyectoescuela1.Modelo.Seccion;
/**
 *
 * @author ALEX
 */
public class controladorEscuela {
    private ArrayList <Curso> cursos;
    private ArrayList <Horario> horarios;
    private ArrayList <Seccion> secciones;

    public controladorEscuela() {
        this.cursos = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.secciones = new ArrayList<>();
    }
    
    //agrega un curso
    public void agregarCurso (Curso curso){
    cursos.add(curso);  
    }
    // buscar el nombre de cada curso//
    
    public String listarCursos(){
        String resultado = "";
        for(Curso curso:cursos){
        resultado = resultado+curso.toString()+"\n";
        }
    return resultado;
    }
    
    
    
    
    
}
