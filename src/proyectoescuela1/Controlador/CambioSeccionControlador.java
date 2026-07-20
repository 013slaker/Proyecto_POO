package proyectoescuela1.Controlador;

import java.util.List;
import proyectoescuela1.Modelo.Alumno;
import proyectoescuela1.Modelo.Matricula;

/**
 * ==========================================================
 * CONTROLADOR DE CAMBIO DE SECCIÓN
 * ----------------------------------------------------------
 * Módulo Administrativo -> Cambio de Sección.
 *
 * Traslada uno o varios alumnos de un grado/sección de
 * origen hacia un grado/sección de destino, actualizando
 * tanto al Alumno como su matrícula vigente del año escolar
 * activo (para que el historial quede coherente).
 * ==========================================================
 */
public class CambioSeccionControlador {

    private final AlumnoControlador alumnoCtrl;
    private final MatriculaControlador matriculaCtrl;
    private final PeriodoControlador periodoCtrl;

    public CambioSeccionControlador(AlumnoControlador alumnoCtrl,
            MatriculaControlador matriculaCtrl,
            PeriodoControlador periodoCtrl) {
        this.alumnoCtrl = alumnoCtrl;
        this.matriculaCtrl = matriculaCtrl;
        this.periodoCtrl = periodoCtrl;
    }

    /**
     * Lista los alumnos activos que actualmente están en el nivel,
     * grado y sección indicados (los candidatos a trasladar).
     *
     * Se filtra también por NIVEL porque el mismo texto de grado
     * (ej. "1°") existe tanto en Primaria como en Secundaria; sin el
     * nivel se mezclarían alumnos de niveles distintos.
     */
    public List<Alumno> listarAlumnosDeSeccion(String nivel, String grado,
            String seccion) {
        return alumnoCtrl.buscarPorGradoSeccion(grado, seccion)
                .stream()
                .filter(Alumno::isEstadoActivo)
                .filter(a -> a.getNivel().equalsIgnoreCase(nivel))
                .toList();
    }

    /**
     * Traslada un único alumno hacia el nuevo nivel/grado/sección.
     * Actualiza el registro del Alumno y, si existe, la matrícula
     * vigente del año escolar activo. Permite cambiar el nivel para
     * cubrir la promoción de 6° de Primaria a 1° de Secundaria.
     */
    public void trasladarAlumno(Alumno alumno, String nuevoNivel,
            String nuevoGrado, String nuevaSeccion) {

        if (nuevoNivel == null || nuevoNivel.isBlank()
                || nuevoGrado == null || nuevoGrado.isBlank()
                || nuevaSeccion == null || nuevaSeccion.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe indicar el nivel, grado y sección de destino.");
        }

        // 1) Actualiza al alumno
        alumno.setNivel(nuevoNivel);
        alumno.setGrado(nuevoGrado);
        alumno.setSeccion(nuevaSeccion);
        alumnoCtrl.actualizarAlumno(alumno);

        // 2) Actualiza su matrícula vigente (si hay año activo)
        if (periodoCtrl.getAnioEscolar() != null) {
            int anio = periodoCtrl.getAnioEscolar().getAnio();
            Matricula vigente = matriculaCtrl.buscarMatriculaVigente(
                    alumno.getCodigoAlumno(), anio);
            if (vigente != null) {
                matriculaCtrl.reasignarNivelGradoSeccion(
                        vigente.getCodigoMatricula(), nuevoNivel,
                        nuevoGrado, nuevaSeccion);
            }
        }
    }

    /**
     * Traslada una lista completa de alumnos hacia el mismo
     * nivel/grado/sección de destino. Devuelve cuántos se trasladaron
     * correctamente (permite continuar aunque alguno falle).
     */
    public int trasladarAlumnos(List<Alumno> alumnos, String nuevoNivel,
            String nuevoGrado, String nuevaSeccion) {

        int exitosos = 0;
        for (Alumno alumno : alumnos) {
            try {
                trasladarAlumno(alumno, nuevoNivel, nuevoGrado, nuevaSeccion);
                exitosos++;
            } catch (Exception e) {
                System.out.println("No se pudo trasladar a "
                        + alumno.getNombreCompleto() + ": " + e.getMessage());
            }
        }
        return exitosos;
    }
}
