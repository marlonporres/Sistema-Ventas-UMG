package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import gt.edu.umg.sistema.estudiantes.vista.FrmFactura;
import javax.swing.JOptionPane;

public class FacturaController {

    private final FrmFactura vistaFactura;
    private final FacturaDAO facturaDAO;

    public FacturaController(FrmFactura vistaFactura) {
        this.vistaFactura = vistaFactura;
        this.facturaDAO = new FacturaDAO();

        // 1. Enlazamos el botón "+ Agregar Producto"
        if (this.vistaFactura.getBtnAgregarProd() != null) {
            this.vistaFactura.getBtnAgregarProd().addActionListener(e -> agregarProductoATabla());
        }

        // 2. Enlazamos el botón "- Eliminar Producto"
        if (this.vistaFactura.getBtnEliminarProd() != null) {
            this.vistaFactura.getBtnEliminarProd().addActionListener(e -> eliminarProductoDeTabla());
        }

        // 3. Enlazamos el botón "Guardar Factura" -> Persistencia en SQL Server
        if (this.vistaFactura.getBtnGuardar() != null) {
            this.vistaFactura.getBtnGuardar().addActionListener(e -> guardarFacturaEnBD());
        }
        
    }

    private void agregarProductoATabla() {
        try {
            // Validar que los campos no estén vacíos
            if (vistaFactura.getTxtCodigoProd().getText().isEmpty() ||
                vistaFactura.getTxtDescripcionProd().getText().isEmpty() ||
                vistaFactura.getTxtCantidadProd().getText().isEmpty() ||
                vistaFactura.getTxtPrecioProd().getText().isEmpty()) {
                
                JOptionPane.showMessageDialog(vistaFactura, "Por favor, llene todos los campos del producto.");
                return;
            }

            // Instanciar modelo Producto según UML
            Producto prod = new Producto();
            prod.setIdProducto(Integer.parseInt(vistaFactura.getTxtCodigoProd().getText().trim()));
            prod.setNombre(vistaFactura.getTxtDescripcionProd().getText().trim());
            prod.setPrecio(Double.parseDouble(vistaFactura.getTxtPrecioProd().getText().trim()));

            // Instanciar modelo DetalleFactura según UML
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(prod);
            detalle.setCantidad(Integer.parseInt(vistaFactura.getTxtCantidadProd().getText().trim()));
            detalle.setPrecioUnitario(prod.getPrecio());

            double subtotal = detalle.calcularSubtotal();
            java.text.DecimalFormat df = new java.text.DecimalFormat("Q #,##0.00");

            Object[] fila = {
                String.valueOf(prod.getIdProducto()),
                prod.getNombre(),
                String.valueOf(detalle.getCantidad()),
                df.format(prod.getPrecio()),
                df.format(subtotal)
            };

            // Mandar la fila a la tabla
            vistaFactura.agregarFilaTabla(fila);

            // Recalcular Subtotal, IVA y Total
            calcularTotales();

            // Limpiar campos de captura de producto
            limpiarCamposProducto();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaFactura, "Error: Cantidad, Precio y Código deben ser valores numéricos válidos.");
        }
    }

    private void eliminarProductoDeTabla() {
        int filaSeleccionada = vistaFactura.getTblDetalle().getSelectedRow();
        if (filaSeleccionada >= 0) {
            javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vistaFactura.getTblDetalle().getModel();
            modelo.removeRow(filaSeleccionada);
            
            // Recalcular montos al eliminar una fila
            calcularTotales();
        } else {
            JOptionPane.showMessageDialog(vistaFactura, "Por favor, seleccione una fila de la tabla para eliminar.");
        }
    }

    private void guardarFacturaEnBD() {
        String nit = vistaFactura.getTxtNit().getText().trim();
        String cliente = vistaFactura.getTxtCliente().getText().trim();

        if (nit.isEmpty() || cliente.isEmpty()) {
            JOptionPane.showMessageDialog(vistaFactura, "Ingrese el NIT y el Nombre del Cliente antes de guardar.");
            return;
        }

        if (vistaFactura.getTblDetalle().getRowCount() == 0) {
            JOptionPane.showMessageDialog(vistaFactura, "Debe agregar al menos un producto a la factura.");
            return;
        }

        double total = obtenerTotalCalculado();

        // Llamada al DAO de persistencia SQL Server
        boolean guardado = facturaDAO.guardarFacturaPrueba(nit, cliente, total);
        if (guardado) {
            limpiarFormularioCompleto();
        }
    }

    private void calcularTotales() {
        double totalConIva = obtenerTotalCalculado();

        // Desglose del 12% de IVA en Guatemala
        double subtotalSinIva = totalConIva / 1.12;
        double montoIva = totalConIva - subtotalSinIva;

        java.text.DecimalFormat df = new java.text.DecimalFormat("Q #,##0.00");

        if (vistaFactura.getLblSubtotal() != null) {
            vistaFactura.getLblSubtotal().setText("Subtotal: " + df.format(subtotalSinIva));
        }
        if (vistaFactura.getLblIva() != null) {
            vistaFactura.getLblIva().setText("IVA (12%): " + df.format(montoIva));
        }
        if (vistaFactura.getLblTotal() != null) {
            vistaFactura.getLblTotal().setText("Total: " + df.format(totalConIva));
        }
    }

    private double obtenerTotalCalculado() {
        double totalConIva = 0.0;
        for (int i = 0; i < vistaFactura.getTblDetalle().getRowCount(); i++) {
            Object valorCelda = vistaFactura.getTblDetalle().getValueAt(i, 4);
            if (valorCelda != null) {
                String textoLimpio = valorCelda.toString().replace("Q", "").replace(",", "").trim();
                totalConIva += Double.parseDouble(textoLimpio);
            }
        }
        return totalConIva;
    }

    private void limpiarCamposProducto() {
        vistaFactura.getTxtCodigoProd().setText("");
        vistaFactura.getTxtDescripcionProd().setText("");
        vistaFactura.getTxtCantidadProd().setText("");
        vistaFactura.getTxtPrecioProd().setText("");
        vistaFactura.getTxtCodigoProd().requestFocus();
    }

    private void limpiarFormularioCompleto() {
        vistaFactura.getTxtNit().setText("");
        vistaFactura.getTxtCliente().setText("");
        limpiarCamposProducto();
        ((javax.swing.table.DefaultTableModel) vistaFactura.getTblDetalle().getModel()).setRowCount(0);
        calcularTotales();
    }
}