package Interfaces;

import Modelo.clsMarca;
import java.util.List;

public interface CRUDMarca {

    List<clsMarca> mtdListarActivos();

    List<clsMarca> mtdListarInactivos();

    clsMarca mtdObtenerPorId(int id);

    List<clsMarca> mtdBuscar(String texto);

    boolean mtdAgregar(clsMarca marca);

    boolean mtdEditar(clsMarca marca);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
}