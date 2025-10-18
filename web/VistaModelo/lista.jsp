<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Lista de Modelos</title>
<style>
    body {
        font-family: 'Poppins', sans-serif;
        background: linear-gradient(135deg, #f3e5f5, #e1bee7);
        margin: 0;
        padding: 30px 0;
        color: #4a148c;
    }

    h1 {
        text-align: center;
        color: #6a1b9a;
        margin-bottom: 25px;
        letter-spacing: 1px;
    }

    .buttons {
        text-align: center;
        margin-bottom: 15px;
    }

    .btn {
        background-color: #8e24aa;
        color: #fff;
        padding: 10px 18px;
        border-radius: 8px;
        text-decoration: none;
        font-weight: 600;
        margin: 5px;
        transition: all 0.2s ease-in-out;
        border: none;
    }

    .btn:hover {
        background-color: #6a1b9a;
        transform: scale(1.05);
    }

    .search-container {
        text-align: center;
        margin: 20px 0 35px 0;
    }

    .search-box {
        display: block;
        margin: 0 auto 10px auto;
        padding: 10px 14px;
        border: 1.5px solid #ce93d8;
        border-radius: 8px;
        width: 320px;
        font-size: 1rem;
        outline: none;
        color: #6a1b9a;
        transition: 0.3s;
        background-color: #fff;
    }

    .search-box:focus {
        border-color: #8e24aa;
        box-shadow: 0 0 6px rgba(142,36,170,0.4);
    }

    .search-btn {
        display: inline-block;
        background-color: #8e24aa;
        border: none;
        padding: 9px 25px;
        border-radius: 6px;
        cursor: pointer;
        color: #fff;
        font-size: 15px;
        font-weight: 600;
        transition: background-color 0.2s ease, transform 0.2s;
    }

    .search-btn:hover {
        background-color: #6a1b9a;
        transform: scale(1.05);
    }

    table {
        margin: auto;
        border-collapse: collapse;
        width: 90%;
        background-color: #ffffff;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    th {
        background-color: #8e24aa;
        color: #fff;
        text-transform: uppercase;
        padding: 12px;
        font-size: 0.9rem;
        letter-spacing: 0.5px;
    }

    td {
        padding: 12px;
        border-bottom: 1px solid #e0e0e0;
        text-align: center;
    }

    tr:hover {
        background-color: #f3e5f5;
    }

    .btn-editar {
        background-color: #ffca28;
        color: #000;
        padding: 6px 10px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: 600;
        margin-right: 5px;
        transition: 0.2s;
    }

    .btn-editar:hover {
        background-color: #ffb300;
        transform: scale(1.05);
    }

    .btn-eliminar {
        background-color: #d81b60;
        color: white;
        padding: 6px 10px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: 600;
        transition: 0.2s;
    }

    .btn-eliminar:hover {
        background-color: #ad1457;
        transform: scale(1.05);
    }

    .footer {
        text-align: center;
        margin-top: 30px;
        color: #8e24aa;
        font-size: 0.9rem;
    }
</style>
</head>
<body>
    <h1>Lista de Modelos</h1>

    <div class="buttons">
        <a href="${pageContext.request.contextPath}/srvModelo?accion=listarActivos" class="btn" target="_top">Activos</a>
        <a href="${pageContext.request.contextPath}/srvModelo?accion=listarInactivos" class="btn" target="_top">Inactivos</a>
    </div>

    <div class="search-container">
        <form action="${pageContext.request.contextPath}/srvModelo" method="get" target="_top">
            <input type="hidden" name="accion" value="buscar">
            <input type="text" name="texto" class="search-box" placeholder="Buscar por ID, Nombre o Marca..." required>
            <br>
            <button type="submit" class="search-btn">🔍 Buscar</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Marca</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="modelo" items="${listaModelos}">
                <tr>
                    <td>${modelo.idModelo}</td>
                    <td>${modelo.nombre}</td>
                    <td>${modelo.marca.nombre}</td>
                    <td>
                        <c:choose>
                            <c:when test="${modelo.estado == 1}">
                                <span style="color: #1b5e20; font-weight: bold;">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #b71c1c; font-weight: bold;">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a class="btn-editar"
                           href="${pageContext.request.contextPath}/srvModelo?accion=editar&id=${modelo.idModelo}"
                           target="_top">Editar</a>

                        <a class="btn-eliminar"
                           href="${pageContext.request.contextPath}/srvModelo?accion=eliminar&id=${modelo.idModelo}"
                           target="_top">
                            <c:choose>
                                <c:when test="${modelo.estado == 1}">Desactivar</c:when>
                                <c:otherwise>Activar</c:otherwise>
                            </c:choose>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="footer">
        <p>🧵 Administra los modelos y sus marcas asociadas.</p>
    </div>
</body>
</html>
