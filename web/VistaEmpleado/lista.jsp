<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Lista de Empleados</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: #f8fbff;
                margin: 0;
                padding: 20px;
                color: #1a237e;
            }

            h1 {
                text-align: center;
                color: #0d47a1;
                margin-bottom: 25px;
                letter-spacing: 0.5px;
            }

            .buttons {
                text-align: center;
                margin-bottom: 20px;
            }

            .btn {
                background-color: #1e88e5;
                color: #fff;
                padding: 10px 20px;
                border-radius: 8px;
                text-decoration: none;
                font-weight: 600;
                margin: 5px;
                transition: all 0.2s ease-in-out;
                border: none;
            }

            .btn:hover {
                background-color: #1565c0;
                transform: scale(1.05);
            }

            .search-container {
                text-align: center;
                margin-bottom: 30px;
            }

            .search-box {
                display: inline-block;
                padding: 10px 14px;
                border: 1.5px solid #90caf9;
                border-radius: 8px;
                width: 350px;
                font-size: 1rem;
                outline: none;
                background-color: #fff;
                transition: border-color 0.3s ease, box-shadow 0.3s ease;
            }

            .search-box:focus {
                border-color: #1e88e5;
                box-shadow: 0 0 6px rgba(30,136,229,0.4);
            }

            .search-btn {
                background-color: #1e88e5;
                border: none;
                padding: 9px 25px;
                border-radius: 6px;
                cursor: pointer;
                color: #fff;
                font-size: 15px;
                font-weight: 600;
                margin-left: 10px;
                transition: 0.2s;
            }

            .search-btn:hover {
                background-color: #1565c0;
                transform: scale(1.05);
            }

            /* ===================== TABLA ===================== */
            .table-container {
                max-width: 98%;
                margin: 0 auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                overflow-x: auto;
                padding: 10px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                border-radius: 10px;
                overflow: hidden;
            }

            th {
                background-color: #42a5f5;
                color: #fff;
                text-transform: uppercase;
                font-size: 0.9rem;
                padding: 14px;
                text-align: center;
                letter-spacing: 0.5px;
            }

            td {
                padding: 12px;
                text-align: center;
                font-size: 0.95rem;
                border-bottom: 1px solid #e0e0e0;
                word-wrap: break-word;
            }

            tr:hover {
                background-color: #e3f2fd;
                transition: background-color 0.2s ease;
            }

            /* Ancho de columnas mejor distribuido */
            th:nth-child(1), td:nth-child(1) {
                width: 5%;
            }   /* ID */
            th:nth-child(2), td:nth-child(2) {
                width: 10%;
            }  /* Nombre */
            th:nth-child(3), td:nth-child(3) {
                width: 12%;
            }  /* Apellido */
            th:nth-child(4), td:nth-child(4) {
                width: 15%;
            }  /* Cargo */
            th:nth-child(5), td:nth-child(5) {
                width: 10%;
            }  /* Usuario */
            th:nth-child(6), td:nth-child(6) {
                width: 13%;
            }  /* Tipo documento */
            th:nth-child(7), td:nth-child(7) {
                width: 10%;
            }  /* Nº documento */
            th:nth-child(8), td:nth-child(8) {
                width: 10%;
            }  /* Teléfono */
            th:nth-child(9), td:nth-child(9) {
                width: 7%;
            }   /* Estado */
            th:nth-child(10), td:nth-child(10) {
                width: 8%;
            } /* Acciones */

            /* ===================== BOTONES ===================== */
            .btn-acciones {
                display: flex;
                justify-content: center;
                gap: 12px;
            }

            .btn-editar {
                background-color: #ffca28;
                color: #000;
                padding: 7px 15px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 600;
                transition: 0.2s;
                min-width: 80px;
            }

            .btn-editar:hover {
                background-color: #fdd835;
                transform: scale(1.05);
            }

            .btn-eliminar {
                background-color: #ef5350;
                color: white;
                padding: 7px 15px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 600;
                transition: 0.2s;
                min-width: 90px;
            }

            .btn-eliminar:hover {
                background-color: #d32f2f;
                transform: scale(1.05);
            }

            /* ===================== ESTADOS ===================== */
            .estado-activo {
                color: #1b5e20;
                font-weight: bold;
            }

            .estado-inactivo {
                color: #b71c1c;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <h1>Lista de Empleados</h1>

        <div class="buttons">
            <a href="${pageContext.request.contextPath}/srvEmpleado?accion=listarActivos" class="btn" target="_top">Activos</a>
            <a href="${pageContext.request.contextPath}/srvEmpleado?accion=listarInactivos" class="btn" target="_top">Inactivos</a>
        </div>

        <div class="search-container">
            <form action="${pageContext.request.contextPath}/srvEmpleado" method="get" target="_top">
                <input type="hidden" name="accion" value="buscar">
                <input type="text" name="texto" class="search-box" placeholder="Buscar por ID, nombre, apellido o usuario" required>
                <button type="submit" class="search-btn">🔍 Buscar</button>
            </form>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Cargo</th>
                        <th>Usuario</th>
                        <th>Tipo Documento</th>
                        <th>N° Documento</th>
                        <th>Teléfono</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="empleado" items="${listaEmpleados}">
                        <tr>
                            <td>${empleado.idEmpleado}</td>
                            <td>${empleado.nombre}</td>
                            <td>${empleado.apellido}</td>
                            <td>${empty empleado.cargoNombre ? '—' : empleado.cargoNombre}</td>
                            <td>${empleado.usuario}</td>
                            <td>${empty empleado.tipoDocumentoNombre ? '—' : empleado.tipoDocumentoNombre}</td>
                            <td>${empleado.numeroDocumento}</td>
                            <td>${empleado.telefono}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${empleado.estado == 1}">
                                        <span class="estado-activo">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="estado-inactivo">Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="btn-acciones">
                                <a class="btn-editar"
                                   href="${pageContext.request.contextPath}/srvEmpleado?accion=editar&id=${empleado.idEmpleado}"
                                   target="_top">Editar</a>

                                <a class="btn-eliminar"
                                   href="${pageContext.request.contextPath}/srvEmpleado?accion=eliminar&id=${empleado.idEmpleado}"
                                   target="_top">
                                    <c:choose>
                                        <c:when test="${empleado.estado == 1}">Desactivar</c:when>
                                        <c:otherwise>Activar</c:otherwise>
                                    </c:choose>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
