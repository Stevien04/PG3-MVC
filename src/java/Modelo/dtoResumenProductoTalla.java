package Modelo;

public class dtoResumenProductoTalla {

    private int idProducto;
    private String nombreProducto;
    private int cantidadProducto;
    private int sumaTallas;
    private int diferencia;

    public dtoResumenProductoTalla() {
    }

    public dtoResumenProductoTalla(int idProducto, String nombreProducto, int cantidadProducto, int sumaTallas) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadProducto = cantidadProducto;
        this.sumaTallas = sumaTallas;
        this.diferencia = cantidadProducto - sumaTallas;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
        this.diferencia = this.cantidadProducto - this.sumaTallas;
    }

    public int getSumaTallas() {
        return sumaTallas;
    }

    public void setSumaTallas(int sumaTallas) {
        this.sumaTallas = sumaTallas;
        this.diferencia = this.cantidadProducto - this.sumaTallas;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }
}