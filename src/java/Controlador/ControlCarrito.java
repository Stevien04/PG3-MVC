package Controlador;

import Modelo.clsItemCarrito;
import Modelo.clsProducto;
import ModeloDao.clsDAOProducto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControlCarrito", urlPatterns = {"/carrito"})
public class ControlCarrito extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final clsDAOProducto daoProducto = new clsDAOProducto();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (!estaClienteAutenticado(session)) {
            guardarMensaje(session, "alerta", "Debes iniciar sesión para revisar tu carrito.");
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }

        List<clsItemCarrito> carrito = obtenerCarrito(session);

        request.setAttribute("itemsCarrito", carrito);
        request.setAttribute("totalItemsCarrito", contarItems(carrito));
        request.setAttribute("totalCarrito", calcularTotal(carrito));

        request.getRequestDispatcher("VistaMenu/CarritoMain.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        if (!estaClienteAutenticado(session)) {
            guardarMensaje(session, "alerta", "Debes iniciar sesión para gestionar tu carrito.");
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }

        String accion = obtenerParametro(request.getParameter("accion"));

        switch (accion) {
            case "agregar":
                agregarProducto(request, session);
                break;
            case "actualizar":
                actualizarCantidad(request, session);
                break;
            case "eliminar":
                eliminarProducto(request, session);
                break;
            case "vaciar":
                vaciarCarrito(session);
                break;
            default:
                guardarMensaje(session, "error", "Acción no reconocida para el carrito.");
                break;
        }

        response.sendRedirect(request.getContextPath() + obtenerRedireccion(request));
    }

    private void agregarProducto(HttpServletRequest request, HttpSession session) {
        int idProducto = parseEnteroPositivo(request.getParameter("idProducto"));
        int cantidad = parseEnteroPositivo(request.getParameter("cantidad"));

        if (idProducto <= 0 || cantidad <= 0) {
            guardarMensaje(session, "error", "No se pudo agregar el producto al carrito.");
            return;
        }

        clsProducto producto = daoProducto.mtdObtenerPorId(idProducto);
        if (producto == null || producto.getEstado() != 1) {
            guardarMensaje(session, "error", "El producto seleccionado no está disponible.");
            return;
        }

        if (producto.getCantidad() <= 0) {
            guardarMensaje(session, "error", "El producto no tiene stock disponible.");
            return;
        }

        List<clsItemCarrito> carrito = obtenerCarrito(session);
        clsItemCarrito existente = buscarItemPorId(carrito, idProducto);

        int stockDisponible = producto.getCantidad();
        if (existente == null) {
            int cantidadAgregar = Math.min(cantidad, stockDisponible);
            clsItemCarrito nuevo = construirItem(producto, cantidadAgregar);
            carrito.add(nuevo);
            if (cantidadAgregar < cantidad) {
                guardarMensaje(session, "alerta", "Solo se agregó la cantidad máxima disponible de este producto.");
            } else {
                guardarMensaje(session, "exito", "Producto agregado al carrito.");
            }
        } else {
            int nuevaCantidad = existente.getCantidad() + cantidad;
            if (nuevaCantidad > stockDisponible) {
                existente.setCantidad(stockDisponible);
                guardarMensaje(session, "alerta", "Se agregó la cantidad máxima disponible de este producto.");
            } else {
                existente.setCantidad(nuevaCantidad);
                guardarMensaje(session, "exito", "Cantidad del producto actualizada en el carrito.");
            }
            existente.setStockDisponible(stockDisponible);
        }

        session.setAttribute("carrito", carrito);
    }

    private void actualizarCantidad(HttpServletRequest request, HttpSession session) {
        int idProducto = parseEnteroPositivo(request.getParameter("idProducto"));
        int cantidad = parseEnteroPositivo(request.getParameter("cantidad"));

        if (idProducto <= 0 || cantidad <= 0) {
            guardarMensaje(session, "error", "Cantidad no válida para el producto.");
            return;
        }

        clsProducto producto = daoProducto.mtdObtenerPorId(idProducto);
        if (producto == null) {
            guardarMensaje(session, "error", "El producto ya no se encuentra disponible.");
            return;
        }

        List<clsItemCarrito> carrito = obtenerCarrito(session);
        clsItemCarrito item = buscarItemPorId(carrito, idProducto);
        if (item == null) {
            guardarMensaje(session, "error", "El producto no está en el carrito.");
            return;
        }

        int stockDisponible = producto.getCantidad();
        if (stockDisponible <= 0) {
            carrito.remove(item);
            session.setAttribute("carrito", carrito);
            guardarMensaje(session, "alerta", "El producto se retiró del carrito porque no tiene stock disponible.");
            return;
        }

        if (cantidad > stockDisponible) {
            item.setCantidad(stockDisponible);
            item.setStockDisponible(stockDisponible);
            guardarMensaje(session, "alerta", "Se asignó la cantidad máxima disponible para el producto.");
        } else {
            item.setCantidad(cantidad);
            item.setStockDisponible(stockDisponible);
            guardarMensaje(session, "exito", "Cantidad actualizada correctamente.");
        }

        session.setAttribute("carrito", carrito);
    }

    private void eliminarProducto(HttpServletRequest request, HttpSession session) {
        int idProducto = parseEnteroPositivo(request.getParameter("idProducto"));
        if (idProducto <= 0) {
            guardarMensaje(session, "error", "No se pudo eliminar el producto del carrito.");
            return;
        }

        List<clsItemCarrito> carrito = obtenerCarrito(session);
        boolean eliminado = carrito.removeIf(item -> item.getIdProducto() == idProducto);
        if (eliminado) {
            guardarMensaje(session, "exito", "Producto eliminado del carrito.");
        } else {
            guardarMensaje(session, "error", "El producto no se encontró en el carrito.");
        }
        session.setAttribute("carrito", carrito);
    }

    private void vaciarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
        guardarMensaje(session, "exito", "Carrito vaciado correctamente.");
    }

    private List<clsItemCarrito> obtenerCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<clsItemCarrito> carrito = (List<clsItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private clsItemCarrito buscarItemPorId(List<clsItemCarrito> carrito, int idProducto) {
        for (clsItemCarrito item : carrito) {
            if (item.getIdProducto() == idProducto) {
                return item;
            }
        }
        return null;
    }

    private clsItemCarrito construirItem(clsProducto producto, int cantidad) {
        clsItemCarrito item = new clsItemCarrito();
        item.setIdProducto(producto.getIdProducto());
        item.setNombre(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecioUnitario());
        item.setCantidad(cantidad);
        item.setStockDisponible(producto.getCantidad());
        item.setNombreCategoria(producto.getNombreCategoria());
        item.setNombreMarca(producto.getNombreMarca());
        item.setNombreModelo(producto.getNombreModelo());
        item.setNombreColor(producto.getNombreColor());
        item.setFotoBase64(producto.getFotoBase64());
        return item;
    }

    private int contarItems(List<clsItemCarrito> carrito) {
        int total = 0;
        for (clsItemCarrito item : carrito) {
            total += item.getCantidad();
        }
        return total;
    }

    private BigDecimal calcularTotal(List<clsItemCarrito> carrito) {
        BigDecimal total = BigDecimal.ZERO;
        for (clsItemCarrito item : carrito) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    private boolean estaClienteAutenticado(HttpSession session) {
        return session != null && session.getAttribute("clienteAutenticado") != null;
    }

    private int parseEnteroPositivo(String valor) {
        try {
            int numero = Integer.parseInt(valor);
            return Math.max(numero, 0);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void guardarMensaje(HttpSession session, String tipo, String texto) {
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("tipo", tipo);
        mensaje.put("texto", texto);
        session.setAttribute("mensajeCarrito", mensaje);
    }

    private String obtenerParametro(String valor) {
        return valor != null ? valor.trim().toLowerCase() : "";
    }

    private String obtenerRedireccion(HttpServletRequest request) {
        String redirect = request.getParameter("redirect");
        if (redirect == null || redirect.trim().isEmpty()) {
            return "/catalogo";
        }
        String destino = redirect.trim();
        if (!destino.startsWith("/")) {
            destino = "/" + destino;
        }
        return destino;
    }
}
