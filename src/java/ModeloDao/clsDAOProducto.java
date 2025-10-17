package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDProducto;
import Modelo.clsProducto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOProducto implements CRUDProducto {

    private static final String SQL_LISTAR = "SELECT idProducto, idcategoria, idmodelo, idcolor, idmarca, Nombre, Cantidad, PrecioUnitario, Estado FROM tbproducto";

    @Override
    public List<clsProducto> mtdListar() {
        List<clsProducto> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsProducto producto = new clsProducto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setIdCategoria(rs.getInt("idcategoria"));
                int idModelo = rs.getInt("idmodelo");
                producto.setIdModelo(rs.wasNull() ? null : idModelo);
                int idColor = rs.getInt("idcolor");
                producto.setIdColor(rs.wasNull() ? null : idColor);
                producto.setIdMarca(rs.getInt("idmarca"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setCantidad(rs.getInt("Cantidad"));
                BigDecimal precio = rs.getBigDecimal("PrecioUnitario");
                producto.setPrecioUnitario(precio);
                producto.setEstado(rs.getInt("Estado"));
                lista.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }
}