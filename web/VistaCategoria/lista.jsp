<%-- 
    Document   : lista
    Created on : 17 oct. 2025, 9:55:53 a. m.
    Author     : Razse
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
 
    </body>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Lista de Categorías</title>
<style>
    body {
        font-family: 'Poppins', sans-serif;
        background: linear-gradient(135deg, #e3f2fd, #bbdefb);
        margin: 0;
        padding: 30px 0;
        color: #1a237e;
    }

    h1 {
        text-align: center;
        color: #0d47a1;
        margin-bottom: 25px;
        letter-spacing: 1px;
    }

    .buttons {
        text-align: center;
        margin-bottom: 15px;
    }

    .btn {
        background-color: #1976d2;
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
        background-color: #0d47a1;
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
        border: 1.5px solid #90caf9;
        border-radius: 8px;
        width: 320px;
        font-size: 1rem;
        outline: none;
        color: #0d47a1;
        transition: 0.3s;
        background-color: #fff;
    }

    .search-box:focus {
        border-color: #1976d2;
        box-shadow: 0 0 6px rgba(25,118,210,0.4);
    }

    .search-btn {
        display: inline-block;
        background-color: #1976d2;
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
        background-color: #0d47a1;
        transform: scale(1.05);
    }

    table {
        margin: auto;
        border-collapse: collapse;
        width: 85%;
        background-color: #ffffff;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    th {
        background-color: #2196f3;
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
        background-color: #e3f2fd;
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
        background-color: #e53935;
        color: white;
        padding: 6px 10px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: 600;
        transition: 0.2s;
    }

    .btn-eliminar:hover {
        background-color: #c62828;
        transform: scale(1.05);
    }

    .footer {
        text-align: center;
        margin-top: 30px;
        color: #1976d2;
        font-size: 0.9rem;
    }
</style>
</head>
<body>
    <h1>Lista de Categorías</h1>

    <div class="buttons">
        <a href="${pageContext.request.contextPath}/srvCategoria?accion=listarActivos" class="btn" target="_top">Activas</a>
        <a href="${pageContext.request.contextPath}/srvCategoria?accion=listarInactivos" class="btn" target="_top">Inactivas</a>
    </div>

    <div class="search-container">
        <form action="${pageContext.request.contextPath}/srvCategoria" method="get" target="_top">
            <input type="hidden" name="accion" value="buscar">
            <input type="text" name="texto" class="search-box" placeholder="Buscar por ID o Nombre..." required>
            <br>
            <button type="submit" class="search-btn">🔍 Buscar</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="categoria" items="${listaCategorias}">
                <tr>
                    <td>${categoria.idCategoria}</td>
                    <td>${categoria.nombre}</td>
                    <td>
                        <c:choose>
                            <c:when test="${categoria.estado == 1}">
                                <span style="color: #1b5e20; font-weight: bold;">Activa</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #b71c1c; font-weight: bold;">Inactiva</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a class="btn-editar"
                           href="${pageContext.request.contextPath}/srvCategoria?accion=editar&id=${categoria.idCategoria}"
                           target="_top">Editar</a>

                        <a class="btn-eliminar"
                           href="${pageContext.request.contextPath}/srvCategoria?accion=eliminar&id=${categoria.idCategoria}"
                           target="_top">
                            <c:choose>
                                <c:when test="${categoria.estado == 1}">Desactivar</c:when>
                                <c:otherwise>Activar</c:otherwise>
                            </c:choose>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="footer">
        <p>📚 Ordenar y clasificar para servir mejor a nuestros clientes.</p>
    </div>
</body>
</html>