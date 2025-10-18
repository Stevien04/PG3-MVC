package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDModelo;
import Modelo.clsModelo;
import Modelo.clsMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOModelo implements CRUDModelo {

    // Consultas SQL actualizadas con JOIN a tbmarca
    private static final String SQL_LISTAR_ACTIVOS =
        "SELECT mo.idmodelo, mo.idmarca, mo.nombre, mo.estado, ma.nombre AS nombre_marca " +
        "FROM tbmodelo mo INNER JOIN tbmarca ma ON mo.idmarca = ma.idmarca " +
        "WHERE mo.estado = 1 ORDER BY mo.nombre";

    private static final String SQL_LISTAR_INACTIVOS =
        "SELECT mo.idmodelo, mo.idmarca, mo.nombre, mo.estado, ma.nombre AS nombre_marca " +
        "FROM tbmodelo mo INNER JOIN tbmarca ma ON mo.idmarca = ma.idmarca " +
        "WHERE mo.estado = 0 ORDER BY mo.nombre";

    private static final String SQL_OBTENER_POR_ID =
        "SELECT mo.idmodelo, mo.idmarca, mo.nombre, mo.estado, ma.nombre AS nombre_marca " +
        "FROM tbmodelo mo INNER JOIN tbmarca ma ON mo.idmarca = ma.idmarca " +
        "WHERE mo.idmodelo = ?";

    private static final String SQL_BUSCAR =
        "SELECT mo.idmodelo, mo.idmarca, mo.nombre, mo.estado, ma.nombre AS nombre_marca " +
        "FROM tbmodelo mo INNER JOIN tbmarca ma ON mo.idmarca = ma.idmarca " +
        "WHERE CAST(mo.idmodelo AS CHAR) LIKE ? OR mo.nombre LIKE ? OR ma.nombre LIKE ? " +
        "ORDER BY mo.nombre";

    private static final String SQL_INSERTAR =
        "INSERT INTO tbmodelo(idmarca, nombre, estado) VALUES(?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
        "UPDATE tbmodelo SET idmarca = ?, nombre = ?, estado = ? WHERE idmodelo = ?";

    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE tbmodelo SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idmodelo = ?";

    private static final String SQL_EXISTE_NOMBRE =
        "SELECT COUNT(*) FROM tbmodelo WHERE LOWER(nombre) = LOWER(?)";

    // ----------------------------------------------------------
    // LISTAR ACTIVOS / INACTIVOS
    // ----------------------------------------------------------
    @Override
    public List<clsModelo> mtdListarActivos() {
        return listarPorEstado(SQL_LISTAR_ACTIVOS);
    }

    @Override
    public List<clsModelo> mtdListarInactivos() {
        return listarPorEstado(SQL_LISTAR_INACTIVOS);
    }

    private List<clsModelo> listarPorEstado(String sql) {
        List<clsModelo> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearModelo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar modelos: " + e.getMessage());
        }
        return lista;
    }

    // ----------------------------------------------------------
    // OBTENER POR ID
    // ----------------------------------------------------------
    @Override
    public clsModelo mtdObtenerPorId(int id) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearModelo(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId (Modelo): " + e.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------
    // BUSCAR
    // ----------------------------------------------------------
    @Override
    public List<clsModelo> mtdBuscar(String texto) {
        List<clsModelo> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            ps.setString(3, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearModelo(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar (Modelo): " + e.getMessage());
        }
        return lista;
    }

    // ----------------------------------------------------------
    // AGREGAR
    // ----------------------------------------------------------
    @Override
    public boolean mtdAgregar(clsModelo modelo) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setInt(1, modelo.getIdMarca());
            ps.setString(2, modelo.getNombre());
            ps.setInt(3, modelo.getEstado());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar (Modelo): " + e.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------
    // EDITAR
    // ----------------------------------------------------------
    @Override
    public boolean mtdEditar(clsModelo modelo) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setInt(1, modelo.getIdMarca());
            ps.setString(2, modelo.getNombre());
            ps.setInt(3, modelo.getEstado());
            ps.setInt(4, modelo.getIdModelo());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdEditar (Modelo): " + e.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------
    // CAMBIAR ESTADO
    // ----------------------------------------------------------
    @Override
    public boolean mtdCambiarEstado(int id) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdCambiarEstado (Modelo): " + e.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------
    // VERIFICAR EXISTENCIA
    // ----------------------------------------------------------
    @Override
    public boolean mtdExisteNombre(String nombre) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {

            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdExisteNombre (Modelo): " + e.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------
    // MAPEO COMPLETO (Modelo + Marca)
    // ----------------------------------------------------------
    private clsModelo mapearModelo(ResultSet rs) throws SQLException {
        clsModelo modelo = new clsModelo();
        modelo.setIdModelo(rs.getInt("idmodelo"));
        modelo.setIdMarca(rs.getInt("idmarca"));
        modelo.setNombre(rs.getString("nombre"));
        modelo.setEstado(rs.getInt("estado"));

        clsMarca marca = new clsMarca();
        marca.setIdMarca(rs.getInt("idmarca"));
        marca.setNombre(rs.getString("nombre_marca"));

        modelo.setMarca(marca);
        return modelo;
    }
}
