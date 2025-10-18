package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDTipoTalla;
import Modelo.clsTipoTalla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOTipoTalla implements CRUDTipoTalla {

    @Override
    public List<clsTipoTalla> mtdListarActivos() {
        List<clsTipoTalla> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbtipotalla WHERE estado = 1";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsTipoTalla tipoTalla = new clsTipoTalla();
                tipoTalla.setIdTipoTalla(rs.getInt("idtipotalla"));
                tipoTalla.setNombre(rs.getString("nombre"));
                tipoTalla.setEstado(rs.getInt("estado"));
                lista.add(tipoTalla);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos TipoTalla: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<clsTipoTalla> mtdListarInactivos() {
        List<clsTipoTalla> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbtipotalla WHERE estado = 0";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsTipoTalla tipoTalla = new clsTipoTalla();
                tipoTalla.setIdTipoTalla(rs.getInt("idtipotalla"));
                tipoTalla.setNombre(rs.getString("nombre"));
                tipoTalla.setEstado(rs.getInt("estado"));
                lista.add(tipoTalla);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarInactivos TipoTalla: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public clsTipoTalla mtdObtenerPorId(int id) {
        String sql = "SELECT * FROM tbtipotalla WHERE idtipotalla = ?";
        clsTipoTalla tipoTalla = null;
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tipoTalla = new clsTipoTalla(
                            rs.getInt("idtipotalla"),
                            rs.getString("nombre"),
                            rs.getInt("estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId TipoTalla: " + e.getMessage());
        }
        return tipoTalla;
    }

    @Override
    public List<clsTipoTalla> mtdBuscar(String texto) {
        List<clsTipoTalla> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbtipotalla WHERE CAST(idtipotalla AS CHAR) LIKE ? OR nombre LIKE ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clsTipoTalla tipoTalla = new clsTipoTalla();
                    tipoTalla.setIdTipoTalla(rs.getInt("idtipotalla"));
                    tipoTalla.setNombre(rs.getString("nombre"));
                    tipoTalla.setEstado(rs.getInt("estado"));
                    lista.add(tipoTalla);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar TipoTalla: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsTipoTalla tipoTalla) {
        String sql = "INSERT INTO tbtipotalla(nombre, estado) VALUES(?, ?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipoTalla.getNombre());
            ps.setInt(2, tipoTalla.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar TipoTalla: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsTipoTalla tipoTalla) {
        String sql = "UPDATE tbtipotalla SET nombre = ?, estado = ? WHERE idtipotalla = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipoTalla.getNombre());
            ps.setInt(2, tipoTalla.getEstado());
            ps.setInt(3, tipoTalla.getIdTipoTalla());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdEditar TipoTalla: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        String sql = "UPDATE tbtipotalla SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idtipotalla = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdCambiarEstado TipoTalla: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM tbtipotalla WHERE LOWER(nombre) = LOWER(?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdExisteNombre TipoTalla: " + e.getMessage());
        }
        return false;
    }
}