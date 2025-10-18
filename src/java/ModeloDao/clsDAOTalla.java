/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDTalla;
import Modelo.clsTalla;
import Modelo.clsTipoTalla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOTalla implements CRUDTalla {

    private static final String BASE_SELECT =
            "SELECT ta.idtalla, ta.idtipotalla, ta.valor, ta.estado, tt.nombre AS nombre_tipo "
            + "FROM tbtalla ta INNER JOIN tbtipotalla tt ON ta.idtipotalla = tt.idtipotalla ";

    private static final String SQL_LISTAR_ACTIVOS =
            BASE_SELECT + "WHERE ta.estado = 1 ORDER BY tt.nombre, ta.valor";

    private static final String SQL_LISTAR_INACTIVOS =
            BASE_SELECT + "WHERE ta.estado = 0 ORDER BY tt.nombre, ta.valor";

    private static final String SQL_OBTENER_POR_ID =
            BASE_SELECT + "WHERE ta.idtalla = ?";

    private static final String SQL_BUSCAR =
            BASE_SELECT
            + "WHERE CAST(ta.idtalla AS CHAR) LIKE ? "
            + "OR ta.valor LIKE ? "
            + "OR tt.nombre LIKE ? "
            + "ORDER BY tt.nombre, ta.valor";

    private static final String SQL_INSERTAR =
            "INSERT INTO tbtalla(idtipotalla, valor, estado) VALUES(?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE tbtalla SET idtipotalla = ?, valor = ?, estado = ? WHERE idtalla = ?";

    private static final String SQL_CAMBIAR_ESTADO =
            "UPDATE tbtalla SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idtalla = ?";

    @Override
    public List<clsTalla> mtdListarActivos() {
        return listarPorEstado(SQL_LISTAR_ACTIVOS);
    }

    @Override
    public List<clsTalla> mtdListarInactivos() {
        return listarPorEstado(SQL_LISTAR_INACTIVOS);
    }

    private List<clsTalla> listarPorEstado(String sql) {
        List<clsTalla> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTalla(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tallas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public clsTalla mtdObtenerPorId(int id) {
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTalla(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId (Talla): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<clsTalla> mtdBuscar(String texto) {
        List<clsTalla> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {

            String filtro = "%" + texto + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTalla(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar (Talla): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsTalla talla) {
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setInt(1, talla.getIdTipoTalla());
            ps.setString(2, talla.getValor());
            ps.setInt(3, talla.getEstado());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar (Talla): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsTalla talla) {
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setInt(1, talla.getIdTipoTalla());
            ps.setString(2, talla.getValor());
            ps.setInt(3, talla.getEstado());
            ps.setInt(4, talla.getIdTalla());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdEditar (Talla): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdCambiarEstado (Talla): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteValor(int idTipoTalla, String valor, Integer idExcluir) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM tbtalla WHERE idtipotalla = ? AND LOWER(valor) = LOWER(?)");
        if (idExcluir != null) {
            sql.append(" AND idtalla <> ?");
        }

        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, idTipoTalla);
            ps.setString(2, valor.trim());
            if (idExcluir != null) {
                ps.setInt(3, idExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdExisteValor (Talla): " + e.getMessage());
        }
        return false;
    }

    private clsTalla mapearTalla(ResultSet rs) throws SQLException {
        clsTalla talla = new clsTalla();
        talla.setIdTalla(rs.getInt("idtalla"));
        talla.setIdTipoTalla(rs.getInt("idtipotalla"));
        talla.setValor(rs.getString("valor"));
        talla.setEstado(rs.getInt("estado"));

        clsTipoTalla tipo = new clsTipoTalla();
        tipo.setIdTipoTalla(rs.getInt("idtipotalla"));
        tipo.setNombre(rs.getString("nombre_tipo"));

        talla.setTipoTalla(tipo);
        return talla;
    }
}