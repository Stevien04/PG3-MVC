<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Gestión de Productos</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f5f5f5;
                margin: 0;
                padding: 0;
            }

            .contenedor {
                display: flex;
                height: 100vh;
                gap: 0;
            }

            .izquierda {
                flex: 3.5;
                padding: 25px;
                background-color: #fff;
                border-right: 2px solid #ddd;
                overflow-y: auto;
            }

            .derecha {
                flex: 1.5;
                padding: 25px;
                background-color: #fafafa;
                overflow-y: auto;
                display: flex;
                flex-direction: column;
                gap: 24px;
            }

            h1 {
                text-align: center;
                margin: 0;
                padding: 24px 0 0;
                color: #0d47a1;
            }

            @media (max-width: 1000px) {
                .contenedor {
                    flex-direction: column;
                    height: auto;
                }

                .izquierda,
                .derecha {
                    width: 100%;
                    border: none;
                }
            }

            .formulario {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
            }

            .formulario h2 {
                margin-top: 0;
                color: #1a237e;
                font-size: 1.2rem;
            }

            .form {
                display: flex;
                flex-direction: column;
                gap: 16px;
            }

            .campo {
                display: flex;
                flex-direction: column;
                gap: 6px;
            }

            .campo-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
                gap: 16px;
            }

            label {
                font-weight: bold;
                color: #424242;
            }

            input[type="text"],
            input[type="number"],
            select {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 0.95rem;
            }

            button {
                align-self: flex-start;
                background: #0d47a1;
                color: #fff;
                border: none;
                padding: 10px 18px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 0.95rem;
            }

            button:hover {
                background: #1565c0;
            }

            .bloque-lista {
                margin-bottom: 32px;
            }

            .bloque-encabezado {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                align-items: center;
                gap: 16px;
                margin-bottom: 16px;
            }

            .bloque-encabezado h2 {
                margin: 0;
                color: #1a237e;
            }

            .buscador {
                display: flex;
                gap: 8px;
            }

            .buscador input[type="text"] {
                padding: 8px 10px;
                border-radius: 4px;
                border: 1px solid #ccc;
                min-width: 220px;
            }

            .buscador button {
                padding: 8px 14px;
            }

            .tabla-contenedor {
                overflow-x: auto;
            }

            .tabla {
                width: 100%;
                border-collapse: collapse;
                font-size: 0.95rem;
            }

            .tabla th,
            .tabla td {
                padding: 10px 12px;
                border-bottom: 1px solid #e0e0e0;
                text-align: left;
            }

            .tabla th {
                background: #e8eaf6;
                color: #1a237e;
                font-weight: 600;
            }

            .tabla tbody tr:nth-child(even) {
                background: #f8f9ff;
            }

            .estado {
                padding: 4px 10px;
                border-radius: 12px;
                font-size: 0.85rem;
                font-weight: 600;
            }

            .estado.activo {
                background: #e8f5e9;
                color: #2e7d32;
            }

            .estado.inactivo {
                background: #ffebee;
                color: #c62828;
            }

            .estado.sin-estado {
                background: #fff3e0;
                color: #ef6c00;
            }

            .mensaje-vacio,
            .mensaje-info {
                text-align: center;
                color: #616161;
                font-style: italic;
            }

            .mensaje-error {
                background: #ffebee;
                color: #c62828;
                padding: 10px 12px;
                border-radius: 6px;
                margin: 0 0 16px;
            }

            .mensaje-exito {
                background: #e8f5e9;
                color: #2e7d32;
                padding: 10px 12px;
                border-radius: 6px;
                margin: 0 0 16px;
            }
        </style>
    </head>
    <body>

        <h1>Gestión de Productos</h1>
        <div class="contenedor">
            <div class="izquierda">
                <jsp:include page="lista.jsp" />
            </div>

            <div class="derecha">
                <div class="formulario">
                    <c:choose>
                        <c:when test="${not empty producto}">
                            <jsp:include page="edit.jsp" />
                        </c:when>
                        <c:otherwise>
                            <jsp:include page="add.jsp" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="formulario">
                    <jsp:include page="productoTallaForm.jsp" />
                </div>
            </div>
        </div>

    </body>
</html>