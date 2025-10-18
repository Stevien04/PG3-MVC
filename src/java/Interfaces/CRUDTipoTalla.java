package Interfaces;

import Modelo.clsTipoTalla;
import java.util.List;

public interface CRUDTipoTalla {
    

    List<clsTipoTalla> mtdListarActivos();

    List<clsTipoTalla> mtdListarInactivos();

    clsTipoTalla mtdObtenerPorId(int id);

    List<clsTipoTalla> mtdBuscar(String texto);

    boolean mtdAgregar(clsTipoTalla tipoTalla);

    boolean mtdEditar(clsTipoTalla tipoTalla);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
}