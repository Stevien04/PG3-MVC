/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import Modelo.clsCliente;
import java.util.List;

public interface CRUDCliente {

    List<clsCliente> mtdListarActivos();

    List<clsCliente> mtdListarInactivos();

    clsCliente mtdObtenerPorId(int id);

    List<clsCliente> mtdBuscar(String texto);

    boolean mtdAgregar(clsCliente cliente);

    boolean mtdEditar(clsCliente cliente);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteDocumento(String numeroDocumento);

    boolean mtdExisteEmail(String email);
}