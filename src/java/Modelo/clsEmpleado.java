package Modelo;

public class clsEmpleado {

    private int idEmpleado;
    private String nombre;
    private String apellido;
    private int idCargo;
    private String cargoNombre;
    private String usuario;
    private String clave;
    private String claveVisible;
    private int idTipoDocumento;
    private String tipoDocumentoNombre;
    private String numeroDocumento;
    private String telefono;
    private int estado;

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getCargoNombre() {
        return cargoNombre;
    }

    public void setCargoNombre(String cargoNombre) {
        this.cargoNombre = cargoNombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveVisible() {
        return claveVisible;
    }

    public void setClaveVisible(String claveVisible) {
        this.claveVisible = claveVisible;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getTipoDocumentoNombre() {
        return tipoDocumentoNombre;
    }

    public void setTipoDocumentoNombre(String tipoDocumentoNombre) {
        this.tipoDocumentoNombre = tipoDocumentoNombre;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}