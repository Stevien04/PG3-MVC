<div class="tabla-wrapper">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Producto</th>
                <th>Tipo de talla</th>
                <th>Talla</th>
                <th>Cantidad</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listaProductoTallas}">
                    <tr>
                        <td colspan="7" style="text-align:center; padding: 18px; font-weight: 600; color: #6c7a91;">
                            No hay combinaciones registradas.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${listaProductoTallas}">
                        <tr>
                            <td>${item.idProductoTalla}</td>
                            <td>
                                <div style="display:flex; flex-direction:column; gap:4px;">
                                    <span style="font-weight:600;">${item.nombreProducto}</span>
                                    <small style="color:#486581;">Stock total: ${item.cantidadProducto}</small>
                                </div>
                            </td>
                            <td>${item.nombreTipoTalla}</td>
                            <td>${item.valorTalla}</td>
                            <td>${item.cantidad}</td>
                            <td>
                                <span class="estado ${item.estado == 1 ? 'activo' : 'inactivo'}">
                                    <c:choose>
                                        <c:when test="${item.estado == 1}">Activo</c:when>
                                        <c:otherwise>Inactivo</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>
                                <div class="acciones">
                                    <a class="btn-tabla btn-editar" href="${pageContext.request.contextPath}/srvProductoTalla?accion=editar&id=${item.idProductoTalla}">
                                        Editar
                                    </a>
                                    <a class="btn-tabla btn-estado ${item.estado == 1 ? '' : 'activar'}" href="${pageContext.request.contextPath}/srvProductoTalla?accion=cambiarEstado&id=${item.idProductoTalla}"
                                       onclick="return confirm('¿Desea ${item.estado == 1 ? 'desactivar' : 'activar'} esta combinación?');">
                                        <c:choose>
                                            <c:when test="${item.estado == 1}">Desactivar</c:when>
                                            <c:otherwise>Activar</c:otherwise>
                                        </c:choose>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<c:if test="${not empty resumenProductoTalla}">
    <div class="resumen">
        <h2>Resumen por producto</h2>
        <c:forEach var="resumen" items="${resumenProductoTalla}">
            <div class="resumen-item">
                <span>${resumen.nombreProducto}</span>
                <span>
                    <strong>${resumen.sumaTallas}</strong>
                    / ${resumen.cantidadProducto}
                    <c:if test="${resumen.diferencia != 0}">
                        <span class="desbalance">(Dif: ${resumen.diferencia})</span>
                    </c:if>
                </span>
            </div>
        </c:forEach>
    </div>
</c:if>