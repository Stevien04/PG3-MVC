<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<section class="bloque-lista">
    <header class="bloque-encabezado">
        <h2>Productos registrados</h2>
        <form action="srvProducto" method="get" class="buscador">
            <input type="text" name="texto" placeholder="Buscar por nombre o ID" value="${textoBusqueda}">
            <input type="hidden" name="accion" value="buscar">
            <button type="submit">Buscar</button>
        </form>
    </header>

    <!-- Resultados de búsqueda -->
    <c:if test="${not empty textoBusqueda}">
        <h3>Resultados de búsqueda</h3>
        <div class="tabla-contenedor">
            <table class="tabla">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Color</th>
                        <th>Stock</th>
                        <th>Precio</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty listaBusquedaProductos}">
                            <c:forEach var="producto" items="${listaBusquedaProductos}">
                                <tr>
                                    <td>${producto.idProducto}</td>
                                    <td>${producto.nombre}</td>
                                    <td>${producto.nombreCategoria}</td>
                                    <td>${producto.nombreMarca}</td>
                                    <td>${empty producto.nombreModelo ? '-' : producto.nombreModelo}</td>
                                    <td>${empty producto.nombreColor ? '-' : producto.nombreColor}</td>
                                    <td>${producto.cantidad}</td>
                                    <td>S/ ${producto.precioUnitario}</td>
                                    <td>
                                        <span class="estado ${producto.estado == 1 ? 'activo' : 'inactivo'}">
                                            ${producto.estado == 1 ? 'Activo' : 'Inactivo'}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="srvProducto?accion=editar&id=${producto.idProducto}">Editar</a> |
                                        <a href="srvProducto?accion=cambiarEstado&id=${producto.idProducto}">
                                            ${producto.estado == 1 ? 'Desactivar' : 'Activar'}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="10" class="mensaje-vacio">
                                    No se encontraron productos que coincidan con "${textoBusqueda}".
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </c:if>

    <!-- Productos activos -->
    <h3>Productos activos</h3>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Stock</th>
                    <th>Precio</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaProductosActivos}">
                        <c:forEach var="producto" items="${listaProductosActivos}">
                            <tr>
                                <td>${producto.idProducto}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.nombreCategoria}</td>
                                <td>${producto.nombreMarca}</td>
                                <td>${empty producto.nombreModelo ? '-' : producto.nombreModelo}</td>
                                <td>${empty producto.nombreColor ? '-' : producto.nombreColor}</td>
                                <td>${producto.cantidad}</td>
                                <td>S/ ${producto.precioUnitario}</td>
                                <td><span class="estado activo">Activo</span></td>
                                <td>
                                    <a href="srvProducto?accion=editar&id=${producto.idProducto}">Editar</a> |
                                    <a href="srvProducto?accion=cambiarEstado&id=${producto.idProducto}">Desactivar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="mensaje-vacio">No hay productos activos registrados.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Productos inactivos -->
    <h3>Productos inactivos</h3>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Stock</th>
                    <th>Precio</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaProductosInactivos}">
                        <c:forEach var="producto" items="${listaProductosInactivos}">
                            <tr>
                                <td>${producto.idProducto}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.nombreCategoria}</td>
                                <td>${producto.nombreMarca}</td>
                                <td>${empty producto.nombreModelo ? '-' : producto.nombreModelo}</td>
                                <td>${empty producto.nombreColor ? '-' : producto.nombreColor}</td>
                                <td>${producto.cantidad}</td>
                                <td>S/ ${producto.precioUnitario}</td>
                                <td><span class="estado inactivo">Inactivo</span></td>
                                <td>
                                    <a href="srvProducto?accion=editar&id=${producto.idProducto}">Editar</a> |
                                    <a href="srvProducto?accion=cambiarEstado&id=${producto.idProducto}">Activar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="mensaje-vacio">No hay productos inactivos registrados.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</section>

<section class="bloque-lista">
    <header class="bloque-encabezado">
        <h2>Relaciones Producto - Talla</h2>
    </header>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Producto</th>
                    <th>Talla</th>
                    <th>Stock</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <!-- Aquí irán tus tallas -->
                </c:choose>
            </tbody>
        </table>
    </div>
</section>
