package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDcargo;
import Modelo.clsCargo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class clsDAOcargo implements CRUDcargo {

  
    @Override
    public List<clsCargo> mtdListarActivos() {
        List<clsCargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcargo WHERE estado = 1";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsCargo c = new clsCargo();
                c.setIdCargo(rs.getInt("idcargo"));
                c.setNombre(rs.getString("nombre"));
                c.setEstado(rs.getInt("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos: " + e.getMessage());
        }
        return lista;
    }

    
    @Override
    public List<clsCargo> mtdListarInactivos() {
        List<clsCargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcargo WHERE estado = 0";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsCargo c = new clsCargo();
                c.setIdCargo(rs.getInt("idcargo"));
                c.setNombre(rs.getString("nombre"));
                c.setEstado(rs.getInt("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarInactivos: " + e.getMessage());
        }
        return lista;
    }


    @Override
    public clsCargo mtdObtenerPorId(int id) {
        String sql = "SELECT * FROM tbcargo WHERE idcargo = ?";
        clsCargo c = null;
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new clsCargo(
                            rs.getInt("idcargo"),
                            rs.getString("nombre"),
                            rs.getInt("estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdObtenerPorId: " + e.getMessage());
        }
        return c;
    }

    @Override
    public List<clsCargo> mtdBuscar(String texto) {
        List<clsCargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbcargo WHERE CAST(idcargo AS CHAR) LIKE ? OR nombre LIKE ?";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clsCargo c = new clsCargo();
                    c.setIdCargo(rs.getInt("idcargo"));
                    c.setNombre(rs.getString("nombre"));
                    c.setEstado(rs.getInt("estado"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdBuscar: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean mtdAgregar(clsCargo cargo) {
        String sql = "INSERT INTO tbcargo(nombre, estado) VALUES(?, ?)";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cargo.getNombre());
            ps.setInt(2, cargo.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdAgregar: " + e.getMessage());
        }
        return false;
    }

   
    @Override
    public boolean mtdEditar(clsCargo cargo) {
        String sql = "UPDATE tbcargo SET nombre = ?, estado = ? WHERE idcargo = ?";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cargo.getNombre());
            ps.setInt(2, cargo.getEstado());
            ps.setInt(3, cargo.getIdCargo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en mtdEditar: " + e.getMessage());
        }
        return false;
    }


    @Override
    public boolean mtdCambiarEstado(int id) {
        String sql = "UPDATE tbcargo SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idcargo = ?";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
        String sql = "SELECT COUNT(*) FROM tbcargo WHERE LOWER(nombre) = LOWER(?)";
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
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
