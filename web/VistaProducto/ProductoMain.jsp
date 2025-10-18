<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Productos</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Poppins', Arial, sans-serif;
            margin: 0;
            background: linear-gradient(135deg, #e8f0ff, #f5f9ff);
            color: #0d1b2a;
        }

        .contenedor {
            display: flex;
            gap: 24px;
            padding: 32px;
            min-height: 100vh;
        }

        .panel {
            background: #ffffff;
            border-radius: 18px;
            box-shadow: 0 18px 45px rgba(13, 27, 42, 0.12);
            padding: 28px;
            overflow: hidden;
        }

        .panel-izquierdo {
            flex: 2;
        }

        .panel-derecho {
            flex: 1.1;
        }

        .titulo-seccion {
            margin-top: 0;
            margin-bottom: 22px;
            font-size: 28px;
            font-weight: 700;
            color: #14213d;
        }

        .buttons {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 18px;
        }

        .btn {
            display: inline-block;
            padding: 10px 18px;
            border-radius: 999px;
            border: none;
            background: #4361ee;
            color: #ffffff;
            font-weight: 600;
            text-decoration: none;
            transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(67, 97, 238, 0.25);
        }

        .btn.secundario {
            background: #adb5bd;
            color: #1b263b;
        }

        .btn.activo {
            background: #1b263b;
            color: #fefefe;
        }

        .search-container {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 24px;
        }

        .search-box {
            flex: 1 1 260px;
            padding: 12px 16px;
            border-radius: 12px;
            border: 1px solid #ced4da;
            font-size: 15px;
            outline: none;
            transition: border 0.2s ease, box-shadow 0.2s ease;
        }

        .search-box:focus {
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.2);
        }

        .search-btn {
            padding: 12px 24px;
            border-radius: 12px;
            border: none;
            background: #1b9aaa;
            color: #ffffff;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
        }

        .search-btn:hover {
            background: #14868d;
            transform: translateY(-1px);
            box-shadow: 0 10px 20px rgba(20, 134, 141, 0.25);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 16px;
            overflow: hidden;
        }

        .tabla-wrapper {
            overflow-x: auto;
            border-radius: 16px;
        }

        thead {
            background: #14213d;
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
            background: #e0ebff;
        }

        .estado {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 999px;
            font-weight: 600;
            font-size: 13px;
        }

        .estado.activo {
            background: rgba(16, 172, 132, 0.15);
            color: #10845d;
        }

        .estado.inactivo {
            background: rgba(239, 71, 111, 0.15);
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
            background: #06d6a0;
        }

        .btn-estado:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 20px rgba(239, 71, 111, 0.25);
        }

        .btn-estado.activar:hover {
            box-shadow: 0 10px 20px rgba(6, 214, 160, 0.25);
        }

        .foto-miniatura {
            width: 64px;
            height: 64px;
            object-fit: cover;
            border-radius: 12px;
            border: 1px solid #dee2e6;
            background: #f8f9fa;
        }

        .texto-vacio {
            text-align: center;
            padding: 32px 16px;
            font-weight: 600;
            color: #6c757d;
        }

        .form-card {
            background: #f8faff;
            border-radius: 16px;
            padding: 24px;
            box-shadow: inset 0 0 0 1px rgba(67, 97, 238, 0.08);
        }

        .form-card h2 {
            margin-top: 0;
            font-size: 24px;
            font-weight: 700;
            color: #14213d;
        }

        .alerta-error {
            background: rgba(239, 71, 111, 0.1);
            color: #9d0208;
            border-radius: 12px;
            padding: 12px 16px;
            margin-bottom: 16px;
            font-weight: 600;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
            margin-bottom: 16px;
        }

        .form-group label {
            font-weight: 600;
            color: #1b263b;
        }

        .form-group input,
        .form-group select,
        .form-group textarea {
            padding: 11px 14px;
            border-radius: 10px;
            border: 1px solid #ced4da;
            font-size: 14px;
            transition: border 0.2s ease, box-shadow 0.2s ease;
        }

        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.2);
            outline: none;
        }

        .form-acciones {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 8px;
        }

        .btn-submit {
            padding: 12px 24px;
            border-radius: 12px;
            border: none;
            background: #4361ee;
            color: #ffffff;
            font-weight: 700;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .btn-submit:hover {
            transform: translateY(-1px);
            box-shadow: 0 12px 24px rgba(67, 97, 238, 0.25);
        }

        .btn-reset {
            padding: 12px 24px;
            border-radius: 12px;
            border: none;
            background: #adb5bd;
            color: #1b263b;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .btn-reset:hover {
            transform: translateY(-1px);
            box-shadow: 0 12px 24px rgba(173, 181, 189, 0.25);
        }

        .preview-foto {
            margin-top: 12px;
            display: flex;
            gap: 12px;
            align-items: center;
        }

        .preview-foto img {
            width: 96px;
            height: 96px;
            object-fit: cover;
            border-radius: 14px;
            border: 1px solid #dee2e6;
            background: #f8f9fa;
        }

        .nota-campo {
            font-size: 12px;
            color: #6c757d;
        }

        @media (max-width: 1200px) {
            .contenedor {
                flex-direction: column;
            }

            .panel {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <div class="panel panel-izquierdo">
            <jsp:include page="lista.jsp" />
        </div>
        <div class="panel panel-derecho">
            <c:choose>
                <c:when test="${not empty producto}">
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