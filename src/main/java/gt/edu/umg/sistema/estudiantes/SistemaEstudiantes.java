/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.umg.sistema.estudiantes;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import gt.edu.umg.sistema.estudiantes.vista.FrmContenedorPadre;
import gt.edu.umg.sistema.estudiantes.controlador.ContenedorPadreController;

/**
 * @author marlo
 */
public class SistemaEstudiantes {

    public static void main(String[] args) {
        // 1. Inyectamos el look moderno plano en modo oscuro ANTES de inicializar las ventanas
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            System.out.println(">>> Interfaz gráfica actualizada a FlatLaf Dark con éxito.");
        } catch (Exception ex) {
            System.err.println(">>> No se pudo aplicar el tema moderno: " + ex.getMessage());
        }

        // 2. Ejecutamos la vista de forma segura en el hilo de despacho de eventos de Java Swing
        java.awt.EventQueue.invokeLater(() -> {
            // Instancia la vista del contenedor MDI real que tienes en tu proyecto
            FrmContenedorPadre menu = new FrmContenedorPadre();
            
            // Asigna su controlador correspondiente
            ContenedorPadreController control = new ContenedorPadreController(menu);
            
            // Muestra el formulario padre en pantalla
            menu.setVisible(true);
        });
    }
}