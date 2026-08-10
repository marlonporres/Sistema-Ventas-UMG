package gt.edu.umg.sistema.estudiantes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class FacturaDAO {
    
    public boolean guardarFacturaPrueba(String nit, String cliente, double total) {
        String sql = "INSERT INTO factura (nit, cliente, total) VALUES (?, ?, ?)";
        
        // Al estar en el mismo paquete 'dao', no necesita import externo
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nit);
            ps.setString(2, cliente);
            ps.setDouble(3, total);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "¡Factura guardada en SQL Server con éxito!");
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar en BD: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}