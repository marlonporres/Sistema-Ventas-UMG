package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
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

        int totalFilas = vistaFactura.getTblDetalle().getRowCount();
        if (totalFilas == 0) {
            JOptionPane.showMessageDialog(vistaFactura, "Debe agregar al menos un producto a la factura.");
            return;
        }

        // 1. Instanciamos el modelo maestro Factura y pasamos datos limpios
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setNit(nit);

        double totalConIva = obtenerTotalCalculado();
        double subtotalSinIva = totalConIva / 1.12;
        double montoIva = totalConIva - subtotalSinIva;

        factura.setSubtotal(subtotalSinIva);
        factura.setIva(montoIva);
        factura.setTotal(totalConIva);

        // 2. Recorremos el JTable para rellenar la lista del detalle
        for (int i = 0; i < totalFilas; i++) {
            DetalleFactura det = new DetalleFactura();
            
            // Creamos el sub-objeto Producto requerido por tu lógica interna
            Producto p = new Producto();
            p.setIdProducto(Integer.parseInt(vistaFactura.getTblDetalle().getValueAt(i, 0).toString()));
            p.setNombre(vistaFactura.getTblDetalle().getValueAt(i, 1).toString());
            
            String txtPrecio = vistaFactura.getTblDetalle().getValueAt(i, 3).toString().replace("Q", "").replace(",", "").trim();
            p.setPrecio(Double.parseDouble(txtPrecio));
            
            det.setProducto(p);
            det.setCantidad(Integer.parseInt(vistaFactura.getTblDetalle().getValueAt(i, 2).toString()));
            det.setPrecioUnitario(p.getPrecio());
            
            String txtSubtotalProd = vistaFactura.getTblDetalle().getValueAt(i, 4).toString().replace("Q", "").replace(",", "").trim();
            det.setSubtotal(Double.parseDouble(txtSubtotalProd));

            // Agregamos el detalle al objeto contenedor Factura
            factura.addDetalle(det);
        }

        System.out.println(">>> Enviando factura relacional al DAO...");

        // 3. Ejecución en segundo plano para evitar congelar la interfaz
        new Thread(() -> {
            boolean guardado = facturaDAO.guardarFacturaReal(factura);
            
            java.awt.EventQueue.invokeLater(() -> {
                if (guardado) {
                    limpiarFormularioCompleto();
                }
            });
        }).start();
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
                try {
                    // Limpieza robusta de cadenas monetarias
                    String textoLimpio = valorCelda.toString()
                            .replace("Q", "")
                            .replace(",", "")
                            .trim();
                    totalConIva += Double.parseDouble(textoLimpio);
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia de formato en fila " + i + ": " + e.getMessage());
                }
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