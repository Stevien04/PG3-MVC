<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div>
    <h1 class="titulo-seccion">Listado de Productos</h1>
    <div class="buttons">
        <a class="btn ${mostrarActivos ? 'activo' : 'secundario'}" href="srvProducto?accion=listarActivos">Activos</a>
        <a class="btn ${!mostrarActivos ? 'activo' : 'secundario'}" href="srvProducto?accion=listarInactivos">Inactivos</a>
    </div>
    <form class="search-container" method="get" action="srvProducto">
        <input type="hidden" name="accion" value="buscar" />
        <input type="text" name="texto" class="search-box" placeholder="Buscar por código, nombre, marca o categoría" value="${fn:escapeXml(textoBusqueda)}" />
        <button type="submit" class="search-btn">Buscar</button>
    </form>
    <div class="tabla-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Cantidad</th>
                    <th>Precio</th>
                    <th>Estado</th>
                    <th>Foto</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty listaProductos}">
                        <tr>
                            <td colspan="11" class="texto-vacio">No hay productos para mostrar.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="producto" items="${listaProductos}">
                            <tr>
                                <td><c:out value="${producto.idProducto}" /></td>
                                <td><c:out value="${producto.nombre}" /></td>
                                <td><c:out value="${producto.nombreCategoria}" /></td>
                                <td><c:out value="${producto.nombreMarca}" /></td>
                                <td><c:out value="${producto.nombreModelo}" /></td>
                                <td><c:out value="${producto.nombreColor}" /></td>
                                <td><c:out value="${producto.cantidad}" /></td>
                                <td>S/ <c:out value="${producto.precioUnitario}" /></td>
                                <td>
                                    <span class="estado ${producto.estado == 1 ? 'activo' : 'inactivo'}">
                                        <c:choose>
                                            <c:when test="${producto.estado == 1}">Activo</c:when>
                                            <c:otherwise>Inactivo</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty producto.fotoBase64}">
                                            <img class="foto-miniatura" src="data:image/*;base64,${producto.fotoBase64}" alt="Foto" />
                                        </c:when>
                                        <c:otherwise>
                                            <span class="nota-campo">Sin foto</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="acciones">
                                        <a class="btn-tabla btn-editar" href="srvProducto?accion=editar&id=${producto.idProducto}&vista=${mostrarActivos ? 'activos' : 'inactivos'}">Editar</a>
                                        <a class="btn-tabla btn-estado ${mostrarActivos ? '' : 'activar'}" href="srvProducto?accion=cambiarEstado&id=${producto.idProducto}&vista=${mostrarActivos ? 'activos' : 'inactivos'}" onclick="return confirm('¿Desea ${mostrarActivos ? 'desactivar' : 'activar'} este producto?');">
                                            ${mostrarActivos ? 'Desactivar' : 'Activar'}
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>