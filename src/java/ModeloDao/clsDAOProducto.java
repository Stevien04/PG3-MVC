package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDProducto;
import Modelo.clsProducto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import Modelo.clsItemCarrito;

public class clsDAOProducto implements CRUDProducto {

    private static final String BASE_SELECT
            = "SELECT p.idProducto, p.idcategoria, p.idmodelo, p.idcolor, p.idmarca, p.Nombre, p.Cantidad, p.PrecioUnitario, p.Estado, p.Foto, "
            + "c.Categoria AS categoriaNombre, ma.nombre AS marcaNombre, m.nombre AS modeloNombre, co.nombre AS colorNombre "
            + "FROM tbproducto p "
            + "LEFT JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
            + "LEFT JOIN tbmarca ma ON p.idmarca = ma.idmarca "
            + "LEFT JOIN tbmodelo m ON p.idmodelo = m.idmodelo "
            + "LEFT JOIN tbcolor co ON p.idcolor = co.idcolor ";

    private static final String SQL_LISTAR_ACTIVOS = BASE_SELECT + "WHERE p.Estado = 1 ORDER BY p.Nombre";
    private static final String SQL_LISTAR_INACTIVOS = BASE_SELECT + "WHERE p.Estado = 0 ORDER BY p.Nombre";
    private static final String SQL_OBTENER_POR_ID = BASE_SELECT + "WHERE p.idProducto = ?";
    private static final String SQL_BUSCAR = BASE_SELECT
            + "WHERE CAST(p.idProducto AS CHAR) LIKE ? OR LOWER(p.Nombre) LIKE LOWER(?) OR LOWER(ma.nombre) LIKE LOWER(?) OR LOWER(c.Categoria) LIKE LOWER(?) "
            + "ORDER BY p.Nombre";
    private static final String SQL_INSERT
            = "INSERT INTO tbproducto(idcategoria, idmodelo, idcolor, idmarca, Nombre, Cantidad, PrecioUnitario, Estado, Foto) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE
            = "UPDATE tbproducto SET idcategoria=?, idmodelo=?, idcolor=?, idmarca=?, Nombre=?, Cantidad=?, PrecioUnitario=?, Estado=?, Foto=? WHERE idProducto=?";
    private static final String SQL_CAMBIAR_ESTADO
            = "UPDATE tbproducto SET Estado = CASE WHEN Estado = 1 THEN 0 ELSE 1 END WHERE idProducto = ?";
    private static final String SQL_EXISTE_NOMBRE
            = "SELECT COUNT(*) FROM tbproducto WHERE LOWER(Nombre) = LOWER(?)";
    private static final String SQL_DESCONTAR_STOCK
            = "UPDATE tbproducto SET Cantidad = Cantidad - ? WHERE idProducto = ? AND Cantidad >= ?";

    @Override
    public List<clsProducto> mtdListarActivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_ACTIVOS, ps -> {
        });
    }

    @Override
    public List<clsProducto> mtdListarInactivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_INACTIVOS, ps -> {
        });
    }

    @Override
    public clsProducto mtdObtenerPorId(int id) {
        List<clsProducto> lista = ejecutarConsultaMultiple(SQL_OBTENER_POR_ID, ps -> ps.setInt(1, id));
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<clsProducto> mtdBuscar(String texto) {
        String parametro = "%" + texto + "%";
        return ejecutarConsultaMultiple(SQL_BUSCAR, ps -> {
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            ps.setString(3, parametro);
            ps.setString(4, parametro);
        });
    }

    @Override
    public boolean mtdAgregar(clsProducto producto) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, producto.getIdCategoria());
            setNullableInteger(ps, 2, producto.getIdModelo());
            setNullableInteger(ps, 3, producto.getIdColor());
            ps.setInt(4, producto.getIdMarca());
            ps.setString(5, producto.getNombre());
            ps.setInt(6, producto.getCantidad());
            ps.setBigDecimal(7, producto.getPrecioUnitario() != null ? producto.getPrecioUnitario() : BigDecimal.ZERO);
            ps.setInt(8, producto.getEstado());
            setNullableBytes(ps, 9, producto.getFoto());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsProducto producto) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, producto.getIdCategoria());
            setNullableInteger(ps, 2, producto.getIdModelo());
            setNullableInteger(ps, 3, producto.getIdColor());
            ps.setInt(4, producto.getIdMarca());
            ps.setString(5, producto.getNombre());
            ps.setInt(6, producto.getCantidad());
            ps.setBigDecimal(7, producto.getPrecioUnitario() != null ? producto.getPrecioUnitario() : BigDecimal.ZERO);
            ps.setInt(8, producto.getEstado());
            setNullableBytes(ps, 9, producto.getFoto());
            ps.setInt(10, producto.getIdProducto());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al editar producto: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del producto: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteNombre(String nombre) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar nombre de producto: " + e.getMessage());
        }
        return false;
    }

    public boolean mtdProcesarCompra(List<clsItemCarrito> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = clsConexion.getConnection();
            if (con == null) {
                return false;
            }
            con.setAutoCommit(false);
            ps = con.prepareStatement(SQL_DESCONTAR_STOCK);

            for (clsItemCarrito item : items) {
                if (item == null || item.getCantidad() <= 0) {
                    con.rollback();
                    return false;
                }

                ps.setInt(1, item.getCantidad());
                ps.setInt(2, item.getIdProducto());
                ps.setInt(3, item.getCantidad());
                int filasActualizadas = ps.executeUpdate();

                if (filasActualizadas == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al revertir la transacción de compra: " + ex.getMessage());
                }
            }
            System.out.println("Error al procesar compra: " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar el PreparedStatement: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error al restaurar el autoCommit: " + e.getMessage());
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public boolean mtdExisteNombreEnOtro(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM tbproducto WHERE LOWER(Nombre) = LOWER(?) AND idProducto <> ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar nombre de producto (excluyendo actual): " + e.getMessage());
        }
        return false;
    }

    private interface StatementConsumer {

        void accept(PreparedStatement ps) throws SQLException;
    }

    private List<clsProducto> ejecutarConsultaMultiple(String sql, StatementConsumer consumer) {
        List<clsProducto> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta de productos: " + e.getMessage());
        }
        return lista;
    }

    private clsProducto mapearProducto(ResultSet rs) throws SQLException {
        clsProducto producto = new clsProducto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setIdCategoria(rs.getInt("idcategoria"));
        Object idModeloObj = rs.getObject("idmodelo");
        producto.setIdModelo(idModeloObj != null ? ((Number) idModeloObj).intValue() : null);
        Object idColorObj = rs.getObject("idcolor");
        producto.setIdColor(idColorObj != null ? ((Number) idColorObj).intValue() : null);
        producto.setIdMarca(rs.getInt("idmarca"));
        producto.setNombre(rs.getString("Nombre"));
        producto.setCantidad(rs.getInt("Cantidad"));
        producto.setPrecioUnitario(rs.getBigDecimal("PrecioUnitario"));
        producto.setEstado(rs.getInt("Estado"));
        producto.setFoto(rs.getBytes("Foto"));
        producto.setNombreCategoria(rs.getString("categoriaNombre"));
        producto.setNombreMarca(rs.getString("marcaNombre"));
        producto.setNombreModelo(rs.getString("modeloNombre"));
        producto.setNombreColor(rs.getString("colorNombre"));
        return producto;
    }

    private void setNullableInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value != null && value > 0) {
            ps.setInt(index, value);
        } else {
            ps.setNull(index, Types.INTEGER);
        }
    }

    private void setNullableBytes(PreparedStatement ps, int index, byte[] data) throws SQLException {
        if (data != null && data.length > 0) {
            ps.setBytes(index, data);
        } else {
            ps.setNull(index, Types.BLOB);
        }
    }
}
