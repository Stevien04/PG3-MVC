package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDModelo;
import Modelo.clsModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOModelo implements CRUDModelo {

    private static final String SQL_LISTAR_ACTIVOS = "SELECT idmodelo, idmarca, nombre, estado FROM tbmodelo WHERE estado = 1 ORDER BY nombre";

    @Override
    public List<clsModelo> mtdListarActivos() {
        List<clsModelo> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ACTIVOS);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsModelo modelo = new clsModelo();
                modelo.setIdModelo(rs.getInt("idmodelo"));
                modelo.setIdMarca(rs.getInt("idmarca"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setEstado(rs.getInt("estado"));
                lista.add(modelo);
            }
        } catch (SQLException e) {
            System.out.println("Error en mtdListarActivos (Modelo): " + e.getMessage());
        }
        return lista;
    }
}