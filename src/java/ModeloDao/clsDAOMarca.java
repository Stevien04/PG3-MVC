package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDMarca;
import Modelo.clsMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOMarca implements CRUDMarca {

    @Override
    public List<clsMarca> mtdListarActivos() {
        List<clsMarca> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbmarca WHERE estado = 1";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idmarca"));
                marca.setNombre(rs.getString("nombre"));
                marca.setEstado(rs.getInt("estado"));
                lista.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos (Marca): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<clsMarca> mtdListarInactivos() {
        List<clsMarca> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbmarca WHERE estado = 0";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idmarca"));
                marca.setNombre(rs.getString("nombre"));
                marca.setEstado(rs.getInt("estado"));
                lista.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarInactivos (Marca): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public clsMarca mtdObtenerPorId(int id) {
        String sql = "SELECT * FROM tbmarca WHERE idmarca = ?";
        clsMarca marca = null;
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    marca = new clsMarca(
                            rs.getInt("idmarca"),
                            rs.getString("nombre"),
                            rs.getInt("estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId (Marca): " + e.getMessage());
        }
        return marca;
    }

    @Override
    public List<clsMarca> mtdBuscar(String texto) {
        List<clsMarca> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbmarca WHERE CAST(idmarca AS CHAR) LIKE ? OR nombre LIKE ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clsMarca marca = new clsMarca();
                    marca.setIdMarca(rs.getInt("idmarca"));
                    marca.setNombre(rs.getString("nombre"));
                    marca.setEstado(rs.getInt("estado"));
                    lista.add(marca);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar (Marca): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsMarca marca) {
        String sql = "INSERT INTO tbmarca(nombre, estado) VALUES(?, ?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, marca.getNombre());
            ps.setInt(2, marca.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar (Marca): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsMarca marca) {
        String sql = "UPDATE tbmarca SET nombre = ?, estado = ? WHERE idmarca = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, marca.getNombre());
            ps.setInt(2, marca.getEstado());
            ps.setInt(3, marca.getIdMarca());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdEditar (Marca): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        String sql = "UPDATE tbmarca SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idmarca = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdCambiarEstado (Marca): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM tbmarca WHERE LOWER(nombre) = LOWER(?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdExisteNombre (Marca): " + e.getMessage());
        }
        return false;
    }
}