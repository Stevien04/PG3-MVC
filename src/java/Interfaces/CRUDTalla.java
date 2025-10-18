/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Modelo.clsTalla;
import java.util.List;

public interface CRUDTalla {
    

    List<clsTalla> mtdListarActivos();

    List<clsTalla> mtdListarInactivos();

    clsTalla mtdObtenerPorId(int id);

    List<clsTalla> mtdBuscar(String texto);

    boolean mtdAgregar(clsTalla talla);

    boolean mtdEditar(clsTalla talla);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteValor(int idTipoTalla, String valor, Integer idExcluir);
}