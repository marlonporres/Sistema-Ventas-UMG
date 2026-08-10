package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.EstudianteDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import gt.edu.umg.sistema.estudiantes.vista.FrmEstudiante;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EstudianteController {
    
    private final EstudianteDAOImpl dao;
    private final FrmEstudiante vista;

    public EstudianteController(FrmEstudiante vista) {
        this.dao = new EstudianteDAOImpl(); 
        this.vista = vista;
        
        // Enlace de eventos con los botones exactos de tu FrmEstudiante
        if (this.vista.getBtnGuardar() != null) {
            this.vista.getBtnGuardar().addActionListener(e -> ejecutarGuardar());
        }
        
        if (this.vista.getBtnActualizar() != null) {
            this.vista.getBtnActualizar().addActionListener(e -> ejecutarActualizar());
        }
        
        if (this.vista.getBtnEliminar() != null) {
            this.vista.getBtnEliminar().addActionListener(e -> ejecutarEliminar());
        }
        
        if (this.vista.getBtnBuscar() != null) {
            this.vista.getBtnBuscar().addActionListener(e -> llenarTabla());
        }
        
        // Al hacer clic en una fila de la tabla, carga los datos a las cajas de texto
        this.vista.getTblEstudiantes().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarFilaSeleccionada();
            }
        });
        
        llenarTabla();
    }
    
    private void ejecutarGuardar() {
        if (validarCampos()) {
            Estudiante est = mapearDeVistaAModelo(false);
            
            // Forzar el guardado en un hilo secundario para NO congelar el MDI
            new Thread(() -> {
                dao.guardar(est);
                
                // Regresamos al hilo principal para actualizar los elementos visuales
                java.awt.EventQueue.invokeLater(() -> {
                    JOptionPane.showMessageDialog(vista, "Estudiante guardado con éxito.");
                    limpiarCampos();
                    llenarTabla();
                });
            }).start();
        }
    }

    private void ejecutarActualizar() {
        if (vista.getTxtID().getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un registro de la tabla para actualizar.");
            return;
        }
        
        if (validarCampos()) {
            Estudiante est = mapearDeVistaAModelo(true);
            
            // Forzar la actualización en un hilo secundario
            new Thread(() -> {
                dao.actualizar(est); 
                
                java.awt.EventQueue.invokeLater(() -> {
                    JOptionPane.showMessageDialog(vista, "Registro actualizado correctamente.");
                    limpiarCampos();
                    llenarTabla();
                });
            }).start();
        }
    }

    private void ejecutarEliminar() {
        String idStr = vista.getTxtID().getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un registro de la tabla para eliminar.");
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(vista, 
            "¿Seguro que desea eliminar el registro con ID: " + idStr + "?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            // Forzar la eliminación en un hilo secundario
            new Thread(() -> {
                dao.eliminar(Integer.parseInt(idStr)); 
                
                java.awt.EventQueue.invokeLater(() -> {
                    JOptionPane.showMessageDialog(vista, "Registro eliminado.");
                    limpiarCampos();
                    llenarTabla();
                });
            }).start();
        }
    }

    private void llenarTabla() {
        // Ejecutamos la carga de datos en segundo plano para evitar lags al abrir o actualizar
        new Thread(() -> {
            List<Estudiante> lista = dao.listar();
            
            java.awt.EventQueue.invokeLater(() -> {
                DefaultTableModel modelo = (DefaultTableModel) vista.getTblEstudiantes().getModel();
                modelo.setRowCount(0);
                for (Estudiante e : lista) {
                    modelo.addRow(new Object[]{e.getCarnet(), e.getNombre(), e.getEmail()});
                }
            });
        }).start();
    }

    private void cargarFilaSeleccionada() {
        int fila = vista.getTblEstudiantes().getSelectedRow();
        if (fila >= 0) {
            vista.getTxtCarnet().setText(vista.getTblEstudiantes().getValueAt(fila, 0).toString());
            vista.getTxtNombre().setText(vista.getTblEstudiantes().getValueAt(fila, 1).toString());
            vista.getTxtEmail().setText(vista.getTblEstudiantes().getValueAt(fila, 2).toString());
        }
    }

    private Estudiante mapearDeVistaAModelo(boolean incluirId) {
        Estudiante est = new Estudiante();
        if (incluirId && !vista.getTxtID().getText().isEmpty()) {
            est.setId(Integer.parseInt(vista.getTxtID().getText().trim()));
        }
        est.setCarnet(vista.getTxtCarnet().getText().trim());
        est.setNombre(vista.getTxtNombre().getText().trim());
        est.setEmail(vista.getTxtEmail().getText().trim());
        est.setNit(vista.getTxtNit().getText().trim());
        est.setTelefono(vista.getTxtTelefono().getText().trim());
        return est;
    }

    private boolean validarCampos() {
        if (vista.getTxtCarnet().getText().isEmpty() || 
            vista.getTxtNombre().getText().isEmpty() || 
            vista.getTxtEmail().getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Carnet, Nombre y Email son obligatorios.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        vista.getTxtID().setText("");
        vista.getTxtCarnet().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtNit().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCarnet().requestFocus();
    }
}