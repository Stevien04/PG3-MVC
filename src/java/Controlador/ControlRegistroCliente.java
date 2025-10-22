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
import javax.servlet.http.HttpSession;

@WebServlet(name = "RegistroClienteServlet", urlPatterns = {"/registroCliente"})
public class ControlRegistroCliente extends HttpServlet {

    private static final int CARGO_CLIENTE_ID = 5;
    private final clsDAOCliente daoCliente = new clsDAOCliente();
    private final clsDAOTipoDocumento daoTipoDocumento = new clsDAOTipoDocumento();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepararFormulario(request);
        request.getRequestDispatcher("/VistaLogin/RegistroCliente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cargoIdParam = request.getParameter("cargoId");
        if (cargoIdParam == null || !cargoIdParam.trim().equals(String.valueOf(CARGO_CLIENTE_ID))) {
            request.setAttribute("mensajeError", "El cargo seleccionado no es válido.");
            reenviarConDatos(request, response);
            return;
        }

        clsCliente cliente = construirClienteDesdeRequest(request);
        request.setAttribute("clienteForm", cliente);

        String mensajeValidacion = validarCliente(cliente);
        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            reenviarConDatos(request, response);
            return;
        }

        if (!daoCliente.mtdAgregar(cliente)) {
            request.setAttribute("mensajeError", "No se pudo registrar tu cuenta. Inténtalo nuevamente.");
            reenviarConDatos(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("mensajeExitoRegistro", "Tu cuenta se creó correctamente. Ahora puedes iniciar sesión.");
        response.sendRedirect(request.getContextPath() + "/loginCliente");
    }

    private void reenviarConDatos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepararFormulario(request);
        request.getRequestDispatcher("/VistaLogin/RegistroCliente.jsp").forward(request, response);
    }

    private void prepararFormulario(HttpServletRequest request) {
        List<clsTipoDocumento> tiposDocumento = daoTipoDocumento.listarTodos();
        request.setAttribute("listaTiposDocumento", tiposDocumento);
        request.setAttribute("cargoClienteId", CARGO_CLIENTE_ID);
    }

    private clsCliente construirClienteDesdeRequest(HttpServletRequest request) {
        clsCliente cliente = new clsCliente();
        cliente.setNombre(obtenerParametro(request, "nombre").toUpperCase());
        cliente.setApellido(obtenerParametro(request, "apellido").toUpperCase());
        cliente.setIdTipoDocumento(parseEntero(request.getParameter("idTipoDocumento"), 0));
        cliente.setNumeroDocumento(obtenerParametro(request, "numeroDocumento"));
        cliente.setTelefono(obtenerParametro(request, "telefono"));
        cliente.setDireccion(obtenerParametro(request, "direccion").toUpperCase());
        cliente.setEmail(obtenerParametro(request, "email").toLowerCase());
        cliente.setClave(obtenerParametro(request, "clave"));
        cliente.setClaveVisible(cliente.getClave());
        cliente.setEstado(1);
        return cliente;
    }

    private String validarCliente(clsCliente cliente) {

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
        if (daoCliente.mtdExisteDocumento(cliente.getNumeroDocumento())) {
            return "El número de documento ya está registrado.";
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
        if (daoCliente.mtdExisteEmail(cliente.getEmail())) {
            return "El correo electrónico ya se encuentra registrado.";
        }

        if (cliente.getClave() == null || cliente.getClave().trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (cliente.getClave().length() < 4 || cliente.getClave().length() > 30) {
            return "La contraseña debe tener entre 4 y 30 caracteres.";
        }

        return null;
    }

    private String obtenerParametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor != null ? valor.trim() : "";
    }

    private int parseEntero(String valor, int porDefecto) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            return porDefecto;
        }
    }
}