<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String mensajeError = (String) request.getAttribute("mensajeError");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String emailIngresado = (String) request.getAttribute("emailIngresado");
    if (emailIngresado == null) {
        emailIngresado = "";
    }
    String emailEscapado = emailIngresado
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <title>Ingreso de clientes</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #e8f5e9, #bbdefb);
                margin: 0;
                padding: 0;
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .login-container {
                background: #ffffff;
                border-radius: 12px;
                box-shadow: 0 18px 40px rgba(25, 118, 210, 0.15);
                padding: 2.5rem 2.25rem;
                width: 360px;
                box-sizing: border-box;
            }

            h1 {
                margin: 0 0 1.5rem 0;
                color: #0d47a1;
                font-size: 1.8rem;
                text-align: center;
            }

            .message {
                padding: 0.85rem 1rem;
                border-radius: 8px;
                margin-bottom: 1rem;
                font-size: 0.95rem;
                text-align: center;
            }

            .message.error {
                background: #ffebee;
                color: #c62828;
                border: 1px solid #ef5350;
            }

            .message.success {
                background: #e8f5e9;
                color: #2e7d32;
                border: 1px solid #81c784;
            }

            label {
                display: block;
                font-weight: 600;
                color: #1a237e;
                margin-bottom: 0.35rem;
            }

            input[type="email"],
            input[type="password"] {
                width: 100%;
                padding: 0.75rem;
                border-radius: 8px;
                border: 1.5px solid #90caf9;
                font-size: 0.95rem;
                margin-bottom: 1.1rem;
                transition: border-color 0.2s ease, box-shadow 0.2s ease;
                box-sizing: border-box;
            }

            input:focus {
                border-color: #1e88e5;
                box-shadow: 0 0 5px rgba(30, 136, 229, 0.4);
                outline: none;
            }

            .actions {
                display: flex;
                gap: 0.75rem;
                margin-top: 1.25rem;
            }

            .btn-primary,
            .btn-secondary {
                flex: 1;
                border: none;
                border-radius: 8px;
                padding: 0.75rem 0;
                font-size: 0.95rem;
                font-weight: 600;
                cursor: pointer;
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                text-align: center;
                text-decoration: none;
                display: inline-block;
            }

            .btn-primary {
                background: #1e88e5;
                color: #ffffff;
                box-shadow: 0 8px 18px rgba(30, 136, 229, 0.2);
            }

            .btn-primary:hover {
                transform: translateY(-1px);
                box-shadow: 0 10px 20px rgba(30, 136, 229, 0.25);
            }

            .btn-secondary {
                background: #e3f2fd;
                color: #0d47a1;
                box-shadow: 0 6px 12px rgba(13, 71, 161, 0.15);
            }

            .btn-secondary:hover {
                transform: translateY(-1px);
                box-shadow: 0 8px 18px rgba(13, 71, 161, 0.2);
            }

            .help-text {
                margin-top: 1.25rem;
                font-size: 0.85rem;
                color: #455a64;
                text-align: center;
                line-height: 1.4;
            }

            .help-text strong {
                color: #0d47a1;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h1>Zona de clientes</h1>

            <% if (mensajeExito != null) { %>
                <div class="message success"><%= mensajeExito %></div>
            <% } %>

            <% if (mensajeError != null) { %>
                <div class="message error"><%= mensajeError %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/loginCliente" method="post">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" value="<%= emailEscapado %>" placeholder="usuario@correo.com" required>

                <label for="clave">Contraseña</label>
                <input type="password" id="clave" name="clave" placeholder="Ingresa tu contraseña" minlength="4" maxlength="30" required>

                <div class="actions">
                    <button type="submit" class="btn-primary">Ingresar</button>
                    <a class="btn-secondary" href="<%= request.getContextPath() %>/registroCliente">Registrar</a>
                </div>
            </form>

            <p class="help-text">
                ¿Aún no tienes cuenta? Regístrate para obtener acceso como <strong>Cliente (ID 5)</strong>.
            </p>
        </div>
    </body>
</html>