package Controlador;

import Modelo.clsProducto;
import Modelo.clsProductoTalla;
import Modelo.clsTalla;
import Modelo.dtoResumenProductoTalla;
import ModeloDao.clsDAOProducto;
import ModeloDao.clsDAOProductoTalla;
import ModeloDao.clsDAOTalla;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "srvProductoTalla", urlPatterns = {"/srvProductoTalla"})
public class ControlProductoTalla extends HttpServlet {

    private final clsDAOProductoTalla daoProductoTalla = new clsDAOProductoTalla();
    private final clsDAOProducto daoProducto = new clsDAOProducto();
    private final clsDAOTalla daoTalla = new clsDAOTalla();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                mostrarPrincipal(request, response);
                break;
            case "editar":
                mostrarEdicion(request, response);
                break;
            case "cambiarEstado":
                cambiarEstado(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        switch (accion) {
            case "agregar":
                procesarAgregar(request, response);
                break;
            case "actualizar":
                procesarActualizar(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
                break;
        }
    }

    private void mostrarPrincipal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        trasladarMensajesFlash(request);

        List<clsProductoTalla> lista = daoProductoTalla.mtdListar();
        request.setAttribute("listaProductoTallas", lista);
        request.setAttribute("resumenProductoTalla", construirResumen(lista));

        clsProductoTalla enEdicion = (clsProductoTalla) request.getAttribute("productoTalla");
        List<clsProducto> productos = obtenerProductosParaFormulario(enEdicion);
        List<clsTalla> tallas = obtenerTallasParaFormulario(enEdicion);

        request.setAttribute("listaProductos", productos);
        request.setAttribute("listaTallas", tallas);

        request.getRequestDispatcher("VistaProductoTalla/ProductoTallaMain.jsp").forward(request, response);
    }

    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = parseInt(idParam);
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        clsProductoTalla productoTalla = daoProductoTalla.mtdObtenerPorId(id);
        if (productoTalla == null) {
            agregarMensajeFlash(request, "mensajeErrorProductoTalla", "La combinación seleccionada no existe.");
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        request.setAttribute("productoTalla", productoTalla);
        mostrarPrincipal(request, response);
    }

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        int id = parseInt(idParam);
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        if (daoProductoTalla.mtdCambiarEstado(id)) {
            agregarMensajeFlash(request, "mensajeExitoProductoTalla", "El estado se actualizó correctamente.");
        } else {
            agregarMensajeFlash(request, "mensajeErrorProductoTalla", "No se pudo cambiar el estado de la combinación.");
        }
        response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
    }

    private void procesarAgregar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clsProductoTalla formulario = construirDesdeFormulario(request);

        String mensajeError = validarFormulario(formulario);
        if (mensajeError != null) {
            request.setAttribute("mensajeErrorProductoTalla", mensajeError);
            request.setAttribute("productoTallaForm", formulario);
            mostrarPrincipal(request, response);
            return;
        }

        if (daoProductoTalla.mtdExisteCombinacion(formulario.getIdProducto(), formulario.getIdTalla(), null)) {
            request.setAttribute("mensajeErrorProductoTalla", "Ya existe una combinación para el producto y talla seleccionados.");
            request.setAttribute("productoTallaForm", formulario);
            mostrarPrincipal(request, response);
            return;
        }

        if (daoProductoTalla.mtdAgregar(formulario)) {
            agregarMensajeFlash(request, "mensajeExitoProductoTalla", "Combinación registrada correctamente.");
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
        } else {
            request.setAttribute("mensajeErrorProductoTalla", "No se pudo registrar la combinación. Intente nuevamente.");
            request.setAttribute("productoTallaForm", formulario);
            mostrarPrincipal(request, response);
        }
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = parseInt(idParam);
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        clsProductoTalla existente = daoProductoTalla.mtdObtenerPorId(id);
        if (existente == null) {
            agregarMensajeFlash(request, "mensajeErrorProductoTalla", "La combinación a actualizar no existe.");
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
            return;
        }

        clsProductoTalla formulario = construirDesdeFormulario(request);
        formulario.setIdProductoTalla(id);

        String mensajeError = validarFormulario(formulario);
        if (mensajeError != null) {
            request.setAttribute("mensajeErrorProductoTalla", mensajeError);
            request.setAttribute("productoTalla", formulario);
            mostrarPrincipal(request, response);
            return;
        }

        if (daoProductoTalla.mtdExisteCombinacion(formulario.getIdProducto(), formulario.getIdTalla(), formulario.getIdProductoTalla())) {
            request.setAttribute("mensajeErrorProductoTalla", "Ya existe otra combinación para el producto y talla seleccionados.");
            request.setAttribute("productoTalla", formulario);
            mostrarPrincipal(request, response);
            return;
        }

        if (daoProductoTalla.mtdActualizar(formulario, existente.getIdProducto())) {
            agregarMensajeFlash(request, "mensajeExitoProductoTalla", "Combinación actualizada correctamente.");
            response.sendRedirect(request.getContextPath() + "/srvProductoTalla?accion=listar");
        } else {
            request.setAttribute("mensajeErrorProductoTalla", "No se pudo actualizar la combinación. Intente nuevamente.");
            request.setAttribute("productoTalla", formulario);
            mostrarPrincipal(request, response);
        }
    }

    private clsProductoTalla construirDesdeFormulario(HttpServletRequest request) {
        clsProductoTalla productoTalla = new clsProductoTalla();
        productoTalla.setIdProducto(parseInt(request.getParameter("idProducto")));
        productoTalla.setIdTalla(parseInt(request.getParameter("idTalla")));
        productoTalla.setCantidad(parseInt(request.getParameter("cantidad")));
        String estadoParam = request.getParameter("estado");
        if (estadoParam == null || estadoParam.trim().isEmpty()) {
            productoTalla.setEstado(1);
        } else {
            productoTalla.setEstado(parseInt(estadoParam));
        }
        return productoTalla;
    }

    private String validarFormulario(clsProductoTalla formulario) {
        if (formulario.getIdProducto() <= 0) {
            return "Debe seleccionar un producto válido.";
        }
        if (formulario.getIdTalla() <= 0) {
            return "Debe seleccionar una talla válida.";
        }
        if (formulario.getCantidad() < 0) {
            return "La cantidad no puede ser negativa.";
        }
        if (formulario.getEstado() != null && formulario.getEstado() != 0 && formulario.getEstado() != 1) {
            return "El estado seleccionado no es válido.";
        }

        if (daoProducto.mtdObtenerPorId(formulario.getIdProducto()) == null) {
            return "El producto seleccionado no existe.";
        }
        if (daoTalla.mtdObtenerPorId(formulario.getIdTalla()) == null) {
            return "La talla seleccionada no existe.";
        }

        return null;
    }

    private List<clsProducto> obtenerProductosParaFormulario(clsProductoTalla enEdicion) {
        List<clsProducto> productos = new ArrayList<>(daoProducto.mtdListarActivos());
        if (enEdicion != null) {
            boolean existe = contieneProducto(productos, enEdicion.getIdProducto());
            if (!existe) {
                clsProducto producto = daoProducto.mtdObtenerPorId(enEdicion.getIdProducto());
                if (producto != null) {
                    productos.add(0, producto);
                }
            }
        }
        return productos;
    }

    private List<clsTalla> obtenerTallasParaFormulario(clsProductoTalla enEdicion) {
        List<clsTalla> tallas = new ArrayList<>(daoTalla.mtdListarActivos());
        if (enEdicion != null) {
            boolean existe = contieneTalla(tallas, enEdicion.getIdTalla());
            if (!existe) {
                clsTalla talla = daoTalla.mtdObtenerPorId(enEdicion.getIdTalla());
                if (talla != null) {
                    tallas.add(0, talla);
                }
            }
        }
        return tallas;
    }

    private boolean contieneProducto(List<clsProducto> productos, int idProducto) {
        for (clsProducto producto : productos) {
            if (producto.getIdProducto() == idProducto) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneTalla(List<clsTalla> tallas, int idTalla) {
        for (clsTalla talla : tallas) {
            if (talla.getIdTalla() == idTalla) {
                return true;
            }
        }
        return false;
    }

    private List<dtoResumenProductoTalla> construirResumen(List<clsProductoTalla> lista) {
        Map<Integer, dtoResumenProductoTalla> resumen = new LinkedHashMap<>();
        for (clsProductoTalla item : lista) {
            dtoResumenProductoTalla data = resumen.get(item.getIdProducto());
            if (data == null) {
                int cantidadProducto = item.getCantidadProducto() != null ? item.getCantidadProducto() : 0;
                data = new dtoResumenProductoTalla(item.getIdProducto(), item.getNombreProducto(), cantidadProducto, 0);
                resumen.put(item.getIdProducto(), data);
            }
            if (item.getEstado() == null || item.getEstado() == 1) {
                data.setSumaTallas(data.getSumaTallas() + item.getCantidad());
            }
        }
        return new ArrayList<>(resumen.values());
    }

    private void agregarMensajeFlash(HttpServletRequest request, String atributo, String mensaje) {
        HttpSession session = request.getSession();
        session.setAttribute(atributo, mensaje);
    }

    private void trasladarMensajesFlash(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        transferirMensaje(session, request, "mensajeExitoProductoTalla");
        transferirMensaje(session, request, "mensajeErrorProductoTalla");
    }

    private void transferirMensaje(HttpSession session, HttpServletRequest request, String atributo) {
        Object valor = session.getAttribute(atributo);
        if (valor != null) {
            request.setAttribute(atributo, valor);
            session.removeAttribute(atributo);
        }
    }

    private int parseInt(String valor) {
        if (valor == null) {
            return 0;
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}