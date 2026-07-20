package proyectoescuela1.Controlador;

import proyectoescuela1.Modelo.Secretaria;

public class SecretariaControlador {

    private Secretaria secretaria;

    public SecretariaControlador(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    // El registro de matrícula real vive en MatriculaControlador /
    // MatriculaVista. Este wrapper llamaba a un método de Secretaria
    // que ya no existe (usaba un constructor de Matricula obsoleto).

    public void emitirDocumento(String alumno) {
        secretaria.emitirDocumento(alumno);
    }

    public void redactarComunicado(String titulo, String mensaje) {
        secretaria.redactarComunicado(titulo, mensaje);
    }
}