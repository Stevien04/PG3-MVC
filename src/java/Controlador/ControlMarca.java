package Controlador;

import Modelo.clsMarca;
import ModeloDao.clsDAOMarca;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvMarca", urlPatterns = {"/srvMarca"})
public class ControlMarca extends HttpServlet {

    private final clsDAOMarca dao = new clsDAOMarca();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarMarcas(request, response, true);
                break;
            case "listarInactivos":
                listarMarcas(request, response, false);
                break;
            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                clsMarca marcaEditar = dao.mtdObtenerPorId(idEditar);
                request.setAttribute("marca", marcaEditar);
                listarMarcas(request, response, true);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect("srvMarca?accion=listarActivos");
                break;
            case "buscar":
                String texto = request.getParameter("texto");
                List<clsMarca> resultado = dao.mtdBuscar(texto);
                request.setAttribute("listaMarcas", resultado);
                request.getRequestDispatcher("VistaMarca/MarcaMain.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("srvMarca?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,20}$";

        if ("agregar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String nombreLimpio = nombre != null ? nombre.trim() : "";

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("marcaFormEstado", estadoParam);
                request.setAttribute("marcaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 20 caracteres).");
                request.setAttribute("marcaFormEstado", estadoParam);
                request.setAttribute("marcaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombreLimpio)) {
                request.setAttribute("mensajeError", "El nombre de la marca ya está registrado.");
                request.setAttribute("marcaFormEstado", estadoParam);
                request.setAttribute("marcaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            int estado = Integer.parseInt(estadoParam);
            clsMarca marca = new clsMarca();
            marca.setNombre(nombreLimpio.toUpperCase());
            marca.setEstado(estado);
            dao.mtdAgregar(marca);

            response.sendRedirect("srvMarca?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");
            String nombreLimpio = nombre != null ? nombre.trim() : "";

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("marca", construirMarcaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 20 caracteres).");
                request.setAttribute("marca", construirMarcaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int id = Integer.parseInt(idParam);

            if (dao.mtdExisteNombre(nombreLimpio)) {
                clsMarca actual = dao.mtdObtenerPorId(id);
                if (actual != null && !actual.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    request.setAttribute("mensajeError", "Ya existe otra marca con ese nombre.");
                    request.setAttribute("marca", construirMarcaTemporal(idParam, nombreLimpio, estadoParam));
                    reenviarConError(request, response);
                    return;
                }
            }

            int estado = Integer.parseInt(estadoParam);
            clsMarca marca = new clsMarca(id, nombreLimpio.toUpperCase(), estado);
            dao.mtdEditar(marca);

            response.sendRedirect("srvMarca?accion=listarActivos");
        }
    }

    private void listarMarcas(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsMarca> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        request.setAttribute("listaMarcas", lista);
        request.getRequestDispatcher("VistaMarca/MarcaMain.jsp").forward(request, response);
    }

    private clsMarca construirMarcaTemporal(String idParam, String nombre, String estadoParam) {
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
        clsMarca marca = new clsMarca();
        marca.setIdMarca(id);
        marca.setNombre(nombre != null ? nombre.trim().toUpperCase() : "");
        marca.setEstado(estado);
        return marca;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsMarca> listaActivos = dao.mtdListarActivos();
        request.setAttribute("listaMarcas", listaActivos);
        request.getRequestDispatcher("VistaMarca/MarcaMain.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de marcas";
    }
}