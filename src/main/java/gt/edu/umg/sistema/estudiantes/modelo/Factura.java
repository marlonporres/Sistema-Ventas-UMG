/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.modelo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int idFactura;
    private LocalDate fecha;
    private Cliente cliente;
    private List<DetalleFactura> detalles; // Composición
    
    public Factura() {
        this.detalles = new ArrayList<>();
        this.fecha = LocalDate.now();
    }
    
    public void agregarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }
    
    public double calcularTotal() {
        double total = 0;
        for(DetalleFactura d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
   
}