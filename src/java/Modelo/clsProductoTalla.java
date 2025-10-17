package Modelo;

public class clsProductoTalla {

    private int idProductoTalla;
    private int idProducto;
    private int idTalla;
    private int cantidad;
    private Integer estado;

    public clsProductoTalla() {
    }

    public clsProductoTalla(int idProductoTalla, int idProducto, int idTalla, int cantidad, Integer estado) {
        this.idProductoTalla = idProductoTalla;
        this.idProducto = idProducto;
        this.idTalla = idTalla;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public int getIdProductoTalla() {
        return idProductoTalla;
    }

    public void setIdProductoTalla(int idProductoTalla) {
        this.idProductoTalla = idProductoTalla;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(int idTalla) {
        this.idTalla = idTalla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}