package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {
    
    @Override
    public void guardar(Estudiante estudiante) {
        // Añadimos dbo. explícitamente para asegurar el esquema correcto
        String sql = "INSERT INTO dbo.estudiante (carnet, nombre, email, nit, telefono) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false); 
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, estudiante.getCarnet());
                ps.setString(2, estudiante.getNombre());
                ps.setString(3, estudiante.getEmail());
                ps.setString(4, estudiante.getNit());
                ps.setString(5, estudiante.getTelefono());
                
                int filasAfectadas = ps.executeUpdate();
                
                if (filasAfectadas > 0) {
                    con.commit(); 
                    System.out.println(">>> ÉXITO REAL: Registro guardado en dbo.estudiante");
                } else {
                    con.rollback();
                }
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        } catch (Exception e) {
            System.err.println(">>> ERROR AL GUARDAR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        // Forzamos la lectura exacta desde dbo.estudiante
        String sql = "SELECT id, carnet, nombre, email, nit, telefono FROM dbo.estudiante";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setId(rs.getInt("id"));
                est.setCarnet(rs.getString("carnet"));
                est.setNombre(rs.getString("nombre"));
                est.setEmail(rs.getString("email"));
                est.setNit(rs.getString("nit"));
                est.setTelefono(rs.getString("telefono"));
                lista.add(est);
            }
            System.out.println(">>> Listar ejecutado. Registros recuperados: " + lista.size());
        } catch (Exception e) {
            System.err.println(">>> ERROR AL LISTAR: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE dbo.estudiante SET carnet = ?, nombre = ?, email = ?, nit = ?, telefono = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estudiante.getCarnet());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getEmail());
            ps.setString(4, estudiante.getNit());
            ps.setString(5, estudiante.getTelefono());
            ps.setInt(6, estudiante.getId());
            
            ps.executeUpdate();
            System.out.println(">>> ÉXITO: Estudiante ID " + estudiante.getId() + " actualizado.");
        } catch (Exception e) {
            System.err.println(">>> ERROR AL ACTUALIZAR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM dbo.estudiante WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            ps.executeUpdate();
            System.out.println(">>> ÉXITO: Estudiante ID " + id + " eliminado.");
        } catch (Exception e) {
            System.err.println(">>> ERROR AL ELIMINAR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}