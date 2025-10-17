<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="formProducto" value="${productoForm}" />

<h2>Registrar nuevo producto</h2>

<c:if test="${not empty mensajeError}">
    <p class="mensaje-error">${mensajeError}</p>
</c:if>

<form action="srvProducto" method="post" class="form">
    <input type="hidden" name="accion" value="agregar" />

    <div class="campo">
        <label for="nombre">Nombre</label>
        <input type="text" id="nombre" name="nombre"
               value="${not empty formProducto ? formProducto.nombre : ''}" required>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="idCategoria">Categoría</label>
            <select id="idCategoria" name="idCategoria" required>
                <option value="">Seleccione una categoría</option>
                <c:forEach var="categoria" items="${listaCategorias}">
                    <option value="${categoria.idCategoria}"
                        ${not empty formProducto && formProducto.idCategoria == categoria.idCategoria ? 'selected' : ''}>
                        ${categoria.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="campo">
            <label for="idModelo">Modelo</label>
            <select id="idModelo" name="idModelo">
                <option value="" ${empty formProducto.idModelo ? 'selected' : ''}>Sin modelo</option>
                <c:forEach var="modelo" items="${listaModelos}">
                    <option value="${modelo.idModelo}"
                        ${not empty formProducto.idModelo && formProducto.idModelo == modelo.idModelo ? 'selected' : ''}>
                        ${modelo.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="idMarca">Marca</label>
            <select id="idMarca" name="idMarca" required>
                <option value="">Seleccione una marca</option>
                <c:forEach var="marca" items="${listaMarcas}">
                    <option value="${marca.idMarca}"
                        ${not empty formProducto && formProducto.idMarca == marca.idMarca ? 'selected' : ''}>
                        ${marca.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="campo-grid">
        <div class="campo">
            <label for="cantidad">Cantidad</label>
            <input type="number" id="cantidad" name="cantidad" min="0"
                   value="${not empty formProducto ? formProducto.cantidad : ''}" required>
        </div>

        <div class="campo">
            <label for="precioUnitario">Precio Unitario</label>
            <input type="number" step="0.01" id="precioUnitario" name="precioUnitario" min="0"
                   value="${not empty formProducto ? formProducto.precioUnitario : ''}" required>
        </div>
    </div>

    <div class="campo">
        <label for="estado">Estado</label>
        <select id="estado" name="estado">
            <option value="1" ${empty formProducto || formProducto.estado == 1 ? 'selected' : ''}>Activo</option>
            <option value="0" ${not empty formProducto && formProducto.estado == 0 ? 'selected' : ''}>Inactivo</option>
        </select>
    </div>

    <button type="submit">Registrar producto</button>
</form>
