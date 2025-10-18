<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Productos por Talla</title>
    <style>
        body {
            font-family: 'Poppins', Arial, sans-serif;
            background: #f5f7fb;
            margin: 0;
            color: #102542;
        }

        .barra-superior {
            background: linear-gradient(90deg, #1c7c54, #3ba99c);
            padding: 18px 32px;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            box-shadow: 0 4px 18px rgba(28, 124, 84, 0.25);
        }

        .btn-menu {
            background: #ffffff;
            color: #1c7c54;
            padding: 10px 22px;
            border-radius: 999px;
            text-decoration: none;
            font-weight: 600;
            transition: transform 0.2s ease, box-shadow 0.2s ease, color 0.2s ease;
            box-shadow: 0 12px 24px rgba(28, 124, 84, 0.25);
        }

        .btn-menu:hover {
            color: #14563b;
            transform: translateY(-2px);
            box-shadow: 0 16px 28px rgba(28, 124, 84, 0.35);
        }

        .contenedor {
            display: flex;
            gap: 24px;
            padding: 28px;
            min-height: calc(100vh - 110px);
        }

        .panel {
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 16px 35px rgba(16, 37, 66, 0.08);
            padding: 26px;
            overflow-y: auto;
        }

        .panel-izquierdo {
            flex: 2;
        }

        .panel-derecho {
            flex: 1;
        }

        h1 {
            margin: 0 0 18px;
            text-align: center;
            color: #1c7c54;
            font-size: 28px;
        }

        @media (max-width: 960px) {
            .contenedor {
                flex-direction: column;
                min-height: auto;
            }

            .panel {
                max-height: none;
            }
        }
    </style>
</head>
<body>

    <div class="barra-superior">
        <a class="btn-menu" href="<c:url value='/VistaMenu/MenuMain.jsp'/>">Volver al Menú</a>
    </div>

    <div class="contenedor">
        <div class="panel panel-izquierdo">
            <h1>Lista de combinaciones</h1>
            <jsp:include page="lista.jsp" />
        </div>

        <div class="panel panel-derecho">
            <h1>${not empty productoTalla ? 'Editar combinación' : 'Nueva combinación'}</h1>
            <c:choose>
                <c:when test="${not empty productoTalla}">
                    <jsp:include page="edit.jsp" />
                </c:when>
                <c:otherwise>
                    <jsp:include page="add.jsp" />
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</body>
</html>