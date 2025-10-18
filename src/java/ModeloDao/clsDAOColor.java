package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDColor;
import Modelo.clsColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOColor implements CRUDColor {

    private static final String SQL_LISTAR_ACTIVOS = "SELECT idcolor, nombre, estado FROM tbcolor WHERE estado = 1 ORDER BY nombre";
    private static final String SQL_LISTAR_INACTIVOS = "SELECT idcolor, nombre, estado FROM tbcolor WHERE estado = 0 ORDER BY nombre";
    private static final String SQL_OBTENER_POR_ID = "SELECT idcolor, nombre, estado FROM tbcolor WHERE idcolor = ?";
    private static final String SQL_BUSCAR = "SELECT idcolor, nombre, estado FROM tbcolor WHERE CAST(idcolor AS CHAR) LIKE ? OR nombre LIKE ? ORDER BY nombre";
    private static final String SQL_INSERTAR = "INSERT INTO tbcolor(nombre, estado) VALUES(?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE tbcolor SET nombre = ?, estado = ? WHERE idcolor = ?";
    private static final String SQL_CAMBIAR_ESTADO = "UPDATE tbcolor SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idcolor = ?";
    private static final String SQL_EXISTE_NOMBRE = "SELECT COUNT(*) FROM tbcolor WHERE LOWER(nombre) = LOWER(?)";

    @Override
    public List<clsColor> mtdListarActivos() {
        return listarPorEstado(SQL_LISTAR_ACTIVOS);
    }

    @Override
    public List<clsColor> mtdListarInactivos() {
        return listarPorEstado(SQL_LISTAR_INACTIVOS);
    }

    private List<clsColor> listarPorEstado(String sql) {
        List<clsColor> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearColor(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar colores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public clsColor mtdObtenerPorId(int id) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearColor(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId (Color): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<clsColor> mtdBuscar(String texto) {
        List<clsColor> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearColor(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar (Color): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsColor color) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, color.getNombre());
            ps.setInt(2, color.getEstado());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar (Color): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsColor color) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, color.getNombre());
            ps.setInt(2, color.getEstado());
            ps.setInt(3, color.getIdColor());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en mtdEditar (Color): " + e.getMessage());
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
            System.out.println("Error en mtdCambiarEstado (Color): " + e.getMessage());
        }
        return false;
    }

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
            System.out.println("Error en mtdExisteNombre (Color): " + e.getMessage());
        }
        return false;
    }

    private clsColor mapearColor(ResultSet rs) throws SQLException {
        return new clsColor(
                rs.getInt("idcolor"),
                rs.getString("nombre"),
                rs.getInt("estado")
        );
    }
}
