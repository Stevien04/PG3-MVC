package Controlador;

import Modelo.clsItemCarrito;
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

@WebServlet(name = "ControlCheckout", urlPatterns = {"/checkout"})
public class ControlCheckout extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final clsDAOProducto daoProducto = new clsDAOProducto();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<clsItemCarrito> carrito = obtenerCarrito(session);

        if (carrito == null || carrito.isEmpty()) {
            guardarMensaje(session, "mensajeCarrito", "alerta",
                    "Tu carrito está vacío. Agrega productos antes de simular el pago.");
            response.sendRedirect(request.getContextPath() + "/carrito");
            return;
        }

        request.setAttribute("itemsCarrito", carrito);
        request.setAttribute("totalItemsCarrito", contarItems(carrito));
        request.setAttribute("totalCarrito", calcularTotal(carrito));

        request.getRequestDispatcher("VistaMenu/CheckoutPaypal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String accion = request.getParameter("accion");
        if (accion == null || !"simular".equalsIgnoreCase(accion.trim())) {
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        List<clsItemCarrito> carrito = obtenerCarrito(session);
        if (carrito == null || carrito.isEmpty()) {
            guardarMensaje(session, "mensajeCarrito", "alerta",
                    "No hay productos en tu carrito para procesar el pago.");
            response.sendRedirect(request.getContextPath() + "/carrito");
            return;
        }

        List<clsItemCarrito> resumenCompra = copiarCarrito(carrito);
        boolean compraProcesada = daoProducto.mtdProcesarCompra(carrito);

        if (!compraProcesada) {
            guardarMensaje(session, "mensajeCarrito", "error",
                    "No se pudo completar la compra. Verifica el stock disponible e inténtalo nuevamente.");
            response.sendRedirect(request.getContextPath() + "/carrito");
            return;
        }

        session.removeAttribute("carrito");

        request.setAttribute("itemsComprados", resumenCompra);
        request.setAttribute("totalItemsCarrito", contarItems(resumenCompra));
        request.setAttribute("totalCarrito", calcularTotal(resumenCompra));

        request.getRequestDispatcher("VistaMenu/CheckoutResultado.jsp").forward(request, response);
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

    private List<clsItemCarrito> copiarCarrito(List<clsItemCarrito> original) {
        List<clsItemCarrito> copia = new ArrayList<>();
        for (clsItemCarrito item : original) {
            if (item == null) {
                continue;
            }
            clsItemCarrito copiaItem = new clsItemCarrito();
            copiaItem.setIdProducto(item.getIdProducto());
            copiaItem.setNombre(item.getNombre());
            copiaItem.setPrecioUnitario(item.getPrecioUnitario());
            copiaItem.setCantidad(item.getCantidad());
            copiaItem.setStockDisponible(item.getStockDisponible());
            copiaItem.setNombreCategoria(item.getNombreCategoria());
            copiaItem.setNombreMarca(item.getNombreMarca());
            copiaItem.setNombreModelo(item.getNombreModelo());
            copiaItem.setNombreColor(item.getNombreColor());
            copiaItem.setFotoBase64(item.getFotoBase64());
            copia.add(copiaItem);
        }
        return copia;
    }

    private void guardarMensaje(HttpSession session, String atributo, String tipo, String texto) {
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("tipo", tipo);
        mensaje.put("texto", texto);
        session.setAttribute(atributo, mensaje);
    }
}