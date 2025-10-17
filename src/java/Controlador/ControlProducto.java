package Controlador;

import Modelo.clsProducto;
import Modelo.clsProductoTalla;
import ModeloDao.clsDAOProducto;
import ModeloDao.clsDAOProductoTalla;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "srvProducto", urlPatterns = {"/srvProducto"})
public class ControlProducto extends HttpServlet {

    private final clsDAOProducto daoProducto = new clsDAOProducto();
    private final clsDAOProductoTalla daoProductoTalla = new clsDAOProductoTalla();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsProducto> productos = daoProducto.mtdListar();
        List<clsProductoTalla> productoTallas = daoProductoTalla.mtdListar();

        request.setAttribute("listaProductos", productos);
        request.setAttribute("listaProductoTallas", productoTallas);

        request.getRequestDispatcher("VistaProducto/ProductoMain.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador para la vista de productos";
    }
}