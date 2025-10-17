<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Actualizar producto</h2>
<c:if test="${not empty mensajeError}">
    <p class="mensaje-error">${mensajeError}</p>
</c:if>
<c:if test="${not empty mensajeExito}">
    <p class="mensaje-exito">${mensajeExito}</p>
</c:if>
<c:choose>
    <c:when test="${not empty producto}">
        <form action="srvProducto" method="post" class="form">
            <input type="hidden" name="accion" value="actualizar" />
            <input type="hidden" name="idProducto" value="${producto.idProducto}" />

            <div class="campo">
                <label for="nombreEditar">Nombre</label>
                <input type="text" id="nombreEditar" name="nombre" value="${producto.nombre}" required>
            </div>

            <div class="campo-grid">
                <div class="campo">
                    <label for="idCategoriaEditar">ID Categoría</label>
                    <input type="number" id="idCategoriaEditar" name="idCategoria" min="1" value="${producto.idCategoria}" required>
                </div>
                <div class="campo">
                    <label for="idModeloEditar">ID Modelo</label>
                    <input type="number" id="idModeloEditar" name="idModelo" min="1" value="${producto.idModelo}">
                </div>
            </div>

            <div class="campo-grid">
                <div class="campo">
                    <label for="idColorEditar">ID Color</label>
                    <input type="number" id="idColorEditar" name="idColor" min="1" value="${producto.idColor}">
                </div>
                <div class="campo">
                    <label for="idMarcaEditar">ID Marca</label>
                    <input type="number" id="idMarcaEditar" name="idMarca" min="1" value="${producto.idMarca}" required>
                </div>
            </div>

            <div class="campo-grid">
                <div class="campo">
                    <label for="cantidadEditar">Cantidad</label>
                    <input type="number" id="cantidadEditar" name="cantidad" min="0" value="${producto.cantidad}" required>
                </div>
                <div class="campo">
                    <label for="precioUnitarioEditar">Precio Unitario</label>
                    <input type="number" step="0.01" id="precioUnitarioEditar" name="precioUnitario" min="0" value="${producto.precioUnitario}" required>
                </div>
            </div>

            <div class="campo">
                <label for="estadoEditar">Estado</label>
                <select id="estadoEditar" name="estado">
                    <option value="1" ${producto.estado == 1 ? 'selected' : ''}>Activo</option>
                    <option value="0" ${producto.estado == 0 ? 'selected' : ''}>Inactivo</option>
                </select>
            </div>

            <button type="submit">Actualizar producto</button>
        </form>
    </c:when>
    <c:otherwise>
        <p class="mensaje-info">Seleccione un producto de la lista para editarlo.</p>
    </c:otherwise>
</c:choose>