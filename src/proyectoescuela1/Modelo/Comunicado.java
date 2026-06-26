package proyectoescuela1.Modelo;

public class Comunicado {

    private String titulo;
    private String mensaje;

    public Comunicado(String titulo, String mensaje) {
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }
}