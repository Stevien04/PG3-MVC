package Interfaces;

import Modelo.clsCliente;
import java.util.List;
import java.util.Optional;

public interface CRUDCliente {

    List<clsCliente> mtdListarActivos();

    List<clsCliente> mtdListarInactivos();

    clsCliente mtdObtenerPorId(int id);

    List<clsCliente> mtdBuscar(String texto);

    boolean mtdAgregar(clsCliente cliente);

    boolean mtdEditar(clsCliente cliente);

    boolean mtdCambiarEstado(int id);

    boolean mtdExisteDocumento(String numeroDocumento);

    boolean mtdExisteEmail(String email);

    Optional<clsCliente> mtdBuscarPorEmail(String email);
}