/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.vista.FrmFacturaFiltro;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FacturaFiltroController {

    private final FrmFacturaFiltro vistaFiltro;
    private final String tipoFiltro; // Puede ser "CLIENTE" o "PRODUCTO"
    private Object seleccion = null; // Guardará el objeto final seleccionado

    public FacturaFiltroController(FrmFacturaFiltro vistaFiltro, String tipoFiltro) {
        this.vistaFiltro = vistaFiltro;
        this.tipoFiltro = tipoFiltro;

        // Configurar las columnas de la tabla según lo que se va a buscar
        inicializarTabla();

        // Enlazar eventos
        this.vistaFiltro.getBtnBuscar().addActionListener(e -> buscarEnSQLServer());
        this.vistaFiltro.getBtnSeleccionar().addActionListener(e -> capturarSeleccion());
        
        // Carga inicial automática de datos
        buscarEnSQLServer();
    }

    private void inicializarTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // No editable
        };

        if ("CLIENTE".equals(tipoFiltro)) {
            vistaFiltro.setTitle("Buscar Cliente - SQL Server");
            modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre / Razón Social", "NIT", "Teléfono"});
        } else {
            vistaFiltro.setTitle("Buscar Producto - SQL Server");
            modelo.setColumnIdentifiers(new Object[]{"Código", "Descripción", "Precio Unitario", "Existencia"});
        }
        vistaFiltro.setModeloTabla(modelo);
    }

    private void buscarEnSQLServer() {
        String textoBusqueda = vistaFiltro.getTxtFiltro().getText().trim();
        DefaultTableModel modelo = (DefaultTableModel) vistaFiltro.getTblResultados().getModel();
        modelo.setRowCount(0); // Limpiar tabla anterior

        // AQUÍ CONECTAMOS CON TUS DAOS DE SQL SERVER MAS ADELANTE
        if ("CLIENTE".equals(tipoFiltro)) {
            // Ejemplo de llenado temporal para pruebas visuales
            modelo.addRow(new Object[]{"1", "Consumidor Final", "CF", "00000000"});
            // List<Cliente> lista = clienteDAO.buscar(textoBusqueda);
            // recorrer y modelo.addRow(...)
        } else {
            // Ejemplo de llenado temporal para pruebas visuales
            modelo.addRow(new Object[]{"101", "Martillo Mecánico Industrial", "150.00", "25"});
            // List<Producto> lista = productoDAO.buscar(textoBusqueda);
            // recorrer y modelo.addRow(...)
        }
    }

    private void capturarSeleccion() {
        int fila = vistaFiltro.getTblResultados().getSelectedRow();
        if (fila >= 0) {
            // Captura los datos de la fila seleccionada
            DefaultTableModel modelo = (DefaultTableModel) vistaFiltro.getTblResultados().getModel();
            
            if ("CLIENTE".equals(tipoFiltro)) {
                // Mapeas al modelo Cliente de tu UML
                // this.seleccion = clienteObjeto;
            } else {
                // Mapeas al modelo Producto de tu UML
                // this.seleccion = productoObjeto;
            }
            
            vistaFiltro.dispose(); // Cierra el buscador al seleccionar
        } else {
            JOptionPane.showMessageDialog(vistaFiltro, "Deberá seleccionar un registro de la tabla.");
        }
    }

    public Object getSeleccion() {
        return seleccion;
    }
}