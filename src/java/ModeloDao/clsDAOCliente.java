package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDCliente;
import Modelo.clsCliente;
import Util.AESGCMUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsDAOCliente implements CRUDCliente {

    private static final String BASE_SELECT
            = "SELECT c.idcliente, c.Nombre, c.Apellido, c.idTipoDocumento, td.nombre AS tipoDocumentoNombre, "
            + "c.NumeroDocumento, c.Telefono, c.Direccion, c.Email, c.Clave, c.Estado "
            + "FROM tbcliente c "
            + "LEFT JOIN tbtipodocumento td ON c.idTipoDocumento = td.idtipodocumento ";

    private static final String SQL_LISTAR_ACTIVOS = BASE_SELECT + "WHERE c.Estado = 1 ORDER BY c.Nombre, c.Apellido";
    private static final String SQL_LISTAR_INACTIVOS = BASE_SELECT + "WHERE c.Estado = 0 ORDER BY c.Nombre, c.Apellido";
    private static final String SQL_OBTENER_POR_ID = BASE_SELECT + "WHERE c.idcliente = ?";
    private static final String SQL_BUSCAR = BASE_SELECT
            + "WHERE CAST(c.idcliente AS CHAR) LIKE ? "
            + "OR LOWER(c.Nombre) LIKE ? OR LOWER(c.Apellido) LIKE ? "
            + "OR LOWER(c.NumeroDocumento) LIKE ? OR LOWER(c.Email) LIKE ?";
    private static final String SQL_INSERT
            = "INSERT INTO tbcliente(Nombre, Apellido, idTipoDocumento, NumeroDocumento, Telefono, Direccion, Email, Clave, Estado) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE
            = "UPDATE tbcliente SET Nombre=?, Apellido=?, idTipoDocumento=?, NumeroDocumento=?, Telefono=?, Direccion=?, Email=?, Clave=?, Estado=? "
            + "WHERE idcliente=?";
    private static final String SQL_CAMBIAR_ESTADO
            = "UPDATE tbcliente SET Estado = CASE WHEN Estado = 1 THEN 0 ELSE 1 END WHERE idcliente = ?";
    private static final String SQL_EXISTE_DOCUMENTO = "SELECT COUNT(*) FROM tbcliente WHERE NumeroDocumento = ?";
    private static final String SQL_EXISTE_DOCUMENTO_EXCEPTO = "SELECT COUNT(*) FROM tbcliente WHERE NumeroDocumento = ? AND idcliente <> ?";
    private static final String SQL_EXISTE_EMAIL = "SELECT COUNT(*) FROM tbcliente WHERE LOWER(Email) = LOWER(?)";
    private static final String SQL_EXISTE_EMAIL_EXCEPTO = "SELECT COUNT(*) FROM tbcliente WHERE LOWER(Email) = LOWER(?) AND idcliente <> ?";

    @Override
    public List<clsCliente> mtdListarActivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_ACTIVOS, ps -> {
        });
    }

    @Override
    public List<clsCliente> mtdListarInactivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_INACTIVOS, ps -> {
        });
    }

    @Override
    public clsCliente mtdObtenerPorId(int id) {
        List<clsCliente> lista = ejecutarConsultaMultiple(SQL_OBTENER_POR_ID, ps -> ps.setInt(1, id));
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<clsCliente> mtdBuscar(String texto) {
        String parametroId = "%" + texto + "%";
        String parametroLower = "%" + texto.toLowerCase() + "%";
        return ejecutarConsultaMultiple(SQL_BUSCAR, ps -> {
            ps.setString(1, parametroId);
            ps.setString(2, parametroLower);
            ps.setString(3, parametroLower);
            ps.setString(4, parametroLower);
            ps.setString(5, parametroLower);
        });
    }

    @Override
    public boolean mtdAgregar(clsCliente cliente) {
        String claveCifrada = cifrarClave(cliente.getClave());
        if (claveCifrada == null) {
            return false;
        }

        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getIdTipoDocumento());
            ps.setString(4, cliente.getNumeroDocumento());
            ps.setString(5, valorONull(cliente.getTelefono()));
            ps.setString(6, valorONull(cliente.getDireccion()));
            ps.setString(7, valorONull(cliente.getEmail()));
            ps.setString(8, claveCifrada);
            ps.setInt(9, cliente.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsCliente cliente) {
        String claveCifrada = cifrarClave(cliente.getClave());
        if (claveCifrada == null) {
            return false;
        }

        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getIdTipoDocumento());
            ps.setString(4, cliente.getNumeroDocumento());
            ps.setString(5, valorONull(cliente.getTelefono()));
            ps.setString(6, valorONull(cliente.getDireccion()));
            ps.setString(7, valorONull(cliente.getEmail()));
            ps.setString(8, claveCifrada);
            ps.setInt(9, cliente.getEstado());
            ps.setInt(10, cliente.getIdCliente());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al editar cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdCambiarEstado(int id) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteDocumento(String numeroDocumento) {
        return existeConParametro(SQL_EXISTE_DOCUMENTO, numeroDocumento);
    }

    public boolean mtdExisteDocumentoEnOtro(String numeroDocumento, int idExcluir) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_DOCUMENTO_EXCEPTO)) {
            ps.setString(1, numeroDocumento.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar documento (excluyendo actual) en cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteEmail(String email) {
        return existeConParametro(SQL_EXISTE_EMAIL, email);
    }

    public boolean mtdExisteEmailEnOtro(String email, int idExcluir) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_EMAIL_EXCEPTO)) {
            ps.setString(1, email.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar email (excluyendo actual) en cliente: " + e.getMessage());
        }
        return false;
    }

    private boolean existeConParametro(String sql, String valor) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar existencia de valor en cliente: " + e.getMessage());
        }
        return false;
    }

    private List<clsCliente> ejecutarConsultaMultiple(String sql, StatementConsumer consumer) {
        List<clsCliente> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta de clientes: " + e.getMessage());
        }
        return lista;
    }

    private clsCliente mapearCliente(ResultSet rs) throws SQLException {
        clsCliente c = new clsCliente();
        c.setIdCliente(rs.getInt("idcliente"));
        c.setNombre(rs.getString("Nombre"));
        c.setApellido(rs.getString("Apellido"));
        c.setIdTipoDocumento(rs.getInt("idTipoDocumento"));
        c.setTipoDocumentoNombre(rs.getString("tipoDocumentoNombre"));
        c.setNumeroDocumento(rs.getString("NumeroDocumento"));
        c.setTelefono(rs.getString("Telefono"));
        c.setDireccion(rs.getString("Direccion"));
        c.setEmail(rs.getString("Email"));
        String clave = rs.getString("Clave");
        c.setClave(clave);
        c.setClaveVisible(descifrarClave(clave));
        c.setEstado(rs.getInt("Estado"));
        return c;
    }

    private String cifrarClave(String clave) {
        if (clave == null) {
            return null;
        }
        try {
            return AESGCMUtil.encrypt(clave);
        } catch (GeneralSecurityException ex) {
            System.out.println("Error al cifrar clave de cliente: " + ex.getMessage());
            return null;
        }
    }

    private String descifrarClave(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }
        try {
            return AESGCMUtil.decrypt(valor);
        } catch (GeneralSecurityException | IllegalArgumentException | java.nio.BufferUnderflowException ex) {
            return valor;
        }
    }

    private String valorONull(String valor) {
        if (valor == null) {
            return null;
        }
        String trimmed = valor.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @FunctionalInterface
    private interface StatementConsumer {

        void accept(PreparedStatement ps) throws SQLException;
    }
}