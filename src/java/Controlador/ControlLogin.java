package Controlador;

import Modelo.clsEmpleado;
import ModeloDao.clsDAOEmpleado;
import Util.AESGCMUtil;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class ControlLogin extends HttpServlet {

    private final clsDAOEmpleado daoEmpleado = new clsDAOEmpleado();
    private static final String SESSION_CAPTCHA_KEY = "captcha";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] CAPTCHA_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
    private static final int CAPTCHA_LENGTH = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String captcha = generarCaptcha();
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_CAPTCHA_KEY, captcha);
        request.setAttribute("captcha", captcha);
        request.getRequestDispatcher("/VistaLogin/LoginEm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String captchaIngresado = request.getParameter("captcha");

        HttpSession session = request.getSession(false);
        String captchaEsperado = session != null ? (String) session.getAttribute(SESSION_CAPTCHA_KEY) : null;

        if (captchaEsperado == null || captchaIngresado == null
                || !captchaEsperado.equalsIgnoreCase(captchaIngresado)) {
            mostrarError(request, response, "El captcha ingresado es incorrecto.");
            return;
        }

        Optional<clsEmpleado> empleadoOpt = daoEmpleado.buscarPorUsuario(usuario);
        if (empleadoOpt.isPresent() && AESGCMUtil.matches(contrasena, empleadoOpt.get().getClave())) {
            HttpSession sessionActual = session != null ? session : request.getSession(true);
            sessionActual.setAttribute("usuarioAutenticado", empleadoOpt.get());
            sessionActual.removeAttribute(SESSION_CAPTCHA_KEY);
            response.sendRedirect(request.getContextPath() + "/VistaLogin/LoginEm.jsp");
        } else {
            mostrarError(request, response, "Usuario o contraseña incorrectos.");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        doGet(request, response);
    }

    private String generarCaptcha() {
        char[] captcha = new char[CAPTCHA_LENGTH];
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha[i] = CAPTCHA_CHARS[RANDOM.nextInt(CAPTCHA_CHARS.length)];
        }
        return new String(captcha);
    }
}