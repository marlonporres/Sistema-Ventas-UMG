package gt.edu.umg.sistema.estudiantes.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    
    private int idFactura;
    private LocalDate fecha;
    
    // Variables adaptadas para la tabla de la base de datos
    private String cliente;
    private String nit;
    private double subtotal;
    private double iva;
    private double total;
    
    private List<DetalleFactura> detalles; 
    
    public Factura() {
        this.detalles = new ArrayList<>();
        this.fecha = LocalDate.now();
    }
    
    // Renombrado a addDetalle para que coincida con tu Controlador
    public void addDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }
    
    public double calcularTotal() {
        double calcTotal = 0;
        for(DetalleFactura d : detalles) {
            calcTotal += d.calcularSubtotal();
        }
        return calcTotal;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}