package Modelo;

public class clsMarca {

    private int idMarca;
    private String nombre;
    private int estado;

    public clsMarca() {
    }

    public clsMarca(int idMarca, String nombre, int estado) {
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
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