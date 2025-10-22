<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Catálogo de productos</title>
    <style>
        :root {
            color-scheme: light;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: 'Poppins', Arial, sans-serif;
            background: linear-gradient(120deg, #f6f9ff 0%, #eef3ff 40%, #ffffff 100%);
            color: #1b263b;
            min-height: 100vh;
        }

        .layout {
            max-width: 1200px;
            margin: 0 auto;
            padding: 32px 20px 48px;
        }
        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 18px;
            color: #1b263b;
        }

        .topbar-links {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
        }

        .topbar-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 6px 14px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.85);
            border: 1px solid #cbd5f5;
            color: #1b263b;
            font-weight: 600;
            text-decoration: none;
            transition: background 0.2s ease, color 0.2s ease, box-shadow 0.2s ease;
        }

        .topbar-link:hover {
            background: #4361ee;
            color: #ffffff;
            box-shadow: 0 12px 24px rgba(67, 97, 238, 0.18);
        }

        .topbar-user {
            font-weight: 600;
            color: rgba(27, 38, 59, 0.85);
        }

        .topbar-user strong {
            color: #0d1b2a;
        }
        .header-top {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            flex-wrap: wrap;
        }

        header {
            
            margin-bottom: 36px;
        }
        
        .header-titles {
            flex: 1 1 320px;
        }

        .header-titles h1,
        .header-titles p {
            text-align: left;
        }

        header h1 {
            margin: 0 0 12px;
            font-size: 42px;
            font-weight: 700;
            letter-spacing: 0.5px;
            color: #0d1b2a;
        }

        header p {
            margin: 0;
            font-size: 17px;
            color: rgba(27, 38, 59, 0.75);
            max-width: 680px;
            margin-inline: auto;
        }
        
         .btn.carrito {
            background: linear-gradient(135deg, #2ec4b6, #1b998b);
            padding-inline: 24px;
        }

        .btn.carrito:hover {
            box-shadow: 0 16px 32px rgba(27, 153, 139, 0.25);
        }

        .badge-cantidad {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 32px;
            padding: 4px 10px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.18);
            color: inherit;
            font-size: 14px;
            font-weight: 600;
        }

        .alert {
            margin: 0 0 24px;
            padding: 16px 20px;
            border-radius: 16px;
            font-size: 15px;
            font-weight: 500;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
        }

        .alert button {
            border: none;
            background: transparent;
            color: inherit;
            font-size: 18px;
            cursor: pointer;
        }

        .alert-exito {
            background: rgba(46, 196, 182, 0.16);
            color: #0f766e;
        }

        .alert-error {
            background: rgba(239, 68, 68, 0.16);
            color: #b91c1c;
        }

        .alert-alerta {
            background: rgba(250, 204, 21, 0.2);
            color: #b45309;
        }


        .filters {
            display: grid;
            gap: 16px;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            align-items: end;
            background: rgba(255, 255, 255, 0.85);
            border-radius: 20px;
            padding: 20px 24px;
            box-shadow: 0 18px 45px rgba(15, 37, 78, 0.12);
            margin-bottom: 28px;
        }

        .filters label {
            display: block;
            font-weight: 600;
            color: #1b263b;
            margin-bottom: 8px;
            font-size: 15px;
        }

        .filters input[type="text"],
        .filters select {
            width: 100%;
            padding: 12px 16px;
            border-radius: 12px;
            border: 1px solid #cbd5f5;
            background: #f8faff;
            font-size: 15px;
            outline: none;
            transition: border 0.2s ease, box-shadow 0.2s ease;
        }

        .filters input[type="text"]:focus,
        .filters select:focus {
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.18);
        }

        .botones-filtro {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            align-items: center;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 20px;
            border-radius: 999px;
            border: none;
            background: linear-gradient(135deg, #4361ee, #3547d2);
            color: #ffffff;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
        }

        .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 16px 32px rgba(67, 97, 238, 0.25);
        }

        .btn.secundario {
            background: #e0e7ff;
            color: #2c3e50;
        }

        .btn.secundario:hover {
            background: #c8d3ff;
            box-shadow: none;
        }

        .resumen-resultados {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
            margin-bottom: 18px;
            color: rgba(27, 38, 59, 0.75);
            font-size: 15px;
        }

        .grid-productos {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            gap: 24px;
        }

        .card-producto {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            padding: 20px;
            box-shadow: 0 18px 45px rgba(15, 37, 78, 0.12);
            display: flex;
            flex-direction: column;
            gap: 16px;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .card-producto:hover {
            transform: translateY(-6px);
            box-shadow: 0 24px 55px rgba(15, 37, 78, 0.18);
        }

        .card-producto .imagen {
            position: relative;
            border-radius: 16px;
            overflow: hidden;
            aspect-ratio: 4 / 3;
            background: linear-gradient(135deg, #b1c8ff, #d6e4ff);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card-producto img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .card-producto .imagen span {
            font-size: 48px;
            font-weight: 600;
            color: rgba(27, 38, 59, 0.4);
        }

        .card-producto h3 {
            margin: 0;
            font-size: 20px;
            color: #0d1b2a;
        }

        .badges {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .badge {
            background: rgba(67, 97, 238, 0.12);
            color: #3547d2;
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 600;
            letter-spacing: 0.3px;
        }

        .precio {
            font-size: 22px;
            font-weight: 700;
            color: #2c5282;
        }

        .stock {
            font-size: 13px;
            color: rgba(27, 38, 59, 0.65);
        }
        .form-carrito {
            margin-top: auto;
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .acciones-carrito {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }

        .form-carrito label {
            font-size: 13px;
            color: rgba(27, 38, 59, 0.7);
        }

        .form-carrito input[type="number"] {
            width: 90px;
            padding: 10px 12px;
            border-radius: 12px;
            border: 1px solid #cbd5f5;
            background: #f8faff;
            font-size: 14px;
        }

        .form-carrito input[type="number"]:focus {
            border-color: #4361ee;
            outline: none;
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.18);
        }

        .btn-agregar {
            flex: 1;
            min-width: 160px;
        }

        .sin-stock {
            margin-top: auto;
            padding: 12px 16px;
            background: rgba(239, 68, 68, 0.12);
            border-radius: 12px;
            color: #b91c1c;
            font-weight: 600;
            text-align: center;
        }


        .empty-state {
            text-align: center;
            padding: 60px 20px;
            background: rgba(255, 255, 255, 0.92);
            border-radius: 24px;
            box-shadow: 0 18px 45px rgba(15, 37, 78, 0.08);
        }

        .empty-state h2 {
            margin: 0 0 12px;
            color: #0d1b2a;
            font-size: 26px;
        }

        .empty-state p {
            margin: 0;
            color: rgba(27, 38, 59, 0.7);
            font-size: 15px;
        }

        @media (max-width: 768px) {
            header h1 {
                font-size: 32px;
            }

            .filters {
                padding: 16px;
            }
        }
    </style>
</head>
<body>
<fmt:setLocale value="es_PE" />
<div class="layout">
    <div class="topbar">
        <div class="topbar-links">
            <c:choose>
                <c:when test="${not empty sessionScope.clienteAutenticado}">
                    <span class="topbar-user">Hola, <strong><c:out value="${sessionScope.clienteAutenticado.nombre}" /></strong></span>
                    <a class="topbar-link" href="logout">Cerrar sesión</a>
                </c:when>
                <c:otherwise>
                    <a class="topbar-link" href="loginCliente">Iniciar sesión</a>
                </c:otherwise>
            </c:choose>
            <a class="topbar-link" href="login">Administrativo</a>
        </div>
    </div>
    <header>
       <div class="header-top">
            <div class="header-titles">
                <h1>Catálogo de la tienda</h1>
                <p>Explora los productos disponibles en tu inventario. La información se actualiza automáticamente con los
                    registros del sistema.</p>
            </div>
            <a class="btn carrito" href="carrito">
                Ver carrito
                <span class="badge-cantidad">
                    <c:out value="${totalItemsCarrito != null ? totalItemsCarrito : 0}" />
                </span>
            </a>
        </div>
    </header>
                <c:set var="mensajeCarrito" value="${sessionScope.mensajeCarrito}" />
    <c:if test="${not empty mensajeCarrito}">
        <div class="alert alert-${mensajeCarrito.tipo}">
            <span><c:out value="${mensajeCarrito.texto}" /></span>
            <button type="button" onclick="this.parentElement.remove()" aria-label="Cerrar aviso">&times;</button>
        </div>
        <c:remove var="mensajeCarrito" scope="session" />
    </c:if>

    <form class="filters" method="get" action="catalogo">
        <div>
            <label for="buscar">Buscar producto</label>
            <input id="buscar" type="text" name="buscar" value="${fn:escapeXml(textoBusqueda)}"
                   placeholder="Nombre, marca, modelo o categoría" />
        </div>
        <div>
            <label for="categoria">Categoría</label>
            <select id="categoria" name="categoria" onchange="this.form.submit()">
                <option value="">Todas las categorías</option>
                <c:forEach var="categoria" items="${categorias}">
                    <c:choose>
                        <c:when test="${categoria.idCategoria == categoriaSeleccionada}">
                            <option value="${categoria.idCategoria}" selected>
                                <c:out value="${categoria.nombre}" />
                            </option>
                        </c:when>
                        <c:otherwise>
                            <option value="${categoria.idCategoria}">
                                <c:out value="${categoria.nombre}" />
                            </option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
        <div class="botones-filtro">
            <button class="btn" type="submit">Buscar</button>
            <c:if test="${not empty textoBusqueda or not empty categoriaSeleccionada}">
                <a class="btn secundario" href="catalogo">Limpiar filtros</a>
            </c:if>
        </div>
    </form>

    <c:set var="totalProductos" value="${fn:length(productosCatalogo)}" />
    <div class="resumen-resultados">
        <span><strong>${totalProductos}</strong> producto<c:if test="${totalProductos != 1}">s</c:if> encontrado<c:if test="${totalProductos != 1}">s</c:if>.</span>
        <c:if test="${not empty textoBusqueda}">
            <span>Búsqueda: “<c:out value="${textoBusqueda}" />”.</span>
        </c:if>
        <c:if test="${not empty categoriaSeleccionada}">
            <span>Filtrado por categoría seleccionada.</span>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${totalProductos == 0}">
            <div class="empty-state">
                <h2>No encontramos productos</h2>
                <p>Prueba ajustando los filtros o registrando nuevos productos en el sistema.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-productos">
                <c:forEach var="producto" items="${productosCatalogo}">
                    <article class="card-producto">
                        <div class="imagen">
                            <c:choose>
                                <c:when test="${not empty producto.fotoBase64}">
                                    <img src="data:image/*;base64,${producto.fotoBase64}" alt="Foto de ${fn:escapeXml(producto.nombre)}" />
                                </c:when>
                                <c:otherwise>
                                    <span>
                                        <c:choose>
                                            <c:when test="${not empty producto.nombre}">
                                                ${fn:substring(producto.nombre, 0, 1)}
                                            </c:when>
                                            <c:otherwise>?</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="badges">
                            <c:if test="${not empty producto.nombreCategoria}">
                                <span class="badge">${fn:escapeXml(producto.nombreCategoria)}</span>
                            </c:if>
                            <c:if test="${not empty producto.nombreMarca}">
                                <span class="badge">${fn:escapeXml(producto.nombreMarca)}</span>
                            </c:if>
                            <c:if test="${not empty producto.nombreModelo}">
                                <span class="badge">${fn:escapeXml(producto.nombreModelo)}</span>
                            </c:if>
                            <c:if test="${not empty producto.nombreColor}">
                                <span class="badge">${fn:escapeXml(producto.nombreColor)}</span>
                            </c:if>
                        </div>
                        <h3><c:out value="${producto.nombre}" /></h3>
                        <div class="precio">
                            <c:choose>
                                <c:when test="${not empty producto.precioUnitario}">
                                    <fmt:formatNumber value="${producto.precioUnitario}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                                </c:when>
                                <c:otherwise>
                                    S/ 0.00
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="stock">Disponible: <strong><c:out value="${producto.cantidad}" /></strong> unidades.</div>
                         <c:choose>
                            <c:when test="${producto.cantidad > 0}">
                                <form class="form-carrito" method="post" action="carrito">
                                    <input type="hidden" name="accion" value="agregar" />
                                    <input type="hidden" name="idProducto" value="${producto.idProducto}" />
                                    <input type="hidden" name="redirect" value="catalogo" />
                                    <label for="cantidad_${producto.idProducto}">Cantidad</label>
                                    <div class="acciones-carrito">
                                        <input id="cantidad_${producto.idProducto}" type="number" name="cantidad" min="1"
                                               max="${producto.cantidad}" value="1" />
                                        <button class="btn btn-agregar" type="submit">Añadir al carrito</button>
                                    </div>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <div class="sin-stock">Sin stock disponible</div>
                            </c:otherwise>
                        </c:choose>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>