<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Tallas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .contenedor {
            display: flex;
            min-height: calc(100vh - 80px);
        }
        .izquierda {
            flex: 2;
            padding: 20px;
            background-color: #fff;
            border-right: 2px solid #ddd;
            overflow-y: auto;
        }
        .derecha {
            flex: 1;
            padding: 20px;
            background-color: #fafafa;
        }
        h1 {
            text-align: center;
        }

        .barra-superior {
            background: linear-gradient(90deg, #006d77, #83c5be);
            padding: 18px 32px;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            box-shadow: 0 4px 18px rgba(0, 109, 119, 0.25);
        }

        .btn-menu {
            background: #ffffff;
            color: #006d77;
            padding: 10px 20px;
            border-radius: 999px;
            text-decoration: none;
            font-weight: 600;
            transition: transform 0.2s ease, box-shadow 0.2s ease, color 0.2s ease;
            box-shadow: 0 10px 20px rgba(0, 109, 119, 0.25);
        }

        .btn-menu:hover {
            color: #004f55;
            transform: translateY(-2px);
            box-shadow: 0 14px 24px rgba(0, 109, 119, 0.35);
        }
    </style>
</head>
<body>

    <div class="barra-superior">
        <a class="btn-menu" href="<c:url value='/VistaMenu/MenuMain.jsp'/>">Volver al Menú</a>
    </div>

    <div class="contenedor">
        <div class="izquierda">
            <jsp:include page="lista.jsp" />
        </div>

        <div class="derecha">
            <c:choose>
                <c:when test="${not empty talla}">
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