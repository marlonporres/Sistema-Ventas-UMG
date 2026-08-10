/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.vista.FrmContenedorPadre;
import gt.edu.umg.sistema.estudiantes.vista.FrmEstudiante;
import gt.edu.umg.sistema.estudiantes.vista.FrmFactura;
import javax.swing.JInternalFrame;

public class ContenedorPadreController {

    private final FrmContenedorPadre vistaPadre;

    public ContenedorPadreController(FrmContenedorPadre vistaPadre) {
        this.vistaPadre = vistaPadre;

        // Escuchar clics en el menú Estudiantes
        if (this.vistaPadre.getMenuEstudiantes() != null) {
            this.vistaPadre.getMenuEstudiantes().addActionListener(e -> abrirVentanaEstudiantes());
        }

        // Escuchar clics en el menú Facturación
        if (this.vistaPadre.getMenuFactura() != null) {
            this.vistaPadre.getMenuFactura().addActionListener(e -> abrirVentanaFactura());
        }
    }

    private void abrirVentanaEstudiantes() {
        if (noEstaAbierta(FrmEstudiante.class)) {
            FrmEstudiante vistaEstudiante = new FrmEstudiante();
            new EstudianteController(vistaEstudiante);
            
            vistaPadre.getDesktopPane().add(vistaEstudiante);
            vistaEstudiante.setVisible(true);
        }
    }

    private void abrirVentanaFactura() {
        if (noEstaAbierta(FrmFactura.class)) {
            FrmFactura vistaFactura = new FrmFactura();
            new FacturaController(vistaFactura);
            
            vistaPadre.getDesktopPane().add(vistaFactura);
            vistaFactura.setVisible(true);
        }
    }

    private boolean noEstaAbierta(Class<?> claseFormulario) {
        for (JInternalFrame frame : vistaPadre.getDesktopPane().getAllFrames()) {
            if (claseFormulario.isInstance(frame)) {
                try {
                    frame.setSelected(true);
                } catch (Exception ignored) {}
                return false;
            }
        }
        return true;
    }
}