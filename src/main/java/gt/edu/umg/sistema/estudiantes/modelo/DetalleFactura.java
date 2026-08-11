package gt.edu.umg.sistema.estudiantes.modelo;

public class DetalleFactura {
    
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    // Se agrega la variable subtotal para que el DAO y Controlador puedan leerla y escribirla
    private double subtotal; 
    
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    // --- GETTERS Y SETTERS ---

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}