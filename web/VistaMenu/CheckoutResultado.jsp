<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Compra simulada con PayPal</title>
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
            max-width: 980px;
            margin: 0 auto;
            padding: 32px 20px 48px;
        }

        header {
            text-align: center;
            margin-bottom: 32px;
        }

        h1 {
            margin: 0;
            font-size: 34px;
            color: #0d1b2a;
        }

        .subtitulo {
            margin-top: 12px;
            color: rgba(13, 27, 42, 0.72);
            font-size: 16px;
        }

        .panel {
            background: rgba(255, 255, 255, 0.96);
            border-radius: 24px;
            padding: 28px;
            box-shadow: 0 18px 40px rgba(15, 37, 78, 0.12);
            margin-bottom: 28px;
        }

        .panel h2 {
            margin-top: 0;
            color: #1b263b;
        }

        .items-lista {
            display: flex;
            flex-direction: column;
            gap: 18px;
            margin: 0;
            padding: 0;
        }

        .item {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 16px;
            padding-bottom: 16px;
            border-bottom: 1px solid rgba(27, 38, 59, 0.1);
        }

        .item:last-child {
            border-bottom: none;
            padding-bottom: 0;
        }

        .item h3 {
            margin: 0 0 6px;
            font-size: 20px;
            color: #14213d;
        }

        .item-detalle {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            font-size: 14px;
            color: rgba(13, 27, 42, 0.75);
        }

        .item-subtotal {
            font-weight: 700;
            color: #2f855a;
            font-size: 18px;
        }

        .resumen {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            align-items: center;
            justify-content: space-between;
            margin-top: 24px;
        }

        .resumen strong {
            font-size: 24px;
            color: #14213d;
        }

        .acciones {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            justify-content: center;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 24px;
            border-radius: 999px;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            border: none;
            transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
        }

        .btn-primario {
            background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
            color: #ffffff;
            box-shadow: 0 12px 24px rgba(72, 187, 120, 0.28);
        }

        .btn-primario:hover {
            transform: translateY(-2px);
            box-shadow: 0 16px 32px rgba(72, 187, 120, 0.32);
        }

        .btn-secundario {
            background: rgba(67, 97, 238, 0.12);
            color: #4361ee;
        }

        .btn-secundario:hover {
            background: rgba(67, 97, 238, 0.18);
            transform: translateY(-1px);
        }

        .nota {
            text-align: center;
            color: rgba(13, 27, 42, 0.7);
            font-size: 14px;
            margin-top: 16px;
        }

        @media (max-width: 768px) {
            .item {
                grid-template-columns: 1fr;
            }

            .resumen {
                flex-direction: column;
                align-items: flex-start;
            }

            .acciones {
                flex-direction: column;
                align-items: stretch;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<fmt:setLocale value="es_PE" />
<div class="layout">
    <header>
        <h1>¡Compra simulada con éxito!</h1>
        <p class="subtitulo">Tu pago con PayPal ha sido simulado correctamente y el inventario se actualizó.</p>
    </header>

    <div class="panel">
        <h2>Resumen de tu pedido</h2>

        <c:if test="${empty itemsComprados}">
            <p>No se encontraron productos en el resumen de compra.</p>
        </c:if>

        <c:if test="${not empty itemsComprados}">
            <div class="items-lista">
                <c:forEach var="item" items="${itemsComprados}">
                    <article class="item">
                        <div>
                            <h3><c:out value="${item.nombre}" /></h3>
                            <div class="item-detalle">
                                <span>Cantidad: <strong><c:out value="${item.cantidad}" /></strong></span>
                                <span>Precio unitario: <strong>
                                        <fmt:formatNumber value="${item.precioUnitario}" type="currency" currencySymbol="S/ "
                                                          minFractionDigits="2" />
                                    </strong></span>
                            </div>
                        </div>
                        <div class="item-subtotal">
                            <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                        </div>
                    </article>
                </c:forEach>
            </div>

            <div class="resumen">
                <div>
                    <span>Total de artículos: <strong><c:out value="${totalItemsCarrito}" /></strong></span><br />
                    <span>Total pagado: <strong>
                            <fmt:formatNumber value="${totalCarrito}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                        </strong></span>
                </div>
            </div>
        </c:if>
    </div>

    <div class="acciones">
        <a class="btn btn-primario" href="catalogo">Seguir explorando productos</a>
        <a class="btn btn-secundario" href="carrito">Volver al carrito</a>
    </div>

    <p class="nota">Recibirás una confirmación simulada de PayPal. Esta acción ya descontó las unidades compradas del inventario.</p>
</div>
</body>
</html>