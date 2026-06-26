/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Modelo;

import proyectoescuela1.Interfaces.Autenticable;

/**
 *
 * @author USER
 */
public class Cuenta implements Autenticable {

    private int id;
    private String usuario;
    private String contrasena;
    private String rol;
    private boolean activo;
    private int intentosFallidos;

    // Constructor
    public Cuenta(int id, String usuario,
            String contrasena, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = true;
        this.intentosFallidos = 0;
    }

    // ── IMPLEMENTACIÓN DE Autenticable ────────────
    @Override
    public boolean login(String usuario, String clave) {
        if (!activo) {
            System.out.println("Cuenta bloqueada.");
            return false;
        }
        if (this.usuario.equals(usuario)
                && this.contrasena.equals(clave)) {
            intentosFallidos = 0;
            System.out.println("Login exitoso: " + usuario);
            return true;
        } else {
            intentosFallidos++;
            System.out.println("Credenciales incorrectas. "
                    + "Intento " + intentosFallidos + " de 3");
            if (intentosFallidos >= 3) {
                bloquear();
            }
            return false;
        }
    }

    @Override
    public void logout() {
        System.out.println("Sesión cerrada: " + usuario);
    }

    @Override
    public void cambiarClave(String nueva) {
        if (nueva == null || nueva.length() < 6) {
            System.out.println(
                    "La clave debe tener al menos 6 caracteres."
            );
            return;
        }
        this.contrasena = nueva;
        System.out.println("Clave actualizada correctamente.");
    }

    public void bloquear() {
        this.activo = false;
        System.out.println("Cuenta bloqueada por "
                + "intentos fallidos: " + usuario);
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
        this.intentosFallidos = 0;
    }

    @Override
    public String toString() {
        return "Cuenta{usuario='" + usuario + "'"
                + ", rol='" + rol + "'"
                + ", activo=" + activo + "}";
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
