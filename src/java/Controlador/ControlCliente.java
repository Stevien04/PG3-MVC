package Controlador;

import Modelo.clsCliente;
import Modelo.clsTipoDocumento;
import ModeloDao.clsDAOCliente;
import ModeloDao.clsDAOTipoDocumento;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvCliente", urlPatterns = {"/srvCliente"})
public class ControlCliente extends HttpServlet {

    private final clsDAOCliente daoCliente = new clsDAOCliente();
    private final clsDAOTipoDocumento daoTipoDocumento = new clsDAOTipoDocumento();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarClientes(request, response, true);
                break;

            case "listarInactivos":
                listarClientes(request, response, false);
                break;

            case "editar":
                int idEditar = parseEntero(request.getParameter("id"), -1);
                if (idEditar <= 0) {
                    response.sendRedirect("srvCliente?accion=listarActivos");
                    return;
                }
                clsCliente clienteEditar = daoCliente.mtdObtenerPorId(idEditar);
                if (clienteEditar == null) {
                    response.sendRedirect("srvCliente?accion=listarActivos");
                    return;
                }
                request.setAttribute("cliente", clienteEditar);
                listarClientes(request, response, true);
                break;

            case "eliminar":
                int idEliminar = parseEntero(request.getParameter("id"), -1);
                if (idEliminar > 0) {
                    daoCliente.mtdCambiarEstado(idEliminar);
                }
                response.sendRedirect("srvCliente?accion=listarActivos");
                break;

            case "buscar":
                String texto = request.getParameter("texto");
                List<clsCliente> resultado = daoCliente.mtdBuscar(texto == null ? "" : texto.trim());
                request.setAttribute("listaClientes", resultado);
                cargarListasAuxiliares(request);
                request.getRequestDispatcher("VistaCliente/ClienteMain.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("srvCliente?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("agregar".equals(accion)) {
            procesarAgregar(request, response);
        } else if ("actualizar".equals(accion)) {
            procesarActualizar(request, response);
        } else {
            response.sendRedirect("srvCliente?accion=listarActivos");
        }
    }

    private void procesarAgregar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        clsCliente cliente = construirClienteDesdeRequest(request, false);
        String mensajeValidacion = validarCliente(cliente, false, 0);

        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("clienteForm", cliente);
            reenviarConError(request, response);
            return;
        }

        if (!daoCliente.mtdAgregar(cliente)) {
            request.setAttribute("mensajeError", "No se pudo registrar al cliente. Intente nuevamente.");
            request.setAttribute("clienteForm", cliente);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvCliente?accion=listarActivos");
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseEntero(request.getParameter("id"), 0);
        if (id <= 0) {
            response.sendRedirect("srvCliente?accion=listarActivos");
            return;
        }

        clsCliente cliente = construirClienteDesdeRequest(request, true);
        cliente.setIdCliente(id);

        String mensajeValidacion = validarCliente(cliente, true, id);
        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("cliente", cliente);
            reenviarConError(request, response);
            return;
        }

        if (!daoCliente.mtdEditar(cliente)) {
            request.setAttribute("mensajeError", "No se pudo actualizar la información del cliente.");
            request.setAttribute("cliente", cliente);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvCliente?accion=listarActivos");
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {

        request.removeAttribute("clienteForm");
        request.removeAttribute("mensajeError");

        List<clsCliente> lista = activos
                ? daoCliente.mtdListarActivos()
                : daoCliente.mtdListarInactivos();

        request.setAttribute("listaClientes", lista);
        cargarListasAuxiliares(request);
        request.getRequestDispatcher("VistaCliente/ClienteMain.jsp").forward(request, response);
    }

    private void cargarListasAuxiliares(HttpServletRequest request) {
        List<clsTipoDocumento> tipos = daoTipoDocumento.listarTodos();
        request.setAttribute("listaTiposDocumento", tipos);
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsCliente> listaActivos = daoCliente.mtdListarActivos();
        request.setAttribute("listaClientes", listaActivos);
        cargarListasAuxiliares(request);
        request.getRequestDispatcher("VistaCliente/ClienteMain.jsp").forward(request, response);
    }

    private clsCliente construirClienteDesdeRequest(HttpServletRequest request, boolean esActualizacion) {
        clsCliente cliente = new clsCliente();
        cliente.setNombre(obtenerParametro(request, "nombre"));
        cliente.setApellido(obtenerParametro(request, "apellido"));
        cliente.setIdTipoDocumento(parseEntero(request.getParameter("idTipoDocumento"), 0));
        cliente.setNumeroDocumento(obtenerParametro(request, "numeroDocumento"));
        cliente.setTelefono(obtenerParametro(request, "telefono"));
        cliente.setDireccion(obtenerParametro(request, "direccion"));
        cliente.setEmail(obtenerParametro(request, "email").toLowerCase());
        cliente.setClave(obtenerParametro(request, "clave"));
        cliente.setEstado(parseEntero(request.getParameter("estado"), 1));
        cliente.setClaveVisible(cliente.getClave());

        if (esActualizacion) {
            cliente.setIdCliente(parseEntero(request.getParameter("id"), 0));
        }

        if (cliente.getNombre() != null) cliente.setNombre(cliente.getNombre().toUpperCase());
        if (cliente.getApellido() != null) cliente.setApellido(cliente.getApellido().toUpperCase());
        if (cliente.getDireccion() != null) cliente.setDireccion(cliente.getDireccion().toUpperCase());

        return cliente;
    }

    private int parseEntero(String valor, int porDefecto) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            return porDefecto;
        }
    }

    private String obtenerParametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor != null ? valor.trim() : "";
    }

    private String validarCliente(clsCliente cliente, boolean esActualizacion, int idActual) {

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (!cliente.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}$")) {
            return "El nombre solo debe contener letras y espacios (máximo 50 caracteres).";
        }

        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            return "El apellido no puede estar vacío.";
        }
        if (!cliente.getApellido().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}$")) {
            return "El apellido solo debe contener letras y espacios (máximo 50 caracteres).";
        }

        if (cliente.getIdTipoDocumento() <= 0) {
            return "Debes seleccionar un tipo de documento válido.";
        }

        if (cliente.getNumeroDocumento() == null || cliente.getNumeroDocumento().trim().isEmpty()) {
            return "El número de documento es obligatorio.";
        }
        if (!cliente.getNumeroDocumento().matches("^[0-9]{8,15}$")) {
            return "El número de documento debe contener entre 8 y 15 dígitos.";
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            return "El teléfono es obligatorio.";
        }
        if (!cliente.getTelefono().matches("^[0-9]{6,15}$")) {
            return "El teléfono debe contener entre 6 y 15 dígitos.";
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            return "La dirección es obligatoria.";
        }
        if (cliente.getDireccion().length() < 5 || cliente.getDireccion().length() > 80) {
            return "La dirección debe tener entre 5 y 80 caracteres.";
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            return "El correo electrónico es obligatorio.";
        }
        if (!cliente.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Ingresa un correo electrónico válido.";
        }

        if (cliente.getClave() == null || cliente.getClave().trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (cliente.getClave().length() < 4 || cliente.getClave().length() > 30) {
            return "La contraseña debe tener entre 4 y 30 caracteres.";
        }

        if (!esActualizacion && daoCliente.mtdExisteDocumento(cliente.getNumeroDocumento())) {
            return "El número de documento ya está registrado.";
        }

        if (esActualizacion && daoCliente.mtdExisteDocumentoEnOtro(cliente.getNumeroDocumento(), idActual)) {
            return "El número de documento pertenece a otro cliente.";
        }

        if (!esActualizacion && daoCliente.mtdExisteEmail(cliente.getEmail())) {
            return "El correo electrónico ya se encuentra registrado.";
        }

        if (esActualizacion && daoCliente.mtdExisteEmailEnOtro(cliente.getEmail(), idActual)) {
            return "El correo electrónico pertenece a otro cliente.";
        }

        return null;
    }
}
