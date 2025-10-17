<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String captcha = (String) request.getAttribute("captcha");
    if (captcha == null) {
        captcha = "";
    }
    String mensajeError = (String) request.getAttribute("mensajeError");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesión</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f5f5f5;
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 100vh;
            }
            .login-container {
                background: #fff;
                padding: 2rem;
                border-radius: 10px;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
                width: 360px;
            }
            h1 {
                text-align: center;
                margin-bottom: 1.5rem;
            }
            .form-group {
                margin-bottom: 1rem;
            }
            label {
                display: block;
                font-weight: bold;
                margin-bottom: 0.5rem;
            }
            input[type="text"],
            input[type="password"] {
                width: 100%;
                padding: 0.75rem;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 1rem;
            }
            .captcha-box {
                font-weight: bold;
                font-size: 1.5rem;
                letter-spacing: 0.4rem;
                background: linear-gradient(135deg, #93a5cf 0%, #e4efe9 100%);
                color: #2c3e50;
                text-align: center;
                border-radius: 6px;
                padding: 0.75rem;
                margin-bottom: 0.5rem;
            }
            .btn-submit {
                width: 100%;
                padding: 0.75rem;
                border: none;
                border-radius: 6px;
                background: #4caf50;
                color: #fff;
                font-size: 1rem;
                cursor: pointer;
                transition: background 0.3s ease;
            }
            .btn-submit:hover {
                background: #43a047;
            }
            .error-message {
                background: #ffcdd2;
                color: #b71c1c;
                border-radius: 6px;
                padding: 0.75rem;
                margin-bottom: 1rem;
                text-align: center;
            }
            .refresh {
                text-align: center;
                margin-bottom: 1rem;
            }
            .refresh a {
                color: #007bff;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h1>Iniciar sesión</h1>
            <% if (mensajeError != null) { %>
                <div class="error-message"><%= mensajeError %></div>
            <% } %>
            <form action="login" method="post">
                <div class="form-group">
                    <label for="usuario">Usuario</label>
                    <input type="text" id="usuario" name="usuario" required>
                </div>
                <div class="form-group">
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" required>
                </div>
                <div class="form-group">
                    <label>Captcha</label>
                    <div class="captcha-box"><%= captcha %></div>
                    <input type="text" name="captcha" placeholder="Ingresa el código" required>
                </div>
                <div class="refresh">
                    <a href="login">Generar nuevo captcha</a>
                </div>
                <button type="submit" class="btn-submit">Ingresar</button>
            </form>
        </div>
    </body>
</html>