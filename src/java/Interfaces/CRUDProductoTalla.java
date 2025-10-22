package Interfaces;

import Modelo.clsProductoTalla;
import java.util.List;

public interface CRUDProductoTalla {

    List<clsProductoTalla> mtdListar();

    clsProductoTalla mtdObtenerPorId(int id);

    boolean mtdAgregar(clsProductoTalla productoTalla);

    boolean mtdActualizar(clsProductoTalla productoTalla, int idProductoAnterior);

    boolean mtdCambiarEstado(int idProductoTalla);

    boolean mtdExisteCombinacion(int idProducto, int idTalla, Integer idExcluir);
}