package Controlador;

import Modelo.clsCargo;
import ModeloDao.clsDAOcargo;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvCargo", urlPatterns = {"/srvCargo"})
public class ControlCargo extends HttpServlet {

    private final clsDAOcargo dao = new clsDAOcargo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                List<clsCargo> listaActivos = dao.mtdListarActivos();
                request.setAttribute("listaCargos", listaActivos);
                request.getRequestDispatcher("VistaCargo/cargoMain.jsp").forward(request, response);
                break;

            case "listarInactivos":
                List<clsCargo> listaInactivos = dao.mtdListarInactivos();
                request.setAttribute("listaCargos", listaInactivos);
                request.getRequestDispatcher("VistaCargo/cargoMain.jsp").forward(request, response);
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                clsCargo cargoEditar = dao.mtdObtenerPorId(idEditar);
                request.setAttribute("cargo", cargoEditar);
                List<clsCargo> lista = dao.mtdListarActivos();
                request.setAttribute("listaCargos", lista);
                request.getRequestDispatcher("VistaCargo/cargoMain.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect("srvCargo?accion=listarActivos");
                break;

            case "buscar":
                String texto = request.getParameter("texto");
                List<clsCargo> resultado = dao.mtdBuscar(texto);
                request.setAttribute("listaCargos", resultado);
                request.getRequestDispatcher("VistaCargo/cargoMain.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("srvCargo?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,10}$";

        if ("agregar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");

            if (nombre == null || nombre.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                reenviarConError(request, response);
                return;
            }

            if (!nombre.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 15 caracteres).");
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombre)) {
                request.setAttribute("mensajeError", "El nombre de cargo ya está registrado.");
                reenviarConError(request, response);
                return;
            }

            int estado = Integer.parseInt(estadoParam);
            clsCargo cargo = new clsCargo();
            cargo.setNombre(nombre.trim());
            cargo.setEstado(estado);
            dao.mtdAgregar(cargo);

            response.sendRedirect("srvCargo?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");

            if (nombre == null || nombre.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                reenviarConError(request, response);
                return;
            }

            if (!nombre.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 15 caracteres).");
                reenviarConError(request, response);
                return;
            }
            int id = Integer.parseInt(idParam);

            if (dao.mtdExisteNombre(nombre)) {
                clsCargo actual = dao.mtdObtenerPorId(id);
                if (!actual.getNombre().equalsIgnoreCase(nombre)) {
                    request.setAttribute("mensajeError", "Ya existe otro cargo con ese nombre.");
                    reenviarConError(request, response);
                    return;
                }
            }
            int estado = Integer.parseInt(estadoParam);
            clsCargo cargo = new clsCargo(id, nombre.trim(), estado);
            dao.mtdEditar(cargo);

            response.sendRedirect("srvCargo?accion=listarActivos");
        }
    }

   
    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsCargo> listaActivos = dao.mtdListarActivos();
        request.setAttribute("listaCargos", listaActivos);
        request.getRequestDispatcher("VistaCargo/cargoMain.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de cargos - MVC con validaciones";
    }
}
