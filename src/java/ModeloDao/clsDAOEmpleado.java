package ModeloDao;

import Config.clsConexion;
import Interfaces.CRUDEmpleado;
import Modelo.clsEmpleado;
import Util.AESGCMUtil;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

public class clsDAOEmpleado implements CRUDEmpleado {

    // --- CONSULTAS SQL ---
    private static final String BASE_SELECT
            = "SELECT e.idEmpleado, e.Nombre, e.Apellido, e.idcargo, c.nombre AS cargoNombre, "
            + "e.Usuario, e.Clave, e.idTipoDocumento, td.nombre AS tipoDocumentoNombre, "
            + "e.NumeroDocumento, e.Telefono, e.Estado "
            + "FROM tbempleado e "
            + "LEFT JOIN tbcargo c ON e.idcargo = c.idcargo "
            + "LEFT JOIN tbtipodocumento td ON e.idTipoDocumento = td.idtipodocumento ";

    private static final String SQL_LISTAR_ACTIVOS = BASE_SELECT + "WHERE e.Estado = 1 ORDER BY e.Nombre, e.Apellido";
    private static final String SQL_LISTAR_INACTIVOS = BASE_SELECT + "WHERE e.Estado = 0 ORDER BY e.Nombre, e.Apellido";
    private static final String SQL_OBTENER_POR_ID = BASE_SELECT + "WHERE e.idEmpleado = ?";
    private static final String SQL_BUSCAR = BASE_SELECT
            + "WHERE CAST(e.idEmpleado AS CHAR) LIKE ? OR e.Nombre LIKE ? OR e.Apellido LIKE ? OR e.Usuario LIKE ?";
    private static final String SQL_INSERT
            = "INSERT INTO tbempleado(Nombre, Apellido, idcargo, Usuario, Clave, idTipoDocumento, NumeroDocumento, Telefono, Estado) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE
            = "UPDATE tbempleado SET Nombre=?, Apellido=?, idcargo=?, Usuario=?, Clave=?, idTipoDocumento=?, NumeroDocumento=?, Telefono=?, Estado=? WHERE idEmpleado=?";
    private static final String SQL_CAMBIAR_ESTADO
            = "UPDATE tbempleado SET Estado = CASE WHEN Estado = 1 THEN 0 ELSE 1 END WHERE idEmpleado = ?";
    private static final String SQL_EXISTE_USUARIO = "SELECT COUNT(*) FROM tbempleado WHERE LOWER(Usuario)=LOWER(?)";
    private static final String SQL_EXISTE_DOCUMENTO = "SELECT COUNT(*) FROM tbempleado WHERE NumeroDocumento = ?";
    private static final String SQL_EXISTE_USUARIO_EXCEPTO
            = "SELECT COUNT(*) FROM tbempleado WHERE LOWER(Usuario)=LOWER(?) AND idEmpleado<>?";
    private static final String SQL_EXISTE_DOCUMENTO_EXCEPTO
            = "SELECT COUNT(*) FROM tbempleado WHERE NumeroDocumento=? AND idEmpleado<>?";
    private static final String SQL_FIND_BY_USER = BASE_SELECT + "WHERE e.Usuario = ?";

    
    @Override
    public List<clsEmpleado> mtdListarActivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_ACTIVOS, ps -> {
        });
    }

    @Override
    public List<clsEmpleado> mtdListarInactivos() {
        return ejecutarConsultaMultiple(SQL_LISTAR_INACTIVOS, ps -> {
        });
    }

    @Override
    public clsEmpleado mtdObtenerPorId(int id) {
        List<clsEmpleado> lista = ejecutarConsultaMultiple(SQL_OBTENER_POR_ID, ps -> ps.setInt(1, id));
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<clsEmpleado> mtdBuscar(String texto) {
        String parametro = "%" + texto + "%";
        return ejecutarConsultaMultiple(SQL_BUSCAR, ps -> {
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            ps.setString(3, parametro);
            ps.setString(4, parametro);
        });
    }

    @Override
    public boolean mtdAgregar(clsEmpleado empleado) {
        String claveCifrada = cifrarClave(empleado.getClave());
        if (claveCifrada == null) {
            return false;
        }

        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setInt(3, empleado.getIdCargo());
            ps.setString(4, empleado.getUsuario());
            ps.setString(5, claveCifrada);
            ps.setInt(6, empleado.getIdTipoDocumento());
            ps.setString(7, empleado.getNumeroDocumento());
            ps.setString(8, empleado.getTelefono());
            ps.setInt(9, empleado.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdEditar(clsEmpleado empleado) {
        String claveCifrada = cifrarClave(empleado.getClave());
        if (claveCifrada == null) {
            return false;
        }

        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setInt(3, empleado.getIdCargo());
            ps.setString(4, empleado.getUsuario());
            ps.setString(5, claveCifrada);
            ps.setInt(6, empleado.getIdTipoDocumento());
            ps.setString(7, empleado.getNumeroDocumento());
            ps.setString(8, empleado.getTelefono());
            ps.setInt(9, empleado.getEstado());
            ps.setInt(10, empleado.getIdEmpleado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al editar empleado: " + e.getMessage());
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
            System.out.println("Error al cambiar estado: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteUsuario(String usuario) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_USUARIO)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean mtdExisteDocumento(String numeroDocumento) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_DOCUMENTO)) {
            ps.setString(1, numeroDocumento.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar documento: " + e.getMessage());
        }
        return false;
    }

    // --- MÉTODOS ADICIONALES ---
    public boolean mtdExisteUsuarioEnOtro(String usuario, int idExcluir) {
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_EXISTE_USUARIO_EXCEPTO)) {
            ps.setString(1, usuario.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario (excluyendo actual): " + e.getMessage());
        }
        return false;
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
            System.out.println("Error al validar documento (excluyendo actual): " + e.getMessage());
        }
        return false;
    }

    public Optional<clsEmpleado> buscarPorUsuario(String usuario) {
        List<clsEmpleado> lista = ejecutarConsultaMultiple(SQL_FIND_BY_USER, ps -> ps.setString(1, usuario));
        return lista.stream().findFirst();
    }

    public boolean validarCredenciales(String usuario, String contrasena) {
        return buscarPorUsuario(usuario)
                .map(emp -> AESGCMUtil.matches(contrasena, emp.getClave()))
                .orElse(false);
    }

    // --- INTERFAZ FUNCIONAL ---
    private interface StatementConsumer {

        void accept(PreparedStatement ps) throws SQLException;
    }

    // --- MÉTODO GENÉRICO ---
    private List<clsEmpleado> ejecutarConsultaMultiple(String sql, StatementConsumer consumer) {
        List<clsEmpleado> lista = new ArrayList<>();
        try (Connection con = clsConexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta: " + e.getMessage());
        }
        return lista;
    }

    // --- MAPEADOR ---
    private clsEmpleado mapearEmpleado(ResultSet rs) throws SQLException {
        clsEmpleado e = new clsEmpleado();
        e.setIdEmpleado(rs.getInt("idEmpleado"));
        e.setNombre(rs.getString("Nombre"));
        e.setApellido(rs.getString("Apellido"));
        e.setIdCargo(rs.getInt("idcargo"));
        e.setCargoNombre(rs.getString("cargoNombre"));
        e.setUsuario(rs.getString("Usuario"));
        String clave = rs.getString("Clave");
        e.setClave(clave);
        e.setClaveVisible(descifrarClave(clave));
        e.setIdTipoDocumento(rs.getInt("idTipoDocumento"));
        e.setTipoDocumentoNombre(rs.getString("tipoDocumentoNombre"));
        e.setNumeroDocumento(rs.getString("NumeroDocumento"));
        e.setTelefono(rs.getString("Telefono"));
        e.setEstado(rs.getInt("Estado"));
        return e;
    }

    // --- CIFRADO ---
    private String cifrarClave(String clave) {
        if (clave == null) {
            return null;
        }
        try {
            return AESGCMUtil.encrypt(clave);
        } catch (GeneralSecurityException ex) {
            System.out.println("Error al cifrar clave: " + ex.getMessage());
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
            return valor; // devuelve el mismo valor si no se puede descifrar
        }
    }

}
