<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Registrar nuevo producto</h2>
<c:if test="${not empty mensajeError}">
    <p class="mensaje-error">${mensajeError}</p>
</c:if>
<c:if test="${not empty mensajeExito}">
    <p class="mensaje-exito">${mensajeExito}</p>
</c:if>
<form action="srvProducto" method="post" class="form">
    <input type="hidden" name="accion" value="agregar" />

    <div class="campo">
        <label for="nombre">Nombre</label>
        <input type="text" id="nombre" name="nombre" value="${param.nombre}" required>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="idCategoria">ID Categoría</label>
            <input type="number" id="idCategoria" name="idCategoria" min="1" value="${param.idCategoria}" required>
        </div>
        <div class="campo">
            <label for="idModelo">ID Modelo</label>
            <input type="number" id="idModelo" name="idModelo" min="1" value="${param.idModelo}">
        </div>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="idColor">ID Color</label>
            <input type="number" id="idColor" name="idColor" min="1" value="${param.idColor}">
        </div>
        <div class="campo">
            <label for="idMarca">ID Marca</label>
            <input type="number" id="idMarca" name="idMarca" min="1" value="${param.idMarca}" required>
        </div>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="cantidad">Cantidad</label>
            <input type="number" id="cantidad" name="cantidad" min="0" value="${param.cantidad}" required>
        </div>
        <div class="campo">
            <label for="precioUnitario">Precio Unitario</label>
            <input type="number" step="0.01" id="precioUnitario" name="precioUnitario" min="0" value="${param.precioUnitario}" required>
        </div>
    </div>

    <div class="campo">
        <label for="estado">Estado</label>
        <select id="estado" name="estado">
            <option value="1" ${param.estado == '1' ? 'selected' : ''}>Activo</option>
            <option value="0" ${param.estado == '0' ? 'selected' : ''}>Inactivo</option>
        </select>
    </div>

    <button type="submit">Registrar producto</button>
</form>