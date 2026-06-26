package proyectoescuela1.Modelo;

import java.util.Date;
import javax.swing.JOptionPane;

public class Secretaria extends Usuario{

    public Secretaria(int id, String nombre, String apellidos, String dni, String email, String telefono, String direccion, Date fechaNac) {
        super(id, nombre, apellidos, dni, email, telefono, direccion, fechaNac);
    }

    @Override
    public String toString() {
        return "Secretaria{" + '}';
    }

    
    

   
    
    

    public void registrarMatricula(String alumno, String grado, String seccion) {

        Matricula m = new Matricula(alumno, grado, seccion);

        JOptionPane.showMessageDialog(null,
                "Alumno: " + m.getAlumno()
                + "\nGrado: " + m.getGrado()
                + "\nSección: " + m.getSeccion()
                + "\n\nMatrícula registrada correctamente");
    }

    public void emitirDocumento(String alumno) {

        JOptionPane.showMessageDialog(null,
                "Constancia emitida para: " + alumno);
    }

    public void redactarComunicado(String titulo, String mensaje) {

        Comunicado c = new Comunicado(titulo, mensaje);

        JOptionPane.showMessageDialog(null,
                "COMUNICADO\n\n"
                + c.getTitulo()
                + "\n\n"
                + c.getMensaje());
    }
}
//ddddd