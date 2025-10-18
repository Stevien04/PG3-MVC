package Interfaces;

import Modelo.clsProducto;
import java.util.List;

public interface CRUDProducto {

    List<clsProducto> mtdListarActivos();

    List<clsProducto> mtdListarInactivos();

    clsProducto mtdObtenerPorId(int id);

    List<clsProducto> mtdBuscar(String texto);

    boolean mtdAgregar(clsProducto producto);

    boolean mtdEditar(clsProducto producto);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
}