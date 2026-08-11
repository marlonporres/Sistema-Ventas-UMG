package gt.edu.umg.sistema.estudiantes.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;"
            + "databaseName=Estudiantes;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String USER = "sa"; 
    private static final String PASSWORD = "12345"; // La contraseña que le asignaste arriba

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: No se encontró el driver JDBC: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXIÓN POST-REPARACIÓN ===");
        try (Connection con = getConexion()) {
            if (con != null && !con.isClosed()) {
                System.out.println("✅ ¡CONEXIÓN EXITOSA!");
                System.out.println("Base de datos conectada: " + con.getCatalog());
            }
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión:");
            System.err.println("Mensaje: " + e.getMessage());
        }
        System.out.println("=== FIN DE LA PRUEBA ===");
    }
}