package Interfaces;

import Modelo.clsEmpleado;
import java.util.List;

public interface CRUDEmpleado {

    List<clsEmpleado> mtdListarActivos();

    List<clsEmpleado> mtdListarInactivos();

    clsEmpleado mtdObtenerPorId(int id);

    List<clsEmpleado> mtdBuscar(String texto);

    boolean mtdAgregar(clsEmpleado empleado);

    boolean mtdEditar(clsEmpleado empleado);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteUsuario(String usuario);

    boolean mtdExisteDocumento(String numeroDocumento);
}