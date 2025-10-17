package Modelo;

import java.math.BigDecimal;

public class clsProducto {

    private int idProducto;
    private int idCategoria;
    private Integer idModelo;
    private Integer idColor;
    private int idMarca;
    private String nombre;
    private int cantidad;
    private BigDecimal precioUnitario;
    private int estado;
    private byte[] foto;

    public clsProducto() {
    }

    public clsProducto(int idProducto, int idCategoria, Integer idModelo, Integer idColor, int idMarca,
            String nombre, int cantidad, BigDecimal precioUnitario, int estado, byte[] foto) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.idModelo = idModelo;
        this.idColor = idColor;
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.foto = foto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public Integer getIdColor() {
        return idColor;
    }

    public void setIdColor(Integer idColor) {
        this.idColor = idColor;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}