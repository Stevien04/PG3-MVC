<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Carrito de compras</title>
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
            background: linear-gradient(135deg, #f7fbff 0%, #eef2ff 40%, #ffffff 100%);
            color: #1b263b;
            min-height: 100vh;
        }

        a {
            color: inherit;
            text-decoration: none;
        }

        .layout {
            max-width: 1100px;
            margin: 0 auto;
            padding: 32px 20px 48px;
        }

        header {
            display: flex;
            flex-wrap: wrap;
            gap: 16px;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 28px;
        }

        header h1 {
            margin: 0;
            font-size: 36px;
            font-weight: 700;
            color: #0d1b2a;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 22px;
            border-radius: 999px;
            border: none;
            background: linear-gradient(135deg, #4361ee, #3547d2);
            color: #ffffff;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 14px 32px rgba(67, 97, 238, 0.25);
        }

        .btn-secundario {
            background: #e0e7ff;
            color: #1f2a44;
        }

        .btn-peligro {
            background: linear-gradient(135deg, #ef4444, #dc2626);
        }

        .alert {
            margin-bottom: 24px;
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

        .carrito-contenido {
            display: grid;
            gap: 20px;
        }

        .item-carrito {
            display: grid;
            grid-template-columns: 120px 1fr;
            gap: 18px;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            padding: 18px;
            box-shadow: 0 16px 40px rgba(15, 37, 78, 0.12);
        }

        .item-imagen {
            width: 120px;
            height: 120px;
            border-radius: 16px;
            overflow: hidden;
            background: linear-gradient(135deg, #b1c8ff, #d6e4ff);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .item-imagen img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .item-imagen span {
            font-size: 42px;
            font-weight: 600;
            color: rgba(27, 38, 59, 0.4);
        }

        .item-detalle {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .item-titulo {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 12px;
        }

        .item-titulo h2 {
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
        }

        .item-acciones {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            justify-content: space-between;
        }

        .item-acciones form {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
        }

        .item-acciones input[type="number"] {
            width: 90px;
            padding: 10px 12px;
            border-radius: 12px;
            border: 1px solid #cbd5f5;
            background: #f8faff;
            font-size: 14px;
        }

        .item-acciones input[type="number"]:focus {
            border-color: #4361ee;
            outline: none;
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.18);
        }

        .item-subtotal {
            font-size: 18px;
            font-weight: 700;
            color: #2c5282;
        }

        .resumen-carrito {
            margin-top: 12px;
            padding: 20px;
            background: rgba(27, 38, 59, 0.05);
            border-radius: 18px;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            gap: 16px;
            align-items: center;
        }

        .resumen-carrito strong {
            font-size: 22px;
        }

        .acciones-finales {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .empty-state {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 24px;
            padding: 60px 20px;
            text-align: center;
            box-shadow: 0 16px 40px rgba(15, 37, 78, 0.12);
        }

        .empty-state h2 {
            margin-bottom: 12px;
            font-size: 26px;
            color: #0d1b2a;
        }

        .empty-state p {
            margin-bottom: 24px;
            color: rgba(27, 38, 59, 0.7);
        }

        @media (max-width: 768px) {
            .item-carrito {
                grid-template-columns: 1fr;
                text-align: center;
            }

            .item-acciones {
                flex-direction: column;
                align-items: stretch;
            }

            .item-acciones form {
                width: 100%;
                justify-content: center;
            }

            header {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
<fmt:setLocale value="es_PE" />
<div class="layout">
    <header>
        <h1>Tu carrito</h1>
        <div class="acciones-finales">
            <a class="btn btn-secundario" href="catalogo">Seguir comprando</a>
            <c:if test="${not empty itemsCarrito}">
                <form method="post" action="carrito">
                    <input type="hidden" name="accion" value="vaciar" />
                    <input type="hidden" name="redirect" value="carrito" />
                    <button class="btn btn-peligro" type="submit">Vaciar carrito</button>
                </form>
            </c:if>
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

    <c:choose>
        <c:when test="${empty itemsCarrito}">
            <div class="empty-state">
                <h2>Tu carrito está vacío</h2>
                <p>Explora el catálogo y agrega los productos que más te interesen.</p>
                <a class="btn" href="catalogo">Ir al catálogo</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="carrito-contenido">
                <c:forEach var="item" items="${itemsCarrito}">
                    <article class="item-carrito">
                        <div class="item-imagen">
                            <c:choose>
                                <c:when test="${not empty item.fotoBase64}">
                                    <img src="data:image/*;base64,${item.fotoBase64}" alt="Foto de ${item.nombre}" />
                                </c:when>
                                <c:otherwise>
                                    <span>
                                        <c:choose>
                                            <c:when test="${not empty item.nombre}">
                                                ${fn:substring(item.nombre, 0, 1)}
                                            </c:when>
                                            <c:otherwise>?</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="item-detalle">
                            <div class="item-titulo">
                                <h2><c:out value="${item.nombre}" /></h2>
                                <span class="item-subtotal">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                                </span>
                            </div>
                            <div class="badges">
                                <c:if test="${not empty item.nombreCategoria}">
                                    <span class="badge"><c:out value="${item.nombreCategoria}" /></span>
                                </c:if>
                                <c:if test="${not empty item.nombreMarca}">
                                    <span class="badge"><c:out value="${item.nombreMarca}" /></span>
                                </c:if>
                                <c:if test="${not empty item.nombreModelo}">
                                    <span class="badge"><c:out value="${item.nombreModelo}" /></span>
                                </c:if>
                                <c:if test="${not empty item.nombreColor}">
                                    <span class="badge"><c:out value="${item.nombreColor}" /></span>
                                </c:if>
                            </div>
                            <div class="item-acciones">
                                <form method="post" action="carrito">
                                    <input type="hidden" name="accion" value="actualizar" />
                                    <input type="hidden" name="idProducto" value="${item.idProducto}" />
                                    <input type="hidden" name="redirect" value="carrito" />
                                    <label for="cantidadItem_${item.idProducto}">Cantidad</label>
                                    <input id="cantidadItem_${item.idProducto}" type="number" name="cantidad" min="1"
                                           max="${item.stockDisponible > 0 ? item.stockDisponible : 1}" value="${item.cantidad}" />
                                    <button class="btn" type="submit">Actualizar</button>
                                </form>
                                <form method="post" action="carrito">
                                    <input type="hidden" name="accion" value="eliminar" />
                                    <input type="hidden" name="idProducto" value="${item.idProducto}" />
                                    <input type="hidden" name="redirect" value="carrito" />
                                    <button class="btn btn-secundario" type="submit">Eliminar</button>
                                </form>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
            <div class="resumen-carrito">
                <div>
                    <span>Total de artículos: <strong><c:out value="${totalItemsCarrito}" /></strong></span>
                </div>
                <div>
                    <span>Total a pagar: <strong>
                        <fmt:formatNumber value="${totalCarrito}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                    </strong></span>
                </div>
                <button class="btn" type="button" disabled>Proceder con la compra</button>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>