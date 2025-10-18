package Modelo;

public class clsModelo {
    private int idModelo;
    private int idMarca;
    private String nombre;
    private int estado;
    private clsMarca marca; // ← ESTA es la nueva propiedad

    public clsModelo() {
    }

    public clsModelo(int idModelo, int idMarca, String nombre, int estado) {
        this.idModelo = idModelo;
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
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

    // ↓↓↓ Aquí el método que te falta ↓↓↓
    public clsMarca getMarca() {
        return marca;
    }

    public void setMarca(clsMarca marca) {
        this.marca = marca;
    }
}
