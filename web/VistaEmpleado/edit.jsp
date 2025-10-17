<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Editar Empleado</title>
<style>
    body {
        font-family: 'Poppins', sans-serif;
        background: linear-gradient(135deg, #e0f7fa, #b3e5fc);
        margin: 0;
        padding: 15px;
        color: #1a237e;
    }

    .form-container {
        background: #ffffff;
        border-radius: 10px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        padding: 1.5rem 2rem;
        max-width: 520px;
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
    input[type="password"],
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

    input:focus,
    select:focus {
        border-color: #1e88e5;
        box-shadow: 0 0 5px rgba(30,136,229,0.3);
    }

    .password-row {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 1rem;
    }

    .toggle-password {
        background: none;
        border: none;
        cursor: pointer;
        font-size: 1.3rem;
        color: #1e88e5;
        transition: transform 0.2s;
    }

    .toggle-password:hover {
        transform: scale(1.15);
    }

    button {
        background-color: #1e88e5;
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
        background-color: #1565c0;
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
        <h2>Editar Empleado</h2>

        <c:if test="${not empty mensajeError}">
            <div class="mensaje-error">${mensajeError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/srvEmpleado" method="post" onsubmit="return validarFormulario();">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="id" value="${empleado.idEmpleado}">

            <label for="nombreEdit">Nombre:</label>
            <input type="text" name="nombre" id="nombreEdit"
                   value="${empleado.nombre}"
                   maxlength="30"
                   required
                   pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
                   title="Solo letras y espacios. Máximo 30 caracteres."
                   oninput="this.value = this.value.toUpperCase();">

            <label for="apellidoEdit">Apellido:</label>
            <input type="text" name="apellido" id="apellidoEdit"
                   value="${empleado.apellido}"
                   maxlength="40"
                   required
                   pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]+"
                   title="Solo letras y espacios. Máximo 40 caracteres."
                   oninput="this.value = this.value.toUpperCase();">

            <label for="usuarioEdit">Usuario:</label>
            <input type="text" name="usuario" id="usuarioEdit"
                   value="${empleado.usuario}"
                   maxlength="20"
                   required
                   pattern="[A-Za-z0-9]+"
                   title="Solo caracteres alfanuméricos. Entre 4 y 20 caracteres.">

            <label for="claveEdit">Contraseña:</label>
            <div class="password-row">
                <input type="password" name="clave" id="claveEdit"
                       value="${empleado.claveVisible}"
                       minlength="4" maxlength="30" required>
                <button type="button" class="toggle-password" 
                        onclick="togglePassword('claveEdit', 'iconoClaveEdit');">
                    <span id="iconoClaveEdit">👁️</span>
                </button>
            </div>

            <label for="idCargoEdit">Cargo:</label>
            <select name="idCargo" id="idCargoEdit" required>
                <option value="">Seleccione un cargo</option>
                <c:forEach var="cargo" items="${listaCargos}">
                    <option value="${cargo.idCargo}"
                            ${empleado.idCargo == cargo.idCargo ? 'selected="selected"' : ''}>${cargo.nombre}</option>
                </c:forEach>
            </select>

            <label for="idTipoDocumentoEdit">Tipo de documento:</label>
            <select name="idTipoDocumento" id="idTipoDocumentoEdit" required>
                <option value="">Seleccione un tipo</option>
                <c:forEach var="tipo" items="${listaTiposDocumento}">
                    <option value="${tipo.idTipoDocumento}"
                            ${empleado.idTipoDocumento == tipo.idTipoDocumento ? 'selected="selected"' : ''}>${tipo.nombre}</option>
                </c:forEach>
            </select>

            <label for="numeroDocumentoEdit">Número de documento:</label>
            <input type="text" name="numeroDocumento" id="numeroDocumentoEdit"
                   value="${empleado.numeroDocumento}"
                   pattern="[0-9]{8,15}"
                   maxlength="15"
                   required
                   title="Solo números. Entre 8 y 15 dígitos.">

            <label for="telefonoEdit">Teléfono:</label>
            <input type="text" name="telefono" id="telefonoEdit"
                   value="${empleado.telefono}"
                   pattern="[0-9]{6,15}"
                   maxlength="15"
                   required
                   title="Solo números. Entre 6 y 15 dígitos.">

            <label for="estadoEdit">Estado:</label>
            <select name="estado" id="estadoEdit" required>
                <option value="1" ${empleado.estado == 1 ? 'selected="selected"' : ''}>Activo</option>
                <option value="0" ${empleado.estado == 0 ? 'selected="selected"' : ''}>Inactivo</option>
            </select>

            <button type="submit">Guardar cambios</button>
        </form>
    </div>

<script>
function validarFormulario() {
    const nombre = document.getElementById('nombreEdit').value.trim();
    const apellido = document.getElementById('apellidoEdit').value.trim();
    const usuario = document.getElementById('usuarioEdit').value.trim();
    const clave = document.getElementById('claveEdit').value.trim();
    const numeroDocumento = document.getElementById('numeroDocumentoEdit').value.trim();
    const telefono = document.getElementById('telefonoEdit').value.trim();

    if (nombre.length === 0 || apellido.length === 0) {
        alert('El nombre y apellido son obligatorios.');
        return false;
    }

    if (usuario.length < 4 || usuario.length > 20) {
        alert('El usuario debe tener entre 4 y 20 caracteres.');
        return false;
    }

    if (clave.length < 4 || clave.length > 30) {
        alert('La contraseña debe tener entre 4 y 30 caracteres.');
        return false;
    }

    if (!/^\d{8,15}$/.test(numeroDocumento)) {
        alert('El número de documento debe contener entre 8 y 15 dígitos.');
        return false;
    }

    if (!/^\d{6,15}$/.test(telefono)) {
        alert('El teléfono debe contener entre 6 y 15 dígitos.');
        return false;
    }

    return true;
}

function togglePassword(fieldId, iconId) {
    const field = document.getElementById(fieldId);
    const icon = document.getElementById(iconId);
    if (field.type === 'password') {
        field.type = 'text';
        icon.textContent = '🙈';
    } else {
        field.type = 'password';
        icon.textContent = '👁️';
    }
}
</script>
</body>
</html>
