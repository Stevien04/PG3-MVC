<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="productoActual" value="${productoForm}" />
<c:set var="estadoSeleccionado" value="${productoActual ne null ? productoActual.estado : 1}" />

<!-- Barra superior -->
<div class="barra-superior">
    <a class="btn-menu" href="<c:url value='/VistaMenu/MenuMain.jsp'/>">Volver al Menú</a>
</div>

<div class="form-card">
    <h2>Registrar producto</h2>
    <p class="nota-campo">Completa la información para añadir un nuevo producto al catálogo.</p>

    <c:if test="${not empty mensajeErrorProducto and empty producto}">
        <div class="alerta-error">
            <c:out value="${mensajeErrorProducto}" />
        </div>
    </c:if>

    <form action="srvProducto" method="post" enctype="multipart/form-data">
        <input type="hidden" name="accion" value="agregar" />
        <input type="hidden" name="vista" value="${mostrarActivos ? 'activos' : 'inactivos'}" />

        <div class="form-group">
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" maxlength="60" placeholder="Nombre del producto"
                   value="${productoActual != null ? productoActual.nombre : ''}" required />
        </div>

        <div class="form-group">
            <label for="idCategoria">Categoría</label>
            <select id="idCategoria" name="idCategoria" required>
                <option value="">Seleccione una categoría</option>
                <c:forEach var="categoria" items="${categorias}">
                    <option value="${categoria.idCategoria}"
                            <c:if test="${productoActual != null && productoActual.idCategoria == categoria.idCategoria}">selected</c:if>>
                        <c:out value="${categoria.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idMarca">Marca</label>
            <select id="idMarca" name="idMarca" required>
                <option value="">Seleccione una marca</option>
                <c:forEach var="marca" items="${marcas}">
                    <option value="${marca.idMarca}"
                            <c:if test="${productoActual != null && productoActual.idMarca == marca.idMarca}">selected</c:if>>
                        <c:out value="${marca.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idModelo">Modelo <span class="nota-campo">(opcional)</span></label>
            <select id="idModelo" name="idModelo">
                <option value="">Sin modelo</option>
                <c:forEach var="modelo" items="${modelos}">
                    <option value="${modelo.idModelo}"
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

<style>
    /* Estilos del botón y barra superior */
    .barra-superior {
        background: linear-gradient(90deg, #14213d, #4361ee);
        padding: 14px 28px;
        display: flex;
        justify-content: flex-end;
        align-items: center;
        border-radius: 10px;
        box-shadow: 0 4px 18px rgba(20, 33, 61, 0.25);
        margin-bottom: 20px;
    }

    .btn-menu {
        background: #ffffff;
        color: #14213d;
        padding: 10px 22px;
        border-radius: 999px;
        text-decoration: none;
        font-weight: 600;
        transition: transform 0.2s ease, box-shadow 0.2s ease, color 0.2s ease;
        box-shadow: 0 10px 20px rgba(20, 33, 61, 0.25);
    }

    .btn-menu:hover {
        color: #0a1128;
        transform: translateY(-2px);
        box-shadow: 0 14px 24px rgba(20, 33, 61, 0.35);
    }
</style>
