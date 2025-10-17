<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Asignar tallas a un producto</h2>
<c:if test="${not empty mensajeErrorProductoTalla}">
    <p class="mensaje-error">${mensajeErrorProductoTalla}</p>
</c:if>
<c:if test="${not empty mensajeExitoProductoTalla}">
    <p class="mensaje-exito">${mensajeExitoProductoTalla}</p>
</c:if>
<form action="srvProducto" method="post" class="form">
    <input type="hidden" name="accion" value="registrarProductoTalla" />

    <div class="campo">
        <label for="idProducto">ID Producto</label>
        <input type="number" name="idProducto" id="idProducto" min="1" required>
    </div>

    <div class="campo">
        <label for="idTalla">ID Talla</label>
        <input type="number" name="idTalla" id="idTalla" min="1" required>
    </div>

    <div class="campo">
        <label for="cantidadTalla">Cantidad</label>
        <input type="number" name="cantidad" id="cantidadTalla" min="0" required>
    </div>

    <div class="campo">
        <label for="estadoTalla">Estado</label>
        <select name="estado" id="estadoTalla">
            <option value="1">Activo</option>
            <option value="0">Inactivo</option>
        </select>
    </div>

    <button type="submit">Guardar relación</button>
</form>