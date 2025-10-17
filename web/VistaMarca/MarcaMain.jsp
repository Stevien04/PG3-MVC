<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Marcas</title>
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
    </style>
</head>
<body>

    <div class="contenedor">
        <div class="izquierda">
            <jsp:include page="lista.jsp" />
        </div>

        <div class="derecha">
            <c:choose>
                <c:when test="${not empty marca}">
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