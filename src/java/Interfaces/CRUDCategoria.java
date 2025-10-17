
package Interfaces;

import Modelo.clsCategoria;
import java.util.List;

    
public interface CRUDCategoria {

    List<clsCategoria> mtdListarActivos();

    List<clsCategoria> mtdListarInactivos();

    clsCategoria mtdObtenerPorId(int id);

    List<clsCategoria> mtdBuscar(String texto);

    boolean mtdAgregar(clsCategoria categoria);

    boolean mtdEditar(clsCategoria categoria);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteNombre(String nombre);
}
