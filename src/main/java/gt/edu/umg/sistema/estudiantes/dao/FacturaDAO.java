package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class FacturaDAO {
    
    public boolean guardarFacturaReal(Factura factura) {
        String sqlFactura = "INSERT INTO dbo.factura (cliente, nit, subtotal, iva, total) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO dbo.detalle_factura (factura_id, codigo_producto, descripcion, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // Iniciamos la transacción

            // 1. Guardar el Encabezado de la factura
            try (PreparedStatement psF = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                psF.setString(1, factura.getCliente());
                psF.setString(2, factura.getNit());
                psF.setDouble(3, factura.getSubtotal());
                psF.setDouble(4, factura.getIva());
                psF.setDouble(5, factura.getTotal());
                psF.executeUpdate();

                // Recuperar el ID de la factura que SQL Server acaba de generar
                int idFacturaGenerado = 0;
                try (ResultSet rs = psF.getGeneratedKeys()) {
                    if (rs.next()) {
                        idFacturaGenerado = rs.getInt(1);
                    }
                }

                // 2. Guardar el Detalle (Productos) enlazados a la factura
                try (PreparedStatement psD = con.prepareStatement(sqlDetalle)) {
                    for (DetalleFactura detalle : factura.getDetalles()) {
                        psD.setInt(1, idFacturaGenerado); 
                        
                        // Aquí está la corrección: Extraemos la info desde el objeto Producto
                        psD.setString(2, String.valueOf(detalle.getProducto().getIdProducto()));
                        psD.setString(3, detalle.getProducto().getNombre());
                        
                        psD.setInt(4, detalle.getCantidad());
                        psD.setDouble(5, detalle.getPrecioUnitario());
                        psD.setDouble(6, detalle.getSubtotal());
                        psD.addBatch(); // Encolar instrucción
                    }
                    psD.executeBatch(); // Ejecutar todo el bloque
                }
            }
            
            con.commit(); // Confirmamos los cambios en la BD
            JOptionPane.showMessageDialog(null, "¡Factura guardada en la base de datos con éxito!");
            return true;
            
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); } // Revertir si hay error
            }
            JOptionPane.showMessageDialog(null, "Error al guardar en BD: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}