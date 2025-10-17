package Interfaces;

import Modelo.clsCargo;
import java.util.List;

public interface CRUDcargo {

   
    public List<clsCargo> mtdListarActivos();
    public List<clsCargo> mtdListarInactivos();
    public clsCargo mtdObtenerPorId(int id);
    public List<clsCargo> mtdBuscar(String texto);
    public boolean mtdAgregar(clsCargo cargo);
    public boolean mtdEditar(clsCargo cargo);
    public boolean mtdCambiarEstado(int id);
    public boolean mtdExisteNombre(String nombre);
}
