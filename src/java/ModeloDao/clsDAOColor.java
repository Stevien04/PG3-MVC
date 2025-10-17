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

    @Override
    public List<clsColor> mtdListarActivos() {
        List<clsColor> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ACTIVOS);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsColor color = new clsColor();
                color.setIdColor(rs.getInt("idcolor"));
                color.setNombre(rs.getString("nombre"));
                color.setEstado(rs.getInt("estado"));
                lista.add(color);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos (Color): " + e.getMessage());
        }
        return lista;
    }
}