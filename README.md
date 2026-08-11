# Sistema de Ventas - UMG

Proyecto desarrollado en Java Swing con arquitectura MVC y conexión a SQL Server mediante JDBC.

## Instrucciones de Ejecución:
1. Ejecutar el archivo `script_base_datos.sql` en SQL Server para crear la base de datos `Estudiantes` y sus tablas.
2. Abrir el proyecto en NetBeans. (Es un proyecto Maven).
3. Asegurarse de tener el driver JDBC de SQL Server configurado.
4. Ejecutar la clase principal para abrir el entorno MDI.
5. Configuración del Motor de Base de Datos: Colocá una nota aclaratoria indicando que el proyecto requiere que SQL Server tenga el protocolo TCP/IP habilitado en el puerto 1433 y soporte Autenticación Mixta (SQL Server and Windows Authentication Mode).

Credenciales por Defecto: Dejá explícito en el texto que la clase Conexion.java está configurada temporalmente para usar el usuario genérico sa con la contraseña 12345. Esto le permite al ingeniero replicar tu entorno en un segundo creando ese login o adaptando el suyo sin romper el código.
