<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="nombreFormulario" value="${not empty colorFormNombre ? colorFormNombre : ''}" />
<c:set var="estadoFormulario" value="${not empty colorFormEstado ? colorFormEstado : 1}" />

<div class="form-container">
    <h2>Registrar Color</h2>

     <c:if test="${not empty mensajeError}">
        <div class="mensaje-error">${mensajeError}</div>
    </c:if>
        <form action="${pageContext.request.contextPath}/srvColor" method="post" onsubmit="return validarFormulario();">
        <input type="hidden" name="accion" value="registrar">

        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" id="nombre"
               value="${nombreFormulario}"
               maxlength="30"
               required
               pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
               title="Solo letras y espacios. Máximo 30 caracteres."
               oninput="this.value = this.value.toUpperCase();">

        <label for="estado">Estado:</label>
        <select name="estado" id="estado" required>
            <option value="1" <c:if test="${estadoFormulario == 1}">selected</c:if>>Activo</option>
            <option value="0" <c:if test="${estadoFormulario == 0}">selected</c:if>>Inactivo</option>
        </select>

        <button type="submit">Guardar Color</button>
    </form>
</div>

<style>
     .form-container {
        background: #ffffff;
        border-radius: 10px;
         box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        padding: 1.5rem 2rem;
        max-width: 420px;
        margin: 0 auto;
        box-sizing: border-box;
        font-family: 'Poppins', sans-serif;
        color: #1b5e20;
    }
     h2 {
        color: #1b5e20;
        margin-bottom: 1rem;
        font-weight: 600;
         text-align: center;
    }
     label {
        display: block;
        text-align: left;
        margin-bottom: 0.5rem;
        color: #1b5e20;
        font-weight: 500;
    }

    input[type="text"],
    select {
        width: 100%;
        padding: 9px;
        border: 1.5px solid #a5d6a7;
        border-radius: 6px;
        font-size: 15px;
        outline: none;
        transition: 0.2s;
        margin-bottom: 1rem;
        box-sizing: border-box;
    }

    input[type="text"]:focus,
    select:focus {
        border-color: #43a047;
        box-shadow: 0 0 5px rgba(67,160,71,0.3);
    }

    button {
        background-color: #43a047;
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
        background-color: #2e7d32;
        transform: scale(1.02);
    }

    .mensaje-error {
        background: #ffebee;
        color: #c62828;
        padding: 10px;
        border-radius: 6px;
        font-weight: bold;
        margin-bottom: 1rem;
        border: 1px solid #c62828;
        text-align: center;
    }
</style>

<script>
    function validarFormulario() {
        const nombreInput = document.getElementById("nombre");
        const nombre = nombreInput.value.trim();

        nombreInput.value = nombre.toUpperCase();

        if (nombre.length === 0) {
            alert("El nombre no puede estar vacío.");
            return false;
        }

        if (nombre.length > 30) {
            alert("El nombre no puede tener más de 30 caracteres.");
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