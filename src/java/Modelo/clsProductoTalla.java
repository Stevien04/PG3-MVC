package Modelo;

public class clsProductoTalla {

    private int idProductoTalla;
    private int idProducto;
    private int idTalla;
    private int cantidad;
    private Integer estado;
    private String nombreProducto;
    private String valorTalla;
    private String nombreTipoTalla;
    private Integer cantidadProducto;

    public clsProductoTalla() {
    }

    public clsProductoTalla(int idProductoTalla, int idProducto, int idTalla, int cantidad, Integer estado) {
        this.idProductoTalla = idProductoTalla;
        this.idProducto = idProducto;
        this.idTalla = idTalla;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public clsProductoTalla(int idProductoTalla, int idProducto, int idTalla, int cantidad, Integer estado,
            String nombreProducto, String valorTalla, String nombreTipoTalla, Integer cantidadProducto) {
        this(idProductoTalla, idProducto, idTalla, cantidad, estado);
        this.nombreProducto = nombreProducto;
        this.valorTalla = valorTalla;
        this.nombreTipoTalla = nombreTipoTalla;
        this.cantidadProducto = cantidadProducto;
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

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getValorTalla() {
        return valorTalla;
    }

    public void setValorTalla(String valorTalla) {
        this.valorTalla = valorTalla;
    }

    public String getNombreTipoTalla() {
        return nombreTipoTalla;
    }

    public void setNombreTipoTalla(String nombreTipoTalla) {
        this.nombreTipoTalla = nombreTipoTalla;
    }

    public Integer getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(Integer cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }
}