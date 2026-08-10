package gt.edu.umg.sistema.estudiantes.modelo;

/**
 * @author maorozco
 */
public class Estudiante {
    private int id;
    private String nombres;
    private String apellidos;
    private String carnet;
    private String email;
    private String telefono;
    private String direccion;
    private String nit; // Agregado para soportar los datos de facturación

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // --- MÉTODOS PUENTE PARA COMPATIBILIDAD CON EL CONTROLADOR ---
    public String getNombre() {
        return (this.nombres + " " + (this.apellidos != null ? this.apellidos : "")).trim();
    }

    public void setNombre(String nombreCompleto) {
        this.nombres = nombreCompleto; 
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
    // -------------------------------------------------------------

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}