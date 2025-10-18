<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de Tallas</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #e0f7fa, #b2ebf2);
                margin: 0;
                padding: 30px 0;
                color: #006064;
            }

            h1 {
                text-align: center;
                color: #004d40;
                margin-bottom: 25px;
                letter-spacing: 1px;
            }

            .buttons {
                text-align: center;
                margin-bottom: 15px;
            }

            .btn {
                background-color: #00838f;
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
                background-color: #006064;
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
                border: 1.5px solid #80deea;
                border-radius: 8px;
                width: 320px;
                font-size: 1rem;
                outline: none;
                color: #006064;
                transition: 0.3s;
                background-color: #fff;
            }

            .search-box:focus {
                border-color: #00838f;
                box-shadow: 0 0 6px rgba(0,131,143,0.4);
            }

            .search-btn {
                display: inline-block;
                background-color: #00838f;
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
                background-color: #006064;
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
                background-color: #00acc1;
                color: #fff;
                text-transform: uppercase;
                padding: 12px;
                font-size: 0.9rem;
                letter-spacing: 0.5px;
            }

            td {
                padding: 12px;
                border-bottom: 1px solid #e0f7fa;
                text-align: center;
            }

            tr:hover {
                background-color: #e0f7fa;
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
                color: #00838f;
                font-size: 0.9rem;
            }
        </style>
    </head>
    <body>
        <h1>Lista de Tallas</h1>

        <div class="buttons">
            <a href="${pageContext.request.contextPath}/srvTalla?accion=listarActivos" class="btn" target="_top">Activas</a>
            <a href="${pageContext.request.contextPath}/srvTalla?accion=listarInactivos" class="btn" target="_top">Inactivas</a>
        </div>

        <div class="search-container">
            <form action="${pageContext.request.contextPath}/srvTalla" method="get" target="_top">
                <input type="hidden" name="accion" value="buscar">
                <input type="text" name="texto" class="search-box" placeholder="Buscar por ID, Valor o Tipo..." required>
                <br>
                <button type="submit" class="search-btn">🔍 Buscar</button>
            </form>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tipo de Talla</th>
                    <th>Valor</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="talla" items="${listaTallas}">
                    <tr>
                        <td>${talla.idTalla}</td>
                        <td>${talla.tipoTalla != null ? talla.tipoTalla.nombre : ''}</td>
                        <td>${talla.valor}</td>
                        <td>
                            <c:choose>
                                <c:when test="${talla.estado == 1}">
                                    <span style="color: #1b5e20; font-weight: bold;">Activa</span>
                                </c:when>
                                <c:otherwise>
                                    <span style="color: #b71c1c; font-weight: bold;">Inactiva</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a class="btn-editar"
                               href="${pageContext.request.contextPath}/srvTalla?accion=editar&id=${talla.idTalla}"
                               target="_top">Editar</a>

                            <a class="btn-eliminar"
                               href="${pageContext.request.contextPath}/srvTalla?accion=eliminar&id=${talla.idTalla}"
                               target="_top">
                                <c:choose>
                                    <c:when test="${talla.estado == 1}">Desactivar</c:when>
                                    <c:otherwise>Activar</c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="footer">
            <p>📏 Administra las tallas disponibles para cada tipo y mantén tu catálogo organizado.</p>
        </div>
    </body>
</html>