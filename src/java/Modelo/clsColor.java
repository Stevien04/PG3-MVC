
package Modelo;


public class clsColor {
    
    private int idColor;
    private String nombre;
    private int estado;

    public clsColor() {
    }

    public clsColor(int idColor, String nombre, int estado) {
        this.idColor = idColor;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
}
