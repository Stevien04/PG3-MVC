package Modelo;


public class clsTipoTalla {
    
     private int idTipoTalla;
    private String nombre;
    private int estado;

    public clsTipoTalla() {
    }

    public clsTipoTalla(int idTipoTalla, String nombre, int estado) {
        this.idTipoTalla = idTipoTalla;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdTipoTalla() {
        return idTipoTalla;
    }

    public void setIdTipoTalla(int idTipoTalla) {
        this.idTipoTalla = idTipoTalla;
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
    

