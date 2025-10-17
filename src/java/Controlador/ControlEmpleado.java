package Controlador;

import Modelo.clsEmpleado;
import Modelo.clsTipoDocumento;
import ModeloDao.clsDAOEmpleado;
import ModeloDao.clsDAOTipoDocumento;
import ModeloDao.clsDAOcargo;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvEmpleado", urlPatterns = {"/srvEmpleado"})
public class ControlEmpleado extends HttpServlet {

    private final clsDAOEmpleado daoEmpleado = new clsDAOEmpleado();
    private final clsDAOcargo daoCargo = new clsDAOcargo();
    private final clsDAOTipoDocumento daoTipoDocumento = new clsDAOTipoDocumento();

    // ----------------------------------------------------------
    //  MÉTODO GET
    // ----------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarEmpleados(request, response, true);
                break;

            case "listarInactivos":
                listarEmpleados(request, response, false);
                break;

            case "editar":
                int idEditar = parseEntero(request.getParameter("id"), -1);
                if (idEditar <= 0) {
                    response.sendRedirect("srvEmpleado?accion=listarActivos");
                    return;
                }
                clsEmpleado empleadoEditar = daoEmpleado.mtdObtenerPorId(idEditar);
                if (empleadoEditar == null) {
                    response.sendRedirect("srvEmpleado?accion=listarActivos");
                    return;
                }
                request.setAttribute("empleado", empleadoEditar);
                listarEmpleados(request, response, true);
                break;

            case "eliminar":
                int idEliminar = parseEntero(request.getParameter("id"), -1);
                if (idEliminar > 0) {
                    daoEmpleado.mtdCambiarEstado(idEliminar);
                }
                response.sendRedirect("srvEmpleado?accion=listarActivos");
                break;

            case "buscar":
                String texto = request.getParameter("texto");
                List<clsEmpleado> resultado = daoEmpleado.mtdBuscar(texto);
                request.setAttribute("listaEmpleados", resultado);
                cargarListasAuxiliares(request);
                request.getRequestDispatcher("VistaEmpleado/EmpleadoMain.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("srvEmpleado?accion=listarActivos");
                break;
        }
    }

    // ----------------------------------------------------------
    //  MÉTODO POST
    // ----------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if ("agregar".equals(accion)) {
            procesarAgregar(request, response);
        } else if ("actualizar".equals(accion)) {
            procesarActualizar(request, response);
        } else {
            response.sendRedirect("srvEmpleado?accion=listarActivos");
        }
    }

    // ----------------------------------------------------------
    //  MÉTODOS DE PROCESO
    // ----------------------------------------------------------
    private void procesarAgregar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        clsEmpleado empleado = construirEmpleadoDesdeRequest(request, false);
        String mensajeValidacion = validarEmpleado(empleado, false, 0);

        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("empleadoForm", empleado);
            reenviarConError(request, response);
            return;
        }

        if (!daoEmpleado.mtdAgregar(empleado)) {
            request.setAttribute("mensajeError", "No se pudo registrar al empleado. Intente nuevamente.");
            request.setAttribute("empleadoForm", empleado);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvEmpleado?accion=listarActivos");
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseEntero(request.getParameter("id"), 0);
        if (id <= 0) {
            response.sendRedirect("srvEmpleado?accion=listarActivos");
            return;
        }

        clsEmpleado empleado = construirEmpleadoDesdeRequest(request, true);
        empleado.setIdEmpleado(id);

        String mensajeValidacion = validarEmpleado(empleado, true, id);
        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("empleado", empleado);
            reenviarConError(request, response);
            return;
        }

        if (!daoEmpleado.mtdEditar(empleado)) {
            request.setAttribute("mensajeError", "No se pudo actualizar la información del empleado.");
            request.setAttribute("empleado", empleado);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvEmpleado?accion=listarActivos");
    }

    // ----------------------------------------------------------
    //  LISTAR
    // ----------------------------------------------------------
    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {

        // ✅ Limpieza de residuos del formulario anterior
        request.removeAttribute("empleadoForm");
        request.removeAttribute("mensajeError");

        List<clsEmpleado> lista = activos
                ? daoEmpleado.mtdListarActivos()
                : daoEmpleado.mtdListarInactivos();

        request.setAttribute("listaEmpleados", lista);
        cargarListasAuxiliares(request);
        request.getRequestDispatcher("VistaEmpleado/EmpleadoMain.jsp").forward(request, response);
    }

    // ----------------------------------------------------------
    //  CARGA DE LISTAS Y REENVÍO
    // ----------------------------------------------------------
    private void cargarListasAuxiliares(HttpServletRequest request) {
        request.setAttribute("listaCargos", daoCargo.mtdListarActivos());
        List<clsTipoDocumento> tipos = daoTipoDocumento.listarTodos();
        request.setAttribute("listaTiposDocumento", tipos);
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsEmpleado> listaActivos = daoEmpleado.mtdListarActivos();
        request.setAttribute("listaEmpleados", listaActivos);
        cargarListasAuxiliares(request);
        request.getRequestDispatcher("VistaEmpleado/EmpleadoMain.jsp").forward(request, response);
    }

    // ----------------------------------------------------------
    //  UTILIDADES
    // ----------------------------------------------------------
    private clsEmpleado construirEmpleadoDesdeRequest(HttpServletRequest request, boolean esActualizacion) {
        clsEmpleado empleado = new clsEmpleado();
        empleado.setNombre(obtenerParametro(request, "nombre"));
        empleado.setApellido(obtenerParametro(request, "apellido"));
        empleado.setUsuario(obtenerParametro(request, "usuario"));
        empleado.setClave(obtenerParametro(request, "clave"));
        empleado.setNumeroDocumento(obtenerParametro(request, "numeroDocumento"));
        empleado.setTelefono(obtenerParametro(request, "telefono"));
        empleado.setEstado(parseEntero(request.getParameter("estado"), 1));
        empleado.setIdCargo(parseEntero(request.getParameter("idCargo"), 0));
        empleado.setIdTipoDocumento(parseEntero(request.getParameter("idTipoDocumento"), 0));
        empleado.setClaveVisible(empleado.getClave());

        if (esActualizacion) {
            empleado.setIdEmpleado(parseEntero(request.getParameter("id"), 0));
        }

        if (empleado.getNombre() != null) {
            empleado.setNombre(empleado.getNombre().toUpperCase());
        }
        if (empleado.getApellido() != null) {
            empleado.setApellido(empleado.getApellido().toUpperCase());
        }

        return empleado;
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

    // ----------------------------------------------------------
    //  VALIDACIÓN
    // ----------------------------------------------------------
    private String validarEmpleado(clsEmpleado empleado, boolean esActualizacion, int idActual) {

        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (!empleado.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,30}$")) {
            return "El nombre solo debe contener letras y espacios (máximo 30 caracteres).";
        }

        if (empleado.getApellido() == null || empleado.getApellido().trim().isEmpty()) {
            return "El apellido no puede estar vacío.";
        }
        if (!empleado.getApellido().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,40}$")) {
            return "El apellido solo debe contener letras y espacios (máximo 40 caracteres).";
        }

        if (empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty()) {
            return "El usuario es obligatorio.";
        }
        if (!empleado.getUsuario().matches("^[A-Za-z0-9]{4,20}$")) {
            return "El usuario debe tener entre 4 y 20 caracteres alfanuméricos.";
        }

        if (empleado.getClave() == null || empleado.getClave().trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (empleado.getClave().length() < 4 || empleado.getClave().length() > 30) {
            return "La contraseña debe tener entre 4 y 30 caracteres.";
        }

        if (empleado.getIdCargo() <= 0) {
            return "Debes seleccionar un cargo válido.";
        }

        if (empleado.getIdTipoDocumento() <= 0) {
            return "Debes seleccionar un tipo de documento válido.";
        }

        if (empleado.getNumeroDocumento() == null || empleado.getNumeroDocumento().trim().isEmpty()) {
            return "El número de documento es obligatorio.";
        }
        if (!empleado.getNumeroDocumento().matches("^[0-9]{8,15}$")) {
            return "El número de documento debe contener entre 8 y 15 dígitos.";
        }

        if (empleado.getTelefono() == null || empleado.getTelefono().trim().isEmpty()) {
            return "El teléfono es obligatorio.";
        }
        if (!empleado.getTelefono().matches("^[0-9]{6,15}$")) {
            return "El teléfono debe contener entre 6 y 15 dígitos.";
        }

        if (!esActualizacion && daoEmpleado.mtdExisteUsuario(empleado.getUsuario())) {
            return "El usuario ya se encuentra registrado.";
        }

        if (esActualizacion && daoEmpleado.mtdExisteUsuarioEnOtro(empleado.getUsuario(), idActual)) {
            return "El usuario ya se encuentra asignado a otro empleado.";
        }

        if (!esActualizacion && daoEmpleado.mtdExisteDocumento(empleado.getNumeroDocumento())) {
            return "El número de documento ya está registrado.";
        }

        if (esActualizacion && daoEmpleado.mtdExisteDocumentoEnOtro(empleado.getNumeroDocumento(), idActual)) {
            return "El número de documento pertenece a otro empleado.";
        }

        return null;
    }
}
