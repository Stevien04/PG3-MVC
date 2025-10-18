<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="productoActual" value="${productoForm}" />
<c:set var="estadoSeleccionado" value="${productoActual ne null ? productoActual.estado : 1}" />

<div class="form-card">
    <h2>Registrar producto</h2>
    <p class="nota-campo">El nombre se genera automáticamente según la categoría, marca y modelo seleccionados.</p>

    <c:if test="${not empty mensajeErrorProducto and empty producto}">
        <div class="alerta-error">
            <c:out value="${mensajeErrorProducto}" />
        </div>
    </c:if>

    <form action="srvProducto" method="post" enctype="multipart/form-data">
        <input type="hidden" name="accion" value="agregar" />
        <input type="hidden" name="vista" value="${mostrarActivos ? 'activos' : 'inactivos'}" />

        <!-- Nombre generado automáticamente -->
        <div class="form-group">
            <label for="nombre">Nombre del producto</label>
            <input type="text" id="nombre" name="nombre"
                   value="${productoActual != null ? productoActual.nombre : ''}"
                   readonly style="background-color: #f0f0f0; cursor: not-allowed;" />
            <span class="nota-campo">Este campo se llenará automáticamente al seleccionar categoría, marca y modelo.</span>
        </div>

        <div class="form-group">
            <label for="idCategoria">Categoría</label>
            <select id="idCategoria" name="idCategoria" required onchange="generarNombre()">
                <option value="">Seleccione una categoría</option>
                <c:forEach var="categoria" items="${categorias}">
                    <option value="${categoria.idCategoria}"
                            data-nombre="${categoria.nombre}"
                            <c:if test="${productoActual != null && productoActual.idCategoria == categoria.idCategoria}">selected</c:if>>
                        <c:out value="${categoria.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idMarca">Marca</label>
            <select id="idMarca" name="idMarca" required onchange="generarNombre()">
                <option value="">Seleccione una marca</option>
                <c:forEach var="marca" items="${marcas}">
                    <option value="${marca.idMarca}"
                            data-nombre="${marca.nombre}"
                            <c:if test="${productoActual != null && productoActual.idMarca == marca.idMarca}">selected</c:if>>
                        <c:out value="${marca.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idModelo">Modelo <span class="nota-campo">(opcional)</span></label>
            <select id="idModelo" name="idModelo" onchange="generarNombre()">
                <option value="">Sin modelo</option>
                <c:forEach var="modelo" items="${modelos}">
                    <option value="${modelo.idModelo}"
                            data-nombre="${modelo.nombre}"
                            <c:if test="${productoActual != null && productoActual.idModelo != null && productoActual.idModelo == modelo.idModelo}">selected</c:if>>
                        <c:out value="${modelo.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idColor">Color <span class="nota-campo">(opcional)</span></label>
            <select id="idColor" name="idColor">
                <option value="">Sin color</option>
                <c:forEach var="color" items="${colores}">
                    <option value="${color.idColor}"
                            <c:if test="${productoActual != null && productoActual.idColor != null && productoActual.idColor == color.idColor}">selected</c:if>>
                        <c:out value="${color.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="cantidad">Cantidad</label>
            <input type="number" id="cantidad" name="cantidad" min="0" step="1" placeholder="0"
                   value="${productoActual != null ? productoActual.cantidad : ''}" required />
        </div>

        <div class="form-group">
            <label for="precioUnitario">Precio unitario</label>
            <input type="number" id="precioUnitario" name="precioUnitario" min="0" step="0.01" placeholder="0.00"
                   value="${productoActual != null ? productoActual.precioUnitario : ''}" required />
        </div>

        <div class="form-group">
            <label for="estado">Estado</label>
            <select id="estado" name="estado" required>
                <option value="1" <c:if test="${estadoSeleccionado == 1}">selected</c:if>>Activo</option>
                <option value="0" <c:if test="${estadoSeleccionado == 0}">selected</c:if>>Inactivo</option>
            </select>
        </div>

        <div class="form-group">
            <label for="foto">Fotografía <span class="nota-campo">(opcional, hasta 5 MB)</span></label>
            <input type="file" id="foto" name="foto" accept="image/*" />
        </div>

        <div class="form-acciones">
            <button type="submit" class="btn-submit">Guardar producto</button>
            <button type="reset" class="btn-reset">Limpiar</button>
        </div>
    </form>
</div>

<script>
function generarNombre() {
    const categoria = document.querySelector("#idCategoria option:checked").dataset.nombre || "";
    const marca = document.querySelector("#idMarca option:checked").dataset.nombre || "";
    const modelo = document.querySelector("#idModelo option:checked").dataset.nombre || "";
    const nombre = (categoria + " " + marca + " " + modelo).trim().toUpperCase();
    document.getElementById("nombre").value = nombre;
}
</script>
