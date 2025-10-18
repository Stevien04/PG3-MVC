package Controlador;

import Modelo.clsModelo;
import Modelo.clsMarca;
import ModeloDao.clsDAOModelo;
import ModeloDao.clsDAOMarca;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvModelo", urlPatterns = {"/srvModelo"})
public class ControlModelo extends HttpServlet {

    private final clsDAOModelo dao = new clsDAOModelo();
    private final clsDAOMarca daoMarca = new clsDAOMarca(); // ← para llenar el combo

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listarActivos";

        switch (accion) {
            case "listarActivos":
                listarModelos(request, response, true);
                break;

            case "listarInactivos":
                listarModelos(request, response, false);
                break;

            case "nuevo": {
                List<clsMarca> listaMarcas = daoMarca.mtdListarActivos();
                request.setAttribute("listaMarcas", listaMarcas);
                request.getRequestDispatcher("VistaModelo/NuevoModelo.jsp").forward(request, response);
                break;
            }

            case "editar": {
                int idEditar = parseIntOrDefault(request.getParameter("id"), 0);
                clsModelo modeloEditar = dao.mtdObtenerPorId(idEditar);

                List<clsMarca> listaMarcas = daoMarca.mtdListarActivos(); // ← cargar combo también aquí
                request.setAttribute("listaMarcas", listaMarcas);
                request.setAttribute("modelo", modeloEditar);

                request.getRequestDispatcher("VistaModelo/edit.jsp").forward(request, response);
                break;
            }

            case "eliminar": {
                int idEliminar = parseIntOrDefault(request.getParameter("id"), 0);
                if (idEliminar > 0) dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect(request.getContextPath() + "/srvModelo?accion=listarActivos");
                break;
            }

            case "buscar": {
                String texto = safe(request.getParameter("texto"));
                List<clsModelo> resultado = dao.mtdBuscar(texto);
                List<clsMarca> listaMarcas = daoMarca.mtdListarActivos();

                request.setAttribute("listaModelos", resultado);
                request.setAttribute("listaMarcas", listaMarcas);
                request.getRequestDispatcher("VistaModelo/ModeloMain.jsp").forward(request, response);
                break;
            }

            default:
                response.sendRedirect(request.getContextPath() + "/srvModelo?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String accion = request.getParameter("accion");
        String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,20}$";

        if ("agregar".equals(accion)) {
            String nombre = safe(request.getParameter("nombre"));
            String estadoParam = request.getParameter("estado");
            String idMarcaParam = request.getParameter("idMarca");
            String nombreLimpio = nombre.trim();

            if (nombreLimpio.isEmpty()) {
                setFormErrorAgregar(request, "El nombre no puede estar vacío.", estadoParam, nombreLimpio);
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                setFormErrorAgregar(request, "El nombre solo puede tener letras y espacios (máximo 20 caracteres).",
                        estadoParam, nombreLimpio);
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombreLimpio)) {
                setFormErrorAgregar(request, "El nombre del modelo ya está registrado.",
                        estadoParam, nombreLimpio);
                reenviarConError(request, response);
                return;
            }

            int estado = parseIntOrDefault(estadoParam, 1);
            int idMarca = parseIntOrDefault(idMarcaParam, 0);

            if (idMarca <= 0) {
                setFormErrorAgregar(request, "Debe seleccionar una marca válida.", estadoParam, nombreLimpio);
                reenviarConError(request, response);
                return;
            }

            clsModelo modelo = new clsModelo();
            modelo.setIdMarca(idMarca);
            modelo.setNombre(nombreLimpio.toUpperCase());
            modelo.setEstado(estado);

            boolean ok = dao.mtdAgregar(modelo);
            if (!ok) {
                setFormErrorAgregar(request, "No se pudo guardar el modelo. Intenta de nuevo.",
                        String.valueOf(estado), nombreLimpio);
                reenviarConError(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/srvModelo?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String nombre = safe(request.getParameter("nombre"));
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");
            String idMarcaParam = request.getParameter("idMarca");
            String nombreLimpio = nombre.trim();

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("modelo", construirModeloTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 20 caracteres).");
                request.setAttribute("modelo", construirModeloTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int id = parseIntOrDefault(idParam, 0);
            int idMarca = parseIntOrDefault(idMarcaParam, 0);
            if (id <= 0 || idMarca <= 0) {
                request.setAttribute("mensajeError", "Datos inválidos para actualización.");
                request.setAttribute("modelo", construirModeloTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombreLimpio)) {
                clsModelo actual = dao.mtdObtenerPorId(id);
                if (actual == null || !actual.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    request.setAttribute("mensajeError", "Ya existe otro modelo con ese nombre.");
                    request.setAttribute("modelo", construirModeloTemporal(idParam, nombreLimpio, estadoParam));
                    reenviarConError(request, response);
                    return;
                }
            }

            int estado = parseIntOrDefault(estadoParam, 1);
            clsModelo modelo = new clsModelo();
            modelo.setIdModelo(id);
            modelo.setIdMarca(idMarca);
            modelo.setNombre(nombreLimpio.toUpperCase());
            modelo.setEstado(estado);

            boolean ok = dao.mtdEditar(modelo);
            if (!ok) {
                request.setAttribute("mensajeError", "No se pudo actualizar el modelo. Intenta de nuevo.");
                request.setAttribute("modelo", construirModeloTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/srvModelo?accion=listarActivos");
        }
    }

    private void listarModelos(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {

        List<clsModelo> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        List<clsMarca> listaMarcas = daoMarca.mtdListarActivos(); // combo de marcas activas

        request.setAttribute("listaModelos", lista);
        request.setAttribute("listaMarcas", listaMarcas);
        request.getRequestDispatcher("VistaModelo/ModeloMain.jsp").forward(request, response);
    }

    private clsModelo construirModeloTemporal(String idParam, String nombre, String estadoParam) {
        int id = parseIntOrDefault(idParam, 0);
        int estado = parseIntOrDefault(estadoParam, 1);
        clsModelo modelo = new clsModelo();
        modelo.setIdModelo(id);
        modelo.setNombre(nombre != null ? nombre.trim().toUpperCase() : "");
        modelo.setEstado(estado);
        return modelo;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsModelo> listaActivos = dao.mtdListarActivos();
        List<clsMarca> listaMarcas = daoMarca.mtdListarActivos(); // también aquí

        request.setAttribute("listaModelos", listaActivos);
        request.setAttribute("listaMarcas", listaMarcas);
        request.getRequestDispatcher("VistaModelo/ModeloMain.jsp").forward(request, response);
    }

    private static int parseIntOrDefault(String val, int def) {
        try { return Integer.parseInt(val); } catch (Exception e) { return def; }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private void setFormErrorAgregar(HttpServletRequest request, String msg, String estadoParam, String nombreLimpio) {
        request.setAttribute("mensajeError", msg);
        request.setAttribute("modeloFormEstado", safe(estadoParam));
        request.setAttribute("modeloFormNombre", nombreLimpio.toUpperCase());
        List<clsMarca> listaMarcas = daoMarca.mtdListarActivos(); // para mantener el combo si hay error
        request.setAttribute("listaMarcas", listaMarcas);
    }

    @Override
    public String getServletInfo() { return "Controlador de modelos"; }
}
