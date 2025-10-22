package Modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class clsItemCarrito {

    private int idProducto;
    private String nombre;
    private BigDecimal precioUnitario;
    private int cantidad;
    private int stockDisponible;
    private String nombreCategoria;
    private String nombreMarca;
    private String nombreModelo;
    private String nombreColor;
    private String fotoBase64;

    public clsItemCarrito() {
        this.precioUnitario = BigDecimal.ZERO;
    }

    public clsItemCarrito(int idProducto, String nombre, BigDecimal precioUnitario, int cantidad,
                          int stockDisponible, String nombreCategoria, String nombreMarca,
                          String nombreModelo, String nombreColor, String fotoBase64) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
        this.cantidad = cantidad;
        this.stockDisponible = stockDisponible;
        this.nombreCategoria = nombreCategoria;
        this.nombreMarca = nombreMarca;
        this.nombreModelo = nombreModelo;
        this.nombreColor = nombreColor;
        this.fotoBase64 = fotoBase64;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public String getNombreColor() {
        return nombreColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public BigDecimal getSubtotal() {
        return getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
    }
}