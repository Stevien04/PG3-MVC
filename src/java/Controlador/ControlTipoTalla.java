/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.clsTipoTalla;
import ModeloDao.clsDAOTipoTalla;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvTipoTalla", urlPatterns = {"/srvTipoTalla"})
public class ControlTipoTalla extends HttpServlet {

    private final clsDAOTipoTalla dao = new clsDAOTipoTalla();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarTipoTallas(request, response, true);
                break;
            case "listarInactivos":
                listarTipoTallas(request, response, false);
                break;
            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                clsTipoTalla tipoTallaEditar = dao.mtdObtenerPorId(idEditar);
                request.setAttribute("tipoTalla", tipoTallaEditar);
                listarTipoTallas(request, response, true);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.mtdCambiarEstado(idEliminar);
                response.sendRedirect("srvTipoTalla?accion=listarActivos");
                break;
            case "buscar":
                String texto = request.getParameter("texto");
                List<clsTipoTalla> resultado = dao.mtdBuscar(texto);
                request.setAttribute("listaTipoTallas", resultado);
                request.getRequestDispatcher("VistaTipoTalla/TipoTallaMain.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("srvTipoTalla?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}$";

        if ("agregar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String nombreLimpio = nombre != null ? nombre.trim() : "";

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("tipoTallaFormEstado", estadoParam);
                request.setAttribute("tipoTallaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 50 caracteres).");
                request.setAttribute("tipoTallaFormEstado", estadoParam);
                request.setAttribute("tipoTallaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteNombre(nombreLimpio)) {
                request.setAttribute("mensajeError", "El nombre del tipo de talla ya está registrado.");
                request.setAttribute("tipoTallaFormEstado", estadoParam);
                request.setAttribute("tipoTallaFormNombre", nombreLimpio.toUpperCase());
                reenviarConError(request, response);
                return;
            }

            int estado = Integer.parseInt(estadoParam);
            clsTipoTalla tipoTalla = new clsTipoTalla();
            tipoTalla.setNombre(nombreLimpio.toUpperCase());
            tipoTalla.setEstado(estado);
            dao.mtdAgregar(tipoTalla);

            response.sendRedirect("srvTipoTalla?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");
            String nombreLimpio = nombre != null ? nombre.trim() : "";

            if (nombreLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El nombre no puede estar vacío.");
                request.setAttribute("tipoTalla", construirTipoTallaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (!nombreLimpio.matches(regexNombre)) {
                request.setAttribute("mensajeError", "El nombre solo puede tener letras y espacios (máximo 50 caracteres).");
                request.setAttribute("tipoTalla", construirTipoTallaTemporal(idParam, nombreLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int id = Integer.parseInt(idParam);

            if (dao.mtdExisteNombre(nombreLimpio)) {
                clsTipoTalla actual = dao.mtdObtenerPorId(id);
                if (actual != null && !actual.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    request.setAttribute("mensajeError", "Ya existe otro tipo de talla con ese nombre.");
                    request.setAttribute("tipoTalla", construirTipoTallaTemporal(idParam, nombreLimpio, estadoParam));
                    reenviarConError(request, response);
                    return;
                }
            }

            int estado = Integer.parseInt(estadoParam);
            clsTipoTalla tipoTalla = new clsTipoTalla(id, nombreLimpio.toUpperCase(), estado);
            dao.mtdEditar(tipoTalla);

            response.sendRedirect("srvTipoTalla?accion=listarActivos");
        }
    }

    private void listarTipoTallas(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsTipoTalla> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        request.setAttribute("listaTipoTallas", lista);
        request.getRequestDispatcher("VistaTipoTalla/TipoTallaMain.jsp").forward(request, response);
    }

    private clsTipoTalla construirTipoTallaTemporal(String idParam, String nombre, String estadoParam) {
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
        clsTipoTalla tipoTalla = new clsTipoTalla();
        tipoTalla.setIdTipoTalla(id);
        tipoTalla.setNombre(nombre != null ? nombre.trim().toUpperCase() : "");
        tipoTalla.setEstado(estado);
        return tipoTalla;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsTipoTalla> listaActivos = dao.mtdListarActivos();
        request.setAttribute("listaTipoTallas", listaActivos);
        request.getRequestDispatcher("VistaTipoTalla/TipoTallaMain.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de tipos de talla";
    }
}