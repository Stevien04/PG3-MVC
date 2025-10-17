package Modelo;

public class clsCargo {
     private int idCargo;
    private String nombre;
    private int estado;

    public clsCargo() { }

    public clsCargo(int idCargo, String nombre, int estado) {
        this.idCargo = idCargo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
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
