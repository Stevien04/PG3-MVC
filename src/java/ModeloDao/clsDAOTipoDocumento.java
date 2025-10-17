package ModeloDao;

import Config.clsConexion;
import Modelo.clsTipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOTipoDocumento {

    private static final String SQL_LISTAR = "SELECT idtipodocumento, nombre FROM tbtipodocumento ORDER BY nombre";

    public List<clsTipoDocumento> listarTodos() {
        List<clsTipoDocumento> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsTipoDocumento tipo = new clsTipoDocumento();
                tipo.setIdTipoDocumento(rs.getInt("idtipodocumento"));
                tipo.setNombre(rs.getString("nombre"));
                lista.add(tipo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de documento: " + e.getMessage());
        }
        return lista;
    }
}