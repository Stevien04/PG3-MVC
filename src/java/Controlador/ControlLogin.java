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

    // ============================================================
    //  MÉTODO GET
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica si se pidió regenerar el captcha manualmente
        String accion = request.getParameter("accion");

        HttpSession session = request.getSession(true);

        // Si no existe captcha o se pidió regenerar, genera uno nuevo
        if (session.getAttribute(SESSION_CAPTCHA_KEY) == null || "nuevoCaptcha".equalsIgnoreCase(accion)) {
            String captcha = generarCaptcha();
            session.setAttribute(SESSION_CAPTCHA_KEY, captcha);
            request.setAttribute("captcha", captcha);
        } else {
            // Si ya existe, lo usa
            request.setAttribute("captcha", session.getAttribute(SESSION_CAPTCHA_KEY));
        }

        request.getRequestDispatcher("/VistaLogin/LoginEm.jsp").forward(request, response);
    }

    // ============================================================
    //  MÉTODO POST
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String captchaIngresado = request.getParameter("captcha");

        HttpSession session = request.getSession(false);
        if (session == null) {
            mostrarError(request, response, "Sesión expirada. Intenta nuevamente.");
            return;
        }

        String captchaEsperado = (String) session.getAttribute(SESSION_CAPTCHA_KEY);

        // Verifica el captcha
        if (captchaEsperado == null || captchaIngresado == null
                || !captchaEsperado.equalsIgnoreCase(captchaIngresado)) {
            mostrarError(request, response, "El captcha ingresado es incorrecto.");
            return;
        }

        // Busca usuario
        Optional<clsEmpleado> empleadoOpt = daoEmpleado.buscarPorUsuario(usuario);
        if (empleadoOpt.isPresent() && AESGCMUtil.matches(contrasena, empleadoOpt.get().getClave())) {

            // Login exitoso
            HttpSession sessionActual = session != null ? session : request.getSession(true);
            sessionActual.setAttribute("usuarioAutenticado", empleadoOpt.get());
            sessionActual.removeAttribute(SESSION_CAPTCHA_KEY); // Limpia captcha
            response.sendRedirect(request.getContextPath() + "/VistaMenu/MenuMain.jsp");

        } else {
            // Login incorrecto -> regenerar captcha
            session.removeAttribute(SESSION_CAPTCHA_KEY);
            session.setAttribute(SESSION_CAPTCHA_KEY, generarCaptcha());
            mostrarError(request, response, "Usuario o contraseña incorrectos.");
        }
    }

    // ============================================================
    //  MÉTODOS AUXILIARES
    // ============================================================
    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_CAPTCHA_KEY) != null) {
            request.setAttribute("captcha", session.getAttribute(SESSION_CAPTCHA_KEY));
        }

        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/VistaLogin/LoginEm.jsp").forward(request, response);
    }

    private String generarCaptcha() {
        char[] captcha = new char[CAPTCHA_LENGTH];
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha[i] = CAPTCHA_CHARS[RANDOM.nextInt(CAPTCHA_CHARS.length)];
        }
        return new String(captcha);
    }
}
