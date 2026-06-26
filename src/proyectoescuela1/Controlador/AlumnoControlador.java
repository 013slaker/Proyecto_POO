
package proyectoescuela1.Controlador;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Asistencia;
import proyectoescuela1.Modelo.Nota;


public class AlumnoControlador {
   private List<Alumno> alumnos = new ArrayList<>();
   
//boton de registro
public void registrarAlumno(Alumno alumno) {
        alumnos.add(alumno);
        System.out.println("Alumno registrado: " + 
                           alumno.getCodigoAlumno() + " - " + 
                           alumno.getNombreCompleto());
    }
//boton eliminar
public void eliminarAlumno(String codigo) {
        Alumno encontrado = buscarPorCodigo(codigo);
        if (encontrado != null) {
            alumnos.remove(encontrado);
            System.out.println("Alumno eliminado: " + codigo);
        } else {
            System.out.println("Alumno no encontrado: " + codigo);
        }
    }
//boton actualizar 
public void actualizarAlumno(Alumno alumno) {
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getCodigoAlumno()
                       .equals(alumno.getCodigoAlumno())) {
                alumnos.set(i, alumno);
                System.out.println("Alumno actualizado: " + 
                                   alumno.getCodigoAlumno());
                return;
            }
        }
        System.out.println("Alumno no encontrado para actualizar.");
    }

//arreglos para almacenamiento 
public List<Alumno> listarTodos() {
        return alumnos;
    }

    public Alumno buscarPorCodigo(String codigo) {
        for (Alumno a : alumnos) {
            if (a.getCodigoAlumno().equals(codigo)) {
                return a;
            }
        }
        return null;
    }
//metodos de busqueda
    public Alumno buscarPorDni(String dni) {
        for (Alumno a : alumnos) {
            if (a.getDni().equals(dni)) {
                return a;
            }
        }
        return null;
    }

    public List<Alumno> buscarPorNivel(String nivel) {
        List<Alumno> resultado = new ArrayList<>();
        for (Alumno a : alumnos) {
            if (a.getNivel().equals(nivel)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<Alumno> buscarPorNombre(String nombre) {
        List<Alumno> resultado = new ArrayList<>();
        for (Alumno a : alumnos) {
            if (a.getNombreCompleto()
                 .toLowerCase()
                 .contains(nombre.toLowerCase())) {
                resultado.add(a);
            }
        }
        return resultado;
    }
    
    public void agregarNota(String codigoAlumno, Nota nota) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno != null) {
            alumno.getNotas().add(nota);
            System.out.println("Nota agregada al alumno: " + 
                               codigoAlumno);
        }
    }
/*
    public double calcularPromedioGeneral(String codigoAlumno) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno == null || alumno.getNotas().isEmpty()) 
            return 0.0;
        double suma = 0;
        for (Nota n : alumno.getNotas()) {
            suma += n.getValor();
        }
        return suma / alumno.getNotas().size();
    }

    public double calcularPromedioBimestre(
            String codigoAlumno, int bimestre) {
        Alumno alumno = buscarPorCodigo(codigoAlumno);
        if (alumno == null) return 0.0;
        List<Nota> notasBim = new ArrayList<>();
        for (Nota n : alumno.getNotas()) {
            if (n.getBimestre() == bimestre) {
                notasBim.add(n);
            }
        }
        if (notasBim.isEmpty()) return 0.0;
        double suma = 0;
        for (Nota n : notasBim) suma += n.getValor();
        return suma / notasBim.size();
    }

    public boolean estaAprobado(String codigoAlumno) {
        return calcularPromedioGeneral(codigoAlumno) >= 11;
    }

    // Ranking de alumnos por promedio — uso de Map
    public Map<String, Double> rankingPromedios() {
        Map<String, Double> ranking = new HashMap<>();
        for (Alumno a : alumnos) {
            ranking.put(
                a.getNombreCompleto(),
                calcularPromedioGeneral(a.getCodigoAlumno())
            );
        }
        return ranking;
    }*/



}
