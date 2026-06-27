/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Controlador;

import proyectoescuela1.Modelo.Docente;
import java.util.ArrayList;
import java.util.List;

public class DocenteControlador {
   private List<Docente> lista = new ArrayList<>();

    public void registrar(Docente d) {
        lista.add(d);
    }

    public void eliminar(String codigo) {
        lista.removeIf(d -> d.getCodigoDocente().equals(codigo));
    }

    public List<Docente> listar() {
        return lista;
    }

    public List<Docente> buscarPorNombre(String nombre) {
        List<Docente> resultado = new ArrayList<>();

        for (Docente d : lista) {
            if (d.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(d);
            }
        }
        return resultado;
    }
}