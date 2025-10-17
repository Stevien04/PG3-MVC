package Controlador;

import Modelo.clsCategoria;
import ModeloDao.clsDAOCategoria;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvCategoria", urlPatterns = {"/srvCategoria"})
public class ControlCategoria extends HttpServlet {

    private final clsDAOCategoria dao = new clsDAOCategoria();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarCategorias(request, response, true);
                break;
            case "listarInactivos":
                listarCategorias(request, response, false);
                break;
            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                clsCategoria categoriaEditar = dao.mtdObtenerPorId(idEditar);
                request.setAttribute("categoria", categoriaEditar);
                listarCategorias(request, response, true);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect("srvCategoria?accion=listarActivos");
                break;
            case "buscar":
                String texto = request.getParameter("texto");
                List<clsCategoria> resultado = dao.mtdBuscar(texto);
                request.setAttribute("listaCategorias", resultado);
                request.getRequestDispatcher("VistaCategoria/CategoriaMain.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("srvCategoria?accion=listarActivos");
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
                request.setAttribute("categoriaFormEstado", estadoParam);
                request.setAttribute("categoriaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 20 caracteres).");
                request.setAttribute("categoriaFormEstado", estadoParam);
                request.setAttribute("categoriaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombreLimpio)) {
                request.setAttribute("mensajeError", "El nombre de la categoría ya está registrado.");
                request.setAttribute("categoriaFormEstado", estadoParam);
                request.setAttribute("categoriaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            int estado = Integer.parseInt(estadoParam);
            clsCategoria categoria = new clsCategoria();
            categoria.setNombre(nombreLimpio.toUpperCase());
            categoria.setEstado(estado);
            dao.mtdAgregar(categoria);

            response.sendRedirect("srvCategoria?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");
            String nombreLimpio = nombre != null ? nombre.trim() : "";

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("categoria", construirCategoriaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 20 caracteres).");
                request.setAttribute("categoria", construirCategoriaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int id = Integer.parseInt(idParam);

            if (dao.mtdExisteNombre(nombreLimpio)) {
                clsCategoria actual = dao.mtdObtenerPorId(id);
                if (actual != null && !actual.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    request.setAttribute("mensajeError", "Ya existe otra categoría con ese nombre.");
                    request.setAttribute("categoria", construirCategoriaTemporal(idParam, nombreLimpio, estadoParam));
                    reenviarConError(request, response);
                    return;
                }
            }

            int estado = Integer.parseInt(estadoParam);
            clsCategoria categoria = new clsCategoria(id, nombreLimpio.toUpperCase(), estado);
            dao.mtdEditar(categoria);

            response.sendRedirect("srvCategoria?accion=listarActivos");
        }
    }

    private void listarCategorias(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsCategoria> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        request.setAttribute("listaCategorias", lista);
        request.getRequestDispatcher("VistaCategoria/CategoriaMain.jsp").forward(request, response);
    }

    private clsCategoria construirCategoriaTemporal(String idParam, String nombre, String estadoParam) {
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
        clsCategoria categoria = new clsCategoria();
        categoria.setIdCategoria(id);
        categoria.setNombre(nombre != null ? nombre.trim().toUpperCase() : "");
        categoria.setEstado(estado);
        return categoria;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsCategoria> listaActivos = dao.mtdListarActivos();
        request.setAttribute("listaCategorias", listaActivos);
        request.getRequestDispatcher("VistaCategoria/CategoriaMain.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de categorías";
    }
}
