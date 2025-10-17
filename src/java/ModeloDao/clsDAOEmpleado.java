package ModeloDao;

import Config.clsConexion;
import Modelo.clsEmpleado;
import Util.AESGCMUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO encargado de las operaciones relacionadas con los empleados. De momento
 * solo se requiere la validación de credenciales para el proceso de inicio de
 * sesión.
 */
public class clsDAOEmpleado {

    private static final String SQL_FIND_BY_USER =
            "SELECT idEmpleado, Nombre, Apellido, idcargo, Usuario, Clave, "
            + "idTipoDocumento, NumeroDocumento, Telefono, Estado "
            + "FROM tbempleado WHERE Usuario = ?";

    /**
     * Busca al empleado por su nombre de usuario.
     *
     * @param usuario identificador del usuario
     * @return empleado envuelto en {@link Optional}
     */
    public Optional<clsEmpleado> buscarPorUsuario(String usuario) {
        try (Connection con = clsConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_USER)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                clsEmpleado empleado = new clsEmpleado();
                empleado.setIdEmpleado(rs.getInt("idEmpleado"));
                empleado.setNombre(rs.getString("Nombre"));
                empleado.setApellido(rs.getString("Apellido"));
                empleado.setIdCargo(rs.getInt("idcargo"));
                empleado.setUsuario(rs.getString("Usuario"));
                empleado.setClave(rs.getString("Clave"));
                empleado.setIdTipoDocumento(rs.getInt("idTipoDocumento"));
                empleado.setNumeroDocumento(rs.getString("NumeroDocumento"));
                empleado.setTelefono(rs.getString("Telefono"));
                empleado.setEstado(rs.getInt("Estado"));
                return Optional.of(empleado);
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar usuario: " + ex.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Valida que el usuario exista y que la contraseña proporcionada coincida
     * con la almacenada. Se utiliza AES-GCM para desencriptar la contraseña
     * guardada antes de compararla.
     *
     * @param usuario   nombre de usuario
     * @param contrasena contraseña sin cifrar proporcionada por el usuario
     * @return {@code true} si las credenciales son correctas
     */
    public boolean validarCredenciales(String usuario, String contrasena) {
        return buscarPorUsuario(usuario)
                .map(emp -> AESGCMUtil.matches(contrasena, emp.getClave()))
                .orElse(false);
    }
}