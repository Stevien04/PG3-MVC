<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="productoEditar" value="${producto}" />

<c:if test="${empty productoEditar}">
    <div class="form-card">
        <h2>Editar producto</h2>
        <div class="alerta-error">No se encontró el producto solicitado.</div>
    </div>
</c:if>

<c:if test="${not empty productoEditar}">
    <div class="form-card">
        <h2>Editar producto</h2>
        <p class="nota-campo">
            El nombre se genera automáticamente según la categoría, marca y modelo seleccionados.
        </p>

        <c:if test="${not empty mensajeErrorProducto}">
            <div class="alerta-error">
                <c:out value="${mensajeErrorProducto}" />
            </div>
        </c:if>

        <form action="srvProducto" method="post" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="actualizar" />
            <input type="hidden" name="vista" value="${mostrarActivos ? 'activos' : 'inactivos'}" />
            <input type="hidden" name="id" value="${productoEditar.idProducto}" />

            <!-- Campo nombre automático -->
            <div class="form-group">
                <label for="nombreEditar">Nombre del producto</label>
                <input type="text" id="nombreEditar" name="nombre"
                       value="${productoEditar.nombre}"
                       readonly style="background-color:#f0f0f0;cursor:not-allowed;" />
                <span class="nota-campo">
                    Este campo se actualizará automáticamente al cambiar la categoría, marca o modelo.
                </span>
            </div>

            <div class="form-group">
                <label for="categoriaEditar">Categoría</label>
                <select id="categoriaEditar" name="idCategoria" required onchange="actualizarNombreEditar()">
                    <option value="">Seleccione una categoría</option>
                    <c:forEach var="categoria" items="${categorias}">
                        <option value="${categoria.idCategoria}"
                                data-nombre="${categoria.nombre}"
                                <c:if test="${productoEditar.idCategoria == categoria.idCategoria}">selected</c:if>>
                            <c:out value="${categoria.nombre}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="marcaEditar">Marca</label>
                <select id="marcaEditar" name="idMarca" required onchange="actualizarNombreEditar()">
                    <option value="">Seleccione una marca</option>
                    <c:forEach var="marca" items="${marcas}">
                        <option value="${marca.idMarca}"
                                data-nombre="${marca.nombre}"
                                <c:if test="${productoEditar.idMarca == marca.idMarca}">selected</c:if>>
                            <c:out value="${marca.nombre}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="modeloEditar">Modelo <span class="nota-campo">(opcional)</span></label>
                <select id="modeloEditar" name="idModelo" onchange="actualizarNombreEditar()">
                    <option value="">Sin modelo</option>
                    <c:forEach var="modelo" items="${modelos}">
                        <option value="${modelo.idModelo}"
                                data-nombre="${modelo.nombre}"
                                <c:if test="${productoEditar.idModelo != null && productoEditar.idModelo == modelo.idModelo}">selected</c:if>>
                            <c:out value="${modelo.nombre}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="colorEditar">Color <span class="nota-campo">(opcional)</span></label>
                <select id="colorEditar" name="idColor">
                    <option value="">Sin color</option>
                    <c:forEach var="color" items="${colores}">
                        <option value="${color.idColor}"
                                <c:if test="${productoEditar.idColor != null && productoEditar.idColor == color.idColor}">selected</c:if>>
                            <c:out value="${color.nombre}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="cantidadEditar">Cantidad</label>
                <input type="number" id="cantidadEditar" name="cantidad" min="0" step="1"
                       value="${productoEditar.cantidad}" required />
            </div>

            <div class="form-group">
                <label for="precioEditar">Precio unitario</label>
                <input type="number" id="precioEditar" name="precioUnitario" min="0" step="0.01"
                       value="${productoEditar.precioUnitario}" required />
            </div>

            <div class="form-group">
                <label for="estadoEditar">Estado</label>
                <select id="estadoEditar" name="estado" required>
                    <option value="1" <c:if test="${productoEditar.estado == 1}">selected</c:if>>Activo</option>
                    <option value="0" <c:if test="${productoEditar.estado == 0}">selected</c:if>>Inactivo</option>
                </select>
            </div>

            <div class="form-group">
                <label for="fotoEditar">Fotografía <span class="nota-campo">(opcional)</span></label>
                <input type="file" id="fotoEditar" name="foto" accept="image/*" />
                <c:if test="${not empty productoEditar.fotoBase64}">
                    <div class="preview-foto">
                        <img src="data:image/*;base64,${productoEditar.fotoBase64}" alt="Foto actual" />
                        <span class="nota-campo">Si no seleccionas una nueva imagen se conservará la actual.</span>
                    </div>
                </c:if>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-submit">Actualizar producto</button>
            </div>
        </form>
    </div>
</c:if>

<!-- Script para regenerar nombre dinámicamente -->
<script>
function actualizarNombreEditar() {
    const categoria = document.querySelector("#categoriaEditar option:checked").dataset.nombre || "";
    const marca = document.querySelector("#marcaEditar option:checked").dataset.nombre || "";
    const modelo = document.querySelector("#modeloEditar option:checked").dataset.nombre || "";
    const nombreGenerado = (categoria + " " + marca + " " + modelo).trim().toUpperCase();
    document.getElementById("nombreEditar").value = nombreGenerado;
}
</script>
