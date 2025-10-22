<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Simulación de pago con PayPal</title>
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
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 24px;
        }

        h1 {
            margin: 0;
            font-size: 32px;
            color: #0d1b2a;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 20px;
            border-radius: 999px;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            border: none;
            transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
        }

        .btn-secundario {
            background: rgba(67, 97, 238, 0.12);
            color: #4361ee;
        }

        .btn-secundario:hover {
            background: rgba(67, 97, 238, 0.18);
            transform: translateY(-1px);
        }

        .btn-paypal {
            background: linear-gradient(135deg, #ffc439 0%, #ffb347 100%);
            color: #142c8e;
            box-shadow: 0 10px 20px rgba(255, 196, 57, 0.28);
        }

        .btn-paypal:hover {
            transform: translateY(-2px);
            box-shadow: 0 14px 28px rgba(255, 196, 57, 0.32);
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

        .panel p {
            color: rgba(13, 27, 42, 0.7);
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
            margin: 0 0 8px;
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
            color: #2c5282;
            font-size: 18px;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: rgba(67, 97, 238, 0.12);
            color: #4361ee;
            padding: 6px 12px;
            border-radius: 999px;
            font-weight: 600;
            font-size: 12px;
        }

        .resumen-final {
            display: flex;
            flex-wrap: wrap;
            gap: 24px;
            align-items: center;
            justify-content: space-between;
        }

        .resumen-total {
            display: flex;
            flex-direction: column;
            gap: 6px;
            font-size: 16px;
        }

        .resumen-total strong {
            font-size: 24px;
            color: #14213d;
        }

        .nota {
            font-size: 14px;
            color: rgba(13, 27, 42, 0.7);
            margin-top: 8px;
            max-width: 380px;
        }

        .alert {
            border-radius: 16px;
            padding: 16px 20px;
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 24px;
            font-weight: 500;
        }

        .alert-exito {
            background: rgba(56, 161, 105, 0.12);
            color: #2f855a;
            border: 1px solid rgba(56, 161, 105, 0.28);
        }

        .alert-alerta {
            background: rgba(237, 137, 54, 0.12);
            color: #c05621;
            border: 1px solid rgba(237, 137, 54, 0.28);
        }

        .alert-error {
            background: rgba(229, 62, 62, 0.12);
            color: #c53030;
            border: 1px solid rgba(229, 62, 62, 0.28);
        }

        @media (max-width: 768px) {
            .item {
                grid-template-columns: 1fr;
            }

            .resumen-final {
                flex-direction: column;
                align-items: stretch;
            }

            .btn-paypal {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<fmt:setLocale value="es_PE" />
<div class="layout">
    <header>
        <h1>Simulación de pago con PayPal</h1>
        <a class="btn btn-secundario" href="carrito">Volver al carrito</a>
    </header>

    <div class="panel">
        <h2>Revisa tu pedido</h2>
        <p>Estás a un paso de completar tu compra. Esta es una simulación de PayPal: no se realizarán cargos reales,
            pero se actualizará el stock disponible para reflejar la venta.</p>

        <c:if test="${not empty sessionScope.mensajeCheckout}">
            <c:set var="mensajeCheckout" value="${sessionScope.mensajeCheckout}" />
            <div class="alert alert-${mensajeCheckout.tipo}">
                <span><c:out value="${mensajeCheckout.texto}" /></span>
            </div>
            <c:remove var="mensajeCheckout" scope="session" />
        </c:if>

        <div class="items-lista">
            <c:forEach var="item" items="${itemsCarrito}">
                <article class="item">
                    <div>
                        <h3><c:out value="${item.nombre}" /></h3>
                        <div class="item-detalle">
                            <span>Cantidad: <strong><c:out value="${item.cantidad}" /></strong></span>
                            <span>Precio unitario: <strong>
                                    <fmt:formatNumber value="${item.precioUnitario}" type="currency" currencySymbol="S/ "
                                                      minFractionDigits="2" />
                                </strong></span>
                            <c:if test="${not empty item.nombreMarca}">
                                <span class="badge">Marca: <c:out value="${item.nombreMarca}" /></span>
                            </c:if>
                            <c:if test="${not empty item.nombreModelo}">
                                <span class="badge">Modelo: <c:out value="${item.nombreModelo}" /></span>
                            </c:if>
                            <c:if test="${not empty item.nombreColor}">
                                <span class="badge">Color: <c:out value="${item.nombreColor}" /></span>
                            </c:if>
                        </div>
                    </div>
                    <div class="item-subtotal">
                        <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                    </div>
                </article>
            </c:forEach>
        </div>

        <div class="resumen-final">
            <div class="resumen-total">
                <span>Total de artículos: <strong><c:out value="${totalItemsCarrito}" /></strong></span>
                <span>Total a pagar: <strong>
                        <fmt:formatNumber value="${totalCarrito}" type="currency" currencySymbol="S/ " minFractionDigits="2" />
                    </strong></span>
            </div>
            <div>
                <form method="post" action="checkout">
                    <input type="hidden" name="accion" value="simular" />
                    <button class="btn btn-paypal" type="submit">Confirmar pago con PayPal (simulación)</button>
                </form>
                <p class="nota">Al confirmar, el sistema descontará las unidades correspondientes del inventario para reflejar la compra.</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>