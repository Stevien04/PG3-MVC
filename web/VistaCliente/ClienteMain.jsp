<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Clientes</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f5f5f5;
                margin: 0;
                padding: 0;
            }

            .contenedor {
                display: flex;
             
                min-height: calc(100vh - 100px);
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
            }

            h1 {
                text-align: center;
                margin-bottom: 20px;
                color: #0d47a1;
            }

            .barra-superior {
                background: linear-gradient(90deg, #0d47a1, #42a5f5);
                padding: 18px 32px;
                display: flex;
                justify-content: flex-end;
                align-items: center;
                box-shadow: 0 4px 18px rgba(13, 71, 161, 0.25);
            }

            .btn-menu {
                background: #ffffff;
                color: #0d47a1;
                padding: 10px 22px;
                border-radius: 999px;
                text-decoration: none;
                font-weight: 600;
                transition: transform 0.2s ease, box-shadow 0.2s ease, color 0.2s ease;
                box-shadow: 0 10px 20px rgba(13, 71, 161, 0.25);
            }

            .btn-menu:hover {
                color: #08306b;
                transform: translateY(-2px);
                box-shadow: 0 14px 24px rgba(13, 71, 161, 0.35);
            }

            @media (max-width: 1000px) {
                .contenedor {
                    flex-direction: column;
                  
                    min-height: auto;
                }
                .izquierda, .derecha {
                    width: 100%;
                    border: none;
                }
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
                    <c:when test="${not empty cliente}">
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