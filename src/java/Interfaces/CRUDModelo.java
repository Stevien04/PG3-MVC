/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;
import Modelo.clsModelo;
import java.util.List;

/**
 *
 * @author HP
 */
public interface CRUDModelo {
    
    List<clsModelo> mtdListarActivos();

    List<clsModelo> mtdListarInactivos();

    clsModelo mtdObtenerPorId(int id);

    List<clsModelo> mtdBuscar(String texto);

    boolean mtdAgregar(clsModelo modelo);

    boolean mtdEditar(clsModelo modelo);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
    
}
