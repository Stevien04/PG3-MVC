<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de cliente</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #fce4ec, #e3f2fd);
                margin: 0;
                padding: 20px;
                color: #1a237e;
            }

            .form-container {
                background: #ffffff;
                border-radius: 12px;
                box-shadow: 0 12px 30px rgba(63, 81, 181, 0.15);
                padding: 2rem 2.5rem;
                max-width: 560px;
                margin: 30px auto;
                box-sizing: border-box;
            }

            h2 {
                margin-top: 0;
                color: #0d47a1;
                text-align: center;
                font-size: 2rem;
            }

            p.subtitle {
                text-align: center;
                color: #546e7a;
                margin-top: -10px;
                margin-bottom: 1.5rem;
            }

            label {
                display: block;
                margin-bottom: 0.45rem;
                color: #0d47a1;
                font-weight: 600;
            }

            input[type="text"],
            input[type="email"],
            input[type="password"],
            select {
                width: 100%;
                padding: 0.75rem;
                border: 1.5px solid #90caf9;
                border-radius: 8px;
                font-size: 0.95rem;
                margin-bottom: 1.2rem;
                transition: border-color 0.2s ease, box-shadow 0.2s ease;
                box-sizing: border-box;
            }

            input:focus,
            select:focus {
                border-color: #1e88e5;
                box-shadow: 0 0 6px rgba(30, 136, 229, 0.35);
                outline: none;
            }

            .mensaje-error {
                background: #ffebee;
                color: #c62828;
                border: 1px solid #ef5350;
                padding: 0.9rem 1rem;
                border-radius: 8px;
                margin-bottom: 1.2rem;
                text-align: center;
                font-weight: 600;
            }

            .static-info {
                background: #e3f2fd;
                color: #0d47a1;
                border: 1px dashed #64b5f6;
                padding: 0.9rem 1rem;
                border-radius: 8px;
                margin-bottom: 1.5rem;
                text-align: center;
                font-weight: 500;
            }

            button {
                width: 100%;
                padding: 0.85rem;
                border: none;
                border-radius: 8px;
                background: #1e88e5;
                color: #ffffff;
                font-size: 1rem;
                font-weight: 600;
                cursor: pointer;
                transition: transform 0.2s ease, box-shadow 0.2s ease;
            }

            button:hover {
                transform: translateY(-1px);
                box-shadow: 0 10px 18px rgba(30, 136, 229, 0.25);
            }

            .back-link {
                margin-top: 1.5rem;
                text-align: center;
                font-size: 0.9rem;
            }

            .back-link a {
                color: #0d47a1;
                text-decoration: none;
                font-weight: 600;
            }

            .back-link a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Registra tu cuenta</h2>
            <p class="subtitle">Completa el formulario para obtener acceso como cliente.</p>

            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>

            <div class="static-info">
                Cargo asignado automáticamente: <strong>Cliente (ID ${cargoClienteId})</strong>
            </div>

            <form action="${pageContext.request.contextPath}/registroCliente" method="post">
                <input type="hidden" name="cargoId" value="${cargoClienteId}">

                <label for="nombre">Nombre</label>
                <input type="text" id="nombre" name="nombre"
                       value="${not empty clienteForm ? clienteForm.nombre : ''}"
                       maxlength="50" required
                       pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
                       title="Solo letras y espacios. Máximo 50 caracteres."
                       oninput="this.value = this.value.toUpperCase();">

                <label for="apellido">Apellido</label>
                <input type="text" id="apellido" name="apellido"
                       value="${not empty clienteForm ? clienteForm.apellido : ''}"
                       maxlength="50" required
                       pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
                       title="Solo letras y espacios. Máximo 50 caracteres."
                       oninput="this.value = this.value.toUpperCase();">

                <label for="idTipoDocumento">Tipo de documento</label>
                <select id="idTipoDocumento" name="idTipoDocumento" required>
                    <option value="">Selecciona una opción</option>
                    <c:forEach var="tipo" items="${listaTiposDocumento}">
                        <option value="${tipo.idTipoDocumento}"
                                ${not empty clienteForm and clienteForm.idTipoDocumento == tipo.idTipoDocumento ? 'selected' : ''}>
                            ${tipo.nombre}
                        </option>
                    </c:forEach>
                </select>

                <label for="numeroDocumento">Número de documento</label>
                <input type="text" id="numeroDocumento" name="numeroDocumento"
                       value="${not empty clienteForm ? clienteForm.numeroDocumento : ''}"
                       pattern="[0-9]{8,15}" maxlength="15" required
                       title="Solo números. Entre 8 y 15 dígitos.">

                <label for="telefono">Teléfono</label>
                <input type="text" id="telefono" name="telefono"
                       value="${not empty clienteForm ? clienteForm.telefono : ''}"
                       pattern="[0-9]{6,15}" maxlength="15" required
                       title="Solo números. Entre 6 y 15 dígitos.">

                <label for="direccion">Dirección</label>
                <input type="text" id="direccion" name="direccion"
                       value="${not empty clienteForm ? clienteForm.direccion : ''}"
                       maxlength="80" required
                       title="Entre 5 y 80 caracteres."
                       oninput="this.value = this.value.toUpperCase();">

                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email"
                       value="${not empty clienteForm ? clienteForm.email : ''}"
                       maxlength="80" required
                       placeholder="usuario@correo.com">

                <label for="clave">Contraseña</label>
                <input type="password" id="clave" name="clave"
                       minlength="4" maxlength="30" required
                       placeholder="Mínimo 4 y máximo 30 caracteres">

                <button type="submit">Crear cuenta</button>
            </form>

            <div class="back-link">
                ¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/loginCliente">Inicia sesión aquí</a>.
            </div>
        </div>
    </body>
</html>