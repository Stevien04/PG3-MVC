<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="bloque-lista">
    <header class="bloque-encabezado">
        <h2>Productos registrados</h2>
        <form action="srvProducto" method="get" class="buscador">
            <input type="text" name="texto" placeholder="Buscar por nombre o ID" value="${param.texto}">
            <button type="submit">Buscar</button>
        </form>
    </header>

    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Stock</th>
                    <th>Precio</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaProductos}">
                        <c:forEach var="producto" items="${listaProductos}">
                            <tr>
                                <td>${producto.idProducto}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.cantidad}</td>
                                <td>S/ ${producto.precioUnitario}</td>
                                <td>
                                    <span class="estado ${producto.estado == 1 ? 'activo' : 'inactivo'}">
                                        ${producto.estado == 1 ? 'Activo' : 'Inactivo'}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="mensaje-vacio">No hay productos registrados.</td>
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
                    <c:when test="${not empty listaProductoTallas}">
                        <c:forEach var="relacion" items="${listaProductoTallas}">
                            <tr>
                                <td>${relacion.idProductoTalla}</td>
                                <td>${relacion.idProducto}</td>
                                <td>${relacion.idTalla}</td>
                                <td>${relacion.cantidad}</td>
                                <td>
                                    <span class="estado ${relacion.estado == null ? 'sin-estado' : (relacion.estado == 1 ? 'activo' : 'inactivo')}">
                                        <c:choose>
                                            <c:when test="${relacion.estado == 1}">Activo</c:when>
                                            <c:when test="${relacion.estado == 0}">Inactivo</c:when>
                                            <c:otherwise>No definido</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="mensaje-vacio">No hay relaciones producto-talla registradas.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</section>