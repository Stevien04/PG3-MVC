package Controlador;

import Modelo.clsColor;
import ModeloDao.clsDAOColor;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvColor", urlPatterns = {"/srvColor"})
public class ControlColor extends HttpServlet {

    private static final String REGEX_NOMBRE = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,30}$";
    private final clsDAOColor dao = new clsDAOColor();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarColores(request, response, true);
                break;
            case "listarInactivos":
                listarColores(request, response, false);
                break;
            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                clsColor colorEditar = dao.mtdObtenerPorId(idEditar);
                request.setAttribute("color", colorEditar);
                listarColores(request, response, true);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect("srvColor?accion=listarActivos");
                break;
            case "buscar":
                String texto = request.getParameter("texto");
                List<clsColor> resultado = dao.mtdBuscar(texto);
                request.setAttribute("listaColores", resultado);
                request.getRequestDispatcher("VistaColor/ColorMain.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("srvColor?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("agregar".equals(accion)) {
            procesarRegistro(request, response);
            return;
        }

        if ("actualizar".equals(accion)) {
            procesarActualizacion(request, response);
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String estadoParam = request.getParameter("estado");
        String nombreLimpio = nombre != null ? nombre.trim() : "";

        if (!validarNombre(nombreLimpio)) {
            request.setAttribute("mensajeError", obtenerMensajeErrorNombre(nombreLimpio));
            request.setAttribute("colorFormNombre", nombreLimpio.toUpperCase());
            request.setAttribute("colorFormEstado", estadoParam);
            reenviarConError(request, response);
            return;
        }

        if (dao.mtdExisteNombre(nombreLimpio)) {
            request.setAttribute("mensajeError", "El nombre del color ya está registrado.");
            request.setAttribute("colorFormNombre", nombreLimpio.toUpperCase());
            request.setAttribute("colorFormEstado", estadoParam);
            reenviarConError(request, response);
            return;
        }

        int estado = Integer.parseInt(estadoParam);
        clsColor color = new clsColor();
        color.setNombre(nombreLimpio.toUpperCase());
        color.setEstado(estado);
        dao.mtdAgregar(color);

        response.sendRedirect("srvColor?accion=listarActivos");
    }

    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String estadoParam = request.getParameter("estado");
        String idParam = request.getParameter("id");
        String nombreLimpio = nombre != null ? nombre.trim() : "";

        if (!validarNombre(nombreLimpio)) {
            request.setAttribute("mensajeError", obtenerMensajeErrorNombre(nombreLimpio));
            request.setAttribute("color", construirColorTemporal(idParam, nombreLimpio, estadoParam));
            reenviarConError(request, response);
            return;
        }

        int id = Integer.parseInt(idParam);

        if (dao.mtdExisteNombre(nombreLimpio)) {
            clsColor actual = dao.mtdObtenerPorId(id);
            if (actual != null && !actual.getNombre().equalsIgnoreCase(nombreLimpio)) {
                request.setAttribute("mensajeError", "Ya existe otro color con ese nombre.");
                request.setAttribute("color", construirColorTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }
        }

        int estado = Integer.parseInt(estadoParam);
        clsColor color = new clsColor(id, nombreLimpio.toUpperCase(), estado);
        dao.mtdEditar(color);

        response.sendRedirect("srvColor?accion=listarActivos");
    }

    private boolean validarNombre(String nombre) {
        return !nombre.isEmpty() && nombre.matches(REGEX_NOMBRE);
    }

    private String obtenerMensajeErrorNombre(String nombre) {
        if (nombre.isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (!nombre.matches(REGEX_NOMBRE)) {
            return "El nombre solo puede tener letras y espacios (máximo 30 caracteres).";
        }
        return "Nombre inválido.";
    }

    private void listarColores(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsColor> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        request.setAttribute("listaColores", lista);
        request.getRequestDispatcher("VistaColor/ColorMain.jsp").forward(request, response);
    }

    private clsColor construirColorTemporal(String idParam, String nombre, String estadoParam) {
        int id = 0;
        int estado = 1;
        try {
            if (idParam != null) {
                id = Integer.parseInt(idParam);
            }
        } catch (NumberFormatException ignored) {
        }
        try {
            if (estadoParam != null) {
                estado = Integer.parseInt(estadoParam);
            }
        } catch (NumberFormatException ignored) {
        }
        clsColor color = new clsColor();
        color.setIdColor(id);
        color.setNombre(nombre != null ? nombre.trim().toUpperCase() : "");
        color.setEstado(estado);
        return color;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsColor> listaActivos = dao.mtdListarActivos();
        request.setAttribute("listaColores", listaActivos);
        request.getRequestDispatcher("VistaColor/ColorMain.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de colores";
    }
}