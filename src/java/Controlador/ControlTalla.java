/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.clsTalla;
import Modelo.clsTipoTalla;
import ModeloDao.clsDAOTalla;
import ModeloDao.clsDAOTipoTalla;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvTalla", urlPatterns = {"/srvTalla"})
public class ControlTalla extends HttpServlet {

    private final clsDAOTalla dao = new clsDAOTalla();
    private final clsDAOTipoTalla daoTipoTalla = new clsDAOTipoTalla();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarTallas(request, response, true);
                break;
            case "listarInactivos":
                listarTallas(request, response, false);
                break;
            case "editar":
                int idEditar = parseIntOrDefault(request.getParameter("id"), 0);
                if (idEditar > 0) {
                    clsTalla tallaEditar = dao.mtdObtenerPorId(idEditar);
                    request.setAttribute("talla", tallaEditar);
                }
                listarTallas(request, response, true);
                break;
            case "eliminar":
                int idEliminar = parseIntOrDefault(request.getParameter("id"), 0);
                if (idEliminar > 0) {
                    dao.mtdCambiarEstado(idEliminar);
                }
                response.sendRedirect(request.getContextPath() + "/srvTalla?accion=listarActivos");
                break;
            case "buscar":
                String texto = safe(request.getParameter("texto"));
                List<clsTalla> resultado = dao.mtdBuscar(texto);
                List<clsTipoTalla> tipos = daoTipoTalla.mtdListarActivos();
                request.setAttribute("listaTallas", resultado);
                request.setAttribute("listaTipoTallas", tipos);
                request.getRequestDispatcher("VistaTalla/TallaMain.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/srvTalla?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String accion = request.getParameter("accion");
        String regexValor = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .-]{1,10}$";

        if ("agregar".equals(accion)) {
            String valor = safe(request.getParameter("valor"));
            String estadoParam = request.getParameter("estado");
            String idTipoParam = request.getParameter("idTipoTalla");
            String valorLimpio = valor.trim();

            if (valorLimpio.isEmpty()) {
                setFormErrorAgregar(request, "El valor no puede estar vacío.", estadoParam, idTipoParam, valorLimpio);
                reenviarConError(request, response);
                return;
            }

            if (!valorLimpio.matches(regexValor)) {
                setFormErrorAgregar(request,
                        "El valor solo puede tener letras, números, espacios, punto o guion (máximo 10 caracteres).",
                        estadoParam, idTipoParam, valorLimpio);
                reenviarConError(request, response);
                return;
            }

            int idTipoTalla = parseIntOrDefault(idTipoParam, 0);
            if (idTipoTalla <= 0) {
                setFormErrorAgregar(request, "Debe seleccionar un tipo de talla válido.", estadoParam, idTipoParam, valorLimpio);
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteValor(idTipoTalla, valorLimpio, null)) {
                setFormErrorAgregar(request, "Ya existe una talla con ese valor para el tipo seleccionado.",
                        estadoParam, idTipoParam, valorLimpio);
                reenviarConError(request, response);
                return;
            }

            int estado = parseIntOrDefault(estadoParam, 1);
            clsTalla talla = new clsTalla();
            talla.setIdTipoTalla(idTipoTalla);
            talla.setValor(valorLimpio.toUpperCase());
            talla.setEstado(estado);
            dao.mtdAgregar(talla);

            response.sendRedirect(request.getContextPath() + "/srvTalla?accion=listarActivos");
            return;
        }

        if ("actualizar".equals(accion)) {
            String valor = safe(request.getParameter("valor"));
            String estadoParam = request.getParameter("estado");
            String idParam = request.getParameter("id");
            String idTipoParam = request.getParameter("idTipoTalla");
            String valorLimpio = valor.trim();

            if (valorLimpio.isEmpty()) {
                request.setAttribute("mensajeError", "El valor no puede estar vacío.");
                request.setAttribute("talla", construirTallaTemporal(idParam, idTipoParam, valorLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (!valorLimpio.matches(regexValor)) {
                request.setAttribute("mensajeError", "El valor solo puede tener letras, números, espacios, punto o guion (máximo 10 caracteres).");
                request.setAttribute("talla", construirTallaTemporal(idParam, idTipoParam, valorLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int id = parseIntOrDefault(idParam, 0);
            int idTipoTalla = parseIntOrDefault(idTipoParam, 0);
            if (id <= 0 || idTipoTalla <= 0) {
                request.setAttribute("mensajeError", "Datos inválidos para actualizar la talla.");
                request.setAttribute("talla", construirTallaTemporal(idParam, idTipoParam, valorLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            if (dao.mtdExisteValor(idTipoTalla, valorLimpio, id)) {
                request.setAttribute("mensajeError", "Ya existe otra talla con ese valor para el tipo seleccionado.");
                request.setAttribute("talla", construirTallaTemporal(idParam, idTipoParam, valorLimpio, estadoParam));
                reenviarConError(request, response);
                return;
            }

            int estado = parseIntOrDefault(estadoParam, 1);
            clsTalla talla = new clsTalla();
            talla.setIdTalla(id);
            talla.setIdTipoTalla(idTipoTalla);
            talla.setValor(valorLimpio.toUpperCase());
            talla.setEstado(estado);
            dao.mtdEditar(talla);

            response.sendRedirect(request.getContextPath() + "/srvTalla?accion=listarActivos");
        }
    }

    private void listarTallas(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsTalla> lista = activos ? dao.mtdListarActivos() : dao.mtdListarInactivos();
        List<clsTipoTalla> tipos = daoTipoTalla.mtdListarActivos();
        request.setAttribute("listaTallas", lista);
        request.setAttribute("listaTipoTallas", tipos);
        request.getRequestDispatcher("VistaTalla/TallaMain.jsp").forward(request, response);
    }

    private void setFormErrorAgregar(HttpServletRequest request, String mensaje, String estadoParam,
            String idTipoParam, String valor) {
        request.setAttribute("mensajeError", mensaje);
        request.setAttribute("tallaFormEstado", estadoParam);
        request.setAttribute("tallaFormTipo", parseIntOrDefault(idTipoParam, 0));
        request.setAttribute("tallaFormValor", valor.toUpperCase());
    }

    private clsTalla construirTallaTemporal(String idParam, String idTipoParam, String valor, String estadoParam) {
        int id = parseIntOrDefault(idParam, 0);
        int idTipo = parseIntOrDefault(idTipoParam, 0);
        int estado = parseIntOrDefault(estadoParam, 1);

        clsTalla talla = new clsTalla();
        talla.setIdTalla(id);
        talla.setIdTipoTalla(idTipo);
        talla.setValor(valor != null ? valor.trim().toUpperCase() : "");
        talla.setEstado(estado);
        return talla;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsTalla> listaActivos = dao.mtdListarActivos();
        List<clsTipoTalla> tipos = daoTipoTalla.mtdListarActivos();
        request.setAttribute("listaTallas", listaActivos);
        request.setAttribute("listaTipoTallas", tipos);
        request.getRequestDispatcher("VistaTalla/TallaMain.jsp").forward(request, response);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String getServletInfo() {
        return "Controlador de tallas";
    }
}
