package proyectoescuela1.Modelo;

public class Matricula {

    private String alumno;
    private String grado;
    private String seccion;

    public Matricula(String alumno, String grado, String seccion) {
        this.alumno = alumno;
        this.grado = grado;
        this.seccion = seccion;
    }

    public String getAlumno() {
        return alumno;
    }

    public String getGrado() {
        return grado;
    }

    public String getSeccion() {
        return seccion;
    }
}