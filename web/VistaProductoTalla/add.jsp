<c:set var="form" value="${empty productoTallaForm ? null : productoTallaForm}" />

<c:if test="${not empty mensajeExitoProductoTalla}">
    <div class="alerta exito">${mensajeExitoProductoTalla}</div>
</c:if>
<c:if test="${not empty mensajeErrorProductoTalla}">
    <div class="alerta error">${mensajeErrorProductoTalla}</div>
</c:if>

<form action="${pageContext.request.contextPath}/srvProductoTalla" method="post">
    <input type="hidden" name="accion" value="agregar" />

    <label for="idProducto">Producto</label>
    <select name="idProducto" id="idProducto" required>
        <option value="">Seleccione un producto</option>
        <c:forEach var="producto" items="${listaProductos}">
            <option value="${producto.idProducto}"
                    ${form != null && form.idProducto == producto.idProducto ? 'selected' : ''}>
                ${producto.nombre}
            </option>
        </c:forEach>
    </select>

    <label for="idTalla">Talla</label>
    <select name="idTalla" id="idTalla" required>
        <option value="">Seleccione una talla</option>
        <c:forEach var="talla" items="${listaTallas}">
            <option value="${talla.idTalla}"
                    ${form != null && form.idTalla == talla.idTalla ? 'selected' : ''}>
                ${talla.valor}
            </option>
        </c:forEach>
    </select>

    <label for="cantidad">Cantidad</label>
    <input type="number" id="cantidad" name="cantidad" min="0" required
           value="${form != null ? form.cantidad : ''}" />

    <label for="estado">Estado</label>
    <select name="estado" id="estado">
        <option value="1" ${form == null || form.estado == null || form.estado == 1 ? 'selected' : ''}>Activo</option>
        <option value="0" ${form != null && form.estado == 0 ? 'selected' : ''}>Inactivo</option>
    </select>

    <button type="submit">Registrar combinación</button>
</form>