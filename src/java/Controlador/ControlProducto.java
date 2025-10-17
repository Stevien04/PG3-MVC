package Controlador;

import Modelo.clsProducto;
import Modelo.clsProductoTalla;
import ModeloDao.clsDAOCategoria;
import ModeloDao.clsDAOColor;
import ModeloDao.clsDAOMarca;
import ModeloDao.clsDAOModelo;
import ModeloDao.clsDAOProducto;
import ModeloDao.clsDAOProductoTalla;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@WebServlet(name = "srvProducto", urlPatterns = {"/srvProducto"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class ControlProducto extends HttpServlet {

    private final clsDAOProducto daoProducto = new clsDAOProducto();
    private final clsDAOProductoTalla daoProductoTalla = new clsDAOProductoTalla();
    private final clsDAOCategoria daoCategoria = new clsDAOCategoria();
    private final clsDAOMarca daoMarca = new clsDAOMarca();
    private final clsDAOColor daoColor = new clsDAOColor();
    private final clsDAOModelo daoModelo = new clsDAOModelo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                mostrarPaginaPrincipal(request, response);
                break;
            case "editar":
                mostrarFormularioEdicion(request, response);
                break;
            case "cambiarEstado":
                cambiarEstadoProducto(request, response);
                break;
            case "buscar":
                buscarProductos(request, response);
                break;
            default:
                response.sendRedirect("srvProducto?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if ("agregar".equals(accion)) {
            procesarAgregar(request, response);
        } else if ("actualizar".equals(accion)) {
            procesarActualizar(request, response);
        } else {
            response.sendRedirect("srvProducto?accion=listar");
        }
    }

    private void mostrarPaginaPrincipal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarListasBasicas(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idProducto = parseEntero(request.getParameter("id"), -1);
        if (idProducto <= 0) {
            response.sendRedirect("srvProducto?accion=listar");
            return;
        }

        clsProducto producto = daoProducto.mtdObtenerPorId(idProducto);
        if (producto == null) {
            response.sendRedirect("srvProducto?accion=listar");
            return;
        }

        request.setAttribute("producto", producto);
        cargarListasBasicas(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private void cambiarEstadoProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idProducto = parseEntero(request.getParameter("id"), -1);
        if (idProducto > 0) {
            daoProducto.mtdCambiarEstado(idProducto);
        }
        response.sendRedirect("srvProducto?accion=listar");
    }

    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String texto = request.getParameter("texto");
        String terminoBusqueda = texto != null ? texto.trim() : "";
        request.setAttribute("textoBusqueda", terminoBusqueda);
        request.setAttribute("listaBusquedaProductos", daoProducto.mtdBuscar(terminoBusqueda));
        cargarListasBasicas(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private void procesarAgregar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clsProducto producto = construirProductoDesdeRequest(request);
        String mensajeValidacion = validarProducto(producto, false);

        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("productoForm", producto);
            reenviarConError(request, response);
            return;
        }

        if (!daoProducto.mtdAgregar(producto)) {
            request.setAttribute("mensajeError", "No se pudo registrar el producto. Intente nuevamente.");
            request.setAttribute("productoForm", producto);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvProducto?accion=listar");
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idProducto = parseEntero(request.getParameter("idProducto"), -1);
        if (idProducto <= 0) {
            response.sendRedirect("srvProducto?accion=listar");
            return;
        }

        clsProducto productoExistente = daoProducto.mtdObtenerPorId(idProducto);
        if (productoExistente == null) {
            response.sendRedirect("srvProducto?accion=listar");
            return;
        }

        clsProducto producto = construirProductoDesdeRequest(request);
        producto.setIdProducto(idProducto);

        // Si no se sube una nueva foto, conservar la existente
        if (producto.getFoto() == null) {
            producto.setFoto(productoExistente.getFoto());
        }

        String mensajeValidacion = validarProducto(producto, true);
        if (mensajeValidacion != null) {
            request.setAttribute("mensajeError", mensajeValidacion);
            request.setAttribute("producto", producto);
            reenviarConError(request, response);
            return;
        }

        if (!daoProducto.mtdEditar(producto)) {
            request.setAttribute("mensajeError", "No se pudo actualizar la información del producto.");
            request.setAttribute("producto", producto);
            reenviarConError(request, response);
            return;
        }

        response.sendRedirect("srvProducto?accion=listar");
    }

    private clsProducto construirProductoDesdeRequest(HttpServletRequest request)
            throws ServletException, IOException {
        clsProducto producto = new clsProducto();
        producto.setNombre(obtenerParametro(request, "nombre"));
        producto.setIdCategoria(parseEntero(request.getParameter("idCategoria"), 0));
        producto.setIdMarca(parseEntero(request.getParameter("idMarca"), 0));
        producto.setIdModelo(parseEnteroNullable(request.getParameter("idModelo")));
        producto.setIdColor(parseEnteroNullable(request.getParameter("idColor")));
        producto.setCantidad(parseEntero(request.getParameter("cantidad"), -1));
        producto.setPrecioUnitario(parseBigDecimal(request.getParameter("precioUnitario")));
        producto.setEstado(parseEntero(request.getParameter("estado"), 1));

        Part fotoPart = request.getPart("foto");
        producto.setFoto(leerBytesDesdePart(fotoPart));
        return producto;
    }

    private String validarProducto(clsProducto producto, boolean esActualizacion) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return "El nombre del producto es obligatorio.";
        }
        if (producto.getIdCategoria() <= 0) {
            return "Seleccione una categoría válida.";
        }
        if (producto.getIdMarca() <= 0) {
            return "Seleccione una marca válida.";
        }
        if (producto.getCantidad() < 0) {
            return "La cantidad debe ser mayor o igual a cero.";
        }
        if (producto.getPrecioUnitario() == null || producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            return "Ingrese un precio unitario válido.";
        }
        if (producto.getEstado() != 0 && producto.getEstado() != 1) {
            return "Seleccione un estado válido.";
        }
        if (!esActualizacion && (producto.getFoto() == null || producto.getFoto().length == 0)) {
            return "Debe seleccionar una fotografía para el producto.";
        }
        producto.setNombre(producto.getNombre().trim());
        return null;
    }

    private void cargarListasBasicas(HttpServletRequest request) {
        List<clsProducto> listaActivos = daoProducto.mtdListarPorEstado(1);
        List<clsProducto> listaInactivos = daoProducto.mtdListarPorEstado(0);
        List<clsProductoTalla> productoTallas = daoProductoTalla.mtdListar();

        request.setAttribute("listaProductosActivos", listaActivos);
        request.setAttribute("listaProductosInactivos", listaInactivos);
        request.setAttribute("listaProductoTallas", productoTallas);
        request.setAttribute("listaCategorias", daoCategoria.mtdListarActivos());
        request.setAttribute("listaMarcas", daoMarca.mtdListarActivos());
        request.setAttribute("listaColores", daoColor.mtdListarActivos());
        request.setAttribute("listaModelos", daoModelo.mtdListarActivos());

        if (request.getAttribute("textoBusqueda") == null) {
            request.setAttribute("textoBusqueda", "");
        }
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarListasBasicas(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private byte[] leerBytesDesdePart(Part parte) throws IOException {
        if (parte == null || parte.getSize() == 0) {
            return null;
        }
        try (InputStream entrada = parte.getInputStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] datos = new byte[4096];
            int bytesLeidos;
            while ((bytesLeidos = entrada.read(datos)) != -1) {
                buffer.write(datos, 0, bytesLeidos);
            }
            return buffer.toByteArray();
        }
    }

    private String obtenerParametro(HttpServletRequest request, String nombreParametro) {
        String valor = request.getParameter(nombreParametro);
        return valor != null ? valor.trim() : null;
    }

    private int parseEntero(String valor, int defecto) {
        try {
            if (valor == null || valor.trim().isEmpty()) return defecto;
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return defecto;
        }
    }

    private Integer parseEnteroNullable(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) return null;
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) return BigDecimal.ZERO;
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
