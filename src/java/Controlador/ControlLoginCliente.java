package Controlador;

import Modelo.clsCliente;
import ModeloDao.clsDAOCliente;
import Util.AESGCMUtil;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginClienteServlet", urlPatterns = {"/loginCliente"})
public class ControlLoginCliente extends HttpServlet {

    private static final int CARGO_CLIENTE_ID = 5;
    private final clsDAOCliente daoCliente = new clsDAOCliente();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object mensajeExito = session.getAttribute("mensajeExitoRegistro");
            if (mensajeExito != null) {
                request.setAttribute("mensajeExito", mensajeExito.toString());
                session.removeAttribute("mensajeExitoRegistro");
            }
        }

        request.getRequestDispatcher("/VistaLogin/LoginClien.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = obtenerParametro(request, "email").toLowerCase();
        String clave = request.getParameter("clave");

        Optional<clsCliente> clienteOpt = daoCliente.mtdBuscarPorEmail(email);

        if (clienteOpt.isPresent()) {
            clsCliente cliente = clienteOpt.get();

            if (cliente.getEstado() == 0) {
                mostrarError(request, response, email, "Tu cuenta está inactiva. Comunícate con soporte.");
                return;
            }

            if (AESGCMUtil.matches(clave, cliente.getClave())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("clienteAutenticado", cliente);
                session.setAttribute("cargoId", CARGO_CLIENTE_ID);
                response.sendRedirect(request.getContextPath() + "/VistaMenu/MenuMain.jsp");
                return;
            }
        }

        mostrarError(request, response, email, "Correo o contraseña incorrectos.");
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String emailIngresado, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.setAttribute("emailIngresado", emailIngresado);
        request.getRequestDispatcher("/VistaLogin/LoginClien.jsp").forward(request, response);
    }

    private String obtenerParametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor != null ? valor.trim() : "";
    }
}