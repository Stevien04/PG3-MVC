package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDProductoTalla;
import Modelo.clsProductoTalla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOProductoTalla implements CRUDProductoTalla {

    private static final String SQL_LISTAR = "SELECT idProductoTalla, idProducto, idTalla, Cantidad, estado FROM producto_talla";

    @Override
    public List<clsProductoTalla> mtdListar() {
        List<clsProductoTalla> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsProductoTalla productoTalla = new clsProductoTalla();
                productoTalla.setIdProductoTalla(rs.getInt("idProductoTalla"));
                productoTalla.setIdProducto(rs.getInt("idProducto"));
                productoTalla.setIdTalla(rs.getInt("idTalla"));
                productoTalla.setCantidad(rs.getInt("Cantidad"));
                int estado = rs.getInt("estado");
                productoTalla.setEstado(rs.wasNull() ? null : estado);
                lista.add(productoTalla);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar producto_talla: " + e.getMessage());
        }
        return lista;
    }
}