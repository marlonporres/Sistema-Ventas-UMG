CREATE DATABASE Estudiantes;
GO

USE Estudiantes;
GO

CREATE TABLE estudiante (
    id INT IDENTITY(1,1) PRIMARY KEY,
    carnet VARCHAR(20),
    nombre VARCHAR(100),
    email VARCHAR(100),
    nit VARCHAR(20),
    telefono VARCHAR(20)
);
GO

CREATE TABLE factura (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nit VARCHAR(20),
    cliente VARCHAR(100),
    total DECIMAL(10,2),
    fecha DATETIME DEFAULT GETDATE()
);
GO
