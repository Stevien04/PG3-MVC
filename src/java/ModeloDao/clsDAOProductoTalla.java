package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDProductoTalla;
import Modelo.clsProductoTalla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOProductoTalla implements CRUDProductoTalla {
    private static final String BASE_SELECT = "SELECT pt.idProductoTalla, pt.idProducto, pt.idTalla, pt.Cantidad, pt.estado, "
            + "p.Nombre AS nombreProducto, p.Cantidad AS cantidadProducto, t.valor AS valorTalla, tt.nombre AS nombreTipoTalla "
            + "FROM producto_talla pt "
            + "INNER JOIN tbproducto p ON pt.idProducto = p.idProducto "
            + "INNER JOIN tbtalla t ON pt.idTalla = t.idtalla "
            + "INNER JOIN tbtipotalla tt ON t.idtipotalla = tt.idtipotalla ";

    private static final String SQL_LISTAR = BASE_SELECT + "ORDER BY p.Nombre, tt.nombre, t.valor";
    private static final String SQL_OBTENER_POR_ID = BASE_SELECT + "WHERE pt.idProductoTalla = ?";
    private static final String SQL_INSERT = "INSERT INTO producto_talla(idProducto, idTalla, Cantidad, estado) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE producto_talla SET idProducto = ?, idTalla = ?, Cantidad = ?, estado = ? WHERE idProductoTalla = ?";
    private static final String SQL_CAMBIAR_ESTADO = "UPDATE producto_talla SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idProductoTalla = ?";
    private static final String SQL_EXISTE_COMBINACION = "SELECT COUNT(*) FROM producto_talla WHERE idProducto = ? AND idTalla = ?";
    private static final String SQL_EXISTE_COMBINACION_EXCLUYENDO = SQL_EXISTE_COMBINACION + " AND idProductoTalla <> ?";
    private static final String SQL_SUMA_TALLAS_ACTIVAS = "SELECT COALESCE(SUM(Cantidad), 0) FROM producto_talla WHERE idProducto = ? AND (estado IS NULL OR estado = 1)";
    private static final String SQL_ACTUALIZAR_CANTIDAD_PRODUCTO = "UPDATE tbproducto SET Cantidad = ? WHERE idProducto = ?";

    @Override
    public List<clsProductoTalla> mtdListar() {
        List<clsProductoTalla> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProductoTalla(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar producto_talla: " + e.getMessage());
        }
        return lista;
    }
    @Override
    public clsProductoTalla mtdObtenerPorId(int id) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProductoTalla(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto_talla por id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean mtdAgregar(clsProductoTalla productoTalla) {
        Connection con = null;
        try {
            con = clsConexion.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
                ps.setInt(1, productoTalla.getIdProducto());
                ps.setInt(2, productoTalla.getIdTalla());
                ps.setInt(3, productoTalla.getCantidad());
                if (productoTalla.getEstado() == null) {
                    ps.setNull(4, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(4, productoTalla.getEstado());
                }
                ps.executeUpdate();
            }

            recalcularCantidadProducto(con, productoTalla.getIdProducto());
            con.commit();
            return true;
        } catch (SQLException e) {
            rollbackSilencioso(con);
            System.out.println("Error al agregar producto_talla: " + e.getMessage());
        } finally {
            restaurarAutoCommit(con);
            cerrarConexion(con);
        }
        return false;
    }

    @Override
    public boolean mtdActualizar(clsProductoTalla productoTalla, int idProductoAnterior) {
        Connection con = null;
        try {
            con = clsConexion.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
                ps.setInt(1, productoTalla.getIdProducto());
                ps.setInt(2, productoTalla.getIdTalla());
                ps.setInt(3, productoTalla.getCantidad());
                if (productoTalla.getEstado() == null) {
                    ps.setNull(4, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(4, productoTalla.getEstado());
                }
                ps.setInt(5, productoTalla.getIdProductoTalla());
                ps.executeUpdate();
            }

            recalcularCantidadProducto(con, productoTalla.getIdProducto());
            if (idProductoAnterior != productoTalla.getIdProducto()) {
                recalcularCantidadProducto(con, idProductoAnterior);
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            rollbackSilencioso(con);
            System.out.println("Error al actualizar producto_talla: " + e.getMessage());
        } finally {
            restaurarAutoCommit(con);
            cerrarConexion(con);
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int idProductoTalla) {
        Connection con = null;
        try {
            con = clsConexion.getConnection();
            con.setAutoCommit(false);

            Integer idProducto = obtenerIdProducto(con, idProductoTalla);
            if (idProducto == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
                ps.setInt(1, idProductoTalla);
                ps.executeUpdate();
            }

            recalcularCantidadProducto(con, idProducto);
            con.commit();
            return true;
        } catch (SQLException e) {
            rollbackSilencioso(con);
            System.out.println("Error al cambiar estado de producto_talla: " + e.getMessage());
        } finally {
            restaurarAutoCommit(con);
            cerrarConexion(con);
        }
        return false;
    }

    @Override
    public boolean mtdExisteCombinacion(int idProducto, int idTalla, Integer idExcluir) {
        String sql = idExcluir == null ? SQL_EXISTE_COMBINACION : SQL_EXISTE_COMBINACION_EXCLUYENDO;
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idTalla);
            if (idExcluir != null) {
                ps.setInt(3, idExcluir);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar combinación producto-talla: " + e.getMessage());
        }
        return false;
    }

    private clsProductoTalla mapearProductoTalla(ResultSet rs) throws SQLException {
        clsProductoTalla productoTalla = new clsProductoTalla();
        productoTalla.setIdProductoTalla(rs.getInt("idProductoTalla"));
        productoTalla.setIdProducto(rs.getInt("idProducto"));
        productoTalla.setIdTalla(rs.getInt("idTalla"));
        productoTalla.setCantidad(rs.getInt("Cantidad"));
        int estado = rs.getInt("estado");
        productoTalla.setEstado(rs.wasNull() ? null : estado);
        productoTalla.setNombreProducto(rs.getString("nombreProducto"));
        productoTalla.setValorTalla(rs.getString("valorTalla"));
        productoTalla.setNombreTipoTalla(rs.getString("nombreTipoTalla"));
        Object cantidadProductoObj = rs.getObject("cantidadProducto");
        if (cantidadProductoObj != null) {
            productoTalla.setCantidadProducto(((Number) cantidadProductoObj).intValue());
        } else {
            productoTalla.setCantidadProducto(null);
        }
        return productoTalla;
    }

    private void recalcularCantidadProducto(Connection con, int idProducto) throws SQLException {
        int total;
        try (PreparedStatement ps = con.prepareStatement(SQL_SUMA_TALLAS_ACTIVAS)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                total = rs.next() ? rs.getInt(1) : 0;
            }
        }

        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_CANTIDAD_PRODUCTO)) {
            ps.setInt(1, total);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        }
    }

    private Integer obtenerIdProducto(Connection con, int idProductoTalla) throws SQLException {
        String sql = "SELECT idProducto FROM producto_talla WHERE idProductoTalla = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProductoTalla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    private void rollbackSilencioso(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                System.out.println("Error en rollback producto_talla: " + e.getMessage());
            }
        }
    }

    private void restaurarAutoCommit(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al restaurar auto-commit producto_talla: " + e.getMessage());
            }
        }
    }

    private void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión producto_talla: " + e.getMessage());
            }
        }
    }
}
            
