package Controlador;

import Modelo.clsProducto;
import ModeloDao.clsDAOColor;
import ModeloDao.clsDAOCategoria;
import ModeloDao.clsDAOMarca;
import ModeloDao.clsDAOModelo;
import ModeloDao.clsDAOProducto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "srvProducto", urlPatterns = {"/srvProducto"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 20)
public class ControlProducto extends HttpServlet {

    private final clsDAOProducto daoProducto = new clsDAOProducto();
    private final clsDAOCategoria daoCategoria = new clsDAOCategoria();
    private final clsDAOMarca daoMarca = new clsDAOMarca();
    private final clsDAOModelo daoModelo = new clsDAOModelo();
    private final clsDAOColor daoColor = new clsDAOColor();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarActivos";
        }

        switch (accion) {
            case "listarActivos":
                listarProductos(request, response, true);
                break;
            case "listarInactivos":
                listarProductos(request, response, false);
                break;
            case "editar":
                mostrarEdicion(request, response);
                break;
            case "cambiarEstado":
                cambiarEstado(request, response);
                break;
            case "buscar":
                buscarProductos(request, response);
                break;
            default:
                response.sendRedirect("srvProducto?accion=listarActivos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("srvProducto?accion=listarActivos");
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
                response.sendRedirect("srvProducto?accion=listarActivos");
                break;
        }
    }

    private void procesarAgregar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean mostrarActivos = esVistaActivos(request.getParameter("vista"));
        request.setAttribute("mostrarActivos", mostrarActivos);

        clsProducto producto = new clsProducto();
        if (!asignarDatosFormulario(request, producto, false)) {
            request.setAttribute("productoForm", producto);
            reenviarConError(request, response, false);
            return;
        }

        if (daoProducto.mtdExisteNombre(producto.getNombre())) {
            request.setAttribute("mensajeErrorProducto", "Ya existe un producto con el mismo nombre.");
            request.setAttribute("productoForm", producto);
            reenviarConError(request, response, false);
            return;
        }

        if (!daoProducto.mtdAgregar(producto)) {
            request.setAttribute("mensajeErrorProducto", "No se pudo registrar el producto. Intente nuevamente.");
            request.setAttribute("productoForm", producto);
            reenviarConError(request, response, false);
            return;
        }

        response.sendRedirect("srvProducto?accion=listarActivos");
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean mostrarActivos = esVistaActivos(request.getParameter("vista"));
        request.setAttribute("mostrarActivos", mostrarActivos);

        String idParam = request.getParameter("id");
        int idProducto;
        try {
            idProducto = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            response.sendRedirect("srvProducto?accion=listarActivos");
            return;
        }

        clsProducto existente = daoProducto.mtdObtenerPorId(idProducto);
        if (existente == null) {
            response.sendRedirect("srvProducto?accion=listarActivos");
            return;
        }

        clsProducto producto = new clsProducto();
        producto.setIdProducto(idProducto);
        producto.setFoto(existente.getFoto());

        if (!asignarDatosFormulario(request, producto, true)) {
            request.setAttribute("producto", producto);
            reenviarConError(request, response, true);
            return;
        }

        if (daoProducto.mtdExisteNombreEnOtro(producto.getNombre(), producto.getIdProducto())) {
            request.setAttribute("mensajeErrorProducto", "Ya existe otro producto con ese nombre.");
            request.setAttribute("producto", producto);
            reenviarConError(request, response, true);
            return;
        }

        if (!daoProducto.mtdEditar(producto)) {
            request.setAttribute("mensajeErrorProducto", "No se pudo actualizar el producto. Intente nuevamente.");
            request.setAttribute("producto", producto);
            reenviarConError(request, response, true);
            return;
        }

        if (mostrarActivos) {
            response.sendRedirect("srvProducto?accion=listarActivos");
        } else {
            response.sendRedirect("srvProducto?accion=listarInactivos");
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response, boolean activos)
            throws ServletException, IOException {
        List<clsProducto> lista = activos ? daoProducto.mtdListarActivos() : daoProducto.mtdListarInactivos();
        request.setAttribute("listaProductos", lista);
        request.setAttribute("mostrarActivos", activos);
        cargarListasReferencia(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        boolean mostrarActivos = esVistaActivos(request.getParameter("vista"));
        int idProducto;
        try {
            idProducto = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            response.sendRedirect("srvProducto?accion=listarActivos");
            return;
        }

        clsProducto producto = daoProducto.mtdObtenerPorId(idProducto);
        if (producto == null) {
            response.sendRedirect("srvProducto?accion=listarActivos");
            return;
        }

        request.setAttribute("producto", producto);
        listarProductos(request, response, mostrarActivos);
    }

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String vista = request.getParameter("vista");
        try {
            int idProducto = Integer.parseInt(idParam);
            daoProducto.mtdCambiarEstado(idProducto);
        } catch (NumberFormatException ex) {
            // Ignorar
        }

        if (esVistaActivos(vista)) {
            response.sendRedirect("srvProducto?accion=listarActivos");
        } else {
            response.sendRedirect("srvProducto?accion=listarInactivos");
        }
    }

    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String texto = request.getParameter("texto");
        List<clsProducto> resultado = daoProducto.mtdBuscar(texto != null ? texto.trim() : "");
        request.setAttribute("listaProductos", resultado);
        request.setAttribute("textoBusqueda", texto);
        request.setAttribute("mostrarActivos", true);
        cargarListasReferencia(request);
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private boolean asignarDatosFormulario(HttpServletRequest request, clsProducto producto, boolean esEdicion)
            throws ServletException, IOException {

        // Obtener y validar IDs
        Integer idCategoria = parseEnteroRequerido(request.getParameter("idCategoria"));
        if (idCategoria == null) {
            request.setAttribute("mensajeErrorProducto", "Debe seleccionar una categoría válida.");
            return false;
        }
        producto.setIdCategoria(idCategoria);

        Integer idMarca = parseEnteroRequerido(request.getParameter("idMarca"));
        if (idMarca == null) {
            request.setAttribute("mensajeErrorProducto", "Debe seleccionar una marca válida.");
            return false;
        }
        producto.setIdMarca(idMarca);

        Integer idModelo = parseEnteroOpcional(request.getParameter("idModelo"));
        producto.setIdModelo(idModelo);

        Integer idColor = parseEnteroOpcional(request.getParameter("idColor"));
        producto.setIdColor(idColor);

        String nombreCategoria = "";
        String nombreMarca = "";
        String nombreModelo = "";

        try {
            nombreCategoria = daoCategoria.mtdListarActivos().stream()
                    .filter(c -> c.getIdCategoria() == idCategoria)
                    .map(c -> c.getNombre())
                    .findFirst()
                    .orElse("");
            nombreMarca = daoMarca.mtdListarActivos().stream()
                    .filter(m -> m.getIdMarca() == idMarca)
                    .map(m -> m.getNombre())
                    .findFirst()
                    .orElse("");
            if (idModelo != null) {
                nombreModelo = daoModelo.mtdListarActivos().stream()
                        .filter(md -> md.getIdModelo() == idModelo)
                        .map(md -> md.getNombre())
                        .findFirst()
                        .orElse("");
            }
        } catch (Exception e) {
            request.setAttribute("mensajeErrorProducto", "Error al obtener nombres de categoría, marca o modelo.");
            return false;
        }

        // Crear nombre automático
        String nombreGenerado = (nombreCategoria + " " + nombreMarca + " " + nombreModelo).trim().toUpperCase();
        producto.setNombre(nombreGenerado);

        // Validar cantidad y precio
        Integer cantidad = parseEnteroRequerido(request.getParameter("cantidad"));
        if (cantidad == null || cantidad < 0) {
            request.setAttribute("mensajeErrorProducto", "La cantidad debe ser un número entero mayor o igual a cero.");
            return false;
        }
        producto.setCantidad(cantidad);

        BigDecimal precio = parseBigDecimal(request.getParameter("precioUnitario"));
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            request.setAttribute("mensajeErrorProducto", "El precio debe ser un número positivo.");
            return false;
        }
        producto.setPrecioUnitario(precio);

        Integer estado = parseEnteroRequerido(request.getParameter("estado"));
        if (estado == null || (estado != 0 && estado != 1)) {
            request.setAttribute("mensajeErrorProducto", "Debe seleccionar un estado válido.");
            return false;
        }
        producto.setEstado(estado);

    
        if (esSolicitudMultipart(request)) {
            Part fotoPart = request.getPart("foto");
            if (fotoPart != null && fotoPart.getSize() > 0) {
                producto.setFoto(leerBytes(fotoPart));
            } else if (!esEdicion) {
                producto.setFoto(null);
            }
        }

        return true;
    }

    private void reenviarConError(HttpServletRequest request, HttpServletResponse response, boolean esEdicion)
            throws ServletException, IOException {
        Object flag = request.getAttribute("mostrarActivos");
        boolean mostrarActivos = flag instanceof Boolean ? (Boolean) flag : true;
        List<clsProducto> lista = mostrarActivos ? daoProducto.mtdListarActivos() : daoProducto.mtdListarInactivos();
        request.setAttribute("listaProductos", lista);
        cargarListasReferencia(request);
        if (esEdicion && request.getAttribute("producto") == null) {
            request.setAttribute("mensajeErrorProducto", "No se pudo cargar la información para editar.");
        }
        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    private void cargarListasReferencia(HttpServletRequest request) {
        request.setAttribute("categorias", daoCategoria.mtdListarActivos());
        request.setAttribute("marcas", daoMarca.mtdListarActivos());
        request.setAttribute("modelos", daoModelo.mtdListarActivos());
        request.setAttribute("colores", daoColor.mtdListarActivos());
    }

    private boolean esVistaActivos(String vista) {
        return vista == null || !"inactivos".equalsIgnoreCase(vista);
    }

    private Integer parseEnteroRequerido(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseEnteroOpcional(String valor) {
        Integer numero = parseEnteroRequerido(valor);
        return (numero == null || numero <= 0) ? null : numero;
    }

    private BigDecimal parseBigDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean esSolicitudMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    private byte[] leerBytes(Part part) throws IOException {
        try (InputStream input = part.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    @Override
    public String getServletInfo() {
        return "Controlador de productos";
    }
}
