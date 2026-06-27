package proyectoescuela1.Controlador;

import java.util.ArrayList;
import java.util.List;
import proyectoescuela1.Modelo.Apoderado;

public class ApoderadoControlador {

    // "Base de datos temporal en memoria"
    private List<Apoderado> lista = new ArrayList<>();

    // ─────────────────────────────
    // REGISTRAR APODERADO
    // ─────────────────────────────
    public void registrar(Apoderado a) {
        lista.add(a);
    }

    // ─────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────
    public List<Apoderado> listar() {
        return lista;
    }

    // ─────────────────────────────
    // ELIMINAR POR CÓDIGO
    // ─────────────────────────────
    public void eliminar(String codigo) {

        lista.removeIf(a ->
                a.getCodigoApoderado().equals(codigo)
        );
    }

    // ─────────────────────────────
    // BUSCAR POR DNI (opcional útil)
    // ─────────────────────────────
    public Apoderado buscarPorDni(String dni) {

        for (Apoderado a : lista) {
            if (a.getDni().equals(dni)) {
                return a;
            }
        }
        return null;
    }
}