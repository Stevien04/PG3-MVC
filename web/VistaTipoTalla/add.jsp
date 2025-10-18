<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <title>Nuevo Tipo de Talla</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #e3f2fd, #bbdefb);
                margin: 0;
                padding: 15px;
                color: #0d47a1;
            }

            .form-container {
                background: #ffffff;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                padding: 1.5rem 2rem;
                max-width: 500px;
                margin: 0 auto;
                box-sizing: border-box;
            }

            h2 {
                color: #0d47a1;
                margin-bottom: 1rem;
                font-weight: 600;
                text-align: center;
            }

            label {
                display: block;
                text-align: left;
                margin-bottom: 0.5rem;
                color: #0d47a1;
                font-weight: 500;
            }

            input[type="text"],
            select {
                width: 100%;
                padding: 9px;
                border: 1.5px solid #90caf9;
                border-radius: 6px;
                font-size: 15px;
                outline: none;
                transition: 0.2s;
                margin-bottom: 1rem;
                box-sizing: border-box;
            }

            input[type="text"]:focus,
            select:focus {
                border-color: #1976d2;
                box-shadow: 0 0 5px rgba(25,118,210,0.3);
            }

            button {
                background-color: #1976d2;
                color: #fff;
                border: none;
                padding: 10px 18px;
                border-radius: 8px;
                cursor: pointer;
                font-size: 15px;
                font-weight: 600;
                width: 100%;
                transition: background-color 0.2s ease, transform 0.2s;
            }

            button:hover {
                background-color: #0d47a1;
                transform: scale(1.02);
            }

            .mensaje-error {
                background: #ffe0e0;
                color: #d32f2f;
                padding: 10px;
                border-radius: 6px;
                font-weight: bold;
                margin-bottom: 1rem;
                border: 1px solid #d32f2f;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Nuevo Tipo de Talla</h2>

            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/srvTipoTalla" method="post" onsubmit="return validarFormulario();">
                <input type="hidden" name="accion" value="agregar">

                <label for="nombre">Nombre:</label>
                <input type="text" name="nombre" id="nombre"
                       value="${empty tipoTallaFormNombre ? '' : tipoTallaFormNombre}"
                       maxlength="50"
                       required
                       pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
                       title="Solo letras y espacios. Máximo 50 caracteres."
                       oninput="this.value = this.value.toUpperCase();">

                <label for="estado">Estado:</label>
                <select name="estado" id="estado" required>
                    <option value="1" ${empty tipoTallaFormEstado || tipoTallaFormEstado == '1' ? 'selected' : ''}>Activo</option>
                    <option value="0" ${tipoTallaFormEstado == '0' ? 'selected' : ''}>Inactivo</option>
                </select>

                <button type="submit">Registrar Tipo de Talla</button>
            </form>
        </div>

    <script>
    function validarFormulario() {
        const nombreInput = document.getElementById("nombre");
        const nombre = nombreInput.value.trim();

        nombreInput.value = nombre.toUpperCase();

        if (nombre.length === 0) {
            alert("El nombre no puede estar vacío.");
            return false;
        }

        if (nombre.length > 50) {
            alert("El nombre no puede tener más de 50 caracteres.");
            return false;
        }

        const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
        if (!regex.test(nombre)) {
            alert("El nombre solo puede contener letras y espacios (sin números ni símbolos).");
            return false;
        }

        return true;
    }
    </script>
    </body>
</html>
