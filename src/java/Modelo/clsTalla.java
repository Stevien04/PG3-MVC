/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author HP
 */
public class clsTalla {
     private int idTalla;
    private int idTipoTalla;
    private String valor;
    private int estado;
    private clsTipoTalla tipoTalla;

    public clsTalla() {
    }

    public clsTalla(int idTalla, int idTipoTalla, String valor, int estado) {
        this.idTalla = idTalla;
        this.idTipoTalla = idTipoTalla;
        this.valor = valor;
        this.estado = estado;
    }

    public int getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(int idTalla) {
        this.idTalla = idTalla;
    }

    public int getIdTipoTalla() {
        return idTipoTalla;
    }

    public void setIdTipoTalla(int idTipoTalla) {
        this.idTipoTalla = idTipoTalla;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public clsTipoTalla getTipoTalla() {
        return tipoTalla;
    }

    public void setTipoTalla(clsTipoTalla tipoTalla) {
        this.tipoTalla = tipoTalla;
    }
    
}
