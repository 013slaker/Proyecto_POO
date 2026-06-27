package proyectoescuela1.Controlador;

import proyectoescuela1.Modelo.Secretaria;

public class SecretariaControlador {

    private Secretaria secretaria;

    public SecretariaControlador(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public void registrarMatricula(String alumno, String grado, String seccion) {
        secretaria.registrarMatricula(alumno, grado, seccion);
    }

    public void emitirDocumento(String alumno) {
        secretaria.emitirDocumento(alumno);
    }

    public void redactarComunicado(String titulo, String mensaje) {
        secretaria.redactarComunicado(titulo, mensaje);
    }
}