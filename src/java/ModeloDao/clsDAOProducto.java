package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDProducto;
import Modelo.clsProducto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class clsDAOProducto implements CRUDProducto {

    // ------------------------------------------------------------------------
    // CONSULTAS SQL
    // ------------------------------------------------------------------------
    private static final String SQL_LISTAR_POR_ESTADO =
        "SELECT p.*, c.nombre AS nombreCategoria, m.nombre AS nombreMarca, "
      + "mo.nombre AS nombreModelo, co.nombre AS nombreColor "
      + "FROM tbproducto p "
      + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
      + "INNER JOIN tbmarca m ON p.idmarca = m.idmarca "
      + "LEFT JOIN tbmodelo mo ON p.idmodelo = mo.idmodelo "
      + "LEFT JOIN tbcolor co ON p.idcolor = co.idcolor "
      + "WHERE p.estado = ?";

    private static final String SQL_BUSCAR =
        "SELECT p.*, c.nombre AS nombreCategoria, m.nombre AS nombreMarca, "
      + "mo.nombre AS nombreModelo, co.nombre AS nombreColor "
      + "FROM tbproducto p "
      + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
      + "INNER JOIN tbmarca m ON p.idmarca = m.idmarca "
      + "LEFT JOIN tbmodelo mo ON p.idmodelo = mo.idmodelo "
      + "LEFT JOIN tbcolor co ON p.idcolor = co.idcolor "
      + "WHERE p.nombre LIKE ? OR m.nombre LIKE ? OR c.nombre LIKE ?";

    private static final String SQL_OBTENER_POR_ID =
        "SELECT p.*, c.nombre AS nombreCategoria, m.nombre AS nombreMarca, "
      + "mo.nombre AS nombreModelo, co.nombre AS nombreColor "
      + "FROM tbproducto p "
      + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
      + "INNER JOIN tbmarca m ON p.idmarca = m.idmarca "
      + "LEFT JOIN tbmodelo mo ON p.idmodelo = mo.idmodelo "
      + "LEFT JOIN tbcolor co ON p.idcolor = co.idcolor "
      + "WHERE p.idproducto = ?";

    private static final String SQL_INSERTAR =
         "INSERT INTO tbproducto (idcategoria, idmodelo, idcolor, idmarca, nombre, cantidad, preciounitario, estado, foto) "
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
        "UPDATE tbproducto SET idcategoria=?, idmodelo=?, idcolor=?, idmarca=?, nombre=?, cantidad=?, preciounitario=?, estado=?, foto=? "
      + "WHERE idproducto=?";

    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE tbproducto SET estado = CASE WHEN estado = 1 THEN 0 ELSE 1 END WHERE idproducto=?";

    // ------------------------------------------------------------------------
    // IMPLEMENTACIÓN DE INTERFAZ
    // ------------------------------------------------------------------------

    @Override
    public List<clsProducto> mtdListarPorEstado(int estado) {
        List<clsProducto> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_ESTADO)) {

            ps.setInt(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirProductoDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos por estado: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public clsProducto mtdObtenerPorId(int idProducto) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirProductoDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean mtdAgregar(clsProducto producto) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setInt(1, producto.getIdCategoria());
            asignarEnteroNulo(ps, 2, producto.getIdModelo());
            asignarEnteroNulo(ps, 3, producto.getIdColor());
            ps.setInt(4, producto.getIdMarca());
            ps.setString(5, producto.getNombre());
            ps.setInt(6, producto.getCantidad());
            ps.setBigDecimal(7, producto.getPrecioUnitario());
            ps.setInt(8, producto.getEstado());
            asignarBytesNulos(ps, 9, producto.getFoto());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsProducto producto) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setInt(1, producto.getIdCategoria());
            asignarEnteroNulo(ps, 2, producto.getIdModelo());
            asignarEnteroNulo(ps, 3, producto.getIdColor());
            ps.setInt(4, producto.getIdMarca());
            ps.setString(5, producto.getNombre());
            ps.setInt(6, producto.getCantidad());
            ps.setBigDecimal(7, producto.getPrecioUnitario());
            ps.setInt(8, producto.getEstado());
            asignarBytesNulos(ps, 9, producto.getFoto());
            ps.setInt(10, producto.getIdProducto());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int idProducto) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<clsProducto> mtdBuscar(String texto) {
        List<clsProducto> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {

            String filtro = "%" + texto + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirProductoDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar productos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // ------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ------------------------------------------------------------------------

    private clsProducto construirProductoDesdeResultSet(ResultSet rs) throws SQLException {
        clsProducto producto = new clsProducto();
        producto.setIdProducto(rs.getInt("idproducto"));
        producto.setIdCategoria(rs.getInt("idcategoria"));

        int idModelo = rs.getInt("idmodelo");
        producto.setIdModelo(rs.wasNull() ? null : idModelo);

        int idColor = rs.getInt("idcolor");
        producto.setIdColor(rs.wasNull() ? null : idColor);

        producto.setIdMarca(rs.getInt("idmarca"));
        producto.setNombre(rs.getString("nombre"));
        producto.setCantidad(rs.getInt("cantidad"));
        producto.setPrecioUnitario(rs.getBigDecimal("preciounitario"));
        producto.setEstado(rs.getInt("estado"));
        producto.setFoto(rs.getBytes("foto"));

        producto.setNombreCategoria(rs.getString("nombreCategoria"));
        producto.setNombreMarca(rs.getString("nombreMarca"));
        producto.setNombreModelo(rs.getString("nombreModelo"));
        producto.setNombreColor(rs.getString("nombreColor"));
        return producto;
    }

    private void asignarEnteroNulo(PreparedStatement ps, int indice, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, valor);
        }
    }

    private void asignarBytesNulos(PreparedStatement ps, int indice, byte[] datos) throws SQLException {
        if (datos == null || datos.length == 0) {
            ps.setNull(indice, Types.BLOB);
        } else {
            ps.setBytes(indice, datos);
        }
    }
}
