package Controlador;

import Modelo.clsItemCarrito;
import Modelo.clsProducto;
import ModeloDao.clsDAOCategoria;
import ModeloDao.clsDAOProducto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControlCatalogo", urlPatterns = {"/catalogo"})
public class ControlCatalogo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final clsDAOProducto daoProducto = new clsDAOProducto();
    private final clsDAOCategoria daoCategoria = new clsDAOCategoria();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String textoBusqueda = limpiarParametro(request.getParameter("buscar"));
        Integer categoriaSeleccionada = obtenerCategoriaSeleccionada(request.getParameter("categoria"));

        List<clsProducto> productos = obtenerProductos(textoBusqueda);
        productos = filtrarPorCategoria(productos, categoriaSeleccionada);
        productos.sort(Comparator.comparing(clsProducto::getNombre,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        request.setAttribute("productosCatalogo", productos);
        request.setAttribute("categorias", daoCategoria.mtdListarActivos());
        request.setAttribute("textoBusqueda", textoBusqueda);
        request.setAttribute("categoriaSeleccionada", categoriaSeleccionada);
        request.setAttribute("totalItemsCarrito", obtenerTotalItemsCarrito(request));
        request.getRequestDispatcher("VistaMenu/CatalogoMain.jsp").forward(request, response);
    }

    private List<clsProducto> obtenerProductos(String textoBusqueda) {
        List<clsProducto> productos;
        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            productos = filtrarActivos(daoProducto.mtdBuscar(textoBusqueda));
        } else {
            productos = daoProducto.mtdListarActivos();
        }
        return productos != null ? new ArrayList<>(productos) : new ArrayList<>();
    }

    private List<clsProducto> filtrarActivos(List<clsProducto> productos) {
        List<clsProducto> activos = new ArrayList<>();
        if (productos != null) {
            for (clsProducto producto : productos) {
                if (producto != null && producto.getEstado() == 1) {
                    activos.add(producto);
                }
            }
        }
        return activos;
    }

    private List<clsProducto> filtrarPorCategoria(List<clsProducto> productos, Integer categoriaSeleccionada) {
        if (categoriaSeleccionada == null) {
            return productos;
        }
        List<clsProducto> filtrados = new ArrayList<>();
        for (clsProducto producto : productos) {
            if (producto.getIdCategoria() == categoriaSeleccionada) {
                filtrados.add(producto);
            }
        }
        return filtrados;
    }

    private Integer obtenerCategoriaSeleccionada(String parametro) {
        if (parametro == null || parametro.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(parametro.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String limpiarParametro(String valor) {
        if (valor == null) {
            return null;
        }
        String texto = valor.trim();
        return texto.isEmpty() ? null : texto;
    }
    private int obtenerTotalItemsCarrito(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return 0;
        }
        @SuppressWarnings("unchecked")
        List<clsItemCarrito> carrito = (List<clsItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            return 0;
        }
        int total = 0;
        for (clsItemCarrito item : carrito) {
            total += item.getCantidad();
        }
        return total;
    }
}
