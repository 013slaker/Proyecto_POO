package proyectoescuela1.Modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Usuario  implementa Serializable.
 *
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int id;
    protected String nombre;
    protected String apellidos;
    protected String dni;
    protected String email;
    protected String telefono;
    protected String direccion;
    protected Date fechaNac;


    protected Usuario() {
    }

    // Constructor
    public Usuario(int id, String nombre, String apellidos,
            String dni, String email, String telefono,
            String direccion, Date fechaNac) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNac = fechaNac;
    }

    @Override
    public abstract String toString();

   
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public int getEdad() {
        Date hoy = new Date();
        long diferencia = hoy.getTime() - fechaNac.getTime();
        return (int) (diferencia / (1000L * 60 * 60 * 24 * 365));
    }

    
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }
  
    
    
    
    
}
