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
        
        .alerta {
            border-radius: 12px;
            padding: 12px 16px;
            margin-bottom: 16px;
            font-weight: 600;
            font-size: 14px;
        }

        .alerta.exito {
            background: rgba(28, 124, 84, 0.12);
            color: #1c7c54;
            border: 1px solid rgba(28, 124, 84, 0.2);
        }

        .alerta.error {
            background: rgba(239, 71, 111, 0.12);
            color: #d62839;
            border: 1px solid rgba(239, 71, 111, 0.2);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 16px;
            overflow: hidden;
        }

        thead {
            background: #155d4b;
            color: #ffffff;
        }

        th, td {
            padding: 12px 14px;
            text-align: left;
            font-size: 14px;
        }

        tbody tr:nth-child(even) {
            background: #f1f5fd;
        }

        tbody tr:hover {
            background: #e3f2f0;
        }

        .tabla-wrapper {
            overflow-x: auto;
        }

        .estado {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 999px;
            font-weight: 600;
            font-size: 13px;
        }

        .estado.activo {
            background: rgba(28, 124, 84, 0.18);
            color: #1c7c54;
        }

        .estado.inactivo {
            background: rgba(239, 71, 111, 0.18);
            color: #ef476f;
        }

        .acciones {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .btn-tabla {
            display: inline-block;
            padding: 8px 12px;
            border-radius: 10px;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            color: #ffffff;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .btn-editar {
            background: #f4a261;
        }

        .btn-editar:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 20px rgba(244, 162, 97, 0.25);
        }

        .btn-estado {
            background: #ef476f;
        }

        .btn-estado.activar {
            background: #118ab2;
        }

        .btn-estado:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 20px rgba(239, 71, 111, 0.25);
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 14px;
        }

        label {
            font-weight: 600;
            font-size: 14px;
        }

        select, input[type="number"] {
            padding: 10px 12px;
            border-radius: 10px;
            border: 1px solid #ccd5e3;
            font-size: 14px;
            transition: border 0.2s ease, box-shadow 0.2s ease;
        }

        select:focus, input[type="number"]:focus {
            border-color: #1c7c54;
            box-shadow: 0 0 0 4px rgba(28, 124, 84, 0.18);
            outline: none;
        }

        button {
            padding: 12px 18px;
            border-radius: 12px;
            border: none;
            background: linear-gradient(90deg, #1c7c54, #3ba99c);
            color: #ffffff;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        button:hover {
            transform: translateY(-1px);
            box-shadow: 0 12px 22px rgba(28, 124, 84, 0.3);
        }

        .resumen {
            margin-top: 26px;
            border-top: 1px solid #d7e3f1;
            padding-top: 18px;
        }

        .resumen h2 {
            font-size: 20px;
            color: #0f3b2e;
            margin-bottom: 12px;
        }

        .resumen-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px dashed #d7e3f1;
            font-size: 14px;
        }

        .resumen-item:last-child {
            border-bottom: none;
        }

        .resumen-item strong {
            color: #1c7c54;
        }

        .resumen-item .desbalance {
            color: #ef476f;
            font-weight: 600;
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