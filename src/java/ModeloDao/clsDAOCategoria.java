package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDCategoria;
import Modelo.clsCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOCategoria implements CRUDCategoria {

    @Override
    public List<clsCategoria> mtdListarActivos() {
        List<clsCategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcategoria WHERE Estado = 1";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsCategoria categoria = new clsCategoria();
                categoria.setIdCategoria(rs.getInt("idcategoria"));
                categoria.setNombre(rs.getString("Categoria"));
                categoria.setEstado(rs.getInt("Estado"));
                lista.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<clsCategoria> mtdListarInactivos() {
        List<clsCategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcategoria WHERE Estado = 0";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsCategoria categoria = new clsCategoria();
                categoria.setIdCategoria(rs.getInt("idcategoria"));
                categoria.setNombre(rs.getString("Categoria"));
                categoria.setEstado(rs.getInt("Estado"));
                lista.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarInactivos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public clsCategoria mtdObtenerPorId(int id) {
        String sql = "SELECT * FROM tbcategoria WHERE idcategoria = ?";
        clsCategoria categoria = null;
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria = new clsCategoria(
                            rs.getInt("idcategoria"),
                            rs.getString("Categoria"),
                            rs.getInt("Estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId: " + e.getMessage());
        }
        return categoria;
    }

    @Override
    public List<clsCategoria> mtdBuscar(String texto) {
        List<clsCategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcategoria WHERE CAST(idcategoria AS CHAR) LIKE ? OR Categoria LIKE ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clsCategoria categoria = new clsCategoria();
                    categoria.setIdCategoria(rs.getInt("idcategoria"));
                    categoria.setNombre(rs.getString("Categoria"));
                    categoria.setEstado(rs.getInt("Estado"));
                    lista.add(categoria);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsCategoria categoria) {
        String sql = "INSERT INTO tbcategoria(Categoria, Estado) VALUES(?, ?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsCategoria categoria) {
        String sql = "UPDATE tbcategoria SET Categoria = ?, Estado = ? WHERE idcategoria = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getEstado());
            ps.setInt(3, categoria.getIdCategoria());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdEditar: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        String sql = "UPDATE tbcategoria SET Estado = CASE WHEN Estado = 1 THEN 0 ELSE 1 END WHERE idcategoria = ?";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdCambiarEstado: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM tbcategoria WHERE LOWER(Categoria) = LOWER(?)";
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdExisteNombre: " + e.getMessage());
        }
        return false;
    }
}
