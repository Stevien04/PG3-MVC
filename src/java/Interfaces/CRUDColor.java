package Interfaces;

import Modelo.clsColor;
import java.util.List;

public interface CRUDColor {
 
    List<clsColor> mtdListarActivos();

    List<clsColor> mtdListarInactivos();

    clsColor mtdObtenerPorId(int id);

    List<clsColor> mtdBuscar(String texto);

    boolean mtdAgregar(clsColor color);

    boolean mtdEditar(clsColor color);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
}