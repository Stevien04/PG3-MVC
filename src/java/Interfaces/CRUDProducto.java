package Interfaces;

import Modelo.clsProducto;
import java.util.List;

public interface CRUDProducto {

    List<clsProducto> mtdListarPorEstado(int estado);

    List<clsProducto> mtdBuscar(String texto);

    clsProducto mtdObtenerPorId(int idProducto);

    boolean mtdAgregar(clsProducto producto);

    boolean mtdEditar(clsProducto producto);

    boolean mtdCambiarEstado(int idProducto);
}